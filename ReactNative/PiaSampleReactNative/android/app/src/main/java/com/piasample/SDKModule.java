package com.piasample;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Pair;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import org.jetbrains.annotations.NotNull;

import androidx.activity.result.ActivityResultLauncher;

import java.util.Arrays;
import java.util.HashSet;

import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.card.CardPaymentRegistration;
import eu.nets.pia.card.CardScheme;
import eu.nets.pia.card.CardTokenPaymentRegistration;
import eu.nets.pia.card.CardTokenizationRegistration;
import eu.nets.pia.card.PayPalPaymentRegistration;
import eu.nets.pia.card.PaytrailPaymentRegistration;
import eu.nets.pia.card.TransactionCallback;
import eu.nets.pia.data.model.MerchantInfo;
import eu.nets.pia.data.model.OrderInfo;
import eu.nets.pia.data.model.SchemeType;
import eu.nets.pia.data.model.TokenCardInfo;
import eu.nets.pia.data.model.TransactionInfo;
import eu.nets.pia.wallets.PaymentProcess;
import eu.nets.pia.wallets.WalletPaymentRegistration;
import eu.nets.pia.wallets.WalletURLCallback;


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

/**
 * The React Native bridge between JavaScript and Java SDK
 */
public class SDKModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    //local object required when calling the SDK
    private OrderInfo orderInfo;
    private MerchantInfo merchantInfo;
    private TokenCardInfo tokenCardInfo;
    private TransactionInfo transactionInfo;
    private String walletType;

    //end
    //Object used in thread synchronization
    private final Object threadSynchronizator = new Object();
    //end
    //Promise used to deliver the payment result back to JavaScript code
    private Promise paymentResult;
    //end

    //Objects used for handling results and callbacks
    ActivityResultLauncher activityResultLauncher;
    Callback registerPaymentCallback;
    WalletURLCallback walletURLCallback;



    public SDKModule(final ReactApplicationContext reactContext) {
        super(reactContext);
        reactContext.addActivityEventListener(this);
    }

    /**
     * OnActivityResult method -- here you can handle the payment result and deliver through promise (paymentResult) the result
     *
     * @param activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        MainActivity mainActivity = ((MainActivity) getCurrentActivity());

        /*Dispatching the result back to the callback provided to ActivityResultLauncher*/
        mainActivity.getActivityResultRegistry().dispatchResult(requestCode, activityResultLauncher.getContract().parseResult(resultCode, data));
        clearCache();
    }


    @Override
    public void onNewIntent(Intent intent) {}

    @Override
    public String getName() {
        return "PiaSDK";
    }

    /**
     * Method used to build the local Merchant info;
     * Call this method from JavaScript before calling #start() or #startPayPalProcess()
     *
     * @param merchantId - the id of the merchant
     */
    @ReactMethod
    public void buildMerchantInfo(String merchantId) {
        merchantInfo = new MerchantInfo(merchantId);
    }

    /**
     * Method used to build the local Merchant info;
     * Call this method from JavaScript before calling #start() or #startPayPalProcess()
     *
     * @param merchantId - the id of the merchant
     * @param isTestMode - flag if test mode is used
     */
    @ReactMethod
    public void buildMerchantInfo(String merchantId, boolean isTestMode) {
        merchantInfo = new MerchantInfo(merchantId, isTestMode);
    }

    /**
     * Method used to build the local MerchantInfo object;
     * Call this method from JavaScript  before calling #start() or #startPayPalProcess()
     *
     * @param merchantId    - the id of the merchant
     * @param isTestMode    - flag if test mode is used
     * @param isCvcRequired - flag if security code will be required in card entry view
     */
    @ReactMethod
    public void buildMerchantInfo(String merchantId, boolean isTestMode, boolean isCvcRequired) {
        merchantInfo = new MerchantInfo(merchantId, isTestMode, isCvcRequired);
    }

    /**
     * Method used to build the local OrderInfo object
     * Call this method from JavaScript before calling #start() or #startPayPalProcess()
     *
     * @param amount       - the amount to be paid
     * @param currencyCode - the currency code
     */
    @ReactMethod
    public void buildOrderInfo(double amount, String currencyCode) {
        orderInfo = new OrderInfo(amount, currencyCode);
    }

    /**
     * Method used to build the local TokenCardInfo object
     * Call this method from JavaScript  before calling #start() or #startPayPalProcess()
     *
     * @param tokenId     - the card token number
     * @param schemeId    - the scheme id: "visa", "mastercard", etc.
     * @param expiryDate  - the expiraton date "0122" (MMYY format)
     * @param cvcRequired - flag if security code will be asked when paying with a saved card
     */
    @ReactMethod
    public void buildTokenCardInfo(String tokenId, String schemeId, String expiryDate, boolean cvcRequired) {
        tokenCardInfo = new TokenCardInfo(tokenId, mapToSchemeIdFromPaymentResponse(schemeId), expiryDate, cvcRequired);
    }

    @ReactMethod
    public void setWalletType(String walletType) {
        this.walletType = walletType;
    }

    /**
     * Method to build the local Transaction Info object.
     * Call this method from JavaScript after you made the register payment API call
     *
     * @param transactionId - the id of the transaction
     * @param redirectOK    - the redirectOk case
     * @param walletUrl     - the walletUrl in case of Wallet Payment like Vipps, Swish etc.
     */
    @ReactMethod
    public void buildTransactionInfo(String transactionId, String redirectOK, String walletUrl) {
        synchronized (threadSynchronizator) {
            if (transactionId == null) {
                transactionInfo = null;
            } else if (walletUrl != null) {
                transactionInfo = new TransactionInfo(walletUrl);
            } else {
                transactionInfo = new TransactionInfo(transactionId, redirectOK);
            }
            threadSynchronizator.notify();
        }

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
        if (issuer.equals("dinersclubinternational")) {
            return SchemeType.DINERS_CLUB_INTERNATIONAL;
        }
        if (issuer.equals("amex") || issuer.equals("americanexpress")) {
            return SchemeType.AMEX;
        }
        return SchemeType.OTHER;
    }

    /**
     * Method to store here the paymentResult promise to be used inside the #onActivityResult method
     * Call this method from JavaScript before calling #start()
     * @param paymentResult
     */
    @ReactMethod
    public void handleSDKResult(Promise paymentResult) {
        this.paymentResult = paymentResult;
    }

    CardPaymentRegistration cardPaymentRegistration = (shouldStoreCard, callbackWithTransaction) -> {
        try {
            registerPaymentCallback.invoke(shouldStoreCard);
            TransactionInfo transactionInfo = getTransactionInfo();
            if (transactionInfo != null && transactionInfo.getTransactionId() != null) {
                callbackWithTransaction.successWithTransactionIDAndRedirectURL(transactionInfo.getTransactionId(), Uri.parse(transactionInfo.getRedirectUrl()));
            } else {
                callbackWithTransaction.failureWithError(null);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    CardTokenPaymentRegistration cardTokenPaymentRegistration = new CardTokenPaymentRegistration() {
        @Override
        public void registerPayment(@NotNull TransactionCallback callbackWithTransaction) {
            try {
                registerPaymentCallback.invoke();
                TransactionInfo transactionInfo = getTransactionInfo();
                if (transactionInfo != null && transactionInfo.getTransactionId() != null) {
                    callbackWithTransaction.successWithTransactionIDAndRedirectURL(transactionInfo.getTransactionId(), Uri.parse(transactionInfo.getRedirectUrl()));
                } else {
                    callbackWithTransaction.failureWithError(null);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };


    private Pair<String, PiaSDK.Environment> merchantIDAndEnvironmentPair(MerchantInfo merchantInfo) {
        String merchantID = merchantInfo.getMerchantId();
        return Pair.create(merchantID, merchantInfo.isTestMode() ? PiaSDK.Environment.TEST : PiaSDK.Environment.PROD);
    }

    private Pair<Integer, String> amountAndCurrencyCodePair(OrderInfo orderInfo) {
        String amount = String.valueOf(orderInfo.getAmount());
        int amountInCents = (int) (Double.parseDouble(
                (amount == null || amount.isEmpty() ? "0" : amount)) * 100
        );
        return Pair.create(amountInCents, orderInfo.getCurrency());
    }


    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * This method proceeds ahead for the card payment with all card schemes included
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void start(final Callback registerPaymentCallback) {

        this.registerPaymentCallback = registerPaymentCallback;

        activityResultLauncher = ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher;

        PiaSDK.startCardProcessActivity(
                ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher,
                PaymentProcess.cardPayment(
                        merchantIDAndEnvironmentPair(merchantInfo),
                        amountAndCurrencyCodePair(orderInfo),
                        cardPaymentRegistration
                ),
                merchantInfo.isCvcRequired()
        );
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * This method excludes all card schemes except visa and proceeds ahead for the card payment
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void startCardPaymentWithOnlyVisa(final Callback registerPaymentCallback) {
        HashSet<CardScheme> excludeCardSchemes = new HashSet<>();
        // Exclude all card schemes except `visa`
        excludeCardSchemes.addAll(Arrays.asList(CardScheme.values()));
        excludeCardSchemes.remove(CardScheme.visa);

        this.registerPaymentCallback = registerPaymentCallback;

        activityResultLauncher = ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher;

        PiaSDK.startCardProcessActivity(
                ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher,
                PaymentProcess.cardPayment(
                        merchantIDAndEnvironmentPair(merchantInfo),
                        excludeCardSchemes, // You can get this set from persisted storage preferably
                        amountAndCurrencyCodePair(orderInfo),
                        cardPaymentRegistration
                ),
                merchantInfo.isCvcRequired()
        );
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void saveCard(final Callback registerPaymentCallback) {

        this.registerPaymentCallback = registerPaymentCallback;

        activityResultLauncher = ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher;

        CardTokenizationRegistration cardStorageRegistration = new CardTokenizationRegistration() {
            @Override
            public void registerPayment(@NotNull TransactionCallback callbackWithTransaction) {
                try {
                    registerPaymentCallback.invoke();
                    TransactionInfo transactionInfo = getTransactionInfo();
                    if (transactionInfo != null && transactionInfo.getTransactionId() != null) {
                        callbackWithTransaction.successWithTransactionIDAndRedirectURL(transactionInfo.getTransactionId(), Uri.parse(transactionInfo.getRedirectUrl()));
                    } else {
                        callbackWithTransaction.failureWithError(null);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        String merchantID = merchantInfo.getMerchantId();
        PiaSDK.Environment environment = merchantInfo.isTestMode() ?
                PiaSDK.Environment.TEST : PiaSDK.Environment.PROD;

        PiaSDK.startCardProcessActivity(
                activityResultLauncher,
                PaymentProcess.cardTokenization(
                        Pair.create(merchantID, environment),
                        cardStorageRegistration
                ),
                merchantInfo.isCvcRequired()
        );
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void startSBusinessCard(final Callback registerPaymentCallback) {
        this.registerPaymentCallback = registerPaymentCallback;

        activityResultLauncher = ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher;

        PiaSDK.startSBusinessCardProcessActivity(
                ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher,
                PaymentProcess.cardPayment(
                        merchantIDAndEnvironmentPair(merchantInfo),
                        amountAndCurrencyCodePair(orderInfo),
                        cardPaymentRegistration
                ),
                merchantInfo.isCvcRequired()
        );
    }


    /**
     * After you set all required local object through above setters, call this method to skip the confirmation and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void startSkipConfirmation(final Callback registerPaymentCallback) {

        this.registerPaymentCallback = registerPaymentCallback;

        PiaInterfaceConfiguration.getInstance().setSkipConfirmationSelected(true);

        activityResultLauncher = ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher;

        PiaSDK.startCardProcessActivity(
                ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher,
                PaymentProcess.cardTokenPayment(
                        merchantIDAndEnvironmentPair(merchantInfo),
                        amountAndCurrencyCodePair(orderInfo),
                        tokenCardInfo.getTokenId(),
                        tokenCardInfo.getSchemeId(),
                        tokenCardInfo.getExpiryDate(),
                        cardTokenPaymentRegistration
                ),
                merchantInfo.isCvcRequired()
        );
    }

    @ReactMethod
    public void startTokenPayment(final Callback registerPaymentCallback) {

        this.registerPaymentCallback = registerPaymentCallback;

        activityResultLauncher = ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher;

        PiaSDK.startCardProcessActivity(
                ((MainActivity) getCurrentActivity()).cardPaymentActivityLauncher,
                PaymentProcess.cardTokenPayment(
                        merchantIDAndEnvironmentPair(merchantInfo),
                        amountAndCurrencyCodePair(orderInfo),
                        tokenCardInfo.getTokenId(),
                        tokenCardInfo.getSchemeId(),
                        tokenCardInfo.getExpiryDate(),
                        cardTokenPaymentRegistration
                ),
                merchantInfo.isCvcRequired()
        );
    }


    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void startPayPalProcess(final Callback registerPaymentCallback) {
        activityResultLauncher = ((MainActivity) getCurrentActivity()).payPalActivityLauncher;

        PayPalPaymentRegistration payPalPaymentRegistration = callbackWithTransaction -> {
            try {
                registerPaymentCallback.invoke();
                TransactionInfo transactionInfo = getTransactionInfo();
                if (transactionInfo != null && transactionInfo.getTransactionId() != null) {
                    callbackWithTransaction.successWithTransactionIDAndRedirectURL(transactionInfo.getTransactionId(), Uri.parse(transactionInfo.getRedirectUrl()));
                } else {
                    callbackWithTransaction.failureWithError(null);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };

        PiaSDK.startPayPalPayment(
                ((MainActivity) getCurrentActivity()).payPalActivityLauncher,
                merchantIDAndEnvironmentPair(merchantInfo),
                payPalPaymentRegistration
        );
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     */
    @ReactMethod
    public void startPaytrailProcess(final Callback registerPaymentCallback) {
        activityResultLauncher = ((MainActivity) getCurrentActivity()).paytrailActivityLauncher;

        PaytrailPaymentRegistration paytrailPaymentRegistration = callbackWithTransaction -> {
            try {
                registerPaymentCallback.invoke();
                TransactionInfo transactionInfo = getTransactionInfo();
                if (transactionInfo != null && transactionInfo.getTransactionId() != null) {
                    callbackWithTransaction.successWithTransactionIDAndRedirectURL(transactionInfo.getTransactionId(), Uri.parse(transactionInfo.getRedirectUrl()));
                } else {
                    callbackWithTransaction.failureWithError(null);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        PiaSDK.startPaytrailPayment(
                ((MainActivity) getCurrentActivity()).paytrailActivityLauncher    ,
                merchantIDAndEnvironmentPair(merchantInfo),
                paytrailPaymentRegistration
        );

    }



 /*   @ReactMethod
    public void setInterruptReceiver(Callback interruptResultReceiver){
        this.interruptResultReceiver = interruptResultReceiver;
    }*/

    /**
     * Single Api for initiating all wallet payments, call this method and instantiate the Callback Parameter
     * walletType should be set before calling this method
     * This callback will notify you when the register payment API call is required to be done from your application.
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void startWalletPayment(final Callback registerPaymentCallback) {

        this.registerPaymentCallback = registerPaymentCallback;
        MainActivity mainActivity = (MainActivity) getCurrentActivity();
        PaymentProcess.WalletPayment walletProcess = null;
        if (walletType.equals("MobilePay")) {
            walletProcess = PaymentProcess.mobilePay(mainActivity);
        }

        boolean canLaunch = PiaSDK.initiateMobileWallet(walletProcess, mobileWalletRegistration);

        if (!canLaunch) paymentResult.resolve("Wallet App not installed");

    }

    WalletPaymentRegistration mobileWalletRegistration = new WalletPaymentRegistration() {
        @Override
        public void registerPayment(final WalletURLCallback callback) {
            walletURLCallback = callback;
            registerPaymentCallback.invoke();
        }
    };

    /**
     * Returns the result back to SDK
     *
     * @param walletUrl - Wallet Url received on register api call
     */
    @ReactMethod
    public void openWalletApp(String walletUrl) {
        if (walletUrl != null) {
            walletURLCallback.successWithWalletURL(Uri.parse(walletUrl));
        } else
            walletURLCallback.failureWithError(null);
    }

    public void returnSuccessfulRedirectResult(String result) {
        paymentResult.resolve(result);
    }

    public void returnInterruption(String result) {
        paymentResult.reject(result);
    }


    /**
     * Method used to clear the local variabled after a payment process has ended (either success, error or cancel)
     * The payment process is usually ending in #OnActivityResult method
     */
    private void clearCache() {
        this.merchantInfo = null;
        this.orderInfo = null;
        this.tokenCardInfo = null;
        this.transactionInfo = null;
    }

    /**
     * This method is called inside the #RegisterPaymentHandler;
     * The logic is:
     * - SDK notifies the Bridge that register call is required (doRegisterPaymentRequest method is called)
     * - the doRegisterPaymentRequest() notifies JS app through callback to do the register payment API call
     * - the doRegisterPaymentRequest() calls getTransactionInfo() method which blocks the current thread untill the buildTransactionInfo() method is called
     * - the buildTransactionInfo() will set the TransactionInfo object locally, then release the thread
     * - the method getTransactionInfo() will return the transactionInfo object
     * - the doRegisterPaymentRequest() will return the transactionInfo object back to SDK to continue with the payment
     *
     * @return
     * @throws InterruptedException
     */
    private TransactionInfo getTransactionInfo() throws InterruptedException {
        synchronized (threadSynchronizator) {
            if (transactionInfo == null)
                threadSynchronizator.wait();
        }

        String log = transactionInfo != null ? transactionInfo.getTransactionId() + " " + transactionInfo.getRedirectUrl() + " " + transactionInfo.getCancelRedirectUrl() : "TransactionInfo : null";
        System.out.println("onTransactionInfo:" + log);
        return transactionInfo;
    }

    public void returnSuccess() {
        paymentResult.resolve("SUCCESS");
    }

    public void returnFailure(String message) {
        paymentResult.resolve(message);
    }

    public void returnCancellation(String message) {
        paymentResult.resolve(message);
    }
}
