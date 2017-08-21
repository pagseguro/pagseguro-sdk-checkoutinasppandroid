package br.com.uol.pslibs.lojinhapagseguro.view.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import br.com.uol.pslibs.checkout_in_app.transparent.vo.PostalCodeResponseVO;
import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.presenter.AddressPresenter;
import br.com.uol.pslibs.lojinhapagseguro.presenter.impl.AddressPresenterImpl;
import br.com.uol.pslibs.lojinhapagseguro.util.Constants;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import br.com.uol.pslibs.lojinhapagseguro.view.AddressView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class AddressFragment extends Fragment implements AddressView, TextWatcher {

    private ProgressDialog progress;

    @BindView(R.id.edt_postal_code)
    EditText edtPostalCode;
    @BindView(R.id.edt_address_number)
    EditText edtNumber;
    @BindView(R.id.edt_address_complement)
    EditText edtComplement;
    @BindView(R.id.tv_street)
    EditText edtStreet;
    @BindView(R.id.tv_district)
    EditText edtDistrict;
    @BindView(R.id.tv_city_state)
    EditText edtCityState;

    @BindView(R.id.bt_search_address)
    ImageButton btSearch;
    @BindView(R.id.bt_next)
    Button btNext;

    private PostalCodeResponseVO mPostalCodeResponseVO;
    private AddressPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.address_fragment, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View view) {
        presenter = new AddressPresenterImpl();
        ButterKnife.bind(this, view);
        progress = new ProgressDialog(getActivity());

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        edtPostalCode.addTextChangedListener(this);
        edtNumber.addTextChangedListener(this);
        edtCityState.addTextChangedListener(this);
        edtStreet.addTextChangedListener(this);

        presenter.attachView(this);

    }

    @Override
    public void onSuccess(PostalCodeResponseVO responseVO) {
        progress.dismiss();
        VisibilityFields();
        mPostalCodeResponseVO = responseVO;
        edtStreet.setText(responseVO.getAddress());
        edtDistrict.setText(responseVO.getDistrict());
        edtCityState.setText(responseVO.getCity() + " - " + responseVO.getState());
        btSearch.setEnabled(false);
        btNext.setEnabled(true);
        btNext.setClickable(true);
    }

    @Override
    public void onFailure(String message) {
        progress.dismiss();
        Toast.makeText(getActivity(), R.string.msg_address_error, Toast.LENGTH_LONG).show();
        getFragmentManager().popBackStack();
    }

    private void startProgress(){
        progress.setMessage(getString(R.string.txt_wait));
        progress.setIndeterminate(true);
        progress.setCancelable(false);
        progress.show();
    }

    private void VisibilityFields(){
        edtStreet.setVisibility(View.VISIBLE);
        edtDistrict.setVisibility(View.VISIBLE);
        edtComplement.setVisibility(View.VISIBLE);
        edtCityState.setVisibility(View.VISIBLE);
        edtNumber.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.bt_search_address)
    public void searchPostalCode() {
        startProgress();
        presenter.searchAddress(edtPostalCode.getText().toString());
    }


    @OnClick(R.id.bt_next)
    public void callPersonalInfoScreen(){
        Bundle bundle = getArguments();
        bundle.putString(Constants.ADDRESS_POSTAL_CODE, edtPostalCode.getText().toString());
        bundle.putString(Constants.ADDRESS_STREET, edtStreet.getText().toString());
        bundle.putString(Constants.ADDRESS_DISTRICT, edtDistrict.getText().toString());
        bundle.putString(Constants.ADDRESS_CITY, mPostalCodeResponseVO.getCity());
        bundle.putString(Constants.ADDRESS_STATE, mPostalCodeResponseVO.getState());
        bundle.putString(Constants.ADDRESS_NUMBER, edtNumber.getText().toString());
        bundle.putString(Constants.ADDRESS_COMPLEMENT, edtComplement.getText().toString());
        bundle.putString(Constants.ADDRESS_COUNTRY, "BRASIL");

        ResumeFragment resumeFragment = new ResumeFragment();
        resumeFragment.setArguments(bundle);
        FragmentUtils.replaceFragmentWithBackStack(getFragmentManager(), R.id.fragment_container, resumeFragment);

    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (edtPostalCode.getText().toString().length() == 8){
            btSearch.setEnabled(true);
            btSearch.setClickable(true);
        }
        if (edtStreet.getText().toString().length() <= 0 || edtPostalCode.getText().toString().length() <= 7
                || edtDistrict.getText().toString().length() <= 0){
            btNext.setEnabled(false);
            btNext.setClickable(false);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.label_delivery_address);
    }
}
