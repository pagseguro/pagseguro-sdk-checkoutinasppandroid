package br.com.uol.pslibs.lojinhapagseguro.presenter;

import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.view.ShoppingView;

public interface ShoppingPresenter extends BasePresenter<ShoppingView>{

    void setProduct(Object product);

    Product getProduct();

}
