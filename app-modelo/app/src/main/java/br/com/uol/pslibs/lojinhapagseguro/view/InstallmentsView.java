package br.com.uol.pslibs.lojinhapagseguro.view;

import android.app.Activity;

import br.com.uol.pslibs.checkout_in_app.transparent.vo.Installment;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;

public interface InstallmentsView {

    void showProgress();

    Activity getActivity();

    void progressDismiss();

    void onFailure(String message);

    void showValueInstallments(final String valueInstallment);

    void setInstallmentsOnBundle(final String valueFormated, final InstallmentVO installment, String totalAmount);

    void setAdapterInstallments();
}
