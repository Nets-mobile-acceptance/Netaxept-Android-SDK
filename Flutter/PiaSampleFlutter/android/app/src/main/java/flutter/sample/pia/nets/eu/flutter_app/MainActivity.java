package flutter.sample.pia.nets.eu.flutter_app;

import android.content.Intent;
import android.os.Bundle;

import eu.nets.pia.PiaSDK;
import eu.nets.pia.data.exception.PiaError;
import eu.nets.pia.data.exception.PiaErrorCode;
import eu.nets.pia.data.model.MerchantInfo;
import eu.nets.pia.data.model.OrderInfo;
import eu.nets.pia.data.model.PiaResult;
import eu.nets.pia.data.model.SchemeType;
import eu.nets.pia.data.model.TokenCardInfo;
import eu.nets.pia.data.model.TransactionInfo;
import eu.nets.pia.ui.main.PiaActivity;
import flutter.sample.pia.nets.eu.flutter_app.network.MerchantRestClient;
import flutter.sample.pia.nets.eu.flutter_app.network.model.Amount;
import flutter.sample.pia.nets.eu.flutter_app.network.model.Method;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentCommitResponse;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentRegisterRequest;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentRegisterResponse;
import io.flutter.app.FlutterActivity;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
public class MainActivity extends FlutterActivity {

    //data required for Merchant BackEnd API calls
	public static final String TEST_MERCHANT_BACKEND_URL = "http://yourTestMerchantBackendUrlHere.com";
	public static final String TEST_MERCHANT_ID = "yourTestMerchantIdHere";
	public static final String PROD_MERCHANT_BACKEND_URL = "http://yourProdMerchantBackendUrlHere.com";
	public static final String PROD_MERCHANT_ID = "yourProdMerchantIdHere";
    public static final String CURRENCY_EUR = "EUR";
    public static final String CURRENCY_DKK = "DKK";
    public static final String ORDER_NUMBER = "Pia-Android-Flutter";
    private static final String PAY_NEW_CARD_CHANNEL = "eu.nets.pia.sample.flutter/launchSDK";
    private String transactionId = "";
    //end

    //store locally the Result callback; the result will be delivered in onActivityResult method
    private MethodChannel.Result paymentCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        GeneratedPluginRegistrant.registerWith(this);

