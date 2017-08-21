package br.com.uol.pslibs.lojinhapagseguro.view;


import br.com.uol.pslibs.checkout_in_app.transparent.vo.PostalCodeResponseVO;

/**
 * Created by alex on 13/04/17.
 */

public interface AddressView {

    void onSuccess(PostalCodeResponseVO responseVO);

    void onFailure(String message);
}
