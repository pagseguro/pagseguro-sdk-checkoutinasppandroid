package br.com.uol.pslibs.lojinhapagseguro.view;


import br.com.uol.pslibs.lojinhapagseguro.model.Product;

public interface ProductDetailView {

    void showProduct(Product product);

    void showError();

}
