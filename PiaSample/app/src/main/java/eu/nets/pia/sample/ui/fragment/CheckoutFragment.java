package eu.nets.pia.sample.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.data.PiaSampleSharedPreferences;
import eu.nets.pia.sample.ui.adapter.CurrencyAdapter;

/**
 * MIT License
 * <p>
 * Copyright (c) 2020 Nets Denmark A/S
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy  of this software
 * and associated documentation files (the "Software"), to deal  in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software is  furnished to do so,
 * subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

public class CheckoutFragment extends Fragment {

    @BindView(R.id.price_value_et)
    protected EditText mPriceView;
    @BindView(R.id.currency_dropdown)
    protected Spinner mCurrencyDropdown;

    private FragmentCallback mCallback;

    public CheckoutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback) {
            mCallback = (FragmentCallback) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentCallback");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mCallback.showActivityToolbar(true);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        handleCurrency();
    }

    private void handleCurrency() {
        List<String> spinnerArrayList = new ArrayList<String>(Arrays.asList(
                getActivity().getString(R.string.currency_eur),
                getActivity().getString(R.string.currency_sek),
                getActivity().getString(R.string.currency_dkk),
                getActivity().getString(R.string.currency_nok)
        ));
        final CurrencyAdapter adapter = new CurrencyAdapter(getActivity(), android.R.layout.simple_spinner_item, spinnerArrayList);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCurrencyDropdown.setAdapter(adapter);

        //set previous selection
        mCurrencyDropdown.setSelection(adapter.getPositionForItem(PiaSampleSharedPreferences.getCustomerCurrency()));

        mCurrencyDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //save the new selection
                PiaSampleSharedPreferences.setCustomerCurrency(adapter.getItem(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    @OnClick(R.id.payment_btn)
    protected void onPayClicked() {
        if (mCallback != null) {
            mCallback.onPayClicked();
        }
    }

    public String getPriceString() {
        return mPriceView.getText().toString();
    }
}
