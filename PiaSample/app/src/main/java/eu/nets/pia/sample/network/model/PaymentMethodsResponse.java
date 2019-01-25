package eu.nets.pia.sample.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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

public class PaymentMethodsResponse implements Parcelable {

    @SerializedName("methods")
    private ArrayList<Method> methods = null;

    @SerializedName("tokens")
    private ArrayList<Token> tokens = null;

    @SerializedName("cardVerificationRequired")
    private Boolean cardVerificationRequired = new Boolean(true);

    protected PaymentMethodsResponse(Parcel in) {
        methods = in.createTypedArrayList(Method.CREATOR);
        tokens = in.createTypedArrayList(Token.CREATOR);
        byte tmpCardVerificationRequired = in.readByte();
        cardVerificationRequired = tmpCardVerificationRequired == 0 ? null : tmpCardVerificationRequired == 1;
    }

    public static final Creator<PaymentMethodsResponse> CREATOR = new Creator<PaymentMethodsResponse>() {
        @Override
        public PaymentMethodsResponse createFromParcel(Parcel in) {
            return new PaymentMethodsResponse(in);
        }

        @Override
        public PaymentMethodsResponse[] newArray(int size) {
            return new PaymentMethodsResponse[size];
        }
    };

    /**
     * List of supported payment methods
     **/
    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }

    public ArrayList<Token> getTokens() {
        return tokens;
    }

    public void setTokens(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    public boolean getCardVerificationRequired() {
        return cardVerificationRequired.booleanValue();
    }

    public void setCardVerificationRequired(Boolean cardVerificationRequired) {
        this.cardVerificationRequired = cardVerificationRequired;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PaymentMethodsResponse{");
        sb.append("methods='").append(methods).append("'");
        sb.append(",tokens='").append(tokens).append("'");
        sb.append(",cardVerificationRequired='").append(cardVerificationRequired).append("'");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(methods);
        dest.writeTypedList(tokens);
        dest.writeByte((byte) (cardVerificationRequired == null ? 0 : cardVerificationRequired ? 1 : 2));
    }
}
