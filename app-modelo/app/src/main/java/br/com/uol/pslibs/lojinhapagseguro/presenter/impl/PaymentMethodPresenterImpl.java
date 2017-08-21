package br.com.uol.pslibs.lojinhapagseguro.presenter.impl;

import android.content.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.wallet.api.helper.Environment;
import br.com.uol.pslibs.checkout_in_app.wallet.listener.PSCheckoutListener;
import br.com.uol.pslibs.checkout_in_app.wallet.util.PSCheckoutConfig;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PSCheckoutRequest;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PagSeguroResponse;
import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.presenter.PaymentMethodPresenter;
import br.com.uol.pslibs.lojinhapagseguro.util.SellerInfo;
import br.com.uol.pslibs.lojinhapagseguro.view.PaymentMethodView;

public class PaymentMethodPresenterImpl implements PaymentMethodPresenter, PSCheckoutListener {

    private PaymentMethodView view;

    @Override
    public void attachView(PaymentMethodView view) {
        this.view = view;
    }

    @Override
    public void dettachView() {

    }

    @Override
    public void configLibTransparent() {
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SellerInfo.SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SellerInfo.SELLER_AUTH_TOKEN);
        PSCheckout.initTransparent(view.getActivity(), psCheckoutConfig);
    }

    @Override
    public void payWallet(Product product, int qtd) {
        BigDecimal amount = new BigDecimal(product.getPrice()).setScale(2, RoundingMode.CEILING);
        String productId = "001";
        String description = "CAFE NESPRESSO";
        PSCheckoutRequest psCheckoutRequest = new PSCheckoutRequest().withReferenceCode("123")
                .withNewItem(description, String.valueOf(qtd), amount, productId);
        PSCheckout.payWallet(psCheckoutRequest, this);
    }

    @Override
    public void configLibWallet(final int container) {
        //Inicialização a lib com parametros necessarios
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SellerInfo.SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SellerInfo.SELLER_AUTH_TOKEN);
        psCheckoutConfig.setEnvironment(Environment.QA);
        psCheckoutConfig.setContainer(container);

        PSCheckout.initWallet(view.getActivity(), psCheckoutConfig);
    }

    @Override
    public void ListCards() {
        PSCheckout.showListCards();
    }

    @Override
    public void onSuccess(PagSeguroResponse pagSeguroResponse, Context context) {
        view.hideLoading();
        view.showSuccess();
    }

    @Override
    public void onFailure(PagSeguroResponse pagSeguroResponse, Context context) {
        view.hideLoading();
        view.showError(pagSeguroResponse.getMessage());
    }

    @Override
    public void onProgress(Context contex) {
        view.showLoading();
    }

    @Override
    public void onCloseProgress(Context contex) {
        view.hideLoading();
    }
}
