package br.com.uol.pslibs.lojinhapagseguro.view;

import android.support.v7.app.AppCompatActivity;

import okhttp3.ResponseBody;

public interface BilletView {

    AppCompatActivity getAppActivity();

    void onSuccess(final String barCode);

    void onFailure();

    void onProcessing();

    void writeFile(ResponseBody body);

    void showConfirmMessage();
}
