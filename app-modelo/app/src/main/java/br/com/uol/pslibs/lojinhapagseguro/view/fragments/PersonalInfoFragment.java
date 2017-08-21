package br.com.uol.pslibs.lojinhapagseguro.view.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.vicmikhailau.maskededittext.MaskedEditText;

import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.view.helper.PaymentMethod;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PersonalInfoFragment extends Fragment implements TextWatcher {

    private ProgressDialog progress;

    @BindView(R.id.edt_document)
    MaskedEditText edtDocument;
    @BindView(R.id.edt_full_name)
    EditText edtFullName;
    @BindView(R.id.edt_email)
    EditText edtEmail;
    @BindView(R.id.edt_phone_number)
    MaskedEditText edtPhoneNumber;
    @BindView(R.id.edt_birth_date)
    MaskedEditText edtBirthDate;
    @BindView(R.id.bt_next_personal_info)
    Button btResume;

    private PaymentMethod paymentMethod;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.personal_info_fragment, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        ButterKnife.bind(this, view);
        progress = new ProgressDialog(getActivity());

        paymentMethod = (PaymentMethod) getArguments().getSerializable(Constants.PAYMENT_METHOD);

        if (paymentMethod ==  PaymentMethod.CREDIT_CARD_TRANSPARENT_DEFAULT){
            edtBirthDate.setVisibility(View.VISIBLE);
            edtBirthDate.addTextChangedListener(this);
        }else {
            edtBirthDate.setVisibility(View.GONE);
        }

        edtDocument.addTextChangedListener(this);
        edtFullName.addTextChangedListener(this);
        edtEmail.addTextChangedListener(this);
        edtPhoneNumber.addTextChangedListener(this);
    }

    @OnClick(R.id.bt_next_personal_info)
    public void callResumeScreen(){
        if (validEmail()){
            Bundle bundle = getArguments();
            bundle.putString(Constants.PERSONAL_DOCUMENT, edtDocument.getUnMaskedText().toString());
            bundle.putString(Constants.PERSONAL_FULL_NAME, edtFullName.getText().toString());
            bundle.putString(Constants.PERSONAL_EMAIL, edtEmail.getText().toString());
            bundle.putString(Constants.PERSONAL_PHONE_NUMBER, edtPhoneNumber.getUnMaskedText().toString());

            if (paymentMethod == PaymentMethod.CREDIT_CARD_TRANSPARENT_DEFAULT){
                bundle.putString(Constants.PERSONAL_BIRTH_DATE, edtBirthDate.getText().toString());
            }

            AddressFragment addressFragment = new AddressFragment();
            addressFragment.setArguments(bundle);
            FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, addressFragment);
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            AlertDialog alertDialog = builder.setTitle("Erro")
                    .setMessage("Email invalido, digite um email valido para continuar.")
                    .setPositiveButton("OK", (dialogInterface, i) -> { })
                    .create();
            alertDialog.show();
        }
    }

    public boolean validEmail() {
        return !TextUtils.isEmpty(edtEmail.getText()) && android.util.Patterns.EMAIL_ADDRESS.matcher(edtEmail.getText()).matches();
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (edtDocument.getUnMaskedText().length() <= 10 || edtFullName.getText().length() <= 0 || edtEmail.getText().length() <= 0
                || edtPhoneNumber.getUnMaskedText().length() <= 10){
            btResume.setEnabled(false);
            btResume.setClickable(false);
        }else {
            btResume.setEnabled(true);
            btResume.setClickable(true);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.personal_info_title);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
