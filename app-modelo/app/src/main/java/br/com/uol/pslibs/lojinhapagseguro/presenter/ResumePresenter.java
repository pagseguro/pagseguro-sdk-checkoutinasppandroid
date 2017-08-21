package br.com.uol.pslibs.lojinhapagseguro.presenter;

import android.support.v7.app.AppCompatActivity;

import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;
import br.com.uol.pslibs.lojinhapagseguro.view.ResumeView;

public interface ResumePresenter extends BasePresenter<ResumeView>{

    void payCreditCard(String number, String expiry, String cvv, String totalValue, String productName,
                       int installment, String quantity, AppCompatActivity appCompatActivity);

    void payTransparentDefault(String documentNumber, String email, String name, String areaCode, String phoneNumber, String postalCode,
                               String country, String state, String city, String district, String street, String addressNumber,
                               String addressComplement, String descriptionPayment, String quantity, String amount,
                               String number, String expiry, String cvv, String productPrice, String totalValue, String productName,
                               InstallmentVO installment, String birthDate, AppCompatActivity appCompatActivity);

    String formatCardNumber(String cardNumber);

}
