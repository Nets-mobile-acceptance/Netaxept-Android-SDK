package com.piasample;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import eu.nets.pia.PiaInterfaceConfiguration;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.RegisterPaymentHandler;
import eu.nets.pia.data.model.MerchantInfo;
import eu.nets.pia.data.model.OrderInfo;
import eu.nets.pia.data.model.PiaResult;
import eu.nets.pia.data.model.SchemeType;
import eu.nets.pia.data.model.TokenCardInfo;
import eu.nets.pia.data.model.TransactionInfo;
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

/**
 * The React Native bridge between JavaScript and Java SDK
 */
public class SDKModule extends ReactContextBaseJavaModule implements ActivityEventListener {

    //local object required when calling the SDK
    private OrderInfo orderInfo;
    private MerchantInfo merchantInfo;
    private TokenCardInfo tokenCardInfo;
    private TransactionInfo transactionInfo;
    //end
    //Object used in thread synchronization
    private final Object threadSynchronizator = new Object();
    //end
    //Promise used to deliver the payment result back to JavaScript code
    private Promise paymentResult;
    //end


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
        if (paymentResult != null) {
            if (resultCode == Activity.RESULT_CANCELED) {
                paymentResult.reject("1", "Canceled");
            } else if (resultCode == Activity.RESULT_OK) {
                PiaResult result = data.getParcelableExtra(PiaActivity.BUNDLE_COMPLETE_RESULT);
                if (result.isSuccess()) {
                    paymentResult.resolve(true);
                } else {
                    if (result.getError() != null) {
                        paymentResult.reject(result.getError().getCode().getStatusCode(), result.getError().getMessage(getReactApplicationContext().getApplicationContext()));
                    } else {
                        paymentResult.reject("Unknown error");
                    }

                }
            }
        }

        //clear local cache
        clearCache();
    }


    @Override
    public void onNewIntent(Intent intent) {

    }

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
     *
     * @param paymentResult
     */
    @ReactMethod
    public void handleSDKResult(Promise paymentResult) {
        this.paymentResult = paymentResult;
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void start(final Callback registerPaymentCallback) {
        Bundle bundle = new Bundle();
        if (merchantInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, merchantInfo);
        }
        if (orderInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, orderInfo);
        }
        if (tokenCardInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_TOKEN_CARD_INFO, tokenCardInfo);
        }


        PiaSDK.getInstance().start(getCurrentActivity(), bundle, new RegisterPaymentHandler() {
            @Override
            public TransactionInfo doRegisterPaymentRequest(final boolean saveCard) {
                registerPaymentCallback.invoke(saveCard);
                try {
                    return getTransactionInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    threadSynchronizator.notify();
                    return null;
                }
            }
        });
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
        Bundle bundle = new Bundle();
        if (merchantInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, merchantInfo);
        }
        if (orderInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, orderInfo);
        }
        if (tokenCardInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_TOKEN_CARD_INFO, tokenCardInfo);
        }

        PiaInterfaceConfiguration.getInstance().setSkipConfirmationSelected(true);
        PiaSDK.getInstance().start(getCurrentActivity(), bundle, new RegisterPaymentHandler() {
            @Override
            public TransactionInfo doRegisterPaymentRequest(final boolean saveCard) {
                registerPaymentCallback.invoke(saveCard);
                try {
                    return getTransactionInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    threadSynchronizator.notify();
                    return null;
                }
            }
        });
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
        Bundle bundle = new Bundle();
        if (merchantInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, merchantInfo);
        }
        if (orderInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, orderInfo);
        }
        PiaSDK.getInstance().startPayPalProcess(getCurrentActivity(), bundle, new RegisterPaymentHandler() {
            @Override
            public TransactionInfo doRegisterPaymentRequest(final boolean saveCard) {
                registerPaymentCallback.invoke(saveCard);
                try {

                    return getTransactionInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    threadSynchronizator.notify();
                    return null;
                }
            }
        });
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void startVippsProcess(final Callback registerPaymentCallback) {
        Bundle bundle = new Bundle();
        if (merchantInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, merchantInfo);
        }
        if (orderInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, orderInfo);
        }
        PiaSDK.getInstance().startVippsProcess(getCurrentActivity(), bundle, new RegisterPaymentHandler() {
            @Override
            public TransactionInfo doRegisterPaymentRequest(final boolean saveCard) {
                registerPaymentCallback.invoke(saveCard);
                try {

                    return getTransactionInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    threadSynchronizator.notify();
                    return null;
                }
            }
        });
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     *
     * @param registerPaymentCallback - callback to notify JavaScript when the register call is required
     */
    @ReactMethod
    public void startSwishProcess(final Callback registerPaymentCallback) {
        Bundle bundle = new Bundle();
        if (merchantInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, merchantInfo);
        }
        if (orderInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, orderInfo);
        }
        PiaSDK.getInstance().startSwishProcess(getCurrentActivity(), bundle, new RegisterPaymentHandler() {
            @Override
            public TransactionInfo doRegisterPaymentRequest(final boolean saveCard) {
                registerPaymentCallback.invoke(saveCard);
                try {

                    return getTransactionInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    threadSynchronizator.notify();
                    return null;
                }
            }
        });
    }

    /**
     * After you set all required local object through above setters, call this method and instantiate the Callback Parameter
     * This callback will notify you when the register payment API call is required to be done from your application.
     * When the register call is completed, call #buildTransactionInfo() to set the required transaction related fields
     */
    @ReactMethod
    public void startPaytrailProcess() {
        Bundle bundle = new Bundle();
        if (merchantInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, merchantInfo);
        }
        if (orderInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, orderInfo);
        }
        if (transactionInfo != null) {
            bundle.putParcelable(PiaActivity.BUNDLE_TRANSACTION_INFO, transactionInfo);
        }
        PiaSDK.getInstance().startPaytrailProcess(getCurrentActivity(), bundle);
    }

    /**
     * Method used to clear the local variabled after a payment process has ended (either success, error or cancel)
     * The payment process is usually ending in #OnActivityResult method
     */
    private void clearCache() {
        this.merchantInfo = null;
        this.orderInfo = null;
        this.tokenCardInfo = null;
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
            while (transactionInfo == null) {
                threadSynchronizator.wait();
            }
        }
        System.out.println("onTransactionInfo:" + transactionInfo.getTransactionId() + " " + transactionInfo.getRedirectUrl() + " " + transactionInfo.getCancelRedirectUrl());
        return transactionInfo;
    }
}