package eu.nets.pia.sample.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.network.model.Method;
import eu.nets.pia.sample.network.model.PaymentMethodsResponse;
import eu.nets.pia.sample.network.model.Token;
import eu.nets.pia.sample.ui.adapter.PaymentMethodsAdapter;
import eu.nets.pia.sample.ui.data.DisplayedToken;
import eu.nets.pia.sample.ui.data.PaymentMethod;
import eu.nets.pia.sample.ui.data.PaymentMethodType;
import eu.nets.pia.sample.ui.widget.CustomToolbar;

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

public class PaymentMethodsFragment extends Fragment {

    public static final String ID_VISA = "Visa";
    public static final String ID_MASTERCARD = "MasterCard";
    public static final String ID_AMERICAN_EXPRESS = "AmericanExpress";
    public static final String ID_DANKORT = "Dankort";
    public static final String ID_DINERS = "DinersClubInternational";
    public static final String ID_APPLE_PAY = "ApplePay";
    public static final String ID_PAY_PAL = "PayPal";
    public static final String ID_VIPPS = "Vipps";
    public static final String ID_SWISH = "Swish";
    public static final String ID_EASY_PAYMENT = "EasyPayment";
    public static final String ID_JCB = "JCB";
    public static final String ID_MAESTRO = "Maestro";

    @BindView(R.id.toolbar)
    CustomToolbar mToolbar;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private static String KEY_PAYMENT_METHODS = "KEY_PAYMENT_METHODS";

    private FragmentCallback mCallback;
    private PaymentMethodsResponse mPaymentMethods;
    private PaymentMethodsAdapter mAdapter;

    public PaymentMethodsFragment() {
        // Required empty public constructor
    }

    public static PaymentMethodsFragment newInstance(PaymentMethodsResponse paymentMethodsResponse) {
        PaymentMethodsFragment fragment = new PaymentMethodsFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_PAYMENT_METHODS, paymentMethodsResponse);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().containsKey(KEY_PAYMENT_METHODS)) {
                mPaymentMethods = getArguments().getParcelable(KEY_PAYMENT_METHODS);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_methods, container, false);
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

        mCallback.showActivityToolbar(false);
        mToolbar.setTitle("Payment Methods");
        setupRecycler();
    }

    private void setupRecycler() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new PaymentMethodsAdapter(getActivity(), getPaymentMethodsList(), getSupportedCardSchemes(), mCallback);
        mRecyclerView.setAdapter(mAdapter);
    }

    private ArrayList<Method> getSupportedCardSchemes() {
        ArrayList<Method> supportedMethods = new ArrayList<>();
        for (Method method : mPaymentMethods.getMethods()) {
            if (!method.getId().equals(ID_APPLE_PAY) && !method.getId().equals(ID_EASY_PAYMENT)
                    && !method.getId().equals(ID_PAY_PAL)
                    && !method.getId().equals(ID_VIPPS)
                    && !method.getId().contains(ID_SWISH)) {
                supportedMethods.add(method);
            }
        }
        return supportedMethods;
    }

    private ArrayList<PaymentMethod> getPaymentMethodsList() {
        ArrayList<Token> tokenList;
        ArrayList<Method> paymentMethodList;
        /**
         * Note:
         * This flag is returned by the Merchant Back-End. The current logic implemented there, is
         * to have two cases (true and false) based on the customerId being odd or even. This was made only
         * for Demo purposes.
         *
         * For your implementation, check with your acquirer if payment for the specific user can be made without CVV/CVC,
         * and send this flag accordingly.
         */
        Boolean cardVerificationRequired;

        if (mPaymentMethods != null) {
            tokenList = mPaymentMethods.getTokens();
            paymentMethodList = mPaymentMethods.getMethods();
            cardVerificationRequired = mPaymentMethods.getCardVerificationRequired();
        } else {
            throw new IllegalArgumentException("PaymentMethodsFragment must receive payment method list");
        }
        ArrayList<PaymentMethod> methods = new ArrayList<>();

        if (tokenList != null && tokenList.size() > 0) {
            //The EasyPayment method is the last in the list of payment methods
            Method easyPayMethod = paymentMethodList.get(paymentMethodList.size() - 1);

            for (Token token : tokenList) {
                DisplayedToken savedCard = new DisplayedToken();
                savedCard.setType(PaymentMethodType.TOKEN);
                savedCard.setId(easyPayMethod.getId());
                savedCard.setDisplayName(easyPayMethod.getDisplayName());
                savedCard.setTokenId(token.getTokenId());
                savedCard.setIssuer(token.getIssuer());
                savedCard.setExpiryDate(token.getExpiryDate());
                savedCard.setCardVerificationRequired(cardVerificationRequired);
                methods.add(savedCard);
            }

            //add header for Other payment methods only if there are saved cards
            PaymentMethod header = new PaymentMethod();
            header.setType(null); // set it to null so that adapter knows that this is a header
            methods.add(header);
        }

        PaymentMethod creditCards = new PaymentMethod();
        creditCards.setType(PaymentMethodType.CREDIT_CARDS);
        creditCards.setDisplayName(getString(R.string.payment_method_credit_cards));
        creditCards.setId(getString(R.string.payment_method_credit_cards));
        creditCards.setCvcRequired(true);
        methods.add(creditCards);

        PaymentMethod payPal = new PaymentMethod();
        payPal.setType(PaymentMethodType.PAY_PAL);
        payPal.setId(ID_PAY_PAL);
        methods.add(payPal);

        PaymentMethod vipps = new PaymentMethod();
        vipps.setType(PaymentMethodType.VIPPS);
        vipps.setId(ID_VIPPS);
        methods.add(vipps);

        PaymentMethod swish = new PaymentMethod();
        swish.setType(PaymentMethodType.SWISH);
        swish.setId("SwishM");
        methods.add(swish);

        return methods;
    }
}
