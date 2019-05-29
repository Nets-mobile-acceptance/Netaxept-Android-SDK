package flutter.sample.pia.nets.eu.flutter_app.network.model;

import com.google.gson.annotations.SerializedName;


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
public class PaymentRegisterResponse {
    @SerializedName("transactionId")
    private String transactionId = null;

    @SerializedName("redirectOK")
    private String redirectOK = null;

    @SerializedName("redirectCancel")
    private String redirectCancel = null;

    /**
     * RedirectOK
     **/
    public String getRedirectOK() {
        return redirectOK;
    }

    public void setRedirectOK(String redirectOK) {
        this.redirectOK = redirectOK;
    }

    /**
     * RedirectCancel
     **/
    public String getRedirectCancel() {
        return redirectCancel;
    }

    public void setRedirectCancel(String redirectCancel) {
        this.redirectCancel = redirectCancel;
    }

    /**
     * TransactionId
     **/
    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PaymentRegisterResponse{");
        sb.append("transactionId='").append(transactionId).append("'");
        sb.append(",redirectOK='").append(redirectOK).append("'");
        sb.append(",redirectCancel='").append(redirectCancel).append("'");

        sb.append("}");
        return sb.toString();
    }
}
