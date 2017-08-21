package br.com.uol.pslibs.lojinhapagseguro.view;

import android.app.Activity;

public interface PaymentMethodView {

    Activity getActivity();

    void showSuccess();

    void showLoading();

    void hideLoading();

    void showError(String message);

    void goToHome();
}
