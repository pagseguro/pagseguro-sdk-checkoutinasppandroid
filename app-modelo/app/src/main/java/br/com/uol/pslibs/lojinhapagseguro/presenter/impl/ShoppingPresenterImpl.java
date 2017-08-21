package br.com.uol.pslibs.lojinhapagseguro.presenter.impl;

import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.presenter.ShoppingPresenter;
import br.com.uol.pslibs.lojinhapagseguro.view.ShoppingView;

public class ShoppingPresenterImpl implements ShoppingPresenter{

    private ShoppingView view;
    private Product product;

    @Override
    public void attachView(ShoppingView view) {
        this.view = view;
    }

    @Override
    public void dettachView() {

    }


    @Override
    public void setProduct(Object product) {
        this.product = (Product) product;
        this.product.setDescription("Dentro de cada cápsula encontram-se os melhores cafés do mundo, misturados, torrados e moídos.");
    }

    @Override
    public Product getProduct() {
        return this.product;
    }
}
