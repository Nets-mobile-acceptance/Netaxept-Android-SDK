package eu.nets.pia.sample.ui.activity.main;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.RegisterPaymentHandler;
import eu.nets.pia.data.model.MerchantInfo;
import eu.nets.pia.data.model.OrderInfo;
import eu.nets.pia.data.model.PiaResult;
import eu.nets.pia.data.model.SchemeType;
import eu.nets.pia.data.model.TokenCardInfo;
import eu.nets.pia.sample.BuildConfig;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.RegisterPaymentHandlerImpl;
import eu.nets.pia.sample.data.PaymentFlowCache;
import eu.nets.pia.sample.data.PaymentFlowState;
import eu.nets.pia.sample.data.PaymentMethodSelected;
import eu.nets.pia.sample.data.PiaSampleSharedPreferences;
import eu.nets.pia.sample.network.MerchantRestClient;
import eu.nets.pia.sample.network.model.Amount;
import eu.nets.pia.sample.network.model.Method;
import eu.nets.pia.sample.network.model.PaymentMethodsResponse;
import eu.nets.pia.sample.network.model.PaymentRegisterRequest;
import eu.nets.pia.sample.ui.activity.ConfirmationActivity;
import eu.nets.pia.sample.ui.activity.LoginActivity;
import eu.nets.pia.sample.ui.data.DisplayedToken;
import eu.nets.pia.sample.ui.data.PaymentMethod;
import eu.nets.pia.sample.ui.data.PaymentMethodType;
import eu.nets.pia.sample.ui.fragment.CheckoutFragment;
import eu.nets.pia.sample.ui.fragment.FragmentCallback;
import eu.nets.pia.sample.ui.fragment.PaymentMethodsFragment;
import eu.nets.pia.ui.main.PiaActivity;
import eu.nets.pia.utils.LogUtils;

import static eu.nets.pia.sample.ui.fragment.PaymentMethodsFragment.ID_SWISH;

