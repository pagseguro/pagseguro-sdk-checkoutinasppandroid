package br.com.uol.pslibs.lojinhapagseguro.view;

public interface ResumeView {

    void showProgress();

    void showSuccessTransparent(final String message);

    void showFailureTransparent(final String message);

    void progressDismiss();
}
