package eu.nets.pia.sample.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * MIT License
 * <p>
 * Copyright (c) 2020 Nets Denmark A/S
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

    /*Vipps wallet*/
    @SerializedName("merchantId")
    private String merchantId = null;
    @SerializedName("token")
    private String token = null;
    @SerializedName("serviceType")
    private String serviceType = null;
    @SerializedName("paymentMethodActionList")
    private String paymentMethodActionList = null;
    @SerializedName("phoneNumber")
    private String phoneNumber = null;
    @SerializedName("currencyCode")
    private String currencyCode = null;
    @SerializedName("redirectUrl")
    private String redirectUrl = null;
    @SerializedName("language")
    private String language = null;
    /*Vipps wallet*/

    /*Paytrail start*/
    @SerializedName("customerEmail")
    private String customerEmail = null;
    @SerializedName("customerFirstName")
    private String customerFirstName = null;
    @SerializedName("customerLastName")
    private String customerLastName = null;
    @SerializedName("customerAddress1")
    private String customerAddress1 = null;
    @SerializedName("customerPostCode")
    private String customerPostCode = null;
    @SerializedName("customerTown")
    private String customerTown = null;
    @SerializedName("customerCountry")
    private String customerCountry = null;

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerFirstName() {
        return customerFirstName;
    }

    public void setCustomerFirstName(String customerFirstName) {
        this.customerFirstName = customerFirstName;
    }

    public String getCustomerLastName() {
        return customerLastName;
    }

    public void setCustomerLastName(String customerLastName) {
        this.customerLastName = customerLastName;
    }

    public String getCustomerAddress1() {
        return customerAddress1;
    }

    public void setCustomerAddress1(String customerAddress1) {
        this.customerAddress1 = customerAddress1;
    }

    public String getCustomerPostCode() {
        return customerPostCode;
    }

    public void setCustomerPostCode(String customerPostCode) {
        this.customerPostCode = customerPostCode;
    }

    public String getCustomerTown() {
        return customerTown;
    }

    public void setCustomerTown(String customerTown) {
        this.customerTown = customerTown;
    }

    public String getCustomerCountry() {
        return customerCountry;
    }

    public void setCustomerCountry(String customerCountry) {
        this.customerCountry = customerCountry;
    }
    /*Paytrail end*/

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


    /*Vipps wallet*/
    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getPaymentMethodActionList() {
        return paymentMethodActionList;
    }

    public void setPaymentMethodActionList(String paymentMethodActionList) {
        this.paymentMethodActionList = paymentMethodActionList;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }
    /*Vipps wallet*/

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
        sb.append("merchantId='").append(merchantId).append("'");
        sb.append(",token='").append(token).append("'");
        sb.append(",serviceType='").append(serviceType).append("'");
        sb.append(",paymentMethodActionList='").append(paymentMethodActionList).append("'");
        sb.append(",phoneNumber='").append(phoneNumber).append("'");
        sb.append(",orderNumber='").append(orderNumber).append("'");
        sb.append(",currencyCode='").append(currencyCode).append("'");
        sb.append(",amount='").append(amount).append("'");
        sb.append(",redirectUrl='").append(redirectUrl).append("'");
        sb.append(",language='").append(language).append("'");
        sb.append("}");
        return sb.toString();
    }
}
