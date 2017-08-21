package com.cooltechworks.creditcarddesign.pager;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cooltechworks.creditcarddesign.CreditCardUtils;
import com.cooltechworks.creditcarddesign.R;

import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_CVV;
import static com.cooltechworks.creditcarddesign.CreditCardUtils.EXTRA_CARD_HOLDER_NAME;
/**
 * Created by sharish on 9/1/15.
 */
public class CardNameFragment extends CreditCardFragment {


    private EditText mCardNameView;

    public CardNameFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {

        View v = inflater.inflate(R.layout.lyt_card_holder_name, group,false);
        mCardNameView = (EditText) v.findViewById(R.id.card_name);
        mCardNameView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if(actionId== EditorInfo.IME_ACTION_DONE){
                    onComplete();
                }
                return false;
            }
        });

        String name = "";
        if(getArguments() != null && getArguments().containsKey(EXTRA_CARD_HOLDER_NAME)) {
            name = getArguments().getString(EXTRA_CARD_HOLDER_NAME);
        }


        if(name == null) {
            name = "";
        }

        mCardNameView.setText(name);
        mCardNameView.addTextChangedListener(this);

        return v;
    }

    @Override
    public void afterTextChanged(Editable s) {

        onEdit(s.toString());
        if(s.length() == CreditCardUtils.MAX_LENGTH_CARD_HOLDER_NAME) {
            onComplete();
        }
    }

    @Override
    public void focus() {

        if(isAdded()) {
            mCardNameView.selectAll();
        }
    }
}
