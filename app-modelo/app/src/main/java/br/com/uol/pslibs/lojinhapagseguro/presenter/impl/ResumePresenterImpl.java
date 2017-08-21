package br.com.uol.pslibs.lojinhapagseguro.presenter.impl;

import android.support.v7.app.AppCompatActivity;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSCheckoutListener;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutResponse;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSTransparentDefaultRequest;
import br.com.uol.pslibs.lojinhapagseguro.presenter.ResumePresenter;
import br.com.uol.pslibs.lojinhapagseguro.view.ResumeView;

public class ResumePresenterImpl implements ResumePresenter, PSCheckoutListener {

    private ResumeView view;

    @Override
    public void attachView(ResumeView view) {
        this.view = view;
    }

    @Override
    public void dettachView() {

    }

    @Override
    public void payCreditCard(String number, String expiry, String cvv, String totalValue, String productName,
                              int installment, String quantity, AppCompatActivity appCompatActivity) {
        br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutRequest psCheckoutRequest = new br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutRequest();

        //NUMERO DO CARTAO
        psCheckoutRequest.setCreditCard(number);
        //CVV DO CARTAO
        psCheckoutRequest.setCvv(cvv);
        //MÊS DE EXPIRACAO (Ex: 03)
        psCheckoutRequest.setExpMonth(expiry.substring(0,2));
        //ANO DE EXPIRACAO, ULTIMOS 2 DIGITOS (Ex: 17)
        psCheckoutRequest.setExpYear(expiry.substring(3,5));
        //VALOR DA TRANSACAO
        psCheckoutRequest.setAmountPayment(Double.parseDouble(totalValue.replace(",",".")));
        //DESCRICAO DO PRODUTO/SERVICO
        psCheckoutRequest.setDescriptionPayment(productName);
        //QUANTIDADE DE PARCELAS
        psCheckoutRequest.setInstallments(installment);
        //QUANTIDADE DE PRODUTOS
        psCheckoutRequest.setQuantity(quantity);
        onProcessing();
        PSCheckout.payTransparentGateway(psCheckoutRequest, this, appCompatActivity);
    }

    @Override
    public void payTransparentDefault(String documentNumber, String email, String name, String areaCode, String phoneNumber,
                                      String postalCode, String country, String state, String city, String district,
                                      String street, String addressNumber, String addressComplement, String descriptionPayment,
                                      String quantity, String amount, String number, String expiry, String cvv, String productPrice,
                                      String totalValue, String productName, InstallmentVO installment, String birthDate,
                                      AppCompatActivity appCompatActivity) {

        PSTransparentDefaultRequest psTransparentDefaultRequest = new PSTransparentDefaultRequest();
        psTransparentDefaultRequest
                .setDocumentNumber(documentNumber)
                .setName(name)
                .setEmail(email)
                .setAreaCode(phoneNumber.substring(0,2))
                .setPhoneNumber(phoneNumber.substring(2, phoneNumber.length()))
                .setStreet(street)
                .setAddressComplement(addressComplement)
                .setAddressNumber(addressNumber)
                .setDistrict(district)
                .setCity(city)
                .setState(state)
                .setCountry(country)
                .setPostalCode(postalCode)
                .setTotalValue(totalValue)
                .setAmount(productPrice)
                .setDescriptionPayment(descriptionPayment)
                .setQuantity(Integer.valueOf(quantity))
                .setCreditCard(number)
                .setCvv(cvv)
                .setExpMonth(expiry.substring(0,2))
                .setExpYear(expiry.substring(3,5))
                .setDescriptionPayment(productName)
                .setBirthDate(birthDate)
                .setInstallments(installment);
        onProcessing();
        PSCheckout.payTransparentDefault(psTransparentDefaultRequest, this, appCompatActivity);
    }

    @Override
    public String formatCardNumber(String cardNumber) {
        if(cardNumber != null && cardNumber.startsWith("3") && cardNumber.length() > 4) {
            return "••••  ••••••  " + cardNumber.substring(10);
        } else if (cardNumber.length() > 4) {
            return "••••  ••••  ••••  " + cardNumber.substring(12);
        }else {
            return "••••  ••••  ••••  " + cardNumber;
        }
    }

    @Override
    public void onSuccess(PSCheckoutResponse responseVO) {
        view.showSuccessTransparent(responseVO.getMessage());
        view.progressDismiss();
    }

    @Override
    public void onFailure(PSCheckoutResponse responseVO) {
        view.showFailureTransparent(responseVO.getMessage());
        view.progressDismiss();
    }

    @Override
    public void onProcessing() {
        view.showProgress();
    }
}
