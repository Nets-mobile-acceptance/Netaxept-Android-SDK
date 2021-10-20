package eu.nets.pia.sample.ui.activity.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.ProcessResult;
import eu.nets.pia.card.CardPaymentRegistration;
import eu.nets.pia.card.CardProcessActivityLauncherInput;
import eu.nets.pia.card.CardProcessActivityResultContract;
import eu.nets.pia.card.CardScheme;
import eu.nets.pia.card.CardTokenPaymentRegistration;
import eu.nets.pia.card.PayPalActivityLauncherInput;
import eu.nets.pia.card.PayPalActivityResultContract;
import eu.nets.pia.card.PayPalPaymentRegistration;
import eu.nets.pia.card.PaytrailActivityLauncherInput;
import eu.nets.pia.card.PaytrailActivityResultContract;
import eu.nets.pia.card.PaytrailPaymentRegistration;
import eu.nets.pia.card.TransactionCallback;
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
import eu.nets.pia.sample.network.model.PaymentRegisterResponse;
import eu.nets.pia.sample.ui.activity.ConfirmationActivity;
import eu.nets.pia.sample.ui.activity.LoginActivity;
import eu.nets.pia.sample.ui.data.DisplayedToken;
import eu.nets.pia.sample.ui.data.PaymentMethod;
import eu.nets.pia.sample.ui.data.PaymentMethodType;
import eu.nets.pia.sample.ui.fragment.CheckoutFragment;
import eu.nets.pia.sample.ui.fragment.FragmentCallback;
import eu.nets.pia.sample.ui.fragment.PaymentMethodsFragment;
import eu.nets.pia.wallets.CardDisplay;
import eu.nets.pia.wallets.MobileWallet;
import eu.nets.pia.wallets.MobileWalletError;
import eu.nets.pia.wallets.MobileWalletListener;
import eu.nets.pia.wallets.PaymentProcess;
import eu.nets.pia.wallets.TokenizedCardPrompt;
import eu.nets.pia.wallets.WalletPaymentRegistration;
import eu.nets.pia.wallets.WalletURLCallback;

import static eu.nets.pia.sample.ui.fragment.PaymentMethodsFragment.ID_SWISH;

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


