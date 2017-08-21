package br.com.uol.pslibs.lojinhapagseguro.view.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CreditCardUtils;

import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;
import java.text.DecimalFormat;

import br.com.uol.pslibs.checkout_in_app.wallet.util.StringUtils;
import br.com.uol.pslibs.lojinhapagseguro.MainActivity;
import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.presenter.ResumePresenter;
import br.com.uol.pslibs.lojinhapagseguro.presenter.impl.ResumePresenterImpl;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.view.ResumeView;
import br.com.uol.pslibs.lojinhapagseguro.view.helper.PaymentMethod;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.MONEY_PATTERN;

public class ResumeFragment extends Fragment implements ResumeView {

    public static final String MESSAGE = "MESSAGE";
    public static final String IS_SUCCESS = "IS_SUCCESS";
    private ProgressDialog progress;

    private String cardName;
    private String cardNumber;
    private String cardExpiry;
    private String cardCvv;

    private Product product;
    private String amount;
    private String total;
    private String totalValue;
    private int installment;
    private InstallmentVO installmentVO;
    private String payment;
    private ResumePresenter presenter;
    private PaymentMethod paymentMethod;
    private DecimalFormat decimalFormat;
    private String documentNumber;
    private String email;
    private String name;
    private String areaCode;
    private String phoneNumber;
    private String country;
    private String state;
    private String city;
    private String district;
    private String street;
    private String addressNumber;
    private String addressComplement;
    private String postalCode;
    private double amountPayment;
    private String paymentDescription;
    private String quantity;
    private String birthDate;

    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_description) TextView tvDescription;
    @BindView(R.id.tv_amount) TextView tvAmount;
    @BindView(R.id.tv_price) TextView tvPrice;
    @BindView(R.id.tv_total) TextView tvTotal;
    @BindView(R.id.tv_card_name_label) TextView tvCardNameLabel;
    @BindView(R.id.tv_card_name) TextView tvCardName;
    @BindView(R.id.tv_card_number) TextView tvCardNumber;
    @BindView(R.id.tv_card_validity_label) TextView tvCardValidityLabel;
    @BindView(R.id.tv_card_validity) TextView tvCardValidity;
    @BindView(R.id.tv_payment) TextView tvPayment;
    @BindView(R.id.card_payment_form)
    CardView cardPaymentForm;
    @BindView(R.id.layout_credit_card) LinearLayout layoutCreditCard;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        decimalFormat = new DecimalFormat(MONEY_PATTERN);
        getValues();
    }

    private void getValues(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            cardName = bundle.getString(CreditCardUtils.EXTRA_CARD_HOLDER_NAME);
            cardNumber = bundle.getString(CreditCardUtils.EXTRA_CARD_NUMBER);
            cardExpiry = bundle.getString(CreditCardUtils.EXTRA_CARD_EXPIRY);
            cardCvv = bundle.getString(CreditCardUtils.EXTRA_CARD_CVV);
            product = (Product) bundle.getSerializable(Constants.PRODUCT);
            total = bundle.getString(Constants.TOTAL);
            amount = bundle.getString(Constants.AMOUNT);
            totalValue = bundle.getString(Constants.TOTAL_VALUE);
            paymentMethod = (PaymentMethod) bundle.get(Constants.PAYMENT_METHOD);
            installment = bundle.getInt(Constants.INSTALLMENT);
            payment = bundle.getString(Constants.PAYMENT);
            quantity = bundle.getString(Constants.QUANTITY);
        }
    }

    private void getValuesTransparentDefault(){
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            product = (Product) bundle.getSerializable(Constants.PRODUCT);
            amount = bundle.getString(Constants.AMOUNT);
            paymentDescription = product.getDescription();
            documentNumber = bundle.getString(Constants.PERSONAL_DOCUMENT);
            name = bundle.getString(Constants.PERSONAL_FULL_NAME);
            email = bundle.getString(Constants.PERSONAL_EMAIL);
            phoneNumber = bundle.getString(Constants.PERSONAL_PHONE_NUMBER);
            postalCode = bundle.getString(Constants.ADDRESS_POSTAL_CODE);
            street = bundle.getString(Constants.ADDRESS_STREET);
            district = bundle.getString(Constants.ADDRESS_DISTRICT);
            city = bundle.getString(Constants.ADDRESS_CITY);
            state = bundle.getString(Constants.ADDRESS_STATE);
            addressNumber = bundle.getString(Constants.ADDRESS_NUMBER);
            addressComplement = bundle.getString(Constants.ADDRESS_COMPLEMENT);
            country = bundle.getString(Constants.ADDRESS_COUNTRY);
            totalValue = bundle.getString(Constants.TOTAL_VALUE).replace(",", ".");
            amount = bundle.getString(Constants.AMOUNT);
            quantity = bundle.getString(Constants.QUANTITY);
            birthDate = bundle.getString(Constants.PERSONAL_BIRTH_DATE);
            installmentVO = (InstallmentVO) bundle.getSerializable(Constants.INSTALLMENT_VO);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_resume, container, false);
        initComponents(view);
        createPresenter();
        setPaymentMethod();
        setValuesOnScreen();
        return view;
    }

    private void initComponents(View view) {
        ButterKnife.bind(this, view);
        progress = new ProgressDialog(getActivity());
    }

    private void createPresenter(){
        presenter = new ResumePresenterImpl();
        presenter.attachView(this);
    }

    private void setValuesOnScreen(){
        tvName.setText(product.getName());
        tvDescription.setText(product.getDescription());
        tvAmount.setText(amount);
        tvPrice.setText(product.getPriceString());
        tvTotal.setText(total);
    }

    private void setPaymentMethod(){
        if (paymentMethod == PaymentMethod.BILLET){
            setPaymentBillet();
        }else {
            setPaymentCreditCard();
        }
    }

    private void setPaymentBillet(){
        tvPayment.setText(getString(R.string.txt_payment_billet));
    }

    private void setPaymentCreditCard(){
        cardPaymentForm.setVisibility(View.VISIBLE);
        setVisibilityGone(tvCardName, tvCardNameLabel, cardName);
        tvCardNumber.setText(formatCardNumber(cardNumber));
        setVisibilityGone(tvCardValidity, tvCardValidityLabel, cardExpiry);
        tvPayment.setText(payment == null ? getString(R.string.txt_payment_credit_card) : payment);
    }

    private void setVisibilityGone(TextView view, TextView viewLabel, String data){
        if (StringUtils.isEmpty(data)){
            view.setVisibility(View.GONE);
            viewLabel.setVisibility(View.GONE);
        }else {
            view.setText(data);
        }
    }

    private void callResultScreen(boolean isSuccess, String message){
        ResultFragment resultFragment = new ResultFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ResumeFragment.MESSAGE, message);
        bundle.putBoolean(ResumeFragment.IS_SUCCESS, isSuccess);
        resultFragment.setArguments(bundle);
        FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, resultFragment);
    }

    private void callBilletScreen(){
        Bundle bundle = getArguments();
        BilletFragment billetFragment = new BilletFragment();
        billetFragment.setArguments(bundle);
        FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, billetFragment);
    }

    private String formatCardNumber(String cardNumber) {
       return presenter.formatCardNumber(cardNumber);
    }

    @OnClick(R.id.bt_confirm)
    public void callConfirm(){
        switch (paymentMethod){
            case CREDIT_CARD_TRANSPARENT_GATEWAY:
                presenter.payCreditCard(cardNumber, cardExpiry, cardCvv, product.getPriceString(), product.getName(), installment
                        , quantity, (AppCompatActivity) getActivity());
                break;
            case CREDIT_CARD_TRANSPARENT_DEFAULT:
                getValuesTransparentDefault();
                presenter.payTransparentDefault(documentNumber, email, name, areaCode, phoneNumber, postalCode,
                        country, state, city, district, street, addressNumber, addressComplement,
                        paymentDescription, quantity, amount ,cardNumber, cardExpiry, cardCvv, product.getPriceString(),
                        total, product.getName(), installmentVO, birthDate, (AppCompatActivity) getActivity());
                break;
            case BILLET:
                callBilletScreen();
                break;
        }
    }

    @Override
    public void progressDismiss(){
        progress.dismiss();
    }

    @Override
    public void showProgress() {
        progress.setMessage(getString(R.string.txt_performing_payment));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    @Override
    public void showSuccessTransparent(final String message) {
        callResultScreen(true, message);
    }

    @Override
    public void showFailureTransparent(final String message) {
        callResultScreen(false, message);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.resume_title);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
