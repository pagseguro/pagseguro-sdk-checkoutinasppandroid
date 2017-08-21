package br.com.uol.pslibs.lojinhapagseguro.presenter.impl;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSSearchAddressListener;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PostalCodeResponseVO;
import br.com.uol.pslibs.lojinhapagseguro.presenter.AddressPresenter;
import br.com.uol.pslibs.lojinhapagseguro.view.AddressView;

public class AddressPresenterImpl implements AddressPresenter, PSSearchAddressListener {

    private AddressView view;

    @Override
    public void searchAddress(String postalCode) {
        PSCheckout.searchPostalCode(postalCode, this);
    }

    @Override
    public void onSuccess(PostalCodeResponseVO responseVO) {
        view.onSuccess(responseVO);
    }

    @Override
    public void onFailure(String message) {
        view.onFailure(message);
    }

    @Override
    public void attachView(AddressView view) {
        this.view = view;
    }

    @Override
    public void dettachView() {

    }
}