        //register a MethodChannel to listen for method calls from dart code
        new MethodChannel(getFlutterView(), PAY_NEW_CARD_CHANNEL).setMethodCallHandler(
                (call, result) -> {
                    this.paymentCallback = result;
                    //call specific method; you can handle all cases here: new card, saved card, paypal, etc
                    switch (call.method) {
                        case "payWithNewCard":
                            payWithNewCard();
                            break;
                        case "payWithSavedCard":
                            String issuer = call.argument("issuer");
                            String tokenId = call.argument("tokenId");
                            String expDate = call.argument("expirationDate"); //in MM/YY format
                            boolean cvcRequired = Boolean.valueOf(call.argument("cvcRequired"));
                            boolean systemAuthRequired = Boolean.valueOf(call.argument("systemAuthRequired"));
                            payWithSavedCard(issuer, tokenId, expDate, cvcRequired, systemAuthRequired);
                            break;
                        case "payWithPayPal":
                            payWithPayPal();
                            break;
                    }
                });
    }

    /**
     * Launch PiaSDK in PayWithNewCard mode and handle the register payment API call
     * For testing purposes, we agreed to make card payments only on Test environment
     */
    private void payWithNewCard() {
        //notify retrofit client to point on test environment
        MerchantRestClient.getInstance().createRetrofitClient(TEST_MERCHANT_BACKEND_URL);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PiaSDK.BUNDLE_MERCHANT_INFO, new MerchantInfo(TEST_MERCHANT_ID, true));
        bundle.putParcelable(PiaSDK.BUNDLE_ORDER_INFO, new OrderInfo(10, CURRENCY_EUR));

        PiaSDK.getInstance().start(this, bundle, b -> {
            //call register payment synchronously
            PaymentRegisterRequest registerRequest = getRegisterRequest(null, CURRENCY_EUR);
            registerRequest.setStoreCard(b);

            PaymentRegisterResponse response = MerchantRestClient.getInstance().registerPayment(
                    registerRequest,
                    TEST_MERCHANT_ID
            );

            if (response == null) {
                return null;
            } else {
                transactionId = response.getTransactionId();
                return new TransactionInfo(response.getTransactionId(), response.getRedirectOK());
            }
        });
    }

    /**
     * Launch PiaSDK in PayWithSavedCard mode and handle the register payment API call
     * For testing purposes, we agreed to make card payments only on Test environment
     */
    private void payWithSavedCard(String issuer, String tokenId, String expirationDate, boolean cvcRequired, boolean systemAuthRequired) {
        //notify retrofit client to point on test environment
        MerchantRestClient.getInstance().createRetrofitClient(TEST_MERCHANT_BACKEND_URL);

        SchemeType type = mapToSchemeIdFromPaymentResponse(issuer);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PiaSDK.BUNDLE_MERCHANT_INFO, new MerchantInfo(TEST_MERCHANT_ID, true));
        bundle.putParcelable(PiaSDK.BUNDLE_ORDER_INFO, new OrderInfo(10, CURRENCY_EUR));
        bundle.putParcelable(PiaSDK.BUNDLE_TOKEN_CARD_INFO, new TokenCardInfo(tokenId, type, expirationDate, cvcRequired, systemAuthRequired));

        PiaSDK.getInstance().start(this, bundle, b -> {
            //call register payment synchronously
            PaymentRegisterRequest registerRequest = getRegisterRequest(new Method("EasyPayment"), CURRENCY_EUR);
            registerRequest.setCardId(tokenId);

            PaymentRegisterResponse response = MerchantRestClient.getInstance().registerPayment(
                    registerRequest,
                    TEST_MERCHANT_ID
            );

            if (response == null) {
                return null;
            } else {
                transactionId = response.getTransactionId();
                return new TransactionInfo(response.getTransactionId(), response.getRedirectOK());
            }
        });
    }

    /**
     * Helper method which maps the card scheme type based on the issuer name
     *
     * @param issuer the issuer name; e.g. Visa, Mastercard etc
     * @return Scheme type enum value
     */
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
     * Launch PiaSDK in PayWithPayPal mode and handle the register payment API call
     * For testing purposes, we agreed to make PayPal payment only on PRODUCTION environment
     */
    private void payWithPayPal() {
        //notify retrofit client to point on prod environment
        MerchantRestClient.getInstance().createRetrofitClient(PROD_MERCHANT_BACKEND_URL);

        Bundle bundle = new Bundle();
        bundle.putParcelable(PiaSDK.BUNDLE_MERCHANT_INFO, new MerchantInfo(PROD_MERCHANT_ID, false)); //notify SDK to use prod

        PiaSDK.getInstance().startPayPalProcess(this, bundle, b -> {
            //call register payment synchronously
            PaymentRegisterResponse response = MerchantRestClient.getInstance().registerPayment(
                    getRegisterRequest(new Method("PayPal"), CURRENCY_DKK),
                    PROD_MERCHANT_ID
            );

            if (response == null) {
                return null;
            } else {
                transactionId = response.getTransactionId();
                return new TransactionInfo(response.getTransactionId(), response.getRedirectOK());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PiaSDK.PIA_SDK_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                paymentCallback.error("Cancel", "Process cancelled", null);
            } else {
                PiaResult result = data.getParcelableExtra(PiaActivity.BUNDLE_COMPLETE_RESULT);
                if (result.isSuccess()) {
                    commitPayment();
                } else {
                    PiaError error = result.getError();
                    paymentCallback.error("Error: ",
                            (error != null && error.getCode() != null) ? error.getCode().getStatusCode() : PiaErrorCode.GENERIC_ERROR.getStatusCode(),
                            null
                    );
                    clearCachedData();
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * Build the Register Payment Request body
     *
     * @param method the payment method; if is null it means that it is pay with new card
     * @return PaymentRegisterRequest object
     */
    private PaymentRegisterRequest getRegisterRequest(Method method, String currency) {
        Amount amount = new Amount();
        amount.setCurrencyCode(currency);
        amount.setTotalAmount(1000L);
        amount.setVatAmount((long) 0);

        PaymentRegisterRequest paymentRequest = new PaymentRegisterRequest();
        paymentRequest.setAmount(amount);
        paymentRequest.setOrderNumber(ORDER_NUMBER);
        paymentRequest.setCustomerId("000004");

        paymentRequest.setMethod(method);

        return paymentRequest;
    }

    /**
     * Call Commit payment if SDK result is successful
     */
    private void commitPayment() {
        String merchantId = "";
        //is register payment was made on test environment, use test merchant id; if not, use prod merchant id
        //the logic for this is: card payments are made on test env and paypal payment is on production env
        if (MerchantRestClient.getInstance().isPointingToTest()) {
            merchantId = TEST_MERCHANT_ID;
        } else {
            merchantId = PROD_MERCHANT_ID;
        }
        MerchantRestClient.getInstance().commitPayment(transactionId, merchantId, new Callback<PaymentCommitResponse>() {
            @Override
            public void onResponse(Call<PaymentCommitResponse> call, Response<PaymentCommitResponse> response) {
                if (response.isSuccessful()) {
                    paymentCallback.success("Process successful");
                } else {
                    paymentCallback.error("Error", "Cannot commit payment", null);
                }
                clearCachedData();
            }

            @Override
            public void onFailure(Call<PaymentCommitResponse> call, Throwable t) {
                paymentCallback.error("Error", "Failure in Commit Payment", null);
                clearCachedData();
            }
        });
    }

    /**
     * Clear all local data to prepare for future payments
     */
    private void clearCachedData() {
        transactionId = "";
        paymentCallback = null;
    }
}
