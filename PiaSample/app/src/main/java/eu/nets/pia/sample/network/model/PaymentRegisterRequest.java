package eu.nets.pia.sample.network.model;

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

public class PaymentRegisterRequest {

    @SerializedName("customerId")
    private String customerId = null;
    @SerializedName("orderNumber")
    private String orderNumber = null;
    @SerializedName("amount")
    private Amount amount = null;
    @SerializedName("method")
    private Method method = null;
    @SerializedName("cardId")
    private String cardId = null;
    @SerializedName("storeCard")
    private Boolean storeCard = null;

    /**
     * Customer Id
     */
    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Order number
     **/
    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * The amount to pay
     **/
    public Amount getAmount() {
        return amount;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    /**
     * Payment method
     **/
    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    /**
     * CardId to use for easyPay
     **/
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    /**
     * Store card for future use
     **/

    public Boolean getStoreCard() {
        return storeCard;
    }

    public void setStoreCard(Boolean storeCard) {
        this.storeCard = storeCard;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PaymentRegisterRequest{");
        sb.append("customerId='").append(customerId).append("'");
        sb.append(",orderNumber='").append(orderNumber).append("'");
        sb.append(",amount='").append(amount).append("'");
        sb.append(",method='").append(method).append("'");
        sb.append(",cardId='").append(cardId).append("'");
        sb.append(",storeCard='").append(storeCard).append("'");
        sb.append("}");
        return sb.toString();
    }
}
