package br.com.uol.pslibs.lojinhapagseguro.view.fragments;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CardEditActivity;
import com.cooltechworks.creditcarddesign.CreditCardUtils;

import br.com.uol.pslibs.checkout_in_app.wallet.listener.PSCheckoutListener;
import br.com.uol.pslibs.checkout_in_app.wallet.view.components.PaymentButton;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PSWalletMainCardVO;
import br.com.uol.pslibs.lojinhapagseguro.MainActivity;
import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.presenter.PaymentMethodPresenter;
import br.com.uol.pslibs.lojinhapagseguro.presenter.impl.PaymentMethodPresenterImpl;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.util.SellerInfo;
import br.com.uol.pslibs.lojinhapagseguro.view.PaymentMethodView;
import br.com.uol.pslibs.lojinhapagseguro.view.helper.PaymentMethod;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaymentMethodFragment extends Fragment implements PaymentMethodView {

    @BindView(R.id.bt_pay)
    Button btPay;

    @BindView(R.id.wallet_payment_button)
    PaymentButton cardWallet;

    @BindView(R.id.cd_credit_card_gateway)
    CardView cardCreditCardTransparentGateway;

    @BindView(R.id.cd_credit_card_default)
    CardView cardCreditCardTransparentDefault;

    @BindView(R.id.cd_billet)
    CardView cardBilletTransparent;

    @BindView(R.id.txt_billet_message)
    TextView tvBilletMessage;

    @BindView(R.id.iv_check_billet)
    ImageView ivCheckBillet;

    @BindView(R.id.iv_check_card_default)
    ImageView ivCheckCardDefault;

    @BindView(R.id.iv_check_card_gateway)
    ImageView ivCheckCardGateway;


    private PaymentMethodPresenter presenter;
    private Product product;
    private String quantity;
    private String value;
    private String total;
    private PaymentMethod paymentMethod;
    private PSWalletMainCardVO mainCard;
    private ProgressDialog progressDialog;
    private SweetAlertDialog pDialog;
    private boolean canMainCard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        product = (Product) getArguments().get(Constants.PRODUCT);
        quantity = getArguments().getString(Constants.AMOUNT);
        value = getArguments().getString(Constants.TOTAL_VALUE);
        total = getArguments().getString(Constants.TOTAL);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_method_fragment, container, false);
        ButterKnife.bind(this, view);
        progressDialog = new ProgressDialog(getActivity());
        presenter = new PaymentMethodPresenterImpl();
        presenter.attachView(this);
        presenter.configLibWallet(R.id.fragment_container);
        presenter.configLibTransparent();

        configurePayment();
        return view;
    }

    public void configurePayment() {
        String productId = "001";
        String description = "CAFE NESPRESSO";

        cardWallet.configurePayment(productId, description, product.getPrice(), Integer.valueOf(quantity),
                R.id.fragment_container, getActivity(), SellerInfo.SELLER_EMAIL,
                SellerInfo.SELLER_AUTH_TOKEN, (PSCheckoutListener) presenter);
    }

    @OnClick(R.id.cd_credit_card_gateway)
    public void creditCardTransparentGateway(){
        setColorCardWhite();
        btPay.setVisibility(View.VISIBLE);
        setSelectedItem(PaymentMethod.CREDIT_CARD_TRANSPARENT_GATEWAY);
        paymentMethod = PaymentMethod.CREDIT_CARD_TRANSPARENT_GATEWAY;
        tvBilletMessage.setVisibility(View.GONE);
    }

    @OnClick(R.id.cd_credit_card_default)
    public void creditCardTransparentDefault(){
        setColorCardWhite();
        btPay.setVisibility(View.VISIBLE);
        setSelectedItem(PaymentMethod.CREDIT_CARD_TRANSPARENT_DEFAULT);
        paymentMethod = PaymentMethod.CREDIT_CARD_TRANSPARENT_DEFAULT;
        tvBilletMessage.setVisibility(View.GONE);
    }

    @OnClick(R.id.cd_billet)
    public void billetTransparent(){
        btPay.setVisibility(View.VISIBLE);
        setSelectedItem(PaymentMethod.BILLET);
        paymentMethod = PaymentMethod.BILLET;
        tvBilletMessage.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.bt_pay)
    public void pay(){
        switch (paymentMethod){
            case CREDIT_CARD_TRANSPARENT_GATEWAY:
                callCreditCardTransparent();
                break;
            case CREDIT_CARD_TRANSPARENT_DEFAULT:
                callCreditCardTransparent();
                break;
            case BILLET:
                callPeronalInfoScreen();
                break;
        }
    }

    private void setSelectedItem(PaymentMethod selectedItem){
        switch (selectedItem){
            case CREDIT_CARD_TRANSPARENT_GATEWAY:
                ivCheckCardGateway.setVisibility(View.VISIBLE);
                ivCheckCardDefault.setVisibility(View.GONE);
                ivCheckBillet.setVisibility(View.GONE);
                break;
            case CREDIT_CARD_TRANSPARENT_DEFAULT:
                ivCheckCardDefault.setVisibility(View.VISIBLE);
                ivCheckCardGateway.setVisibility(View.GONE);
                ivCheckBillet.setVisibility(View.GONE);
                break;
            case BILLET:
                ivCheckCardDefault.setVisibility(View.GONE);
                ivCheckCardGateway.setVisibility(View.GONE);
                ivCheckBillet.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void setColorCardWhite(){
        cardCreditCardTransparentGateway.setCardBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        cardCreditCardTransparentDefault.setCardBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
        cardBilletTransparent.setCardBackgroundColor(ContextCompat.getColor(getActivity(), android.R.color.white));
    }

    private void callCreditCardTransparent(){
        Intent intent = new Intent(getActivity(), CardEditActivity.class);
        ActivityOptions options =
                ActivityOptions.makeCustomAnimation(getContext(), R.anim.slide_in_left, R.anim.slide_out_left);
        startActivityForResult(intent, Constants.GET_NEW_CARD, options.toBundle());
    }

    private void callPeronalInfoScreen(){
        Bundle bundle = setPaymentMethodOnBundle();
        PersonalInfoFragment infoFragment = new PersonalInfoFragment();
        infoFragment.setArguments(bundle);
        FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, infoFragment);
    }

    private Bundle setPaymentMethodOnBundle(){
        Bundle bundle = getArguments();
        bundle.putSerializable(Constants.PAYMENT_METHOD, paymentMethod);
        return bundle;
    }

    private void showResultScreen(String message, boolean isSuccess){
        ResultFragment resultFragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ResumeFragment.MESSAGE, message);
        bundle.putBoolean(ResumeFragment.IS_SUCCESS, isSuccess);
        resultFragment.setArguments(bundle);
        FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, resultFragment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == getActivity().RESULT_OK) {
            Bundle bundle = setPaymentMethodOnBundle();
            bundle.putString(CreditCardUtils.EXTRA_CARD_HOLDER_NAME, data.getStringExtra(CreditCardUtils.EXTRA_CARD_HOLDER_NAME));
            bundle.putString(CreditCardUtils.EXTRA_CARD_NUMBER, data.getStringExtra(CreditCardUtils.EXTRA_CARD_NUMBER));
            bundle.putString(CreditCardUtils.EXTRA_CARD_EXPIRY, data.getStringExtra(CreditCardUtils.EXTRA_CARD_EXPIRY));
            bundle.putString(CreditCardUtils.EXTRA_CARD_CVV, data.getStringExtra(CreditCardUtils.EXTRA_CARD_CVV));

            InstallmentsFragment installmentsFragment = new InstallmentsFragment();
            installmentsFragment.setArguments(bundle);
            FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, installmentsFragment);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.payment_method);
    }

    @Override
    public void showSuccess() {
        showResultScreen("Pagamento Aprovado", true);
    }

    @Override
    public void showLoading() {
        if (pDialog == null) {
            pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(R.color.loading_bar_color);
            pDialog.setTitleText(getString(R.string.txt_wait));
            pDialog.setCancelable(false);
        }
        pDialog.show();
    }

    @Override
    public void hideLoading() {
        pDialog.dismiss();
    }

    @Override
    public void showError(String message) {
        showResultScreen(message, false);
    }

    @Override
    public void goToHome() {
        FragmentUtils.clearBackStack(getFragmentManager());
        FragmentUtils.replaceFragment(getFragmentManager(), R.id.fragment_container, new ProductDetailFragment());
    }
}
