using System;
using System.Net.Http;
using System.Text;
using Android.App;
using Android.Content;
using Android.Content.PM;
using Android.Icu.Text;
using Android.OS;
using Android.Support.Design.Widget;
using Android.Support.V7.App;
using Android.Util;
using Android.Views;
using Android.Widget;
using EU.Nets.Pia;
using EU.Nets.Pia.Data.Model;
using Java.Lang;
using Java.Util;
using Newtonsoft.Json;
using PiaSampleXamarin.Model;

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

namespace PiaSampleXamarin
{
    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity
    {
        Button btnPay;
        Button btnPayWithSavedCard;
        Button btnSaveCard;
        Button btnPayPal;
        Button btnVipps;
        Button btnSwish;
        Button btnSkipConfirmation;
        Button btnPaytrailNordea;

        ProgressDialog progressDialog;



        string merchantIdTest = "YOUR TEST NETAXEPT MERCHANT ID HERE";
        string merchantIdProd = "YOUR PRODUCTION NETAXEPT MERCHANT ID HERE";
        string merchantBaseUrlTest = "YOUR TEST BACKEND BASE URL HERE";
        string merchantBaseUrlProd = "YOUR PRODUCTION BACKEND BASE URL HERE";
        string tokenIdTest = "YOUR TEST TOKED ID";
        SchemeType schemeIdTest = "YOUR TEST CARD PROVIDER NAME";
        string expiryDateTest = "YOUR TEST CARD EXPIRY DATE";


        protected override void OnCreate(Bundle savedInstanceState)
        {
            base.OnCreate(savedInstanceState);
            SetContentView(Resource.Layout.activity_main);

            btnPay = FindViewById<Button>(Resource.Id.pay);
            btnPayWithSavedCard = FindViewById<Button>(Resource.Id.paySavedCard);
            btnSaveCard = FindViewById<Button>(Resource.Id.saveCard);
            btnPayPal = FindViewById<Button>(Resource.Id.paypal);
            btnVipps = FindViewById<Button>(Resource.Id.vipps);
            btnSwish = FindViewById<Button>(Resource.Id.swishBtn);
            btnSkipConfirmation = FindViewById<Button>(Resource.Id.skipConfirmationBtn);
            btnPaytrailNordea = FindViewById<Button>(Resource.Id.paytrailNordea);

            btnPay.Click += onPayWithNewCard;
            btnPayWithSavedCard.Click += onPayWithSavedCard;
            btnSaveCard.Click += onSaveCard;
            btnPayPal.Click += onPayWithPaypal;
            btnVipps.Click += onPayWithVipps;
            btnSwish.Click += onPayWithSwish;
            btnSkipConfirmation.Click += onSkipConfirmation;
            btnPaytrailNordea.Click += onPayViaPaytrail;

            Android.Support.V7.Widget.Toolbar toolbar = FindViewById<Android.Support.V7.Widget.Toolbar>(Resource.Id.toolbar);
            SetSupportActionBar(toolbar);

        }

        protected override void OnActivityResult(int requestCode, Result resultCode, Intent data)
        {
            base.OnActivityResult(requestCode, resultCode, data);

            if (resultCode == Result.Ok)
            {
                if (requestCode == PiaSDK.PiaSdkRequest)
                {
                    PiaResult result = (PiaResult)data.GetParcelableExtra(PiaSDK.BundleCompleteResult);
                    if (result.Success)
                    {
                        Toast.MakeText(this, "SUCCESS", ToastLength.Short).Show();
                        //call commitPayment/storeCard on your backend
                    }
                    else
                    {
                        Toast.MakeText(this, "ERROR", ToastLength.Short).Show();
                        //call rollbackTransaction on your backend
                    }
                }
                else if (requestCode == PiaSDK.PiaVippsRequest)
                {
                    PiaResult result = (PiaResult)data.GetParcelableExtra(PiaSDK.BundleCompleteResult);
                    if (result.Success)
                    {
                        Toast.MakeText(this, "SUCCESS", ToastLength.Short).Show();
                        //call commitPayment/storeCard on your backend
                    }
                    else
                    {
                        Toast.MakeText(this, "ERROR", ToastLength.Short).Show();
                        //call rollbackTransaction on your backend
                    }
                }
                else if (requestCode == PiaSDK.PiaSwishRequest)
                {
                    PiaResult result = (PiaResult)data.GetParcelableExtra(PiaSDK.BundleCompleteResult);
                    if (result.Success)
                    {
                        Toast.MakeText(this, "SUCCESS", ToastLength.Short).Show();
                        //call commitPayment/storeCard on your backend
                    }
                    else
                    {
                        Toast.MakeText(this, "ERROR", ToastLength.Short).Show();
                        //call rollbackTransaction on your backend
                    }
                }
                else if (requestCode == PiaSDK.PiaPaytrailRequest)
                {
                    PiaResult result = (PiaResult)data.GetParcelableExtra(PiaSDK.BundleCompleteResult);
                    if (result.Success)
                    {
                        Toast.MakeText(this, "SUCCESS", ToastLength.Short).Show();
                        //call commitPayment/storeCard on your backend
                    }
                    else
                    {
                        Toast.MakeText(this, "ERROR", ToastLength.Short).Show();
                        //call rollbackTransaction on your backend
                    }
                }
            }
            else
            {
                Toast.MakeText(this, "CANCELED", ToastLength.Short).Show();
            }
        }

        public override bool OnCreateOptionsMenu(IMenu menu)
        {
            MenuInflater.Inflate(Resource.Menu.menu_main, menu);
            return true;
        }

        public override bool OnOptionsItemSelected(IMenuItem item)
        {
            int id = item.ItemId;
            if (id == Resource.Id.action_settings)
            {
                return true;
            }

            return base.OnOptionsItemSelected(item);
        }

        private void onPayWithNewCard(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdTest, true);
            /**
             *   Build the OrderInfo object with the following parameters:
             *  -amount 
             *  -currencyCode
             */
            OrderInfo order = new OrderInfo(1, "DKK");

            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);

