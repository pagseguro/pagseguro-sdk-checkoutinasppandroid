package br.com.uol.pslibs.lojinhapagseguro.presenter;

public interface BasePresenter<V> {

    void attachView(V view);

    void dettachView();

}
