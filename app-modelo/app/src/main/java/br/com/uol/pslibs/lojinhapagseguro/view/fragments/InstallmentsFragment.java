package br.com.uol.pslibs.lojinhapagseguro.view.fragments;

import android.app.ProgressDialog;
import android.content.pm.ProviderInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.creditcarddesign.CreditCardUtils;

import br.com.uol.pslibs.checkout_in_app.transparent.vo.InstallmentVO;
import br.com.uol.pslibs.checkout_in_app.wallet.util.StringUtils;
import br.com.uol.pslibs.lojinhapagseguro.MainActivity;
import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.presenter.InstallmentsPresenter;
import br.com.uol.pslibs.lojinhapagseguro.presenter.impl.InstallmentsPresenterImpl;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.view.InstallmentsView;
import br.com.uol.pslibs.lojinhapagseguro.view.helper.PaymentMethod;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemSelected;

import static br.com.uol.pslibs.lojinhapagseguro.util.Constants.PAYMENT_METHOD;

public class InstallmentsFragment extends Fragment implements InstallmentsView{

    private Bundle bundle;

    @BindView(R.id.sp_installments) Spinner spInstallments;
    @BindView(R.id.tv_value) TextView tvValue;
    @BindView(R.id.bt_parcel) Button button;
    private InstallmentsPresenter presenter;

    private ProgressDialog progress;
    private Double value;
    private PaymentMethod paymentMethod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_installments, container, false);
        bundle = this.getArguments();
        initComponents(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.installments);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((MainActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        button.setEnabled(true);
        button.setClickable(true);
    }

    private void initComponents(View view) {
        ButterKnife.bind(this, view);

        presenter = new InstallmentsPresenterImpl();
        presenter.attachView(this);

        progress = new ProgressDialog(getActivity());
        value = Double.parseDouble(bundle.getString(Constants.TOTAL_AMOUNT).replace(",", "."));
        String cardNumber = bundle.getString(CreditCardUtils.EXTRA_CARD_NUMBER);
        paymentMethod = (PaymentMethod) bundle.getSerializable(PAYMENT_METHOD);

        getInstallments(cardNumber);
    }

    @OnClick(R.id.bt_parcel)
    public void parcelClick(){
        if (paymentMethod == PaymentMethod.CREDIT_CARD_TRANSPARENT_GATEWAY) {
            ResumeFragment resumeFragment = new ResumeFragment();
            resumeFragment.setArguments(bundle);
            FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, resumeFragment);
        } else if (paymentMethod == PaymentMethod.CREDIT_CARD_TRANSPARENT_DEFAULT) {
            PersonalInfoFragment infoFragment = new PersonalInfoFragment();
            infoFragment.setArguments(bundle);
            FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, infoFragment);
        }
    }

    @OnItemSelected(R.id.sp_installments)
    public void onItemSelectedInstallments(AdapterView<?> adapterView, View view, int position, long l){
        presenter.onItemSelectedInstallments(position);
    }

    private void getInstallments(String cardNumber) {
        presenter.getInstallments(cardNumber, value);
    }

    @Override
    public void showProgress(){
        progress.setMessage(getString(R.string.txt_wait));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    public void setAdapterInstallments() {
        ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_spinner_dropdown_item, generateInstallments());
        spInstallments.setAdapter(spinnerArrayAdapter);
    }

    private String[] generateInstallments() {
       return presenter.generateInstallments(value);
    }

    @Override
    public void progressDismiss() {
        progress.dismiss();
    }

    @Override
    public void onFailure(String message) {
        Toast.makeText(getActivity(),
                StringUtils.isEmpty(message) ? getString(R.string.installments_failed) : message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showValueInstallments(String valueInstallment) {
        tvValue.setText(valueInstallment);
    }

    @Override
    public void setInstallmentsOnBundle(String valueFormated, InstallmentVO installment, String totalAmount) {
        bundle.putInt(Constants.INSTALLMENT, installment.getQuantity());
        bundle.putSerializable(Constants.INSTALLMENT_VO, installment);
        bundle.putString(Constants.TOTAL, totalAmount.toString());
        bundle.putString(Constants.PAYMENT, valueFormated);
    }
}
