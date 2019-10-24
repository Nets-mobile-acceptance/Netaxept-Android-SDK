package eu.nets.pia.sample;

import eu.nets.pia.RegisterPaymentHandler;
import eu.nets.pia.data.model.TransactionInfo;
import eu.nets.pia.sample.data.PaymentFlowCache;
import eu.nets.pia.sample.data.PaymentMethodSelected;
import eu.nets.pia.sample.network.MerchantRestClient;
import eu.nets.pia.sample.network.model.PaymentRegisterRequest;
import eu.nets.pia.sample.network.model.PaymentRegisterResponse;

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

public class RegisterPaymentHandlerImpl implements RegisterPaymentHandler {

    private final String TAG = RegisterPaymentHandlerImpl.class.getSimpleName();

    /**
     * Create your own class to implement {@link eu.nets.pia.RegisterPaymentHandler}, and override
     * {@link eu.nets.pia.RegisterPaymentHandler#doRegisterPaymentRequest(boolean)}
     * - in this method create a request to your backend to register a payment synchronously
     * (the body of the request, here, is stored in a {@link eu.nets.pia.sample.data.PaymentFlowCache}
     * - with the register response, create a {@link eu.nets.pia.data.model.TransactionInfo} with
     * transactionId, redirectOK and redirectCance
     * - return the TransactionInfo Object
     * This handler will be passed to the SDK and will be used as a first step when making a payment. This
     * transactionId will be used to send the paymentRequest to Netaxept.
     *
     * @param saveCard Specify if the card should be saved.
     * @return TransactionInfo object
     */

    @Override
    public TransactionInfo doRegisterPaymentRequest(boolean saveCard) {
        PaymentFlowCache paymentFlowCache = PaymentFlowCache.getInstance();
        PaymentRegisterRequest paymentRegisterRequest = paymentFlowCache.getPaymentRegisterRequest();
        paymentRegisterRequest.setStoreCard(saveCard);
        MerchantRestClient.getInstance().registerPayment(paymentRegisterRequest);
        if (!paymentFlowCache.isFinishedWithError() && paymentFlowCache.getPaymentRegisterResponse() != null) {
            PaymentRegisterResponse paymentRegisterResponse = paymentFlowCache.getPaymentRegisterResponse();
            if (paymentFlowCache.getPaymentMethodSelected() == PaymentMethodSelected.VIPPS) {
                return new TransactionInfo(paymentRegisterResponse.getWalletUrl());
            } else {
                return new TransactionInfo(paymentRegisterResponse.getTransactionId(),
                        paymentRegisterResponse.getRedirectOK());
            }
        }
        return null;
    }

}
