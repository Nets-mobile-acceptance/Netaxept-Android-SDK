package eu.nets.pia.sample.ui.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.RegisterPaymentHandler;
import eu.nets.pia.data.model.MerchantInfo;
import eu.nets.pia.data.model.PiaResult;
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
import eu.nets.pia.sample.ui.data.PaymentMethod;
import eu.nets.pia.sample.ui.widget.CustomToolbar;
import eu.nets.pia.ui.main.PiaActivity;

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
    @BindView(R.id.switch_disable_cardio)
    SwitchCompat mDisableCardIOSwitch;
    //end
    @BindView(R.id.spinner_holder)
    protected RelativeLayout mProgressBar;

    private static final String ORDER_NUMBER = "PiaSDK-Android";

    private MerchantRestClient mRestClient = MerchantRestClient.getInstance();
    private PaymentFlowCache mPaymentCache;
    private RegisterPaymentHandler mRegisterPaymentHandler;

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

        if (customerId.isEmpty()) {
            //user is not logged in
            mMainLayout.setVisibility(View.GONE);
            mNotLoggedInLayout.setVisibility(View.VISIBLE);
        } else {
            mMainLayout.setVisibility(View.VISIBLE);
            mCustomerIdLabel.setText(customerId);
            mAppVersionLabel.setText(PiaSDK.getInstance().getVersionName());

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

            mDisableCardIOSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    PiaSampleSharedPreferences.setDisableCardIo(isChecked);
                    //also, update the flag inside the SDK
                    PiaInterfaceConfiguration.getInstance().setDisableCardIO(isChecked);
                }
            });

            mSystemAuthSwitch.setChecked(PiaSampleSharedPreferences.isUseSystemAuth());
            mUrlSwitch.setChecked(PiaSampleSharedPreferences.isPiaTestMode());
            mDisableCardIOSwitch.setChecked(PiaSampleSharedPreferences.isDisableCardIo());
        }
    }

    @OnClick(R.id.change_customer_id)
    public void onChangeCustomerId() {
        //show input popup to change id
        showInputDialog();
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
     * This is the Save Card functionality; As mentioned in the {@link MainActivity#callPiaSDK(PaymentMethod)}
     * Calling {@link eu.nets.pia.PiaSDK#start(Activity, Bundle, RegisterPaymentHandler)}, provide in the bundle
     * only the {@link eu.nets.pia.data.model.MerchantInfo}
     */
    @OnClick(R.id.save_card)
    public void onSaveCardBtnClicked() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, getMerchantInfo());

        //store in cache the payment request -- for register, a payment with zero amount is requred
        mPaymentCache.setPaymentRegisterRequest(getPaymentRequest());

        PiaSDK.getInstance().start(this, bundle, mRegisterPaymentHandler);
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

        Log.d(TAG, "[onActivityResult] result from Pia SDK: " +
                "[requestCode=" + requestCode + "]");

        //in case user canceled
        Bundle bundle = new Bundle();
        if (resultCode == RESULT_CANCELED) {
            /**
             * If the result was cancelled, there is no need to rollback the transaction (no transactionId was created) a this point
             */
            bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_CANCELED, true);
            displayConfirmationScreen(bundle);
            return;
        }

        if (resultCode == RESULT_OK) {
            /**
             * If resultCode is OK, check the PiaResult object
             */
            PiaResult result = data.getParcelableExtra(PiaActivity.BUNDLE_COMPLETE_RESULT);
            if (result.isSuccess()) {
                //in case of success commit the payment
                mProgressBar.setVisibility(View.VISIBLE);
                //call /storeCard API with the transaction ID
                /**
                 * If the result is success, call verifyPayment (storeCard API) on your backend
                 */
                mRestClient.verifyPayment(
                        mPaymentCache.getPaymentRegisterResponse().getTransactionId()
                );
            } else {
                /**
                 * PiaResult is not successful (error occurred)
                 * -handle as desired
                 */
                //save card has finished with error
                bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_SUCCESS, false);
                bundle.putParcelable(ConfirmationActivity.BUNDLE_ERROR_OBJECT, result);
                displayConfirmationScreen(bundle);
            }
        }
    }

    private void displayConfirmationScreen(Bundle bundle) {
        mProgressBar.setVisibility(View.GONE);
        Intent intent = new Intent(this, ConfirmationActivity.class);
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
     * <p>
     * Note:
     * This _cvcRequired_ flag is set based on a local check. The current logic implemented there, is
     * to have two cases (true and false) based on the customerId being odd or even. This was made only
     * for Demo purposes.
     * <p>
     * For your implementation, check with your acquirer if payment for the specific user can be made without CVV/CVC,
     * and send this flag accordingly.
     *
     * @return MerchantInfo object
     */
    private MerchantInfo getMerchantInfo() {

        if (PiaSampleSharedPreferences.isPiaTestMode()) {
            //launch SDK with test merchant id and flag testMode = true to point SDK to test env
            return new MerchantInfo(
                    getMerchantId(true),
                    true,
                    Integer.parseInt(PiaSampleSharedPreferences.getCustomerId()) % 2 != 0
                    //for odd customerId the CVC will be required and for even will not be required
            );
        } else {
            //launch SDK with production merchant id; if testMode is not specified, is false by default
            return new MerchantInfo(
                    getMerchantId(false),
                    false,
                    Integer.parseInt(PiaSampleSharedPreferences.getCustomerId()) % 2 != 0
                    //for odd customerId the CVC will be required and for even will not be required
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
        Log.d(TAG, "[handlePaymentFlowState] [finishedWithError=" + mPaymentCache.finishedWithError() +
                "; state=" + mPaymentCache.getState() + "]");
        if (!mPaymentCache.finishedWithError()) {
            switch (mPaymentCache.getState()) {
                case COMMIT_PAYMENT_CALL_FINISHED:
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_SUCCESS, true);
                    bundle.putString(ConfirmationActivity.BUNDLE_SUCCESS_MESSAGE, getString(R.string.card_saved_success));
                    bundle.putString(ConfirmationActivity.BUNDLE_SUCCESS_TITLE, getString(R.string.toolbar_title_success_save));
                    displayConfirmationScreen(bundle);
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
                //display status screen
                Bundle bundle = new Bundle();
                bundle.putBoolean(ConfirmationActivity.BUNDLE_PAYMENT_SUCCESS, false);
                displayConfirmationScreen(bundle);
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
}