public class MainActivity extends AppCompatActivity implements MerchantRestClient.PaymentFlowCallback,
        FragmentCallback, MobileWalletListener {

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

        //clear all fragments added in backstack -- when activity is created, it's better to have a fresh stack
        clearFragmentStack();

        //first time activity is created show checkout
        changeFragment(new CheckoutFragment());


        //This is need to be set so that the SDK remember that what was the Skip Confirmation status.
        PiaInterfaceConfiguration.getInstance().setSkipConfirmationSelected(PiaSampleSharedPreferences.getEnableSkipConfirmation());

        if (!PiaSDK.willHandleRedirectWithIntent(getIntent(), this)) {
            // Activity launch is not triggered by PiaSDK initiated redirect.
        }
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
        showProgressBar();
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
//        if (mPaymentCache.getState() != PaymentFlowState.IDLE) return;

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
            } else if (method.getType() == PaymentMethodType.MOBILE_PAY) {
                mPaymentCache.setPaymentMethodSelected(PaymentMethodSelected.MOBILE_PAY);
            } else if (method.getType() == PaymentMethodType.AKTIA
                    || method.getType() == PaymentMethodType.ALANDSBANKEN
                    || method.getType() == PaymentMethodType.DANSKEBANK
                    || method.getType() == PaymentMethodType.HANDELSBANKEN
                    || method.getType() == PaymentMethodType.NORDEA
                    || method.getType() == PaymentMethodType.OMA_SAASTOPANKKI
                    || method.getType() == PaymentMethodType.OP_FINLAND
                    || method.getType() == PaymentMethodType.POP_PANKKI_FINLAND
                    || method.getType() == PaymentMethodType.S_PANKKI
                    || method.getType() == PaymentMethodType.SAASTOPANKKI_FINLAND) {
                mPaymentCache.setPaymentMethodSelected(PaymentMethodSelected.PAYTRAIL);
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
        if (method.getType() == PaymentMethodType.TOKEN
                || method.getType() == PaymentMethodType.CREDIT_CARDS
                || method.getType() == PaymentMethodType.S_BUSINESS_CARD
                || method.getType() == PaymentMethodType.VIPPS
                || method.getType() == PaymentMethodType.SWISH
                || method.getType() == PaymentMethodType.PAY_PAL
                || method.getType() == PaymentMethodType.AKTIA
                || method.getType() == PaymentMethodType.ALANDSBANKEN
                || method.getType() == PaymentMethodType.DANSKEBANK
                || method.getType() == PaymentMethodType.HANDELSBANKEN
                || method.getType() == PaymentMethodType.NORDEA
                || method.getType() == PaymentMethodType.OMA_SAASTOPANKKI
                || method.getType() == PaymentMethodType.OP_FINLAND
                || method.getType() == PaymentMethodType.POP_PANKKI_FINLAND
                || method.getType() == PaymentMethodType.S_PANKKI
                || method.getType() == PaymentMethodType.SAASTOPANKKI_FINLAND
                || method.getType() == PaymentMethodType.MOBILE_PAY) {
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
            }
        } else {
            if (mPaymentCache.getState() == PaymentFlowState.CALL_COMMIT_FAILED_NO_ROLLBACK) {
            } else if (mPaymentCache.getState() != PaymentFlowState.SENDING_ROLLBACK_TRANSACTION_CALL &&
                    mPaymentCache.getState() != PaymentFlowState.ROLLBACK_TRANSACTION_FINISHED) {
                //reset error state
                mPaymentCache.setFinishedWithError(false);
                //rollback transaction
                rollbackTransaction();
                //display status screen
                showPaymentResult(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), getString(R.string.process_error));
            } else {
                Toast.makeText(this, getString(R.string.payment_rollback_error), Toast.LENGTH_SHORT).show();
                mPaymentCache.reset();
            }
        }
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
     * @param method selected payment method
     */
    private void callPiaSDK(PaymentMethod method) {
        Log.d(TAG, "[callPiaSDK] start Pia SDK");

        switch (method.getType()) {
            case PAY_PAL:

                PiaSDK.startPayPalPayment(
                        payPalActivityLauncher,
                        merchantIDAndEnvironmentPair(),
                        payPalPaymentRegistration
                );

                break;
            case AKTIA:
            case ALANDSBANKEN:
            case DANSKEBANK:
            case HANDELSBANKEN:
            case NORDEA:
            case OMA_SAASTOPANKKI:
            case OP_FINLAND:
            case POP_PANKKI_FINLAND:
            case S_PANKKI:
            case SAASTOPANKKI_FINLAND:

                PiaSDK.startPaytrailPayment(
                        paytrailActivityLauncher,
                        merchantIDAndEnvironmentPair(),
                        paytrailPaymentRegistration
                );

                break;
            case VIPPS:
            case SWISH:
            case MOBILE_PAY:

                PaymentProcess.WalletPayment walletProcess = null;

                if (method.getType() == PaymentMethodType.MOBILE_PAY) {
                    walletProcess = PaymentProcess.mobilePay(this);
                }

                if (method.getType() == PaymentMethodType.SWISH) {
                    walletProcess = PaymentProcess.swish(this);
                }

                if (method.getType() == PaymentMethodType.VIPPS) {
                    walletProcess = PaymentProcess.vipps(PiaSampleSharedPreferences.isPiaTestMode(), this);
                }

                Boolean canLaunch = PiaSDK.initiateMobileWallet(walletProcess, mobileWalletRegistration);

                if (!canLaunch) {
                    Toast.makeText(this, "Wallet is not installed", Toast.LENGTH_LONG).show();
                    return;
                }

                break;

            case TOKEN:

                TokenCardInfo tokenCardInfo = getTokenizedCardInfo((DisplayedToken) method);

                CardScheme cardScheme = cardSchemeFrom(tokenCardInfo.getSchemeId());

                CardDisplay cardDisplay = PiaSampleSharedPreferences.IsCustomCardSchemeImageSelected() ?
                        CardDisplay.Companion.customCardImage(R.drawable.custom_card, cardScheme) :
                        CardDisplay.Companion.card(cardScheme);

                TokenizedCardPrompt confirmationPrompt = TokenizedCardPrompt.Companion.forAmount(
                        amountAndCurrencyCodePair(),
                        method.isCvcRequired()
                );

                PiaSDK.startCardProcessActivity(
                        cardTokenActivityLauncher,
                        PaymentProcess.cardTokenPayment(
                                merchantIDAndEnvironmentPair(),
                                tokenCardInfo.getTokenId(),
                                tokenCardInfo.getExpiryDate(),
                                cardDisplay,
                                confirmationPrompt,
                                cardTokenPaymentRegistration
                        ),
                        method.isCvcRequired()
                );

                break;

            case S_BUSINESS_CARD:

                PiaSDK.startSBusinessCardProcessActivity(
                        cardPaymentActivityLauncher,
                        PaymentProcess.cardPayment(
                                merchantIDAndEnvironmentPair(),
                                PiaSampleSharedPreferences.getExcludedCardSchemeSet(),
                                amountAndCurrencyCodePair(),
                                cardPaymentRegistration
                        ),
                        method.isCvcRequired()
                );

                break;

            default:/*Default condition includes Card payments*/

                PiaSDK.startCardProcessActivity(
                        cardPaymentActivityLauncher,
                        PaymentProcess.cardPayment(
                                merchantIDAndEnvironmentPair(),
                                PiaSampleSharedPreferences.getExcludedCardSchemeSet(),
                                amountAndCurrencyCodePair(),
                                cardPaymentRegistration
                        ),
                        method.isCvcRequired()
                );
        }
        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        mPaymentCache.setState(PaymentFlowState.CALL_PIA_SDK);

    }

    private CardScheme cardSchemeFrom(SchemeType schemeType) {
        switch (schemeType) {
            case VISA: return CardScheme.visa;
            case MASTER_CARD: return CardScheme.masterCard;
            case AMEX: return CardScheme.amex;
            case DINERS_CLUB_INTERNATIONAL: return CardScheme.dinersClubInternational;
            case DANKORT: return CardScheme.dankort;
            case JCB: return CardScheme.jcb;
            case MAESTRO: return CardScheme.maestro;
            case SGROUP: return CardScheme.sBusiness;
            default: return null;
        }
    }

    private Pair<String, PiaSDK.Environment> merchantIDAndEnvironmentPair() {
        Boolean isTest = PiaSampleSharedPreferences.isPiaTestMode();
        String merchantID = getMerchantId(isTest);
        return Pair.create(merchantID, isTest ? PiaSDK.Environment.TEST : PiaSDK.Environment.PROD);
    }

    private Pair<Integer, String> amountAndCurrencyCodePair() {
        String amount = getCheckoutPriceString();
        int amountInCents = (int) (Double.parseDouble(
                (amount == null || amount.isEmpty() ? "0" : amount)) * 100
        );
        String currency = PiaSampleSharedPreferences.getCustomerCurrency();
        return Pair.create(amountInCents, currency);
    }

    ActivityResultLauncher<CardProcessActivityLauncherInput> cardPaymentActivityLauncher = registerForActivityResult(
            new CardProcessActivityResultContract(),
            this::transactionCompleteResult
    );

    ActivityResultLauncher<CardProcessActivityLauncherInput> cardTokenActivityLauncher = registerForActivityResult(
            new CardProcessActivityResultContract(),
            this::transactionCompleteResult
    );

    ActivityResultLauncher<PayPalActivityLauncherInput> payPalActivityLauncher = registerForActivityResult(
            new PayPalActivityResultContract(),
            this::transactionCompleteResult
    );

    ActivityResultLauncher<PaytrailActivityLauncherInput> paytrailActivityLauncher = registerForActivityResult(
            new PaytrailActivityResultContract(),
            this::transactionCompleteResult
    );

    void transactionCompleteResult(ProcessResult result) {
        if (result instanceof ProcessResult.Success) {
            //in case of success commit the payment
            showProgressBar();

            /*
             *Transaction ID from SDK for rollback call
             */
            String transactionId = ((ProcessResult.Success) result).getTransactionID();

            /*
             * If the result is success, call commit payment on your backend
             */
            callCommitPayment(transactionId);

        } else if (result instanceof ProcessResult.Failure) {

            /*
             *Failure message to be displayed in Confirmation page
             */
            String message = ((ProcessResult.Failure) result).getProcessError().toString();

            showPaymentResult(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), message);

        } else if (result instanceof ProcessResult.Cancellation) {

            /*
             *Cancellation message to be displayed in Confirmation page
             */
            String message = ((ProcessResult.Cancellation) result).getReason();

            /*
             *Transaction ID from SDK for rollback call
             */
            String transactionId = ((ProcessResult.Cancellation) result).getTransactionID();

            if (transactionId != null) {
                mRestClient.transactionRollback(transactionId);
            }

            showPaymentResult(ConfirmationActivity.Result.CANCELLATION, getString(R.string.toolbar_title_cancelled), message);
        }
    }

    CardPaymentRegistration cardPaymentRegistration = new CardPaymentRegistration() {
        @Override
        public void registerPayment(boolean shouldStoreCard, @NotNull TransactionCallback callbackWithTransaction) {
            final PaymentRegisterRequest paymentRegisterRequest = mPaymentCache.getPaymentRegisterRequest();
            paymentRegisterRequest.setStoreCard(shouldStoreCard);
            new Thread() {
                @Override
                public void run() {
                    MerchantRestClient.getInstance().registerPayment(paymentRegisterRequest);
                    PaymentRegisterResponse paymentRegisterResponse = mPaymentCache.getPaymentRegisterResponse();
                    if (paymentRegisterResponse != null && paymentRegisterResponse.getTransactionId() != null) {
                        callbackWithTransaction.successWithTransactionIDAndRedirectURL(paymentRegisterResponse.getTransactionId(), Uri.parse(paymentRegisterResponse.getRedirectOK()));
                    } else {
                        callbackWithTransaction.failureWithError(null);
                    }
                }
            }.start();
        }
    };

    CardTokenPaymentRegistration cardTokenPaymentRegistration = new CardTokenPaymentRegistration() {
        @Override
        public void registerPayment(@NotNull TransactionCallback callbackWithTransaction) {
            final PaymentRegisterRequest paymentRegisterRequest = mPaymentCache.getPaymentRegisterRequest();
            paymentRegisterRequest.setStoreCard(false);
            new Thread() {
                @Override
                public void run() {
                    MerchantRestClient.getInstance().registerPayment(paymentRegisterRequest);
                    PaymentRegisterResponse paymentRegisterResponse = mPaymentCache.getPaymentRegisterResponse();
                    if (paymentRegisterResponse != null && paymentRegisterResponse.getTransactionId() != null) {
                        callbackWithTransaction.successWithTransactionIDAndRedirectURL(paymentRegisterResponse.getTransactionId(), Uri.parse(paymentRegisterResponse.getRedirectOK()));
                    } else {
                        callbackWithTransaction.failureWithError(null);
                    }
                }
            }.start();
        }
    };

    PayPalPaymentRegistration payPalPaymentRegistration = new PayPalPaymentRegistration() {
        @Override
        public void registerPayment(@NotNull TransactionCallback callbackWithTransaction) {
            final PaymentRegisterRequest paymentRegisterRequest = mPaymentCache.getPaymentRegisterRequest();
            paymentRegisterRequest.setStoreCard(false);
            new Thread() {
                @Override
                public void run() {
                    MerchantRestClient.getInstance().registerPayment(paymentRegisterRequest);
                    PaymentRegisterResponse paymentRegisterResponse = mPaymentCache.getPaymentRegisterResponse();
                    if (paymentRegisterResponse != null && paymentRegisterResponse.getTransactionId() != null) {
                        callbackWithTransaction.successWithTransactionIDAndRedirectURL(paymentRegisterResponse.getTransactionId(), Uri.parse(paymentRegisterResponse.getRedirectOK()));
                    } else {
                        callbackWithTransaction.failureWithError(null);
                    }
                }
            }.start();
        }
    };

    PaytrailPaymentRegistration paytrailPaymentRegistration = new PaytrailPaymentRegistration() {
        @Override
        public void registerPayment(@NotNull TransactionCallback callbackWithTransaction) {
            final PaymentRegisterRequest paymentRegisterRequest = mPaymentCache.getPaymentRegisterRequest();
            paymentRegisterRequest.setStoreCard(false);
            new Thread() {
                @Override
                public void run() {
                    MerchantRestClient.getInstance().registerPayment(paymentRegisterRequest);
                    PaymentRegisterResponse paymentRegisterResponse = mPaymentCache.getPaymentRegisterResponse();
                    if (paymentRegisterResponse != null && paymentRegisterResponse.getTransactionId() != null) {
                        callbackWithTransaction.successWithTransactionIDAndRedirectURL(paymentRegisterResponse.getTransactionId(), Uri.parse(paymentRegisterResponse.getRedirectOK()));
                    } else {
                        callbackWithTransaction.failureWithError(null);
                    }
                }
            }.start();
        }
    };

    WalletPaymentRegistration mobileWalletRegistration = new WalletPaymentRegistration() {
        @Override
        public void registerPayment(final WalletURLCallback callback) {
            final PaymentRegisterRequest paymentRegisterRequest = mPaymentCache.getPaymentRegisterRequest();
            paymentRegisterRequest.setStoreCard(false);
            new Thread() {
                @Override
                public void run() {
                    MerchantRestClient.getInstance().registerPayment(paymentRegisterRequest);
                    PaymentRegisterResponse paymentRegisterResponse = mPaymentCache.getPaymentRegisterResponse();
                    if (paymentRegisterResponse != null && paymentRegisterResponse.getWalletUrl() != null) {
                        callback.successWithWalletURL(Uri.parse(paymentRegisterResponse.getWalletUrl()));
                    } else {
                        callback.failureWithError(null);
                    }
                }
            }.start();

        }
    };

    @Override
    public void onMobileWalletRedirect(MobileWallet wallet) {
        commitMobilePay(true, dismissSdkProgressIndicator);
    }

    @Override
    public void onMobileWalletRedirectInterrupted(MobileWallet wallet) {
        commitMobilePay(false, dismissSdkProgressIndicator);
    }

    MerchantRestClient.Completion dismissSdkProgressIndicator = new MerchantRestClient.Completion() {
        @Override
        public void success(boolean isSuccess) {

            if (isSuccess)
                showPaymentResult(ConfirmationActivity.Result.SUCCESS, getString(R.string.toolbar_title_success_pay), getString(R.string.thanks_for_shopping));
            else {
                if (mPaymentCache.getState() == PaymentFlowState.CALL_COMMIT_FAILED_NO_ROLLBACK) {
                    showPaymentResultMaintainCache(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), getString(R.string.payment_interrupted));
                } else {
                    showPaymentResult(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), getString(R.string.process_error));
                }
            }
        }
    };

    @Override
    public void onMobileWalletAppSwitchFailure(MobileWallet wallet, MobileWalletError error) {
        switch (error) {
            case INVALID_WALLET_URL: // The Wallet URL is invalid, typically null
            case NETWORK_ERROR: // Check `error.data` for detail error response
            case WALLET_APP_NOT_FOUND: // User may have uninstalled MobilePay. Recommend other payment methods
        }
        showPaymentResult(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), error.getDescription());
    }

    /**
     * Commit call method
     *
     * @param transactionId Transaction ID returned from the SDK success callback
     */
    private void callCommitPayment(String transactionId) {
        mRestClient.commitPayment(transactionId, true, new MerchantRestClient.Completion() {
            @Override
            public void success(boolean isSuccess) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showPaymentResult(ConfirmationActivity.Result.SUCCESS, getString(R.string.toolbar_title_success_pay), getString(R.string.thanks_for_shopping));
                    }
                });
            }
        });
    }

    private void commitMobilePay(boolean isSuccessfulRedirect, MerchantRestClient.Completion completion) {
        showProgressBar();
        if (mPaymentCache.getPaymentRegisterResponse() != null
                && mPaymentCache.getPaymentRegisterResponse().getTransactionId() != null) {
            mRestClient.commitPayment(mPaymentCache.getPaymentRegisterResponse().getTransactionId(), isSuccessfulRedirect, completion);
        } else {
            showPaymentResult(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), getString(R.string.process_error));
            completion.success(false);
        }
    }

    private void rollbackTransaction() {
        if (mPaymentCache.getPaymentRegisterResponse() != null
                && mPaymentCache.getPaymentRegisterResponse().getTransactionId() != null) {
            mRestClient.transactionRollback(mPaymentCache.getPaymentRegisterResponse().getTransactionId());
        }
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    private void dismissProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void showPaymentResult(ConfirmationActivity.Result result, String title, String message) {
        dismissProgressBar();
        Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.RESULT, result);
        intent.putExtra(ConfirmationActivity.TITLE, title);
        intent.putExtra(ConfirmationActivity.MESSAGE, message);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        mPaymentCache.reset();
    }

    private void showPaymentResultMaintainCache(ConfirmationActivity.Result result, String title, String message) {
        dismissProgressBar();
        Intent intent = new Intent(MainActivity.this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.RESULT, result);
        intent.putExtra(ConfirmationActivity.TITLE, title);
        intent.putExtra(ConfirmationActivity.MESSAGE, message);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        mPaymentCache.resetStateAndPaymentMethod();
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
        if (issuer.equals("diners club")) {
            return SchemeType.DINERS_CLUB_INTERNATIONAL;
        }
        if (issuer.equals("amex") || issuer.equals("americanexpress")) {
            return SchemeType.AMEX;
        }
        if (issuer.equals("sbusinesscard")) {
            return SchemeType.SGROUP;
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

        if (method.getType() == PaymentMethodType.AKTIA
                || method.getType() == PaymentMethodType.ALANDSBANKEN
                || method.getType() == PaymentMethodType.DANSKEBANK
                || method.getType() == PaymentMethodType.HANDELSBANKEN
                || method.getType() == PaymentMethodType.NORDEA
                || method.getType() == PaymentMethodType.OMA_SAASTOPANKKI
                || method.getType() == PaymentMethodType.OP_FINLAND
                || method.getType() == PaymentMethodType.POP_PANKKI_FINLAND
                || method.getType() == PaymentMethodType.S_PANKKI
                || method.getType() == PaymentMethodType.SAASTOPANKKI_FINLAND) {

            paymentRequest.setMethod(new Method(method.getId()));

            //Merchant specific User details
            paymentRequest.setCustomerEmail("bill.buyer@nets.eu");
            paymentRequest.setCustomerFirstName("Bill");
            paymentRequest.setCustomerLastName("Buyer");
            paymentRequest.setCustomerAddress1("Testaddress");
            paymentRequest.setCustomerPostCode("00510");
            paymentRequest.setCustomerTown("Helsinki");
            paymentRequest.setCustomerCountry("FI");
            //Merchant specific User details

            paymentRequest.setOrderNumber(getPaytrailOrderNumber());
        }

        if (method.getType() == PaymentMethodType.MOBILE_PAY) {
            paymentRequest.setPaymentMethodActionList("[{PaymentMethod:MobilePay}]");
            paymentRequest.setRedirectUrl(String.format("%1$s://piasdk_mobilepay",
                    eu.nets.pia.sample.BuildConfig.APPLICATION_ID));
            paymentRequest.setMethod(new Method(method.getId()));
        }

        if (method.getType() == PaymentMethodType.S_BUSINESS_CARD) {
            paymentRequest.setPaymentMethodActionList("[{PaymentMethod:SBusinessCard}]");
        }

        return paymentRequest;
    }

    //An algorithm to create reference number to your invoices as per Finnish Payment Guidelines.
    private String getPaytrailOrderNumber() {

        int checkDigit = -1;
        int[] multipliers = {7, 3, 1};
        int multiplierIndex = 0;
        int sum = 0;
        final String DATE_TIME_FORMAT = "yyMMddHHmmssSSS";

        // Storing random positive integers in an array. '1' is appended in the beginning of the
        // order number in order to differentiate between Android and iOS (0 for iOS and 1 for Android)
        String orderNumber = "1" + new SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault()).format(new Date());

        //Sum of the product of each element of randomNumber and multipliers in right to left manner
        for (int i = orderNumber.length() - 1; i >= 0; i--) {
            if (multiplierIndex == 3) {
                multiplierIndex = 0;
            }
            int value = Character.getNumericValue(orderNumber.charAt(i));
            sum += value * multipliers[multiplierIndex];
            multiplierIndex++;
        }

        //The sum is then subtracted from the next highest ten
        checkDigit = 10 - sum % 10;

        if (checkDigit == 10) {
            checkDigit = 0;
        }

        return orderNumber + checkDigit;

    }

}
