package br.com.uol.pslibs.lojinhapagseguro.view.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.MainActivity;
import br.com.uol.pslibs.lojinhapagseguro.util.FragmentUtils;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResultFragment extends Fragment {

    private boolean isSuccess;
    private String message;

    @BindView(R.id.iv_icon)
    ImageView ivIcon;

    @BindView(R.id.tv_message)
    TextView tvMessage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_success, container, false);
        ButterKnife.bind(this, view);
        ((MainActivity) getActivity()).getSupportActionBar().setTitle(R.string.payment_title);

        if(isSuccess){
            ivIcon.setImageResource(R.drawable.ic_success);
        } else{
            ivIcon.setImageResource(R.drawable.ic_fail);
        }
        tvMessage.setText(message);

        return view;
    }

    @OnClick(R.id.bt_back)
    public void back() {
        FragmentUtils.clearBackStack(getFragmentManager());
        FragmentUtils.replaceFragment(getFragmentManager(), R.id.fragment_container, new ProductDetailFragment());
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            message = bundle.getString(ResumeFragment.MESSAGE);
            isSuccess = bundle.getBoolean(ResumeFragment.IS_SUCCESS);
        }
    }
}
