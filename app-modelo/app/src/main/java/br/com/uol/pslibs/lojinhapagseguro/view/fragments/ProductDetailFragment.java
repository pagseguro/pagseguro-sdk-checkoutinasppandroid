package br.com.uol.pslibs.lojinhapagseguro.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.uol.pslibs.lojinhapagseguro.MainActivity;
import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.presenter.ProductDetailPresenter;
import br.com.uol.pslibs.lojinhapagseguro.presenter.impl.ProductDetailPresenterImpl;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.view.ProductDetailView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class ProductDetailFragment extends Fragment implements ProductDetailView {

    @BindView(R.id.tv_price) TextView tvPrice;

    private ProductDetailPresenter presenter;
    private Product product;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_detail, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View layout) {
        presenter = new ProductDetailPresenterImpl();
        presenter.attachView(this);

        ButterKnife.bind(this, layout);
        ((MainActivity)getActivity()).enableMenu();

        presenter.loadProduct();
    }

    @OnClick(R.id.bt_pay)
    public void pay() {
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constants.PRODUCT, product);
        ShoppingFragment shoppingFragment = new ShoppingFragment();
        shoppingFragment.setArguments(bundle);
        FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, shoppingFragment);
    }

    @Override
    public void showProduct(Product product) {
        this.product = product;
        tvPrice.setText(product.getPriceString().replace(".",","));
    }

    @Override
    public void showError() {
        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                .setTitleText(getString(R.string.txt_error))
                .setContentText(getString(R.string.txt_get_product_error))
                .show();
    }

    @Override
    public void onDestroy() {
        presenter.dettachView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.products));
    }
}
