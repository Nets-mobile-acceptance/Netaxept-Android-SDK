package flutter.sample.pia.nets.eu.flutter_app.network;

import java.io.IOException;

import flutter.sample.pia.nets.eu.flutter_app.MainActivity;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentCommitResponse;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentRegisterRequest;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentRegisterResponse;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


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
public class MerchantRestClient {

    private static MerchantRestClient mInstance;
    private MerchantBackendAPI mMerchantBackendAPI;
    private boolean isPointingToTest = true;

    public static MerchantRestClient getInstance() {
        if (mInstance == null) {
            mInstance = new MerchantRestClient();
        }
        return mInstance;
    }

    private MerchantRestClient() {
        //initially create the client pointing on test environment
        createRetrofitClient(MainActivity.TEST_MERCHANT_BACKEND_URL);
    }

    /**
     * Method to create the client to point to specific environment: test or production
     *
     * @param baseUrl the base URL for backend (test/prod)
     */
    public void createRetrofitClient(String baseUrl) {
        //flag to know if client points to test environment
        isPointingToTest = baseUrl.equals(MainActivity.TEST_MERCHANT_BACKEND_URL);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build();

        mMerchantBackendAPI = retrofit.create(MerchantBackendAPI.class);
    }

    public boolean isPointingToTest() {
        return isPointingToTest;
    }

    public PaymentRegisterResponse registerPayment(PaymentRegisterRequest request, String merchantId) {
        try {
            Response<PaymentRegisterResponse> response = mMerchantBackendAPI.registerPayment(request, merchantId).execute();
            if (response.isSuccessful()) {
                return response.body();
            } else return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void commitPayment(String transactionId, String merchantId, Callback<PaymentCommitResponse> callback) {
        mMerchantBackendAPI.commitPayment(transactionId, merchantId,"{}").enqueue(callback);
    }

}
