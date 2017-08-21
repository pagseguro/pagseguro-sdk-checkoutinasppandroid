package br.com.uol.pslibs.lojinhapagseguro.presenter.impl;

import java.text.DecimalFormat;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSInstallmentsListener;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSInstallmentsResponse;
import br.com.uol.pslibs.lojinhapagseguro.presenter.InstallmentsPresenter;
import br.com.uol.pslibs.lojinhapagseguro.util.LojinhaUtil;
import br.com.uol.pslibs.lojinhapagseguro.view.InstallmentsView;

import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.MONEY_PATTERN;

public class InstallmentsPresenterImpl implements InstallmentsPresenter {

    private InstallmentsView view;
    private PSInstallmentsResponse psInstallmentsResponse;
    private DecimalFormat decimalFormat;

    public InstallmentsPresenterImpl() {
        decimalFormat = new DecimalFormat(MONEY_PATTERN);
    }

    @Override
    public void attachView(InstallmentsView view) {
        this.view = view;
    }

    @Override
    public void dettachView() {

    }

    @Override
    public void getInstallments(String cardNumber, Double amount) {
        view.showProgress();
        String strAmount = LojinhaUtil.setComplementDecimal(amount.toString());
        PSCheckout.getInstallments(cardNumber, strAmount, psInstallmentsListener);
    }

    @Override
    public String[] generateInstallments(final Double value) {
        if (psInstallmentsResponse == null || psInstallmentsResponse.getInstallments() == null
                || psInstallmentsResponse.getInstallments().size() == 0){
            psInstallmentsResponse = new PSInstallmentsResponse();
            InstallmentVO installment = new InstallmentVO();
            installment.setAmount(value);
            installment.setQuantity(1);
            psInstallmentsResponse.getInstallments().add(installment);
        }

        int size = psInstallmentsResponse.getInstallments().size();
        String installments[] = new String[size];
        for (int i = 0; i < size; i++) {
            installments[i] = psInstallmentsResponse.getInstallments().get(i).getQuantity() + "x";
        }

        return installments;
    }

    @Override
    public void onItemSelectedInstallments(int position) {
        InstallmentVO installmentVO = psInstallmentsResponse.getInstallments().get(position);
        String textValue = "R$ " + decimalFormat.format(installmentVO.getAmount());
        String formatAmount = decimalFormat.format(installmentVO.getAmount());
        String formatTotalAmount = decimalFormat.format(installmentVO.getTotalAmount() == null
                ? installmentVO.getAmount() : installmentVO.getTotalAmount());
        view.showValueInstallments(textValue);
        view.setInstallmentsOnBundle(installmentVO.getQuantity() + "x de " + textValue, installmentVO,
                installmentVO.getTotalAmount() == null ? formatAmount : formatTotalAmount);
    }

    PSInstallmentsListener psInstallmentsListener = new PSInstallmentsListener() {
        @Override
        public void onSuccess(PSInstallmentsResponse responseVO) {
            view.progressDismiss();
            psInstallmentsResponse = responseVO;
            view.setAdapterInstallments();
        }

        @Override
        public void onFailure(String message) {
            view.progressDismiss();
            view.onFailure(message);
            view.setAdapterInstallments();
        }
    };
}
