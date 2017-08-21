package br.com.uol.pslibs.lojinhapagseguro.presenter.impl;

import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.presenter.ProductDetailPresenter;
import br.com.uol.pslibs.lojinhapagseguro.view.ProductDetailView;
import rx.Subscription;

public class ProductDetailPresenterImpl implements ProductDetailPresenter {

    private ProductDetailView view;
    private Subscription subscription;

    @Override
    public void loadProduct() {
        view.showProduct(mockProduct());
    }

    @Override
    public void attachView(ProductDetailView view) {
        this.view = view;
    }

    @Override
    public void dettachView() {
        this.view = null;
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    private Product mockProduct() {
        Product product = new Product();
        product.setPriceString("2.65");
        product.setPrice(2.65);
        return product;
    }
    
}
