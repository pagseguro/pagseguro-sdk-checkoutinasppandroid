package br.com.uol.pslibs.lojinhapagseguro.presenter;

import br.com.uol.pslibs.lojinhapagseguro.view.InstallmentsView;

public interface InstallmentsPresenter extends BasePresenter<InstallmentsView>{

    void getInstallments(final String cardNumber, final Double value);

    String[] generateInstallments(final Double value);

    void onItemSelectedInstallments(int position);

}
