package br.com.uol.pslibs.lojinhapagseguro.presenter;

import br.com.uol.pslibs.checkout_in_app.transparent.vo.PaymentResponseVO;
import br.com.uol.pslibs.lojinhapagseguro.view.BilletView;

public interface BilletPresenter extends BasePresenter<BilletView>{

    void generate(String documentNumber, String email, String name, String areaCode, String phoneNumber, String postalCode,
                  String country, String state, String city, String district, String street, String addressNumber,
                  String addressComplement, double totalValue, String descriptionPayment, String quantity, String amount);

    PaymentResponseVO getBillet();

    void downloadFile();
}
