package eu.nets.pia.sample.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.ProcessResult;
import eu.nets.pia.RegisterPaymentHandler;
import eu.nets.pia.card.CardProcessActivityLauncherInput;
import eu.nets.pia.card.CardProcessActivityResultContract;
import eu.nets.pia.card.CardScheme;
import eu.nets.pia.card.CardTokenizationRegistration;
import eu.nets.pia.card.TransactionCallback;
import eu.nets.pia.data.model.MerchantInfo;
import eu.nets.pia.data.model.PiaLanguage;
import eu.nets.pia.sample.BuildConfig;
import eu.nets.pia.sample.R;
import eu.nets.pia.sample.RegisterPaymentHandlerImpl;
import eu.nets.pia.sample.data.PaymentFlowCache;
import eu.nets.pia.sample.data.PaymentFlowState;
import eu.nets.pia.sample.data.PiaSampleSharedPreferences;
import eu.nets.pia.sample.network.MerchantRestClient;
import eu.nets.pia.sample.network.model.Amount;
import eu.nets.pia.sample.network.model.PaymentMethodsResponse;
import eu.nets.pia.sample.network.model.PaymentRegisterRequest;
import eu.nets.pia.sample.network.model.PaymentRegisterResponse;
import eu.nets.pia.sample.ui.activity.main.MainActivity;
import eu.nets.pia.sample.ui.adapter.LanguageAdapter;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import eu.nets.pia.wallets.PaymentProcess;

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

public class LoginActivity extends AppCompatActivity implements MerchantRestClient.PaymentFlowCallback {

    private static final String TAG = LoginActivity.class.getSimpleName();

    //not logged in views
    @BindView(R.id.not_logged_in_layout)
    RelativeLayout mNotLoggedInLayout;
    @BindView(R.id.customer_id_et)
    EditText mSignUpEditText;
    //end
    //logged in views
    @BindView(R.id.main_layout)
    RelativeLayout mMainLayout;
    @BindView(R.id.imageView)
    ImageView mBackground;
    @BindView(R.id.toolbar)
    CustomToolbar mToolbar;
    @BindView(R.id.customer_id_label)
    TextView mCustomerIdLabel;
    @BindView(R.id.app_version_label)
    TextView mAppVersionLabel;
    @BindView(R.id.url_switch)
    SwitchCompat mUrlSwitch;
    @BindView(R.id.switch_sistem_auth)
    SwitchCompat mSystemAuthSwitch;
    @BindView(R.id.switch_disable_visa)
    SwitchCompat disableVisaSwitch;
    @BindView(R.id.switch_disable_image)
    SwitchCompat includeCustomCardImageSwitch;
    //section-start-to-remove-by-script
    @BindView(R.id.switch_disable_cardio)
    SwitchCompat mDisableCardIOSwitch;
    //section-end-to-remove-by-script
    @BindView(R.id.switch_sample_skip_confirmation)
    SwitchCompat mSkipConfirmationSwitch;
    @BindView(R.id.language_dropdown)
    Spinner mLanguageSpinner;

    @BindView(R.id.phone_number_text)
    TextView customerPhoneNumber;
    @BindView(R.id.change_phone_number)
    TextView changePhoneNumber;

    //end
    @BindView(R.id.spinner_holder)
    protected RelativeLayout mProgressBar;

    private static final String ORDER_NUMBER = "PiaSDK-Android";
    private static final String DEFAULT_LANGUAGE = "Select";
    private static PiaLanguage currentSelection;

    private MerchantRestClient mRestClient = MerchantRestClient.getInstance();
    private PaymentFlowCache mPaymentCache;
    private RegisterPaymentHandler mRegisterPaymentHandler;

    ActivityResultLauncher<CardProcessActivityLauncherInput> cardStorageActivityLauncher = registerForActivityResult(
            new CardProcessActivityResultContract(),
            this::transactionCompleteResult
    );

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (PiaSampleSharedPreferences.getCustomerId().isEmpty()) {
            //user is not logged in
            mToolbar.setVisibility(View.GONE);
        } else {
            //user is logged in
            mToolbar.setVisibility(View.VISIBLE);
        }
        mToolbar.setTitle(getString(R.string.settings_title));

        initViews();

