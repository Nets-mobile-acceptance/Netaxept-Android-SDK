package eu.nets.pia.sample.data;

import eu.nets.pia.sample.network.model.PaymentCommitResponse;
import eu.nets.pia.sample.network.model.PaymentRegisterRequest;
import eu.nets.pia.sample.network.model.PaymentRegisterResponse;

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

public class PaymentFlowCache {

    private static PaymentFlowCache mInstance;

    public static PaymentFlowCache getInstance() {
        if (mInstance == null) {
            mInstance = new PaymentFlowCache();
        }
        return mInstance;
    }

    private PaymentFlowState mState = PaymentFlowState.IDLE;
    private boolean mFinishedWithError = false;
    private boolean mFailedRequest = false;
    private PaymentRegisterRequest mPaymentRegisterRequest;
    private PaymentRegisterResponse mPaymentRegisterResponse;
    private PaymentCommitResponse mPaymentCommitResponse;
    private boolean mPaymentMethodSelected = false;

    private PaymentFlowCache() {

    }

    public void reset() {
        mPaymentRegisterResponse = null;
        mPaymentCommitResponse = null;
        mState = PaymentFlowState.IDLE;
        mFinishedWithError = false;
        mFailedRequest = false;
    }

    public void setPaymentRegisterResponse(PaymentRegisterResponse mPaymentRegisterResponse) {
        this.mPaymentRegisterResponse = mPaymentRegisterResponse;
    }

    public PaymentRegisterResponse getPaymentRegisterResponse() {
        return mPaymentRegisterResponse;
    }

    public void setState(PaymentFlowState mState) {
        this.mState = mState;
    }

    public PaymentFlowState getState() {
        return mState;
    }

    public boolean finishedWithError() {
        return mFinishedWithError;
    }

    public void setFinishedWithError(boolean mFinishedWithError) {
        this.mFinishedWithError = mFinishedWithError;
    }

    public boolean isFailedRequest() {
        return mFailedRequest;
    }

    public void setFailedRequest(boolean mFailedRequest) {
        this.mFailedRequest = mFailedRequest;
    }

    public PaymentCommitResponse getPaymentCommitResponse() {
        return mPaymentCommitResponse;
    }

    public PaymentRegisterRequest getPaymentRegisterRequest() {
        return mPaymentRegisterRequest;
    }

    public void setPaymentRegisterRequest(PaymentRegisterRequest mPaymentRegisterRequest) {
        this.mPaymentRegisterRequest = mPaymentRegisterRequest;
    }

    public void setPaymentCommitResponse(PaymentCommitResponse paymentCommitResponse) {
        this.mPaymentCommitResponse = paymentCommitResponse;
    }

    public boolean isPaymentMethodSelected () {
        return mPaymentMethodSelected;
    }

    public void setPaymentMethodSelected (boolean mPaymentMethodSelected ) {
        this.mPaymentMethodSelected = mPaymentMethodSelected;
    }
}
