package br.com.uol.pslibs.lojinhapagseguro.view.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import br.com.uol.pslibs.lojinhapagseguro.R;
import br.com.uol.pslibs.lojinhapagseguro.MainActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutFragment extends Fragment {

    @BindView(R.id.tv_msg_link) TextView tvLink;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.about));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        initComponents(view);
        return view;
    }

    private void initComponents(View layout) {
        ButterKnife.bind(this, layout);
        ((MainActivity)getActivity()).enableMenu();
        getActivity().setTitle(getString(R.string.about));

        tvLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_dev)));
                startActivity(myIntent);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }
}
