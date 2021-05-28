using System;
using System.Net.Http;
using System.Text;
using System.Threading.Tasks;
using Android.App;
using Android.Content;
using Android.Icu.Text;
using Android.OS;
using AndroidX.AppCompat.App;
using Android.Util;
using Android.Views;
using Android.Widget;
using AndroidX.Activity.Result;
using EU.Nets.Pia;
using EU.Nets.Pia.Card;
using EU.Nets.Pia.Data.Model;
using EU.Nets.Pia.Wallets;
using Java.Lang;
using Java.Util;
using Newtonsoft.Json;
using PiaSampleXamarin.Model;
using System.Collections.Generic;

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

    public class PiaActivityResultCallback : Java.Lang.Object, IActivityResultCallback
    {
        Action<ProcessResult> onProcessResult;

        public PiaActivityResultCallback(Action<ProcessResult> onProcessResul)
        {
            this.onProcessResult = onProcessResul;
        }

        public void OnActivityResult(Java.Lang.Object p0)
        {
            onProcessResult((ProcessResult)p0);
        }

    }

    [Activity(Label = "@string/app_name", Theme = "@style/AppTheme.NoActionBar", MainLauncher = true)]
    public class MainActivity : AppCompatActivity, IMobileWalletListener, IWalletPaymentRegistration
    {

        ActivityResultLauncher cardPaymentActivityLauncher, paypalPaymentActivityLauncher, paytrailPaymentActivityLauncher;


        Button btnPay;
        Button btnPayWithSavedCard;
        Button btnSaveCard;
        Button btnPayPal;
        Button btnSkipConfirmation;
        Button btnPaytrailNordea;
        Button btnMobilePay;
        Button btnSBusinessCard;
        Switch switchToVisaOnly;
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
            btnSBusinessCard = FindViewById<Button>(Resource.Id.sBusinessCard);
            switchToVisaOnly = FindViewById<Switch>(Resource.Id.tbtnCardScheme);
            progressBar = FindViewById<RelativeLayout>(Resource.Id.progressBarLayout);

            btnPay.Click += onPayWithNewCard;
            btnPayWithSavedCard.Click += onPayWithSavedCard;
            btnSaveCard.Click += onSaveCard;
            btnPayPal.Click += onPayWithPaypal;
            btnSkipConfirmation.Click += onSkipConfirmation;
            btnPaytrailNordea.Click += onPayViaPaytrail;
            btnMobilePay.Click += payWithMobileWallet;
            btnSBusinessCard.Click += payWithSBusinessCard;

            AndroidX.AppCompat.Widget.Toolbar toolbar = FindViewById<AndroidX.AppCompat.Widget.Toolbar>(Resource.Id.toolbar);
            SetSupportActionBar(toolbar);

            cardPaymentActivityLauncher = RegisterForActivityResult(
                PiaSDK.CardPaymentActivityContractXamarin(),
                new PiaActivityResultCallback(processResult => {

                    switch (processResult) {

                        case ProcessResult.Success:
                            Toast.MakeText(this, "Success", ToastLength.Short).Show();
                            break;

                        case ProcessResult.Failure:
                            Toast.MakeText(this, "Failure", ToastLength.Short).Show();
                            break;

                        case ProcessResult.Cancellation:
                            Toast.MakeText(this, "Cancelled", ToastLength.Short).Show();
                            break;

                        default:
                            break;

                    }
                })
            );

            paypalPaymentActivityLauncher = RegisterForActivityResult(
                PiaSDK.PaypalPaymentActivityContractXamarin(),
                new PiaActivityResultCallback(processResult => {

                    switch (processResult)
                    {

                        case ProcessResult.Success:
                            Toast.MakeText(this, "Success", ToastLength.Short).Show();
                            break;

                        case ProcessResult.Failure:
                            Toast.MakeText(this, "Failure", ToastLength.Short).Show();
                            break;

                        case ProcessResult.Cancellation:
                            Toast.MakeText(this, "Cancelled", ToastLength.Short).Show();
                            break;

                        default:
                            break;

                    }
                })
            );

            paytrailPaymentActivityLauncher = RegisterForActivityResult(
                PiaSDK.PaytrailPaymentActivityContractXamarin(),
                new PiaActivityResultCallback(processResult => {

                    switch (processResult)
                    {

                        case ProcessResult.Success:
                            Toast.MakeText(this, "Success", ToastLength.Short).Show();
                            break;

                        case ProcessResult.Failure:
                            Toast.MakeText(this, "Failure", ToastLength.Short).Show();
                            break;

                        case ProcessResult.Cancellation:
                            Toast.MakeText(this, "Cancelled", ToastLength.Short).Show();
                            break;

                        default:
                            break;

                    }
                })
            );
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

            HashSet<CardScheme> excludeCardSchemes = new HashSet<CardScheme>();

            if (switchToVisaOnly.Checked)
            {
                // Exclude all card schemes except `visa`
                excludeCardSchemes = new HashSet<CardScheme>(CardScheme.Values());
                excludeCardSchemes.Remove(CardScheme.Visa);
            }

            PiaSDK.StartCardProcessActivity(
                        cardPaymentActivityLauncher,
                        PaymentProcess.CardPayment(
                                merchantIDAndEnvironmentPair(),
                                excludeCardSchemes,
                                amountAndCurrencyCodePair(),
                                new CardPaymentRegistration(
                                    merchantBaseUrlTest,
                                    merchantIdTest,
                                    PaymentMode.NEW_CARD)
                                ),
                        (Java.Lang.Boolean)true
                );
        }

        private Pair merchantIDAndEnvironmentPair()
        {
            bool isTest = true;
            string merchantID = merchantIdTest;
            return Pair.Create(merchantID, isTest ? PiaSDK.Environment.Test : PiaSDK.Environment.Prod);
        }

        private Pair amountAndCurrencyCodePair()
        {
            int amountInCents = 1 * 100;
            string currency = "EUR";
            return Pair.Create(amountInCents, currency);
        }

        private void onPayWithSavedCard(object sender, EventArgs eventArgs)
        {
           
            PiaInterfaceConfiguration.Instance.SkipConfirmationSelected = false;

            PiaSDK.StartCardProcessActivity(cardPaymentActivityLauncher,
                PaymentProcess.CardTokenPayment(
                    merchantIDAndEnvironmentPair(),
                    amountAndCurrencyCodePair(),
                    tokenIdTest,
                    schemeIdTest,
                    expiryDateTest,
                new CardTokenPaymentRegistration(
                    merchantBaseUrlTest, 
                    merchantIdTest)),
                (Java.Lang.Boolean)true);

        }

        private void onSaveCard(object sender, EventArgs eventArgs)
        {
            
            PiaSDK.StartCardProcessActivity(
                cardPaymentActivityLauncher,
                PaymentProcess.CardTokenization(
                    Pair.Create(
                        merchantIdTest, 
                        PiaSDK.Environment.Test),
                new CardTokenizationRegistration(merchantBaseUrlTest, 
                    merchantIdTest)), 
                (Java.Lang.Boolean)true);

        }

        private void onPayWithPaypal(object sender, EventArgs eventArgs)
        {
            PiaSDK.StartPayPalPayment(
                paypalPaymentActivityLauncher,
                    Pair.Create(merchantIdProd, PiaSDK.Environment.Prod),
                    new PaypalPaymentRegistration(
                        merchantBaseUrlProd,
                        merchantIdProd));
            
        }

        private void onSkipConfirmation(object sender, EventArgs eventArgs)
        {

            PiaInterfaceConfiguration.Instance.SkipConfirmationSelected = true;

            PiaSDK.StartCardProcessActivity(
                cardPaymentActivityLauncher,
                PaymentProcess.CardTokenPayment(
                    merchantIDAndEnvironmentPair(), 
                    amountAndCurrencyCodePair(), 
                    tokenIdTest,
                    schemeIdTest,
                    expiryDateTest,
                new CardTokenPaymentRegistration(
                    merchantBaseUrlTest, 
                    merchantIdTest)), 
                (Java.Lang.Boolean)false);

        }

        private void onPayViaPaytrail(object sender, EventArgs eventArgs)
        {
            
            PiaSDK.StartPaytrailPayment(
                paytrailPaymentActivityLauncher, 
                merchantIDAndEnvironmentPair(), 
                new PaytrailPaymentRegistration(
                    merchantBaseUrlTest, 
                    merchantIdTest));
            
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

        private void payWithSBusinessCard(object sender, EventArgs eventArgs)
        {
            PiaSDK.StartSBusinessCardProcessActivity(
                        cardPaymentActivityLauncher,
                        PaymentProcess.CardPayment(
                                Pair.Create(merchantISBusinessTest, PiaSDK.Environment.Test ),
                                Pair.Create(100, "EUR"),
                                new CardPaymentRegistration(
                                    merchantBaseUrlTest, 
                                    merchantISBusinessTest,
                                    PaymentMode.S_BUSINESS)
                        ), (Java.Lang.Boolean)true);
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
            PAYTRAIL,
            S_BUSINESS
        }

        class CardPaymentRegistration : Java.Lang.Object, ICardPaymentRegistration
        {
            string merchantBaseUrlTest;
            string merchantIdTest;
            PaymentMode paymentMode;

            public CardPaymentRegistration(string merchantBaseUrlTest, string merchantIdTest, PaymentMode paymentMode) {
                this.merchantBaseUrlTest = merchantBaseUrlTest;
                this.merchantIdTest = merchantIdTest;
                this.paymentMode = paymentMode;
            }

            public void RegisterPayment(bool shouldStoreCard, ITransactionCallback callbackWithTransaction)
            {
                string jsonData;
                if (paymentMode.Equals(PaymentMode.S_BUSINESS)) {
                    jsonData = "{\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":1000,\"vatAmount\":0},\"customerId\":\"000011\",\"orderNumber\":\"PiaSDK-Android\",\"paymentMethodActionList\":\"[{PaymentMethod:SBusinessCard}]\"}";
                } else {
                    jsonData = "{\"storeCard\":true,\"orderNumber\":\"PiaSDK-Android\",\"customerId\":\"000003\",\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":\"100\",\"vatAmount\":0}}";
                }
                var client = new HttpClient();
                client.BaseAddress = new Uri(merchantBaseUrlTest);
                var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
                HttpResponseMessage response = client.PostAsync(merchantIdTest + "/register", content).Result;

                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        if (paymentRegisterResponse != null && paymentRegisterResponse.transactionId != null)
                        {
                            callbackWithTransaction.SuccessWithTransactionIDAndRedirectURL(paymentRegisterResponse.transactionId, Android.Net.Uri.Parse(paymentRegisterResponse.redirectOK));
                        }
                        else
                        {
                            callbackWithTransaction.FailureWithError(null);
                        }
                    }
                }
            }
        }

        class PaypalPaymentRegistration : Java.Lang.Object, IPayPalPaymentRegistration
        {
            string merchantBaseUrlTest;
            string merchantIdTest;

            public PaypalPaymentRegistration(string merchantBaseUrlTest, string merchantIdTest)
            {
                this.merchantBaseUrlTest = merchantBaseUrlTest;
                this.merchantIdTest = merchantIdTest;
            }

            public void RegisterPayment(ITransactionCallback callbackWithTransaction)
            {
                string jsonData = "{\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":1000,\"vatAmount\":0},\"customerId\":\"000011\",\"method\":{\"id\":\"PayPal\"},\"orderNumber\":\"PiaSDK-Android\"}";
                var client = new HttpClient();
                client.BaseAddress = new Uri(merchantBaseUrlTest);
                var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
                HttpResponseMessage response = client.PostAsync(merchantIdTest + "/register", content).Result;

                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        if (paymentRegisterResponse != null && paymentRegisterResponse.transactionId != null)
                        {
                            callbackWithTransaction.SuccessWithTransactionIDAndRedirectURL(paymentRegisterResponse.transactionId, Android.Net.Uri.Parse(paymentRegisterResponse.redirectOK));
                        }
                        else
                        {
                            callbackWithTransaction.FailureWithError(null);
                        }
                    }
                }
            }
        }

        class PaytrailPaymentRegistration : Java.Lang.Object, IPaytrailPaymentRegistration
        {
            string merchantBaseUrlTest;
            string merchantIdTest;

            public PaytrailPaymentRegistration(string merchantBaseUrlTest, string merchantIdTest)
            {
                this.merchantBaseUrlTest = merchantBaseUrlTest;
                this.merchantIdTest = merchantIdTest;
            }

            public void RegisterPayment(ITransactionCallback callbackWithTransaction)
            {
                string jsonData = "{\"amount\":{\"currencyCode\":\"EUR\",\"totalAmount\":1000,\"vatAmount\":0},\"customerAddress1\":\"Testaddress\",\"customerCountry\":\"FI\",\"customerEmail\":\"bill.buyer@nets.eu\",\"customerFirstName\":\"Bill\",\"customerId\":\"000012\",\"customerLastName\":\"Buyer\",\"customerPostCode\":\"00510\",\"customerTown\":\"Helsinki\",\"method\":{\"id\":\"PaytrailNordea\"},\"storeCard\":false,\"orderNumber\":" + new MainActivity().getPaytrailOrderNumber() + "}";
                var client = new HttpClient();
                client.BaseAddress = new Uri(merchantBaseUrlTest);
                var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
                HttpResponseMessage response = client.PostAsync(merchantIdTest + "/register", content).Result;

                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        if (paymentRegisterResponse != null && paymentRegisterResponse.transactionId != null)
                        {
                            callbackWithTransaction.SuccessWithTransactionIDAndRedirectURL(paymentRegisterResponse.transactionId, Android.Net.Uri.Parse(paymentRegisterResponse.redirectOK));
                        }
                        else
                        {
                            callbackWithTransaction.FailureWithError(null);
                        }
                    }
                }
            }
        }

        class CardTokenizationRegistration : Java.Lang.Object, ICardTokenizationRegistration
        {
            string merchantBaseUrlTest;
            string merchantIdTest;

            public CardTokenizationRegistration(string merchantBaseUrlTest, string merchantIdTest)
            {
                this.merchantBaseUrlTest = merchantBaseUrlTest;
                this.merchantIdTest = merchantIdTest;
            }

            public void RegisterPayment(ITransactionCallback callbackWithTransaction)
            {
                string jsonData = "{\"customerId\":\"000012\",\"orderNumber\":\"PiaSDK-Android\",\"amount\":{\"currencyCode\":\"EUR\",\"vatAmount\":0,\"totalAmount\":\"1000\"},\"method\":{\"id\":\"EasyPayment\",\"displayName\":\"\",\"fee\":\"\"},\"cardId\":\"492500******0004\",\"storeCard\":true,\"merchantId\":\"\",\"token\":\"\",\"serviceTyp\":\"\",\"paymentMethodActionList\":\"\",\"phoneNumber\":\"\",\"currencyCode\":\"\",\"redirectUrl\":\"\",\"language\":\"\"}";
                var client = new HttpClient();
                client.BaseAddress = new Uri(merchantBaseUrlTest);
                var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
                HttpResponseMessage response = client.PostAsync(merchantIdTest + "/register", content).Result;

                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        if (paymentRegisterResponse != null && paymentRegisterResponse.transactionId != null)
                        {
                            callbackWithTransaction.SuccessWithTransactionIDAndRedirectURL(paymentRegisterResponse.transactionId, Android.Net.Uri.Parse(paymentRegisterResponse.redirectOK));
                        }
                        else
                        {
                            callbackWithTransaction.FailureWithError(null);
                        }
                    }
                }
            }
        }

        class CardTokenPaymentRegistration : Java.Lang.Object, ICardTokenPaymentRegistration
        {
            string merchantBaseUrlTest;
            string merchantIdTest;

            public CardTokenPaymentRegistration(string merchantBaseUrlTest, string merchantIdTest)
            {
                this.merchantBaseUrlTest = merchantBaseUrlTest;
                this.merchantIdTest = merchantIdTest;
            }

            public void RegisterPayment(ITransactionCallback callbackWithTransaction)
            {
                string jsonData = "{\"amount\":{\"currencyCode\":\"SEK\",\"totalAmount\":1000,\"vatAmount\":0},\"cardId\":\"492500******0004\",\"customerId\":\"000012\",\"method\":{\"id\":\"EasyPayment\"},\"orderNumber\":\"PiaSDK-Android\",\"storeCard\":false}";
                var client = new HttpClient();
                client.BaseAddress = new Uri(merchantBaseUrlTest);
                var content = new StringContent(jsonData, Encoding.UTF8, "application/json");
                HttpResponseMessage response = client.PostAsync(merchantIdTest + "/register", content).Result;

                if (response.IsSuccessStatusCode)
                {
                    string result = response.Content.ReadAsStringAsync().Result;
                    if (result != null)
                    {
                        PaymentRegisterResponse paymentRegisterResponse = JsonConvert.DeserializeObject<PaymentRegisterResponse>(result);
                        if (paymentRegisterResponse != null && paymentRegisterResponse.transactionId != null)
                        {
                            callbackWithTransaction.SuccessWithTransactionIDAndRedirectURL(paymentRegisterResponse.transactionId, Android.Net.Uri.Parse(paymentRegisterResponse.redirectOK));
                        }
                        else
                        {
                            callbackWithTransaction.FailureWithError(null);
                        }
                    }
                }
            }
        }

    }

}
