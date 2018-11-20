package eu.nets.pia.sample.network;

import eu.nets.pia.sample.network.model.PaymentCommitResponse;
import eu.nets.pia.sample.network.model.PaymentMethodsResponse;
import eu.nets.pia.sample.network.model.PaymentRegisterRequest;
import eu.nets.pia.sample.network.model.PaymentRegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * MIT License
 * <p>
 * Copyright (c) 2018 Nets Denmark A/S
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

public interface MerchantBackendAPI {

    @POST("v1/payment/{merchantId}/register")
    @Headers({"Content-Type: application/vnd.nets.pia.v1.2+json",
            "Accept: application/vnd.nets.pia.v1.2+json"})
    Call<PaymentRegisterResponse> registerPayment(
            @Body PaymentRegisterRequest request,
            @Path("merchantId") String merchantId
    );

    @POST("v1/payment/register")
    @Headers({"Content-Type: application/vnd.nets.pia.v1.2+json",
            "Accept: application/vnd.nets.pia.v1.2+json"})
    Call<PaymentRegisterResponse> registerPayment(
            @Body PaymentRegisterRequest request
    );

    @PUT("v1/payment/{merchantId}/{transactionId}/commit")
    Call<PaymentCommitResponse> commitPayment(
            @Path("transactionId") String transactionId,
            @Path("merchantId") String merchantId
    );

    @PUT("v1/payment/{transactionId}/commit")
    Call<PaymentCommitResponse> commitPayment(
            @Path("transactionId") String transactionId
    );

    @PUT("v1/payment/{merchantId}/{transactionId}/storecard")
    Call<PaymentCommitResponse> verifyPayment(
            @Path("transactionId") String transactionId,
            @Path("merchantId") String merchantId
    );

    @PUT("v1/payment/{transactionId}/storecard")
    Call<PaymentCommitResponse> verifyPayment(
            @Path("transactionId") String transactionId
    );

    @GET("v1/payment/methods")
    Call<PaymentMethodsResponse> getPaymentMethods(@Query("consumerId") String consumerId);

    @DELETE("v1/payment/{merchantId}/{transactionId}/rollback")
    Call<String> transactionRollback(
            @Path("transactionId") String transactionId,
            @Path("merchantId") String merchantId
    );

    @DELETE("v1/payment/{transactionId}/rollback")
    Call<String> transactionRollback(
            @Path("transactionId") String transactionId
    );
}
