package br.com.uol.pslibs.lojinhapagseguro.view.fragments;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import br.com.uol.pslibs.checkout_in_app.transparent.util.PermissionManager;
import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.model.Product;
import br.com.uol.pslibs.lojinhapagseguro.presenter.BilletPresenter;
import br.com.uol.pslibs.lojinhapagseguro.presenter.impl.BilletPresenterImpl;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FileUtil;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.util.LojinhaUtil;
import br.com.uol.pslibs.lojinhapagseguro.view.BilletView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.ResponseBody;


public class BilletFragment extends Fragment implements BilletView {

    private static final String FILE_NAME = "boleto_ps.pdf";
    private static final String PLAIN_TEXT_TAG = "billetNumber";

    private Product product;
    private String amount;
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
    private Double totalValue;
    private String fileName;

    private ProgressDialog progress;

    @BindView(R.id.tv_billet_number)
    TextView tvBilletNumber;

    @BindView(R.id.layout_success)
    LinearLayout layoutSuccess;

    @BindView(R.id.bt_back_to_home)
    Button btBackToHome;

    private BilletPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.billet_fragment, container, false);
        initComponents(view);
        createPresenter();
        getValues();
        generateBillet();
        return view;
    }

    private void initComponents(View view) {
        ButterKnife.bind(this, view);
        progress = new ProgressDialog(getActivity());
        PermissionManager.requestPermissionExternalStorage((AppCompatActivity) getActivity(),
                getResources().getString(R.string.app_name));
    }

    private void createPresenter(){
        presenter = new BilletPresenterImpl();
        presenter.attachView(this);
    }

    private void getValues(){
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
            totalValue = Double.parseDouble(bundle.getString(Constants.TOTAL_VALUE).replace(",", "."));
            amount = bundle.getString(Constants.AMOUNT);
            quantity = bundle.getString(Constants.QUANTITY);
        }
    }

    private void generateBillet() {
        onProcessing();
        presenter.generate(documentNumber, email, name, areaCode, phoneNumber, postalCode,
                country, state, city, district, street, addressNumber,
                addressComplement, totalValue, paymentDescription, quantity, amount);
    }

    @Override
    public AppCompatActivity getAppActivity() {
        return (AppCompatActivity) getActivity();
    }

    @Override
    public void onSuccess(final String barCode) {
        progress.dismiss();
        layoutSuccess.setVisibility(View.VISIBLE);
        btBackToHome.setVisibility(View.VISIBLE);
        layoutSuccess.setVisibility(View.VISIBLE);
        tvBilletNumber.setText(barCode == null ? "" : LojinhaUtil.formatBilletNumber(barCode));
    }

    @Override
    public void onFailure() {
        progress.dismiss();
        if(isAdded()) {
            Toast.makeText(getActivity(), R.string.msg_billet_error, Toast.LENGTH_LONG).show();
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onProcessing() {
        if(isAdded()) {
            progress.setMessage(getString(R.string.msg_billet_generate));
            progress.setIndeterminate(true);
            progress.setCancelable(false);
            progress.show();
        }
    }

    @OnClick(R.id.bt_back_to_home)
    public void callHomeScreen(){
        FragmentUtils.clearBackStack(getFragmentManager());
        FragmentUtils.replaceFragment(getFragmentManager(), R.id.fragment_container, new ProductDetailFragment());
    }

    @OnClick(R.id.bt_copy)
    public void copyToClipboard(){
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(PLAIN_TEXT_TAG, tvBilletNumber.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), R.string.txt_copy, Toast.LENGTH_LONG).show();
    }

    @OnClick(R.id.bt_download)
    public void downloadBillet(){
        fileName = String.format("/Download/ps_boleto_%s.pdf", presenter.getBillet().getCode());
        FileUtil.createFile(fileName);
        presenter.downloadFile();
    }

    @Override
    public void writeFile(ResponseBody body) {
        FileUtil.writeFile(body.byteStream(), fileName);
    }

    @Override
    public void showConfirmMessage() {
        Toast.makeText(getActivity(), String.format("Download do boleto %s finalizado com sucesso",
                FileUtil.getBilletFile(fileName).getPath()),
                Toast.LENGTH_LONG).show();
    }


    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).setTitle(R.string.billet_title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
