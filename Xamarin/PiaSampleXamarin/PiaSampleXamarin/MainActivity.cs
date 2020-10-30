using System;
using System.Net.Http;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
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
using EU.Nets.Pia.Wallets;
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
    public class MainActivity : AppCompatActivity, IMobileWalletListener, IWalletPaymentRegistration
    {
        
        Button btnPay;
        Button btnPayWithSavedCard;
        Button btnSaveCard;
        Button btnPayPal;
        Button btnSkipConfirmation;
        Button btnPaytrailNordea;
        Button btnMobilePay;
        public RelativeLayout progressBar;




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
            btnSkipConfirmation = FindViewById<Button>(Resource.Id.skipConfirmationBtn);
            btnPaytrailNordea = FindViewById<Button>(Resource.Id.paytrailNordea);
            btnMobilePay = FindViewById<Button>(Resource.Id.mobilePay);
            progressBar = FindViewById<RelativeLayout>(Resource.Id.progressBarLayout);

            btnPay.Click += onPayWithNewCard;
            btnPayWithSavedCard.Click += onPayWithSavedCard;
            btnSaveCard.Click += onSaveCard;
            btnPayPal.Click += onPayWithPaypal;
            btnSkipConfirmation.Click += onSkipConfirmation;
            btnPaytrailNordea.Click += onPayViaPaytrail;
            btnMobilePay.Click += payWithMobileWallet;

            Android.Support.V7.Widget.Toolbar toolbar = FindViewById<Android.Support.V7.Widget.Toolbar>(Resource.Id.toolbar);
            SetSupportActionBar(toolbar);
            
        }

        protected override void OnActivityResult(int requestCode, Android.App.Result resultCode, Intent data)
        {
            base.OnActivityResult(requestCode, resultCode, data);

            if (resultCode == Android.App.Result.Ok)
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

            PiaSDK.Instance.Start(this, bundle, new HandlerClass(PaymentMode.NEW_CARD.ToString(), merchantBaseUrlTest, merchantIdTest));
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

            PiaSDK.Instance.Start(this, bundle, new HandlerClass(PaymentMode.SAVED_CARD.ToString(), merchantBaseUrlTest, merchantIdTest));
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

            PiaSDK.Instance.Start(this, bundle, new HandlerClass(PaymentMode.SAVE_CARD.ToString(), merchantBaseUrlTest, merchantIdTest));
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

            PiaSDK.Instance.StartPayPalProcess(this, bundle, new HandlerClass(PaymentMode.PAYPAL.ToString(), merchantBaseUrlProd, merchantIdProd));
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

            PiaSDK.Instance.Start(this, bundle, new HandlerClass(PaymentMode.SAVED_CARD_SKIP.ToString(), merchantBaseUrlTest, merchantIdTest));
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

        private TransactionInfo paytrailRegisterCall()
        {
            try
            {
                showLoader();
                string paytrailRequest = "{\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":1000,\"vatAmount\":0},\"customerAddress1\":\"Testaddress\",\"customerCountry\":\"FI\",\"customerEmail\":\"bill.buyer@nets.eu\",\"customerFirstName\":\"Bill\",\"customerId\":\"000012\",\"customerLastName\":\"Buyer\",\"customerPostCode\":\"00510\",\"customerTown\":\"Helsinki\",\"method\":{\"id\":\"PaytrailNordea\"},\"storeCard\":false,\"orderNumber\":" + getPaytrailOrderNumber() + "}";
                HttpResponseMessage response = makeHttpCall(paytrailRequest, merchantIdTest, merchantBaseUrlTest);
                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        return new TransactionInfo(paymentRegisterResponse.transactionId, paymentRegisterResponse.redirectOK);
                    }
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
            if (progressBar != null) {
                progressBar.Visibility = ViewStates.Visible;
            }
        }

        private void cancelLoader()
        {
            if (progressBar != null)
            {
                progressBar.Visibility = ViewStates.Gone;
            }
        }
        
        public class HandlerClass : Java.Lang.Object, IRegisterPaymentHandler
        {
            string payMode;
            string baseUrl;
            string merchantId;

            public new void Dispose()
            {
                //do nothing
            }

            public HandlerClass(string payMode, string baseUrl, string merchantId)
            {
                this.payMode = payMode;
                this.baseUrl = baseUrl;
                this.merchantId = merchantId;
            }

            TransactionInfo IRegisterPaymentHandler.DoRegisterPaymentRequest(bool p0)
            {
                try
                {
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

                    var client = new HttpClient();
                    client.BaseAddress = new Uri(baseUrl);
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
                    ex.PrintStackTrace();
                    return null;
                }
            }
        }

        private void payWithMobileWallet(object sender, EventArgs eventArgs)
        {
            
                /* Vipps Payment
                 * 
                 * isTestMode - true if Test environment and false for Production.
                 */
                //PaymentProcess.WalletPayment walletProcess = PaymentProcess.Vipps(true, this);


                /*
                 * Swish Payment
                 */
                //PaymentProcess.WalletPayment walletProcess = PaymentProcess.Swish(this);


                /*
                 * MobilePay Payment
                 */
                PaymentProcess.WalletPayment walletProcess = PaymentProcess.MobilePay(this);

                bool canLaunch = PiaSDK.InitiateMobileWallet(walletProcess, this);

                if (!canLaunch)
                {
                    Toast.MakeText(this, "Wallet is not installed", ToastLength.Short).Show();
                    return;
                }
            
        }

        public void RegisterPayment(IWalletURLCallback callback)
        {

            Task.Run(async () =>
            {
                string mobileWalletRequest = "{\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":1000,\"vatAmount\":0},\"customerId\":\"000012\",\"method\":{\"id\":\"MobilePay\"},\"orderNumber\":\"PiaSDK-Android\",\"paymentMethodActionList\":\"[{PaymentMethod:MobilePay}]\",\"redirectUrl\":\"PiaSampleXamarin.PiaSampleXamarin://piasdk_mobilepay\",\"storeCard\":false}";
                HttpResponseMessage response = makeHttpCall(mobileWalletRequest, merchantIdTest, merchantBaseUrlTest);
                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        if (paymentRegisterResponse != null && paymentRegisterResponse.walletUrl != null)
                        {
                            Android.Net.Uri uri = Android.Net.Uri.Parse(paymentRegisterResponse.walletUrl);
                            callback.SuccessWithWalletURL(uri);
                        }
                        else
                        {
                            callback.FailureWithError(null);
                        }
                    }
                }
            });
            
        }

        public void OnMobileWalletAppSwitchFailure(MobileWallet p0, MobileWalletError p1)
        {
            Toast.MakeText(this, "Payment failure", ToastLength.Short).Show();
        }

        public void OnMobileWalletRedirect(MobileWallet p0)
        {
            Toast.MakeText(this, "Redirect was successful. Do a commit call to check status of transaction", ToastLength.Short).Show();
        }

        public void OnMobileWalletRedirectInterrupted(MobileWallet p0)
        {
            Toast.MakeText(this, "Process was interrupted. Do a commit call to check status of transaction", ToastLength.Short).Show();
        }
        
        private HttpResponseMessage makeHttpCall(string jsonData, string merchantId, string merchantBaseUrl)
        {
            var client = new HttpClient();
            client.BaseAddress = new Uri(merchantBaseUrl);
            var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
            HttpResponseMessage response = client.PostAsync(merchantId + "/register", content).Result;
            return response;
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

}
