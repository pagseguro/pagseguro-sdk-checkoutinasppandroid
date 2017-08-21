package br.com.uol.pslibs.lojinhapagseguro.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import br.com.uol.pslibs.lojinhapagseguro.MainActivity;
import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.presenter.ShoppingPresenter;
import br.com.uol.pslibs.lojinhapagseguro.presenter.impl.ShoppingPresenterImpl;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.view.ShoppingView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.AMOUNT;
import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.PRODUCT;
import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.QUANTITY;
import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.TOTAL;
import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.TOTAL_AMOUNT;
import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.TOTAL_VALUE;

public class ShoppingFragment extends Fragment implements ShoppingView {

    @BindView(R.id.tv_amount) TextView tvAmount;
    @BindView(R.id.tv_total) TextView tvTotal;
    @BindView(R.id.card)
    CardView card;
    @BindView(R.id.card_clear)
    CardView cardClear;
    @BindView(R.id.bt_payment_method) Button btFinish;
    @BindView(R.id.tv_price) TextView tvPrice;
    @BindView(R.id.iv_sub) ImageView ivSub;
    @BindView(R.id.iv_add) ImageView ivAdd;

    private ShoppingPresenter presenter;
    private DecimalFormat decimalFormat;
    private double price = 0.00;
    private SweetAlertDialog pDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).disableMenu();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shopping, container, false);
        initComponents(view);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private void initComponents(View layout) {
        ButterKnife.bind(this, layout);
        presenter = new ShoppingPresenterImpl();
        presenter.attachView(this);
        presenter.setProduct(getArguments().get(Constants.PRODUCT));
        presenter.getProduct().setName(getResources().getString(R.string.product_name));
        decimalFormat = (DecimalFormat)NumberFormat.getNumberInstance(new Locale("pt","br"));
        decimalFormat.applyPattern(Constants.MONEY_PATTERN);
        price = Double.valueOf(presenter.getProduct().getPriceString());
        tvTotal.setText(decimalFormat.format(price));
        tvPrice.setText(decimalFormat.format(price));
    }

    @OnClick(R.id.iv_add)
    public void increment() {
        int amount = Integer.valueOf(tvAmount.getText().toString());
        if(amount < 10) {
            tvAmount.setText(String.valueOf(amount + 1));
            tvTotal.setText(String.valueOf(decimalFormat.format(price * (amount + 1))));
        }
        handleButtons(Integer.valueOf(tvAmount.getText().toString()));
    }

    @OnClick(R.id.iv_sub)
    public void decrement() {
        int amount = Integer.valueOf(tvAmount.getText().toString());
        if(amount > 1) {
            tvAmount.setText(String.valueOf(amount - 1));
            tvTotal.setText(String.valueOf(decimalFormat.format(price*(amount-1))));
        }
        handleButtons(Integer.valueOf(tvAmount.getText().toString()));
    }

    private void handleButtons(int amount) {
        if(amount == 10)
            ivAdd.setImageResource(R.drawable.ic_add_circle_outline_disable);
        else
            ivAdd.setImageResource(R.drawable.ic_add_circle_outline);

        if(amount > 1)
            ivSub.setImageResource(R.drawable.ic_remove_circle_outline_enable);
        else
            ivSub.setImageResource(R.drawable.ic_remove_circle_outline);
    }

    @OnClick(R.id.iv_remove)
    public void removeProduct() {
        card.setVisibility(View.GONE);
        cardClear.setVisibility(View.VISIBLE);
        tvTotal.setText("0,00");
        btFinish.setEnabled(false);
    }

    private Bundle getProductOnBundle(){
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCT, presenter.getProduct());
        bundle.putString(AMOUNT, tvAmount.getText().toString());
        bundle.putString(QUANTITY, tvAmount.getText().toString());
        bundle.putString(TOTAL, tvTotal.getText().toString());
        bundle.putString(TOTAL_AMOUNT, tvTotal.getText().toString());
        bundle.putString(TOTAL_VALUE, tvPrice.getText().toString());
        return bundle;
    }

    @OnClick(R.id.bt_payment_method)
    public void pay() {
        Bundle bundle = getProductOnBundle();

        PaymentMethodFragment paymentMethodFragment = new PaymentMethodFragment();
        paymentMethodFragment.setArguments(bundle);
        FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, paymentMethodFragment);
    }

    @Override
    public void onDestroyView() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.cart));
    }
}