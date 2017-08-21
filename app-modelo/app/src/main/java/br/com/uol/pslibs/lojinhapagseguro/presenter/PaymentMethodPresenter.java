package br.com.uol.pslibs.lojinhapagseguro.presenter;

import android.app.Activity;

import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.view.PaymentMethodView;

public interface PaymentMethodPresenter extends BasePresenter<PaymentMethodView>{

    void ListCards();

    void configLibTransparent();

    void payWallet(Product product, int qtd);

    void configLibWallet(final int container);
}