/**
 * MIT License
 * <p>
 * Copyright (c) 2019 Nets Denmark A/S
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

public class MainActivity extends AppCompatActivity implements MerchantRestClient.PaymentFlowCallback,
        FragmentCallback {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final String ORDER_NUMBER = "PiaSDK-Android";

    @BindView(R.id.spinner_holder)
    protected RelativeLayout mProgressBar;
    @BindView(R.id.toolbar_view)
    Toolbar mToolbar;
    @BindView(R.id.frame_layout)
    FrameLayout mFrame;

    private AlertDialog mEnvironmentDialog;
    private PaymentFlowCache mPaymentCache;
    private RegisterPaymentHandler mRegisterPaymentHandler;
    private MerchantRestClient mRestClient = MerchantRestClient.getInstance();
    private PaymentMethodsResponse paymentMethodsResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        String customerId = PiaSampleSharedPreferences.getCustomerId();
        if (customerId.isEmpty()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");

        /**
         * Object used for caching all data related to payment
         */
        mPaymentCache = PaymentFlowCache.getInstance();
        /**
         * Instantiate the payment handled which will be sent to the SDK when launching it
         */
        mRegisterPaymentHandler = new RegisterPaymentHandlerImpl();

        //clear all fragments added in backstack -- when activity is created, it's better to have a fresh stack
        clearFragmentStack();

        //first time activity is created show checkout
        changeFragment(new CheckoutFragment());

    }

    /**
     * Each time the Activity is created, clear the fragment stack to remove all cached data
     */
    private void clearFragmentStack() {
        for (int i = 0; i < getSupportFragmentManager().getBackStackEntryCount(); i++) {
            getSupportFragmentManager().popBackStack();
        }
    }

    private void changeFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //if fragment is null or already added in stack, don't add it again
        if (fragment == null || fragmentAlreadyInStack(fragment.getClass().getSimpleName())) {
            return;
        }
        transaction.setTransitionStyle(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

        transaction.replace(mFrame.getId(), fragment, fragment.getClass().getSimpleName());
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        if (isFinishing() || isDestroyed()) {
            return;
        }
        transaction.commit();
    }

    /**
     * Searches for the fragment class name in the backstack; if it's found, returns true
     *
     * @param fragmentToAdd class simple name of the fragment
     * @return boolean with result found or not
     */
    private boolean fragmentAlreadyInStack(String fragmentToAdd) {
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment.getClass().getSimpleName().equals(fragmentToAdd)) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            //if there are more that 1 fragment in the stack allow back navigation
            super.onBackPressed();
        } else {
            //if there is just one fragment -- there is nothing to go back to - finish activity
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        handleVisibleFragment();
        // refresh the Rest Client state (if user switched between PROD and TEST,
        // a new instance will be created. If not, the same instance will be returned
        mRestClient = MerchantRestClient.getInstance();
        mRestClient.addListener(this);

        //the application was put in background before the call finished - restore view state
        handlePaymentFlowState();
    }

    private void handleVisibleFragment() {
        //if stack size is greater than 1 it means that PaymentMethodsFragment is the visible one-- remove it from backstack and show checkout
        if (getSupportFragmentManager().getBackStackEntryCount() > 1
                && (mPaymentCache.getPaymentMethodSelected() != PaymentMethodSelected.VIPPS)) {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private void checkBaseUrl() {
        if (mEnvironmentDialog != null && mEnvironmentDialog.isShowing()) {
            //alert is already visible -- don't show it again
            return;
        }
        if (PiaSampleSharedPreferences.isUsingNetsEnv()) {
            //is using nets environment -- show popup
            try {
                AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

                builder.setTitle("You are using Nets environment");

                builder.setMessage("To switch to your own test or production environment, update your configuration in the Setting page");
                builder.setCancelable(false);

                builder.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                mEnvironmentDialog = null;
                            }
                        });

                mEnvironmentDialog = builder.create();
                mEnvironmentDialog.show();
            } catch (Exception e) {
                //in case activity is not attached to window -- catch exception here and do nothing
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mRestClient.removeListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings_item:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private String getCheckoutPriceString() {
        CheckoutFragment fragment = (CheckoutFragment) getSupportFragmentManager().findFragmentByTag(CheckoutFragment.class.getSimpleName());
        if (fragment != null) {
            return fragment.getPriceString();
        } else {
            return "0";
        }
    }

    @Override
    public void onPayClicked() {
        //store the price in a variable for later use
        String priceString = getCheckoutPriceString();
        Long price = priceString.isEmpty() ? 0 : (long) (Double.parseDouble(priceString) * 100);
        if (price == 0) {
            Toast.makeText(this, getString(R.string.payment_value_zero), Toast.LENGTH_SHORT).show();
            return;
        }

        //make the register call
        Log.d(TAG, "[onPayClicked] start payment methods payment call");
        mProgressBar.setVisibility(View.VISIBLE);
        /**
         * Call your backend to get available payment methods for this user
         */
        mRestClient.getPaymentMethods(PiaSampleSharedPreferences.getCustomerId());

    }

    @Override
    public void showActivityToolbar(boolean show) {
        mToolbar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPaymentCallFinished() {
        handlePaymentFlowState();
    }

    @Override
    public void onGetPaymentMethods(PaymentMethodsResponse response) {
        Log.d(TAG, "[onGetPaymentMethods]");
        dismissProgressBar();
        paymentMethodsResponse = response;
        /**
         * Handle the response and show the payment methods the way you want. Our implementation is only a suggestion,
         * a way to show all the available possibilities offered.
         */
        changeFragment(PaymentMethodsFragment.newInstance(response));
    }

    @Override
    public void onPaymentMethodSelected(PaymentMethod method) {
        if (isPaymentMethodSupported(method)) {
            Log.d(TAG, "[onPaymentMethodSelected] start Pia SDK " + PiaSampleSharedPreferences.getPhoneNumber());
            if ((method.getType() == PaymentMethodType.VIPPS) && PiaSampleSharedPreferences.getPhoneNumber() == null) {
                Log.d(TAG, "[onPaymentMethodSelected] return");
                Toast.makeText(MainActivity.this, "Add your phone number in settings page", Toast.LENGTH_SHORT).show();
                return;
            }

            if (method.getType() == PaymentMethodType.VIPPS) {
                mPaymentCache.setPaymentMethodSelected(PaymentMethodSelected.VIPPS);
            } else if (method.getType() == PaymentMethodType.SWISH) {
                mPaymentCache.setPaymentMethodSelected(PaymentMethodSelected.SWISH);
            } else {
                mPaymentCache.setPaymentMethodSelected(PaymentMethodSelected.OTHERS);
            }

            /**
             * If payment method selected is valid (is supported by backend, or selected saved card is not expired),
             * create a {@link PaymentRegisterRequest} and store it in the cache. It will be used from there by the
             * {@link RegisterPaymentHandlerImpl}. Then launch the SDK with required parameters
             */
            mPaymentCache.setPaymentRegisterRequest(getPaymentRequest(method));
            callPiaSDK(method);
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.payment_method_not_supported), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isPaymentMethodSupported(PaymentMethod method) {
        if (method.getType() == PaymentMethodType.TOKEN || method.getType() == PaymentMethodType.CREDIT_CARDS
                || method.getType() == PaymentMethodType.VIPPS || method.getType() == PaymentMethodType.SWISH
                || method.getType() == PaymentMethodType.PAY_PAL) {
            //these are enabled by default
            return true;
        }
        boolean isSupported = false;
        if (paymentMethodsResponse != null && paymentMethodsResponse.getMethods() != null) {
            for (Method supportedMethod : paymentMethodsResponse.getMethods()) {
                //allow only if the selected method is found in the payment methods response
                if (method.getId().equals(supportedMethod.getId()) && method.getId().contains(ID_SWISH)) {
                    isSupported = true;
                }
            }
        }
        return isSupported;
    }

    private void handlePaymentFlowState() {
        Log.d(TAG, "[handlePaymentFlowState] [isFinishedWithError=" + mPaymentCache.isFinishedWithError() +
                "; state=" + mPaymentCache.getState() + "; requestFailed=" + mPaymentCache.isFailedRequest() + "]");
        if (mPaymentCache.isFailedRequest()) {
            //request has failed -- probably connection issue or timeout -- display popup
            displayRequestFailedDialog();
            dismissProgressBar();
        } else if (!mPaymentCache.isFinishedWithError()) {
            switch (mPaymentCache.getState()) {
                case COMMIT_PAYMENT_CALL_FINISHED:
                    successfulPaymentResult();
                    break;
                case IDLE:
                    dismissProgressBar();
                    break;
                case REGISTER_PAYMENT_CALL_FINISHED:
                case SENDING_REGISTER_PAYMENT_CALL:
                case SENDING_COMMIT_PAYMENT_CALL:
                case SENDING_ROLLBACK_TRANSACTION_CALL:
                case ROLLBACK_TRANSACTION_FINISHED:
                case CALL_PIA_SDK:
                    break;
            }
        } else {
            if (mPaymentCache.getState() != PaymentFlowState.SENDING_ROLLBACK_TRANSACTION_CALL &&
                    mPaymentCache.getState() != PaymentFlowState.ROLLBACK_TRANSACTION_FINISHED) {
                //reset error state
                mPaymentCache.setFinishedWithError(false);
                //rollback transaction
                rollbackTransaction();
                //display status screen
                Bundle bundle = new Bundle();
                bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_SUCCESS, false);
                displayConfirmationScreen(bundle);
            } else {
                Toast.makeText(this, getString(R.string.payment_rollback_error), Toast.LENGTH_SHORT).show();
                mPaymentCache.reset();
            }
        }
    }

    private void successfulPaymentResult() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_SUCCESS, true);
        bundle.putString(ConfirmationActivity.BUNDLE_SUCCESS_MESSAGE, getString(R.string.thanks_for_shopping));
        bundle.putString(ConfirmationActivity.BUNDLE_SUCCESS_TITLE, getString(R.string.toolbar_title_success_pay));
        displayConfirmationScreen(bundle);
    }

    private void displayRequestFailedDialog() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

            builder.setMessage("There was an unexpected error. Please contact customer service if the problem persists.");
            builder.setCancelable(false);

            builder.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                            mPaymentCache.reset();
                        }
                    });

            builder.create().show();
        } catch (Exception e) {
            e.printStackTrace();
            //in case activity is not attached to window -- catch exception here and do nothing
        }
    }

    /**
     * Launch the SDK to perform a payment, and provide the needed parameters: {@link eu.nets.pia.PiaSDK#start(Activity, Bundle, RegisterPaymentHandler)}
     * <p>
     * The Bundle can contain the following data, depending on the desired action:
     * <p>
     * 1. Pay with new card:
     * put only {@link eu.nets.pia.data.model.MerchantInfo} and
     * {@link eu.nets.pia.data.model.OrderInfo}
     * <p>
     * 2. Pay with save card:
     * put only {@link eu.nets.pia.data.model.MerchantInfo},
     * {@link eu.nets.pia.data.model.OrderInfo} and  {@link eu.nets.pia.data.model.TokenCardInfo}
     * <p>
     * 3. Save card:
     * put only {@link eu.nets.pia.data.model.MerchantInfo} - check {@link LoginActivity#onSaveCardBtnClicked}
     *
     * @param method selected payment method
     */
    private void callPiaSDK(PaymentMethod method) {
        Log.d(TAG, "[callPiaSDK] start Pia SDK");

        Bundle bundle = new Bundle();
        bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, getMerchantInfo(method.isCvcRequired()));
        bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, getOrderInfo());
        if (method.getType() == PaymentMethodType.TOKEN) {
            bundle.putParcelable(PiaActivity.BUNDLE_TOKEN_CARD_INFO, getTokenizedCardInfo((DisplayedToken) method));
        }

        switch (method.getType()) {
            case PAY_PAL:
                PiaSDK.getInstance().startPayPalProcess(this, bundle, mRegisterPaymentHandler);
                break;
            case VIPPS:
                PiaSDK.getInstance().startVippsProcess(this, bundle, mRegisterPaymentHandler);
                break;
            case SWISH:
                PiaSDK.getInstance().startSwishProcess(this, bundle, mRegisterPaymentHandler);
                break;
            default:
                PiaSDK.getInstance().start(this, bundle, mRegisterPaymentHandler);
        }
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

        mPaymentCache.setState(PaymentFlowState.CALL_PIA_SDK);
    }

    /**
     * Override the OnActivityResult, to receive the payment result from PiaSDK
     * <p>
     * The result will have the requestCode == {@link eu.nets.pia.PiaSDK#PIA_SDK_REQUEST}
     * <p>
     * If the resultCode = RESULT_OK, the intent will contain a {@link eu.nets.pia.data.model.PiaResult}
     * parcelable object under {@link eu.nets.pia.ui.main.PiaActivity#BUNDLE_COMPLETE_RESULT}
     *
     * @param requestCode -
     * @param resultCode  -
     * @param data        -
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != PiaSDK.PIA_SDK_REQUEST) {
            super.onActivityResult(requestCode, resultCode, data);
        }
        LogUtils.logD(TAG, "[onActivityResult] result from Pia SDK: " +
                "[requestCode=" + requestCode + "]");

        //in case user canceled
        Bundle bundle = new Bundle();

        if (resultCode == RESULT_CANCELED) {
            /**
             * If the result was cancelled, make a call to your backend to rollback the transaction
             * (basically, clear the transactionId, since it won't be available anymore)
             */
            rollbackTransaction();
            bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_CANCELED, true);
            displayConfirmationScreen(bundle);
            return;
        }

        if (resultCode == RESULT_OK) {
            /**
             * If resultCode is OK, check the PiaResult object
             * The PiaResult object will also contain a {@link eu.nets.pia.data.exception.PiaError} which maps a set of error codes and error messages
             * You can also create a map of your own based on them, and apply a specific error message for each.
             * Check more in the documentation for a full list of Terminal Call error codes handled by the SDK.
             */
            PiaResult result = data.getParcelableExtra(PiaActivity.BUNDLE_COMPLETE_RESULT);
            if (result.isSuccess()) {
                //in case of success commit the payment
                mProgressBar.setVisibility(View.VISIBLE);
                if (mPaymentCache.getPaymentRegisterResponse() != null
                        && mPaymentCache.getPaymentRegisterResponse().getTransactionId() != null) {
                    /**
                     * If the result is success, call commit payment on your backend
                     */
                    mRestClient.commitPayment(mPaymentCache.getPaymentRegisterResponse().getTransactionId());
                } else {
                    //register payment is null -- something went wrong
                    bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_SUCCESS, false);
                    bundle.putParcelable(ConfirmationActivity.BUNDLE_ERROR_OBJECT,
                            new PiaResult(false));
                    displayConfirmationScreen(bundle);
                }
            } else {
                /**
                 * PiaResult is not successful (error occured)
                 * - the method displayConfirmationScreen(bundle); will also call rollback transaction
                 */
                bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_SUCCESS, false);
                bundle.putParcelable(ConfirmationActivity.BUNDLE_ERROR_OBJECT, result);
                displayConfirmationScreen(bundle);
            }
        }
    }

    private void rollbackTransaction() {
        if (mPaymentCache.getPaymentRegisterResponse() != null
                && mPaymentCache.getPaymentRegisterResponse().getTransactionId() != null) {
            mRestClient.transactionRollback(mPaymentCache.getPaymentRegisterResponse().getTransactionId());
        }
    }

    private void dismissProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void displayConfirmationScreen(final Bundle bundle) {
        dismissProgressBar();
        Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        mPaymentCache.reset();
    }

    /**
     * In this object, you notify the SDK about:
     * 1. Which merchant is requesting the payments to be processed (your merchant ID)
     * 2. Which base URL should be used: TEST or PRODUCTION
     * 3. If your merchant configuration supports payments without CVC, it notifies the SDK
     * to hide the CVC entry.
     *
     * @param cardVerificationRequired - flag that specifies if the CVC will be required ot not
     * @return MerchantInfo object
     */
    private MerchantInfo getMerchantInfo(boolean cardVerificationRequired) {
        if (PiaSampleSharedPreferences.isPiaTestMode()) {
            //launch SDK with test merchant id and flag testMode = true to point SDK to test env
            return new MerchantInfo(
                    getMerchantId(true),
                    true,
                    cardVerificationRequired
            );
        } else {
            //launch SDK with production merchant id
            return new MerchantInfo(
                    getMerchantId(false),
                    false,
                    cardVerificationRequired
            );
        }
    }

    /**
     * If your application supports test mode (you have a test environment), you must also have a
     * specific merchant Id for this environment. So, provide it accordingly
     *
     * @param isTestMode - flag if test mode is used
     * @return merchant id
     */
    private String getMerchantId(boolean isTestMode) {
        if (isTestMode) {
            return PiaSampleSharedPreferences.getMerchantIdTest() == null ?
                    BuildConfig.MERCHANT_ID_TEST :
                    PiaSampleSharedPreferences.getMerchantIdTest();
        } else {
            return PiaSampleSharedPreferences.getMerchantIdTest() == null ?
                    BuildConfig.MERCHANT_ID_PROD :
                    PiaSampleSharedPreferences.getMerchantIdProd();
        }
    }

    /**
     * The order information to be passed in to the SDK.
     * Basically, it contains the amount and currency. This info will only be displayed
     * inside the SDK (the text on the Pay button: e.g Pay 10 EUR)
     *
     * @return order info object
     */
    private OrderInfo getOrderInfo() {
        String priceString = getCheckoutPriceString();
        double price = priceString.isEmpty() ? 0 : Double.parseDouble(priceString);

        return new OrderInfo(
                price,
                PiaSampleSharedPreferences.getCustomerCurrency()
        );
    }

    /**
     * Builds a TokenCardInfo object to be passed to the SDK, specifying that this is payment with a saved card
     *
     * @param method - the saved card retrieved from the getPaymentMethods
     * @return TokenCardInfo object
     */
    private TokenCardInfo getTokenizedCardInfo(DisplayedToken method) {
        SchemeType type = mapToSchemeIdFromPaymentResponse(method.getIssuer());
        return new TokenCardInfo(method.getTokenId(), type, method.getExpiryDate(),
                method.getCardVerificationRequired());
    }

    private SchemeType mapToSchemeIdFromPaymentResponse(String issuer) {
        issuer = issuer.trim().toLowerCase();
        if (issuer.equals("visa")) {
            return SchemeType.VISA;
        }
        if (issuer.equals("mastercard")) {
            return SchemeType.MASTER_CARD;
        }
        if (issuer.equals("dankort")) {
            return SchemeType.DANKORT;
        }
        if (issuer.equals("diners")) {
            return SchemeType.DINERS_CLUB_INTERNATIONAL;
        }
        if (issuer.equals("amex") || issuer.equals("americanexpress")) {
            return SchemeType.AMEX;
        }
        return SchemeType.OTHER;
    }

    /**
     * Builds a PaymentRegisterRequest, specifying the amount and currency (same ones as passed in the OrderInfo),
     * the current customer ID, a specific orderNumber and the selected payment id (retrieved from getPaymentMethods).
     * If an already saved card has been selected as payment method, send also the tokenId as cardId.
     *
     * @param method the selected payment method
     * @return RegisterPaymentRequest object
     */
    private PaymentRegisterRequest getPaymentRequest(PaymentMethod method) {
        String priceString = getCheckoutPriceString();
        Long price = priceString.isEmpty() ? 0 : (long) (Double.parseDouble(priceString) * 100);

        Amount amount = new Amount();
        amount.setCurrencyCode(PiaSampleSharedPreferences.getCustomerCurrency());
        amount.setTotalAmount(price);
        amount.setVatAmount((long) 0);

        PaymentRegisterRequest paymentRequest = new PaymentRegisterRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setOrderNumber(ORDER_NUMBER);
        paymentRequest.setCustomerId(PiaSampleSharedPreferences.getCustomerId());

        //specify the token id , if a token payment method selected
        //and the payment method - in this case EasyPayment
        if (method.getType() == PaymentMethodType.TOKEN) {
            paymentRequest.setCardId(((DisplayedToken) method).getTokenId());
            paymentRequest.setMethod(new Method(method.getId()));
        }

        if (method.getType() == PaymentMethodType.PAY_PAL) {
            paymentRequest.setMethod(new Method(method.getId()));
        }

        if (method.getType() == PaymentMethodType.VIPPS) {
            paymentRequest.setMethod(new Method(method.getId()));
            paymentRequest.setPaymentMethodActionList("[{PaymentMethod:Vipps}]");
            paymentRequest.setPhoneNumber(PiaSampleSharedPreferences.getPhoneNumber());
            paymentRequest.setRedirectUrl(String.format("%1$s://piasdk",
                    eu.nets.pia.sample.BuildConfig.APPLICATION_ID));
        }

        if (method.getType() == PaymentMethodType.SWISH) {
            paymentRequest.setMethod(new Method(method.getId()));
            paymentRequest.setPaymentMethodActionList("[{PaymentMethod:Swish}]");
            paymentRequest.setRedirectUrl(String.format("%1$s://piasdk",
                    eu.nets.pia.sample.BuildConfig.APPLICATION_ID));
        }

        return paymentRequest;
    }

}
