package br.com.uol.pslibs.lojinhapagseguro.presenter.impl;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSBilletListener;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSBilletRequest;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PaymentResponseVO;
import br.com.uol.pslibs.lojinhapagseguro.presenter.BilletPresenter;
import br.com.uol.pslibs.lojinhapagseguro.service.DownloadService;
import br.com.uol.pslibs.lojinhapagseguro.service.impl.DownloadServiceImpl;
import br.com.uol.pslibs.lojinhapagseguro.view.BilletView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BilletPresenterImpl implements BilletPresenter, PSBilletListener {

    private BilletView view;
    private PaymentResponseVO billet;
    private DownloadService downloadService;

    @Override
    public void generate(String documentNumber, String email, String name, String areaCode, String phoneNumber, String postalCode,
                         String country, String state, String city, String district, String street, String addressNumber,
                         String addressComplement, double totalValue, String descriptionPayment, String quantity, String amount) {

        PSBilletRequest psBilletRequest = new PSBilletRequest();
        psBilletRequest
                .setDocumentNumber(documentNumber) // Documento do comprador
                .setName(name) // Nome do comprador
                .setEmail(email) // Email do comprador
                .setAreaCode(phoneNumber.substring(0,2)) // Código de área (ddd)
                .setPhoneNumber(phoneNumber.substring(2, phoneNumber.length())) //Numero do telefone sem o ddd
                .setStreet(street) // Rua do comprador
                .setAddressComplement(addressComplement) // Complemento do endereco
                .setAddressNumber(addressNumber) // Numero do endereco
                .setDistrict(district) // Bairro
                .setCity(city) //Cidade
                .setState(state) //Estado
                .setCountry(country) // País
                .setPostalCode(postalCode) //CEP
                .setTotalValue(totalValue) //VALOR DA TRANSACAO
                .setAmount(totalValue) // MONTANTE DA TRANSACAO
                .setDescriptionPayment(descriptionPayment) //DESCRICAO DO PRODUTO/SERVICO
                .setQuantity(Integer.valueOf(quantity));
        onProcessing();
        PSCheckout.generateBooklet(psBilletRequest, this, view.getAppActivity());
    }

    @Override
    public PaymentResponseVO getBillet() {
        return billet;
    }

    @Override
    public void downloadFile() {
        downloadService = new DownloadServiceImpl();

        downloadService.downloadFile(billet.getPaymentLink(), new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                view.writeFile(response.body());
                view.showConfirmMessage();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void onSuccess(PaymentResponseVO responseVO) {
        billet = responseVO;
        view.onSuccess(responseVO.getBookletNumber());
    }

    @Override
    public void onFailure(final Exception e) {
        view.onFailure();
    }

    @Override
    public void onProcessing() {
        view.onProcessing();
    }

    @Override
    public void attachView(BilletView view) {
        this.view = view;
    }

    @Override
    public void dettachView() {

    }
}