            PiaSDK.Instance.Start(this, bundle, new Handler(PaymentMode.NEW_CARD.ToString(), merchantBaseUrlTest, merchantIdTest));
        }

        private void onPayWithSavedCard(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdTest, true);
            /**
             *   Build the OrderInfo object with the following parameters:
             *  -amount 
             *  -currencyCode
             */
            OrderInfo order = new OrderInfo(1, "DKK");
            /**
            *   Build the TokenCardInfo object with the following parameters:
            *  -card token pan 
            *  -Scheme type
            *  -expiration date
            *  -cardVerificationRequired (if CVV/CVC will be asked)
            *  -useSystemAuth (if CVV/CVC is not required, the option to confirm payment with unlock screen will be prompted if this flag is true) 
            */
            TokenCardInfo tokenCardInfo = new TokenCardInfo(tokenIdTest, schemeIdTest, expiryDateTest, true);

            PiaInterfaceConfiguration.Instance.SkipConfirmationSelected = false;

            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
            bundle.PutParcelable(PiaSDK.BundleTokenCardInfo, tokenCardInfo);

            PiaSDK.Instance.Start(this, bundle, new Handler(PaymentMode.SAVED_CARD.ToString(), merchantBaseUrlTest, merchantIdTest));
        }

        private void onSaveCard(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdTest, true);
            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);

            PiaSDK.Instance.Start(this, bundle, new Handler(PaymentMode.SAVE_CARD.ToString(), merchantBaseUrlTest, merchantIdTest));
        }

        private void onPayWithPaypal(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdProd, false);
            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);

            PiaSDK.Instance.StartPayPalProcess(this, bundle, new Handler(PaymentMode.PAYPAL.ToString(), merchantBaseUrlProd, merchantIdProd));
        }

        private void onPayWithVipps(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdTest, true);
            /**
             *   Build the OrderInfo object with the following parameters:
             *  -amount 
             *  -currencyCode
             */
            OrderInfo order = new OrderInfo(1, "NOK");
            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */

            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
            PiaSDK.Instance.StartVippsProcess(this, bundle, new Handler(PaymentMode.VIPPS.ToString(), merchantBaseUrlTest, merchantIdTest));
        }

        private void onPayWithSwish(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdTest, false);
            /**
             *   Build the OrderInfo object with the following parameters:
             *  -amount 
             *  -currencyCode
             */
            OrderInfo order = new OrderInfo(1, "SEK");
            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */

            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
            PiaSDK.Instance.StartSwishProcess(this, bundle, new Handler(PaymentMode.SWISH.ToString(), merchantBaseUrlTest, merchantIdTest));
        }

        private void onSkipConfirmation(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdTest, true, true);
            /**
             *   Build the OrderInfo object with the following parameters:
             *  -amount 
             *  -currencyCode
             */
            OrderInfo order = new OrderInfo(1, "EUR");
            /**
            *   Build the TokenCardInfo object with the following parameters:
            *  -card token pan 
            *  -Scheme type
            *  -expiration date
            *  -cardVerificationRequired (if CVV/CVC will be asked)
            *  -useSystemAuth (if CVV/CVC is not required, the option to confirm payment with unlock screen will be prompted if this flag is true) 
            */
            TokenCardInfo tokenCardInfo = new TokenCardInfo(tokenIdTest, schemeIdTest, expiryDateTest, false);

            PiaInterfaceConfiguration.Instance.SkipConfirmationSelected = true;

            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
            bundle.PutParcelable(PiaSDK.BundleTokenCardInfo, tokenCardInfo);

            PiaSDK.Instance.Start(this, bundle, new Handler(PaymentMode.SAVED_CARD_SKIP.ToString(), merchantBaseUrlTest, merchantIdTest));
        }

        private void onPayViaPaytrail(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
            */
            MerchantInfo merchant = new MerchantInfo(merchantIdTest, true);
            /**
             *   Build the OrderInfo object with the following parameters:
             *  -amount 
             *  -currencyCode
             */
            OrderInfo order = new OrderInfo(1, "EUR");
            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */

            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
            TransactionInfo transactionInfo = paytrailRegisterCall();
            if (transactionInfo != null)
            {
                bundle.PutParcelable(PiaSDK.BundleTransactionInfo, transactionInfo);
                PiaSDK.Instance.StartPaytrailProcess(this, bundle);
            }
            else
            {
                Toast.MakeText(this, "Paytrail register failed", ToastLength.Short).Show();
            }
        }

        private TransactionInfo paytrailRegisterCall()
        {
            try
            {
                showLoader();
                var client = new HttpClient();
                client.BaseAddress = new Uri(merchantBaseUrlTest);
                string paytrailRequest = "{\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":1000,\"vatAmount\":0},\"customerAddress1\":\"Testaddress\",\"customerCountry\":\"FI\",\"customerEmail\":\"bill.buyer@nets.eu\",\"customerFirstName\":\"Bill\",\"customerId\":\"000012\",\"customerLastName\":\"Buyer\",\"customerPostCode\":\"00510\",\"customerTown\":\"Helsinki\",\"method\":{\"id\":\"PaytrailNordea\"},\"storeCard\":false,\"orderNumber\":" + getPaytrailOrderNumber() + "}";

                var content = new StringContent(paytrailRequest, Encoding.UTF8, "application/json");
                HttpResponseMessage response = client.PostAsync(merchantIdTest + "/register", content).Result;

                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        cancelLoader();
                        return new TransactionInfo(paymentRegisterResponse.transactionId, paymentRegisterResponse.redirectOK);
                    }
                    cancelLoader();
                    return null;
                }
                cancelLoader();
                return null;
            }
            catch (Java.Lang.Exception ex)
            {
                cancelLoader();
                return null;
            }
        }

        private void showLoader()
        {
            if (progressDialog == null)
            {
                progressDialog = ProgressDialog.Show(this, "", "Loading Please wait...", false, false);
                progressDialog.Indeterminate = true;
                progressDialog.SetProgressStyle(ProgressDialogStyle.Horizontal);
                progressDialog.Show();
            }
        }

        private void cancelLoader()
        {
            if (progressDialog != null && progressDialog.IsShowing)
            {
                progressDialog.Dismiss();
            }
        }

        //An algorithm to create reference number to your invoices as per Finnish Payment Guidelines.
        private string getPaytrailOrderNumber()
        {

            int checkDigit = -1;
            int[] multipliers = { 7, 3, 1 };
            int multiplierIndex = 0;
            int sum = 0;
            string DATE_TIME_FORMAT = "yyMMddHHmmssSSS";

            // Storing random positive integers in an array. '1' is appended in the beginning of the
            // order number in order to differentiate between Android and iOS (0 for iOS and 1 for Android)
            string orderNumber = "1" + new SimpleDateFormat(DATE_TIME_FORMAT, Locale.Default).Format(new Date());

            //Sum of the product of each element of randomNumber and multipliers in right to left manner
            for (int i = orderNumber.Length - 1; i >= 0; i--)
            {
                if (multiplierIndex == 3)
                {
                    multiplierIndex = 0;
                }
                int value = Character.GetNumericValue(orderNumber[i]);
                sum += value * multipliers[multiplierIndex];
                multiplierIndex++;
            }

            //The sum is then subtracted from the next highest ten
            checkDigit = 10 - sum % 10;

            if (checkDigit == 10)
            {
                checkDigit = 0;
            }

            return orderNumber + checkDigit;

        }

    }

    public class Handler : Java.Lang.Object, IRegisterPaymentHandler
    {
        string payMode;
        string baseUrl;
        string merchantId;

        public new void Dispose()
        {
            //do nothing
        }

        public Handler(string payMode, string baseUrl, string merchantId)
        {
            this.payMode = payMode;
            this.baseUrl = baseUrl;
            this.merchantId = merchantId;
        }

        TransactionInfo IRegisterPaymentHandler.DoRegisterPaymentRequest(bool p0)
        {
            try
            {
                var client = new HttpClient();
                client.BaseAddress = new Uri(baseUrl);

                string jsonData;
                if (payMode.Equals(PaymentMode.NEW_CARD.ToString()))
                {
                    jsonData = "{\"storeCard\":true,\"orderNumber\":\"PiaSDK-Android\",\"customerId\":\"000003\",\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":\"100\",\"vatAmount\":0}}";
                }
                else if (payMode.Equals(PaymentMode.SAVED_CARD.ToString()))
                {
                    jsonData = "{\"customerId\":\"000012\",\"orderNumber\":\"PiaSDK-Android\",\"amount\":{\"currencyCode\":\"EUR\",\"vatAmount\":0,\"totalAmount\":\"1000\"},\"method\":{\"id\":\"EasyPayment\",\"displayName\":\"\",\"fee\":\"\"},\"cardId\":\"492500******0004\",\"storeCard\":true,\"merchantId\":\"\",\"token\":\"\",\"serviceTyp\":\"\",\"paymentMethodActionList\":\"\",\"phoneNumber\":\"\",\"currencyCode\":\"\",\"redirectUrl\":\"\",\"language\":\"\"}";
                }
                else if (payMode.Equals(PaymentMode.SAVED_CARD_SKIP.ToString()))
                {
                    jsonData = "{\"amount\":{\"currencyCode\":\"SEK\",\"totalAmount\":1000,\"vatAmount\":0},\"cardId\":\"492500******0004\",\"customerId\":\"000012\",\"method\":{\"id\":\"EasyPayment\"},\"orderNumber\":\"PiaSDK-Android\",\"storeCard\":false}";
                }
                else if (payMode.Equals(PaymentMode.VIPPS.ToString()))
                {
                    jsonData = "{\"amount\":{\"currencyCode\":\"NOK\",\"totalAmount\":1000,\"vatAmount\":0},\"customerId\":\"000013\",\"method\":{\"id\":\"Vipps\"},\"orderNumber\":\"PiaSDK-Android\",\"paymentMethodActionList\":\"[{PaymentMethod:Vipps}]\",\"phoneNumber\":\"+4748059560\",\"redirectUrl\":\"PiaSampleXamarin.PiaSampleXamarin://piasdk\",\"storeCard\":false}";
                }
                else if (payMode.Equals(PaymentMode.PAYPAL.ToString()))
                {
                    jsonData = "{\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":1000,\"vatAmount\":0},\"customerId\":\"000012\",\"method\":{\"id\":\"PayPal\"},\"orderNumber\":\"PiaSDK-Android\",\"storeCard\":false}";
                }
                else
                {
                    jsonData = "{\"storeCard\":true,\"orderNumber\":\"PiaSDK-Android\",\"customerId\":\"000003\",\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":\"100\",\"vatAmount\":0}}";
                }

                var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
                HttpResponseMessage response = client.PostAsync(merchantId + "/register", content).Result;

                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        if (payMode.Equals(PaymentMode.VIPPS.ToString()) || payMode.Equals(PaymentMode.SWISH.ToString()))
                        {
                            return new TransactionInfo(paymentRegisterResponse.walletUrl);
                        }
                        else
                        {
                            return new TransactionInfo(paymentRegisterResponse.transactionId, paymentRegisterResponse.redirectOK);
                        }
                    }
                    return null;
                }
                return null;
            }
            catch (Java.Lang.Exception ex)
            {
                return null;
            }
        }
    }

    enum PaymentMode
    {
        NEW_CARD,
        SAVE_CARD,
        SAVED_CARD,
        SAVED_CARD_SKIP,
        PAYPAL,
        VIPPS,
        SWISH,
        PAYTRAIL
    }

}

