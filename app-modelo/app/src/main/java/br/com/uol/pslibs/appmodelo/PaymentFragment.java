package br.com.uol.pslibs.appmodelo;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import br.com.uol.pslibs.checkout_in_app.PSCheckout;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSBilletListener;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSCheckoutListener;
import br.com.uol.pslibs.checkout_in_app.transparent.listener.PSInstallmentsListener;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSBilletRequest;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutResponse;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSInstallmentsResponse;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PSTransparentDefaultRequest;
import br.com.uol.pslibs.checkout_in_app.transparent.vo.PaymentResponseVO;
import br.com.uol.pslibs.checkout_in_app.wallet.listener.MainCardCallback;
import br.com.uol.pslibs.checkout_in_app.wallet.util.PSCheckoutConfig;
import br.com.uol.pslibs.checkout_in_app.wallet.view.components.PaymentButton;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PSCheckoutRequest;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PSWalletMainCardVO;
import br.com.uol.pslibs.checkout_in_app.wallet.vo.PagSeguroResponse;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PaymentFragment extends Fragment {

    private static final String SELLER_EMAIL = "<Email do vendedor>";
    private static final String SELLER_TOKEN = "<Token do vendedor>";
    private final String NOTIFICATION_URL_PAYMENT = "https://pagseguro.uol.com.br/lojamodelo-qa/RetornoAutomatico-OK.jsp";

    @BindView(R.id.wallet_payment_button)
    PaymentButton cardWallet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_fragment, container, false);
        ButterKnife.bind(this, view);

        initWallet();
        initTransparent();
        configurePayment();
        return view;
    }

    private void initWallet(){
        //Inicialização a lib com parametros necessarios
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SELLER_TOKEN);
        //Informe o fragment container
        psCheckoutConfig.setContainer(R.id.fragment_container);

        //Inicializa apenas os recursos da carteira
        PSCheckout.initWallet(getActivity(), psCheckoutConfig);
    }

    private void initTransparent(){
        PSCheckoutConfig psCheckoutConfig = new PSCheckoutConfig();
        psCheckoutConfig.setSellerEmail(SELLER_EMAIL);
        psCheckoutConfig.setSellerToken(SELLER_TOKEN);
        //Informe o fragment container
        psCheckoutConfig.setContainer(R.id.fragment_container);

        //Inicializa apenas os recursos de pagamento transparente e boleto
        PSCheckout.initTransparent(getActivity(), psCheckoutConfig);
    }

    public void configurePayment() {
        String productId = "001";
        String description = "CAFE NESPRESSO";

        cardWallet.configurePayment(productId, description, 2.50, 1, R.id.fragment_container, getActivity(),
                SELLER_EMAIL, SELLER_TOKEN, psCheckoutListener);
    }

    br.com.uol.pslibs.checkout_in_app.wallet.listener.PSCheckoutListener psCheckoutListener = new br.com.uol.pslibs.checkout_in_app.wallet.listener.PSCheckoutListener() {
        @Override
        public void onSuccess(PagSeguroResponse pagSeguroResponse, Context context) {
            Toast.makeText(getActivity(), "Sucesso de pagamento",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(PagSeguroResponse pagSeguroResponse, Context context) {
            Toast.makeText(getActivity(), "Falha no pagamento",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProgress(Context context) {
            Toast.makeText(getActivity(), "Pagamento em andamento",Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCloseProgress(Context context) {

        }
    };

    @OnClick(R.id.list_card)
    public void listCards(){
        Toast.makeText(getActivity(), "Listar cartões",Toast.LENGTH_LONG).show();
        PSCheckout.showListCards();
    }

    @OnClick(R.id.main_card)
    public void getMainCard(){
        Toast.makeText(getActivity(), "Pegar o cartão principal",Toast.LENGTH_LONG).show();

         //String getCardBrand() -> Retorna o nome da bandeira do cartão principal;
        //String getFinalCard() -> Retorna o final do cartão utilizado

        //Exemplo utilização do metodo PSCheckout.getMainCard(mainCardCallback)

        /* Obtem o cartão principal setado na lib, caso queira deixar visivel no App.
        *
        * @Return String Nome da bandeira do cartão
        * @Return String Final cartão
        */
        if (PSCheckout.isLoggedUser()) {
            PSCheckout.getMainCard(new MainCardCallback() {
                @Override
                public void onSuccess(PSWalletMainCardVO mainCardVO) {
                    if (mainCardVO != null) {
                        Toast.makeText(getActivity(), mainCardVO.getCardBrand() + " **** " + mainCardVO.getFinalCard(),Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getActivity(), "Sem cartão principal no momento...", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFail() {

                }
            });
        }else {
            Toast.makeText(getActivity(), "Usuario não está logado", Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.wallet)
    public void paymentCreditCardWallet(){
        Toast.makeText(getActivity(),"Pagamento com cartão de credito carteira",Toast.LENGTH_LONG).show();
        //Valor do produto / serviço
        Double productPrice = 1.0;
        //id do produto
        String productId = "001";
        //Descrição do produto
        String description = "Produto Exemplo";

        PSCheckoutRequest psCheckoutRequest =
                new PSCheckoutRequest().withReferenceCode("123")
                        .withNewItem(description, "1", productPrice, productId);

        PSCheckout.payWallet(psCheckoutRequest, psCheckoutListener);
    }

    @OnClick(R.id.logout)
    public void logout(){
        Toast.makeText(getActivity(),"Logout do carteira",Toast.LENGTH_LONG).show();
        PSCheckout.logout(getActivity());
    }

    @OnClick(R.id.installment)
    public void installment(){
        Toast.makeText(getActivity(),"Parcelamento do pagamento",Toast.LENGTH_LONG).show();
        PSCheckout.getInstallments("4024007192262755", "50.00", psInstallmentsListener);
    }

    @OnClick(R.id.credit_card_default)
    public void paymentCreditCardDefault(){
        InstallmentVO installment = new InstallmentVO();
        installment.setAmount(Double.parseDouble("50.00"));
        installment.setQuantity(1);

        PSTransparentDefaultRequest psTransparentDefaultRequest = new PSTransparentDefaultRequest();
        psTransparentDefaultRequest
                .setDocumentNumber("80525612050")
                .setName("João da Silva")
                .setEmail("joao.silva@teste.com")
                .setAreaCode("34")
                .setPhoneNumber("999508523")
                .setStreet("Rua Tapajos")
                .setAddressComplement("")
                .setAddressNumber("23")
                .setDistrict("Saraiva")
                .setCity("Uberlândia")
                .setState("MG")
                .setCountry("BRA")
                .setPostalCode("38408414")
                .setTotalValue("45.00")
                .setAmount("50.00")
                .setQuantity(1)
                .setCreditCardName("João da Silva")
                .setCreditCard("4532645179108668")
                .setCvv("817")
                .setExpMonth("12")
                .setExpYear("18")
                .setDescriptionPayment("Pagamento do teste de integração")
                .setBirthDate("04/05/1988")
                .setExtraAmount("-5.00")
                .setInstallments(null)
                .setNottificationUrl(NOTIFICATION_URL_PAYMENT);
        Toast.makeText(getActivity(),"Pagamento com o cartao de credito default",Toast.LENGTH_LONG).show();
        PSCheckout.payTransparentDefault(psTransparentDefaultRequest, psCheckoutListenerTransparent,
                (AppCompatActivity) getActivity());
    }

    @OnClick(R.id.billet)
    public void paymentBillet(){
        PSBilletRequest psBilletRequest = new PSBilletRequest();
        psBilletRequest
                .setDocumentNumber("99404021040") // Documento do comprador
                .setName("João da silva") // Nome do comprador
                .setEmail("joao.silva@teste.com") // Email do comprador
                .setAreaCode("34") // Código de área (ddd)
                .setPhoneNumber("999508523") //Numero do telefone sem o ddd
                .setStreet("Rua Tapajos") // Rua do comprador
                .setAddressComplement("") // Complemento do endereco
                .setAddressNumber("23") // Numero do endereco
                .setDistrict("Saraiva") // Bairro
                .setCity("Uberlândia") //Cidade
                .setState("MG") //Estado
                .setCountry("Brasil") // País
                .setPostalCode("38408414") //CEP
                .setTotalValue(2.50) //VALOR DA TRANSACAO
                .setAmount(2.50) // MONTANTE DA TRANSACAO
                .setDescriptionPayment("Pagamento do teste de integração") //DESCRICAO DO PRODUTO/SERVICO
                .setExtraAmount("-0.50")
                .setQuantity(1)
                .setNottificationUrl(NOTIFICATION_URL_PAYMENT);

        Toast.makeText(getActivity(),"Pagamento com o boleto",Toast.LENGTH_LONG).show();
        PSCheckout.generateBooklet(psBilletRequest, psBilletListener, (AppCompatActivity) getActivity());
    }

    @OnClick(R.id.credit_card_gateway)
    public void paymentCreditCardGateway(){
        br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutRequest psCheckoutRequest = new br.com.uol.pslibs.checkout_in_app.transparent.vo.PSCheckoutRequest();

        //NUMERO DO CARTAO
        psCheckoutRequest.setCreditCard("4024007192262755");
        //CVV DO CARTAO
        psCheckoutRequest.setCvv("378");
        //MÊS DE EXPIRACAO (Ex: 03)
        psCheckoutRequest.setExpMonth("06");
        //ANO DE EXPIRACAO, ULTIMOS 2 DIGITOS (Ex: 17)
        psCheckoutRequest.setExpYear("18");
        //VALOR DA TRANSACAO
        psCheckoutRequest.setAmountPayment(2.50);
        //DESCRICAO DO PRODUTO/SERVICO
        psCheckoutRequest.setDescriptionPayment("Pagamento do teste de integração");
        //QUANTIDADE DE PARCELAS
        psCheckoutRequest.setInstallments(1);
        //QUANTIDADE DE PRODUTOS
        psCheckoutRequest.setQuantity("1");

        Toast.makeText(getActivity(),"Pagamento com o cartao de credito gateway",Toast.LENGTH_LONG).show();
        PSCheckout.payTransparentGateway(psCheckoutRequest, psCheckoutListenerTransparent, (AppCompatActivity) getActivity());
    }

    PSInstallmentsListener psInstallmentsListener = new PSInstallmentsListener() {
        @Override
        public void onSuccess(PSInstallmentsResponse responseVO) {
            //responseVO objeto com a lista de parcelas
            // Item da lista de parcelas InstallmentVO:
            // (String) installmentVO.getCardBrand() - Bandeira do cartão;
            // (int) - installmentVO.getQuantity() - quantidade da parcela;
            // (Double) - installmentVO.getAmount() - valor da parcela;
            // (Double) - installmentVO.getTotalAmount() - Valor total da transação parcelada;
            InstallmentVO installmentVO = responseVO.getInstallments().get(responseVO.getInstallments().size() -1);
            Toast.makeText(getActivity(),"Parcelado em " +
                    installmentVO.getQuantity() +" x R$" + installmentVO.getAmount(),Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(String message) {
            // falha na requisicao
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        }
    };

    private PSCheckoutListener psCheckoutListenerTransparent = new PSCheckoutListener() {
        @Override
        public void onSuccess(PSCheckoutResponse responseVO) {
            // responseVO.getCode() - Codigo da transação
            // responseVO.getStatus() - Status da transação
            // responseVO.getMessage() - Mensagem de retorno da transação(Sucesso/falha)
            Toast.makeText(getActivity(), "Success: "+responseVO.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(PSCheckoutResponse responseVO) {
            Toast.makeText(getActivity(), "Fail: "+responseVO.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProcessing() {
            Toast.makeText(getActivity(), "Processing...", Toast.LENGTH_LONG).show();
        }
    };

    private PSBilletListener psBilletListener = new PSBilletListener() {
        @Override
        public void onSuccess(PaymentResponseVO responseVO) {
            // responseVO.getBookletNumber() - numero do codigo de barras do boleto
            // responseVO.getPaymentLink() - link para download do boleto
            Toast.makeText(getActivity(), "Gerou boleto com o numero: "+ responseVO.getBookletNumber(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFailure(Exception e) {
            // Error
            Toast.makeText(getActivity(), "Falha ao gerar boleto", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onProcessing() {
            // Progress
            Toast.makeText(getActivity(), "Processing...", Toast.LENGTH_LONG).show();
        }
    };

}
