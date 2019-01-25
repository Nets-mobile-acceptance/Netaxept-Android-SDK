package eu.nets.pia.sample.data;

import android.content.Context;
import android.content.SharedPreferences;

import eu.nets.pia.sample.BuildConfig;

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

public class PiaSampleSharedPreferences {

    private static final String PREFERENCES_NAME = "PiaSampleSharedPreferences";
    private static final String CUSTOMER_ID_KEY = "customer_id_key";
    private static final String PIA_TEST_MODE = "pia_test_mode";
    private static final String CUSTOMER_CURRENCY = "customer_currency";
    private static final String USE_SYSTEM_AUTH = "use_system_auth";
    private static final String DISABLE_CARD_IO = "disable_card_io";
    private static final String MERCHANT_ENV_TEST = "merchant_env_test";
    private static final String MERCHANT_ENV_PROD = "merchant_env_prod";
    private static final String MERCHANT_ID_TEST = "merchant_id_test";
    private static final String MERCHANT_ID_PROD = "merchant_id_prod";
    private static final String DISABLE_SAVE_CARD_OPTION = "disable_save_card_option";

    private static SharedPreferences mSharedPrefs;

    public static synchronized void initPrefs(Context context) {
        if (mSharedPrefs == null) {
            mSharedPrefs = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
        }
    }

    public static void saveCustomerId(String customerId) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString(CUSTOMER_ID_KEY, customerId);
        prefsEditor.commit();
    }

    public static String getCustomerId() {
        return mSharedPrefs.getString(CUSTOMER_ID_KEY, "");
    }

    public static void setPiaTestMode(boolean testMode) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putBoolean(PIA_TEST_MODE, testMode);
        prefsEditor.commit();
    }

    public static boolean isUseSystemAuth() {
        return mSharedPrefs.getBoolean(USE_SYSTEM_AUTH, false);
    }

    public static void setUseSystemAuth(boolean useSystemAuth) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putBoolean(USE_SYSTEM_AUTH, useSystemAuth);
        prefsEditor.commit();
    }

    public static boolean isPiaTestMode() {
        return mSharedPrefs.getBoolean(PIA_TEST_MODE, false);
    }

    public static void setDisableCardIo(boolean disableCardIo){
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putBoolean(DISABLE_CARD_IO, disableCardIo);
        prefsEditor.commit();
    }

    public static boolean isDisableCardIo(){
        return mSharedPrefs.getBoolean(DISABLE_CARD_IO, false);
    }

    public static void setDisableSaveCardOption(boolean disableSaveCardOption) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putBoolean(DISABLE_SAVE_CARD_OPTION, disableSaveCardOption);
        prefsEditor.commit();
    }

    public static boolean isDisableSaveCardOption() {
        return mSharedPrefs.getBoolean(DISABLE_SAVE_CARD_OPTION, false);
    }
    
    public static void setCustomerCurrency(String customerCurrency) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString(CUSTOMER_CURRENCY, customerCurrency);
        prefsEditor.commit();
    }

    public static String getCustomerCurrency() {
        //return euro by default if none is set
        return mSharedPrefs.getString(CUSTOMER_CURRENCY, "EUR");
    }

    public static void setMerchantEnvTest(String merchantEnvTest) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString(MERCHANT_ENV_TEST, merchantEnvTest);
        prefsEditor.commit();
    }

    public static String getMerchantEnvTest() {
        String testEnv = mSharedPrefs.getString(MERCHANT_ENV_TEST, BuildConfig.MERCHANT_BACKEND_URL_TEST);
        return testEnv.isEmpty() ? null : testEnv;
    }

    public static void setMerchantEnvProd(String merchantEnvProd) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString(MERCHANT_ENV_PROD, merchantEnvProd);
        prefsEditor.commit();
    }

    public static String getMerchantEnvProd() {
        String prodEnv = mSharedPrefs.getString(MERCHANT_ENV_PROD, BuildConfig.MERCHANT_BACKEND_URL_PROD);
        return prodEnv.isEmpty() ? null : prodEnv;
    }

    public static void setMerchantIdTest(String merchantIdTest) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString(MERCHANT_ID_TEST, merchantIdTest);
        prefsEditor.commit();
    }

    public static String getMerchantIdTest() {
        String testId = mSharedPrefs.getString(MERCHANT_ID_TEST, BuildConfig.MERCHANT_ID_TEST);
        return testId.isEmpty() ? null : testId;
    }

    public static void setMerchantIdProd(String merchantIdProd) {
        SharedPreferences.Editor prefsEditor = mSharedPrefs.edit();
        prefsEditor.putString(MERCHANT_ID_PROD, merchantIdProd);
        prefsEditor.commit();
    }

    public static String getMerchantIdProd() {
        String prodId = mSharedPrefs.getString(MERCHANT_ID_PROD, BuildConfig.MERCHANT_ID_PROD);
        return prodId.isEmpty() ? null : prodId;
    }

    public static boolean isUsingNetsEnv() {
        return mSharedPrefs.getString(MERCHANT_ID_PROD, BuildConfig.MERCHANT_ID_PROD).equals(BuildConfig.MERCHANT_ID_PROD) ||
                mSharedPrefs.getString(MERCHANT_ID_TEST, BuildConfig.MERCHANT_ID_TEST).equals(BuildConfig.MERCHANT_ID_TEST) ||
                mSharedPrefs.getString(MERCHANT_ENV_PROD, BuildConfig.MERCHANT_BACKEND_URL_PROD).equals(BuildConfig.MERCHANT_BACKEND_URL_PROD) ||
                mSharedPrefs.getString(MERCHANT_ENV_TEST, BuildConfig.MERCHANT_BACKEND_URL_TEST).equals(BuildConfig.MERCHANT_BACKEND_URL_TEST);

    }
}
