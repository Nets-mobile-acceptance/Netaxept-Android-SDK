package flutter.sample.pia.nets.eu.flutter_app.network;

import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentCommitResponse;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentRegisterRequest;
import flutter.sample.pia.nets.eu.flutter_app.network.model.PaymentRegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

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
public interface MerchantBackendAPI {
    @POST("v1/payment/{merchantId}/register")
    @Headers({"Content-Type: application/vnd.nets.pia.v1.2+json",
            "Accept: application/vnd.nets.pia.v1.2+json"})
    Call<PaymentRegisterResponse> registerPayment(
            @Body PaymentRegisterRequest request,
            @Path("merchantId") String merchantId
    );

    @PUT("v1/payment/{merchantId}/{transactionId}/commit")
    Call<PaymentCommitResponse> commitPayment(
            @Path("transactionId") String transactionId,
            @Path("merchantId") String merchantId,
            @Body String body
    );
}
