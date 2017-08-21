package br.com.uol.pslibs.lojinhapagseguro.presenter;

import br.com.uol.pslibs.lojinhapagseguro.view.AddressView;

public interface AddressPresenter extends BasePresenter<AddressView>{

    void searchAddress(String postalCode);
}