        mPaymentCache = PaymentFlowCache.getInstance();
        mRegisterPaymentHandler = new RegisterPaymentHandlerImpl();
    }

    @Override
    protected void onResume() {
        // refresh the Rest Client state (if user switched between PROD and TEST,
        // a new instance will be created. If not, the same instance will be returned
        mRestClient = MerchantRestClient.getInstance();
        //add listener to MerchantClient so it will get callbacks when activity is visible
        mRestClient.addListener(this);
        super.onResume();
    }

    @Override
    protected void onPause() {
        //remove listener from MerchantClient to avoid getting unnecessary callbacks when activity is not visible
        mRestClient.removeListener(this);
        super.onPause();
    }

    private void initViews() {
        String customerId = PiaSampleSharedPreferences.getCustomerId();
        String phoneNumber = PiaSampleSharedPreferences.getPhoneNumber();
        if (customerId.isEmpty()) {
            //user is not logged in
            mMainLayout.setVisibility(View.GONE);
            mNotLoggedInLayout.setVisibility(View.VISIBLE);
        } else {
            mMainLayout.setVisibility(View.VISIBLE);
            mCustomerIdLabel.setText(customerId);
            mAppVersionLabel.setText(PiaSDK.getInstance().getVersionName());
            customerPhoneNumber.setText(phoneNumber == null ? "Not configured" : phoneNumber);

            mUrlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //save current configuration
                    PiaSampleSharedPreferences.setPiaTestMode(isChecked);
                    //notify application's Rest client to use the proper url
                    MerchantRestClient.notifyBackendConfigurationChanged();
                }
            });

            mSystemAuthSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PiaSampleSharedPreferences.setUseSystemAuth(isChecked);
                }
            });

            //section-start-to-remove-by-script
            mDisableCardIOSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PiaSampleSharedPreferences.setDisableCardIo(isChecked);
                    //also, update the flag inside the SDK
                    PiaInterfaceConfiguration.getInstance().setDisableCardIO(isChecked);
                }
            });
            //section-end-to-remove-by-script

            disableVisaSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                HashSet<CardScheme> excludeCardSchemes = new HashSet<>();
                if (isChecked) {
                    // Exclude all card schemes except `visa`
                    excludeCardSchemes.addAll(Arrays.asList(CardScheme.values()));
                    excludeCardSchemes.remove(CardScheme.visa);
                }

                PiaSampleSharedPreferences.setExcludedCardSchemeSet(excludeCardSchemes);
            });

            includeCustomCardImageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                PiaSampleSharedPreferences.setCustomCardSchemeImageSelected(isChecked);
            });
            includeCustomCardImageSwitch.setChecked(PiaSampleSharedPreferences.IsCustomCardSchemeImageSelected());

            mSkipConfirmationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PiaSampleSharedPreferences.setEnableSkipConfirmation(isChecked);
                    //also, update the flag inside the SDK
                    PiaInterfaceConfiguration.getInstance().setSkipConfirmationSelected(isChecked);
                }
            });

            mSystemAuthSwitch.setChecked(PiaSampleSharedPreferences.isUseSystemAuth());
            mUrlSwitch.setChecked(PiaSampleSharedPreferences.isPiaTestMode());

            //section-start-to-remove-by-script
            mDisableCardIOSwitch.setChecked(PiaSampleSharedPreferences.isDisableCardIo());
            //section-end-to-remove-by-script

            disableVisaSwitch.setChecked(!PiaSampleSharedPreferences.getExcludedCardSchemeSet().isEmpty());

            mSkipConfirmationSwitch.setChecked(PiaSampleSharedPreferences.getEnableSkipConfirmation());

            setupLanguageSpinner();
        }
    }

    private void setupLanguageSpinner() {
        List<String> spinnerArrayList = new ArrayList<String>(Arrays.asList(
                DEFAULT_LANGUAGE,
                PiaLanguage.ENGLISH.getLanguageName(),
                PiaLanguage.SWEDISH.getLanguageName(),
                PiaLanguage.DANISH.getLanguageName(),
                PiaLanguage.NORWEGIAN.getLanguageName(),
                PiaLanguage.FINNISH.getLanguageName()
        ));

        final LanguageAdapter adapter = new LanguageAdapter(this, android.R.layout.simple_spinner_item, spinnerArrayList);
        mLanguageSpinner.setAdapter(adapter);

        //set previous selection
        if (currentSelection != null) {
            //show previous selection
            mLanguageSpinner.setSelection(adapter.getPositionForItem(currentSelection.getLanguageName()));
        } else {
            //show default option
            mLanguageSpinner.setSelection(adapter.getPositionForItem(DEFAULT_LANGUAGE));
        }


        mLanguageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //set it in the PiaInterfaceConfiguration
                currentSelection = PiaLanguage.findByName(adapter.getItem(position));
                PiaInterfaceConfiguration.getInstance().setPiaLanguage(currentSelection);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });
    }

    @OnClick(R.id.change_customer_id)
    public void onChangeCustomerId() {
        //show input popup to change id
        showInputDialog();
    }

    @OnClick(R.id.change_phone_number)
    public void onChangePhoneNumber() {
        //show input popup to change phone number
        showInputDialogChangeNumber();
    }

    @OnClick(R.id.action_customize_ui)
    public void onCustomizeUi() {
        startActivity(new Intent(this, UICustomizationActivity.class));
    }

    @OnClick(R.id.details_app_version)
    public void onShowAppVersion() {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogTheme);

            builder.setTitle(getString(R.string.app_version));
            builder.setMessage(String.format("%1$s (%2$s)", PiaSDK.getInstance().getVersionName(), PiaSDK.getInstance().getTechnicalVersion()));
            builder.setCancelable(false);

            builder.setPositiveButton(
                    getString(R.string.alert_ok),
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();
        } catch (Exception e) {
            //in case activity is not attached to window -- catch exception here and do nothing
        }
    }

    @OnClick(R.id.sign_up_btn)
    public void onSignUp() {
        String customerId = mSignUpEditText.getText().toString();
        if (customerId.isEmpty()) {
            Toast.makeText(LoginActivity.this, getString(R.string.customer_id_is_invalid), Toast.LENGTH_SHORT).show();
        } else {
            customerId = fillZeros(customerId);
            PiaSampleSharedPreferences.saveCustomerId(customerId);

            //if is first login -- redirect the user to checkout because the MainActivity is not in the backstack
            //and navigating back will close the app
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();

        }
    }

    @OnClick(R.id.action_change_merchant_info)
    public void onChangeMerchantInfo() {
        startActivity(new Intent(this, MerchantBESettingsActivity.class));
    }

    private void showInputDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.InputAlertDialogTheme);

        alertDialog.setCancelable(false);
        alertDialog.setTitle(R.string.new_customer_id);

        LinearLayout layout = new LinearLayout(this);
        final EditText input = new EditText(this);
        input.setHint(R.string.customer_id_hint);
        input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        input.setTextColor(Color.BLACK);
        input.setHintTextColor(ContextCompat.getColor(this, R.color.light_gray));

        LinearLayout.LayoutParams parentLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(parentLp);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(20, 20, 20, 10);

        LinearLayout.LayoutParams childLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        input.setLetterSpacing(0.5f);
        input.setLayoutParams(childLp);
        input.setGravity(Gravity.CENTER);
        childLp.gravity = Gravity.CENTER;

        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(2, ContextCompat.getColor(this, R.color.light_gray));
        input.setBackground(drawable);
        layout.addView(input);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton(R.string.action_save, null);
        alertDialog.setNegativeButton(R.string.pia_action_cancel, null);

        final AlertDialog mDialog = alertDialog.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button saveBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String customerId = input.getText().toString();
                        if (customerId.isEmpty()) {
                            //show the toast but do not dismiss the dialog
                            Toast.makeText(LoginActivity.this, getString(R.string.customer_id_is_invalid), Toast.LENGTH_SHORT).show();
                        } else {
                            customerId = fillZeros(customerId);
                            PiaSampleSharedPreferences.saveCustomerId(customerId);
                            mCustomerIdLabel.setText(customerId);
                            mDialog.cancel();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!PiaSampleSharedPreferences.getCustomerId().isEmpty()) {
                            // dismiss dialog only if is not login
                            mDialog.cancel();
                        }
                    }
                });
            }
        });

        try {
            mDialog.show();
        } catch (Exception e) {
            //just make sure nothing goes wrong
        }
    }

    /**
     * This is the Save Card functionality;
     */
    @OnClick(R.id.save_card)
    public void onSaveCardBtnClicked() {
        //store in cache the payment request -- for register, a payment with zero amount is required
        mPaymentCache.setPaymentRegisterRequest(getPaymentRequest());

        String merchantID = getMerchantInfo().getMerchantId();
        PiaSDK.Environment environment = getMerchantInfo().isTestMode() ?
                PiaSDK.Environment.TEST : PiaSDK.Environment.PROD;

        PiaSDK.startCardProcessActivity(
                cardStorageActivityLauncher,
                PaymentProcess.cardTokenization(
                        Pair.create(merchantID, environment),
                        PiaSampleSharedPreferences.getExcludedCardSchemeSet(),
                        cardStorageRegistration
                ),
                getMerchantInfo().isCvcRequired()
        );

    }

    /**
     * This is the Save S-Business Card functionality;
     */
    @OnClick(R.id.save_sgroup_card)
    public void onSaveSGroupCardBtnClicked() {
        //store in cache the payment request -- for register, a payment with zero amount is requred
        mPaymentCache.setPaymentRegisterRequest(getPaymentRequest());

        String merchantID = getMerchantInfo().getMerchantId();
        PiaSDK.Environment environment = getMerchantInfo().isTestMode() ?
                PiaSDK.Environment.TEST : PiaSDK.Environment.PROD;

        PiaSDK.startSBusinessCardProcessActivity(
                cardPaymentActivityLauncher,
                PaymentProcess.cardTokenization(
                        Pair.create(merchantID, environment),
                        PiaSampleSharedPreferences.getExcludedCardSchemeSet(),
                        cardStorageRegistration
                ),
                getMerchantInfo().isCvcRequired()
        );

    }

    ActivityResultLauncher<CardProcessActivityLauncherInput> cardPaymentActivityLauncher = registerForActivityResult(
            new CardProcessActivityResultContract(),
            this::transactionCompleteResult
    );

    CardTokenizationRegistration cardStorageRegistration = new CardTokenizationRegistration() {
        @Override
        public void registerPayment(@NotNull TransactionCallback callbackWithTransaction) {
            final PaymentRegisterRequest paymentRegisterRequest = mPaymentCache.getPaymentRegisterRequest();
            paymentRegisterRequest.setStoreCard(true);
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

    void transactionCompleteResult(ProcessResult result) {
        if (result instanceof ProcessResult.Success) {
            ProcessResult success = (ProcessResult.Success) result;

            mProgressBar.setVisibility(View.VISIBLE);
            //call /storeCard API with the transaction ID

            /*
             *Transaction ID from SDK for rollback call
             */
            String transactionId = ((ProcessResult.Success) result).getTransactionID();

            /**
             * If the result is success, call verifyPayment (storeCard API) on your backend
             */
            mRestClient.verifyPayment(transactionId, new MerchantRestClient.Completion() {
                @Override
                public void success(boolean isSuccess) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showPaymentResult(ConfirmationActivity.Result.SUCCESS, getString(R.string.toolbar_title_success_save), getString(R.string.card_saved_success));
                        }
                    });
                }
            });

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
        } else {
            /*
             *Failure message to be displayed in Confirmation page
             */
            String message = ((ProcessResult.Failure) result).getProcessError().toString();

            showPaymentResult(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), message);
        }
    }

    private PaymentRegisterRequest getPaymentRequest() {

        Long price = 0L;

        Amount amount = new Amount();
        amount.setCurrencyCode(PiaSampleSharedPreferences.getCustomerCurrency());
        amount.setTotalAmount(price);
        amount.setVatAmount((long) 0);

        PaymentRegisterRequest paymentRequest = new PaymentRegisterRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setOrderNumber(ORDER_NUMBER);
        paymentRequest.setCustomerId(PiaSampleSharedPreferences.getCustomerId());

        return paymentRequest;
    }

    private void showPaymentResult(ConfirmationActivity.Result result, String title, String message) {
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, ConfirmationActivity.class);
        intent.putExtra(ConfirmationActivity.RESULT, result);
        intent.putExtra(ConfirmationActivity.TITLE, title);
        intent.putExtra(ConfirmationActivity.MESSAGE, message);
        startActivity(intent);

        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
        mPaymentCache.reset();
    }

    /**
     * In this object, you notify the SDK about:
     * 1. Which merchant is requesting the payments to be processed (your merchant ID)
     * 2. Which base URL should be used: TEST or PRODUCTION
     * 3. Always show the CVC entry.
     *
     * @return MerchantInfo object
     */
    private MerchantInfo getMerchantInfo() {

        if (PiaSampleSharedPreferences.isPiaTestMode()) {
            //launch SDK with test merchant id and flag testMode = true to point SDK to test env
            return new MerchantInfo(
                    getMerchantId(true),
                    true,
                    true
            );
        } else {
            //launch SDK with production merchant id; if testMode is not specified, is false by default
            return new MerchantInfo(
                    getMerchantId(false),
                    false,
                    true
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

    private String fillZeros(String customerId) {
        String zeros = "";
        for (int i = 0; i < 6 - customerId.length(); i++) {
            zeros = zeros.concat("0");
        }
        return zeros.concat(customerId);
    }

    @Override
    public void onPaymentCallFinished() {
        handlePaymentFlowState();
    }

    @Override
    public void onGetPaymentMethods(PaymentMethodsResponse response) {
        //not available
    }

    private void handlePaymentFlowState() {
        Log.d(TAG, "[handlePaymentFlowState] [isFinishedWithError=" + mPaymentCache.isFinishedWithError() +
                "; state=" + mPaymentCache.getState() + "]");
        if (!mPaymentCache.isFinishedWithError()) {
            switch (mPaymentCache.getState()) {
                case COMMIT_PAYMENT_CALL_FINISHED:
                    showPaymentResult(ConfirmationActivity.Result.SUCCESS, getString(R.string.toolbar_title_success_save), getString(R.string.card_saved_success));
                    break;
                case IDLE:
                    mProgressBar.setVisibility(View.GONE);
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

                showPaymentResult(ConfirmationActivity.Result.FAILURE, getString(R.string.toolbar_title_failed), getString(R.string.process_error));

            } else {
                Toast.makeText(this, getString(R.string.payment_rollback_error), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void rollbackTransaction() {
        if (mPaymentCache.getPaymentRegisterResponse() != null &&
                mPaymentCache.getPaymentRegisterResponse().getTransactionId() != null) {
            mRestClient.transactionRollback(
                    mPaymentCache.getPaymentRegisterResponse().getTransactionId()
            );
        }
    }

    private void showInputDialogChangeNumber() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.InputAlertDialogTheme);

        alertDialog.setCancelable(false);
        alertDialog.setTitle("Phone Number");

        LinearLayout layout = new LinearLayout(this);
        final EditText input = new EditText(this);
        input.setHint("+4741111111");
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setTextColor(Color.BLACK);
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setHintTextColor(ContextCompat.getColor(this, R.color.light_gray));

        LinearLayout.LayoutParams parentLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(parentLp);
        layout.setGravity(Gravity.CENTER);
        layout.setPadding(20, 20, 20, 10);

        LinearLayout.LayoutParams childLp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        input.setLetterSpacing(0.5f);
        input.setLayoutParams(childLp);
        input.setGravity(Gravity.CENTER);
        childLp.gravity = Gravity.CENTER;

        GradientDrawable drawable = new GradientDrawable();
        drawable.setStroke(2, ContextCompat.getColor(this, R.color.light_gray));
        input.setBackground(drawable);
        layout.addView(input);

        alertDialog.setView(layout);

        alertDialog.setPositiveButton(R.string.action_save, null);
        alertDialog.setNegativeButton(R.string.pia_action_cancel, null);

        final AlertDialog mDialog = alertDialog.create();

        mDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button saveBtn = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnCancel = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEGATIVE);

                saveBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String phoneNumner = input.getText().toString();
                        if (phoneNumner.isEmpty()) {
                            //show the toast but do not dismiss the dialog
                            Toast.makeText(LoginActivity.this, "Please enter phone number", Toast.LENGTH_SHORT).show();
                        } else {

                            PiaSampleSharedPreferences.setPhoneNumber(phoneNumner);
                            customerPhoneNumber.setText(phoneNumner);
                            mDialog.cancel();
                        }
                    }
                });

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!PiaSampleSharedPreferences.getCustomerId().isEmpty()) {
                            // dismiss dialog only if is not login
                            mDialog.cancel();
                        }
                    }
                });
            }
        });

        try {
            mDialog.show();
        } catch (Exception e) {
            //just make sure nothing goes wrong
        }
    }
}