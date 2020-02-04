using System;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Support.Design.Widget;
using Android.Support.V7.App;
using Android.Views;
using Android.Widget;
using EU.Nets.Pia;
using EU.Nets.Pia.Data.Model;

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

            btnPay.Click += onPayWithNewCard;
            btnPayWithSavedCard.Click += onPayWithSavedCard;
            btnSaveCard.Click += onSaveCard;
            btnPayPal.Click += onPayWithPaypal;
            btnVipps.Click += onPayWithVipps;
            btnSwish.Click += onPayWithSwish;
            btnSkipConfirmation.Click += onSkipConfirmation;

            Android.Support.V7.Widget.Toolbar toolbar = FindViewById<Android.Support.V7.Widget.Toolbar>(Resource.Id.toolbar);
            SetSupportActionBar(toolbar);

            FloatingActionButton fab = FindViewById<FloatingActionButton>(Resource.Id.fab);
            fab.Click += FabOnClick;
        }

        protected override void OnActivityResult(int requestCode, Result resultCode, Intent data)
        {
            base.OnActivityResult(requestCode, resultCode, data);

            if (requestCode == PiaSDK.PiaSdkRequest)
            {
                if (resultCode == Result.Ok)
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
                else
                {
                    Toast.MakeText(this, "CANCELED", ToastLength.Short).Show();
                }
            }
            else if (requestCode == PiaSDK.PiaVippsRequest)
            {
                if (resultCode == Result.Ok)
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
                else
                {
                    Toast.MakeText(this, "CANCELED", ToastLength.Short).Show();
                }
            }
            else if (requestCode == PiaSDK.PiaSwishRequest)
            {
                if (resultCode == Result.Ok)
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
                else
                {
                    Toast.MakeText(this, "CANCELED", ToastLength.Short).Show();
                }
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

        private void FabOnClick(object sender, EventArgs eventArgs)
        {
            View view = (View)sender;
            Snackbar.Make(view, "Replace with your own action", Snackbar.LengthLong)
                .SetAction("Action", (Android.Views.View.IOnClickListener)null).Show();
        }

        private void onPayWithNewCard(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo("merchant_id", false);
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

            PiaSDK.Instance.Start(this, bundle, new Handler());
        }

        private void onPayWithSavedCard(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo("merchant_id", false);
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
            TokenCardInfo tokenCardInfo = new TokenCardInfo("4925********0004", SchemeType.Visa, "08/22", true);

            PiaInterfaceConfiguration.Instance.SkipConfirmationSelected = false;

            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
            bundle.PutParcelable(PiaSDK.BundleTokenCardInfo, tokenCardInfo);

            PiaSDK.Instance.Start(this, bundle, new Handler());
        }

        private void onSaveCard(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo("merchant_id", true);
            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);

            PiaSDK.Instance.Start(this, bundle, new Handler());
        }

        private void onPayWithPaypal(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo("merchant_id", false);
            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);

            PiaSDK.Instance.StartPayPalProcess(this, bundle, new Handler());
        }

        private void onPayWithVipps(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
            */
            MerchantInfo merchant = new MerchantInfo("merchant_id", false);
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
            PiaSDK.Instance.StartVippsProcess(this, bundle, new VippsHandler());
        }

        private void onPayWithSwish(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
            */
            MerchantInfo merchant = new MerchantInfo("merchant_id", false);
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
            PiaSDK.Instance.StartSwishProcess(this, bundle, new SwishHandler());
        }

        private void onSkipConfirmation(object sender, EventArgs eventArgs)
        {
            /**
                Build the MerchantInfo object with the following parameters:
                -merchantId 
                -testMode
                -cvcRequired
            */
            MerchantInfo merchant = new MerchantInfo("merchant_id", false);
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
            TokenCardInfo tokenCardInfo = new TokenCardInfo("4925********0004", SchemeType.Visa, "08/22", false);

            PiaInterfaceConfiguration.Instance.SkipConfirmationSelected = true;

            /**
            * Put the objects in the bundle; access the keys in the PiaSDK class
            */
            Bundle bundle = new Bundle();
            bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
            bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
            bundle.PutParcelable(PiaSDK.BundleTokenCardInfo, tokenCardInfo);

            PiaSDK.Instance.Start(this, bundle, new Handler());
        }
        
    }

    public class Handler : Java.Lang.Object, IRegisterPaymentHandler
    {
        public new void Dispose()
        {
            //do nothing
        }

        TransactionInfo IRegisterPaymentHandler.DoRegisterPaymentRequest(bool p0)
        {
            //make register payment request synchronous to your backend and return the Transaction info
            return new TransactionInfo("transaction_id", "redirect_ok");
        }
    }

    public class VippsHandler : Java.Lang.Object, IRegisterPaymentHandler
    {
        public new void Dispose()
        {
            //do nothing
        }

        TransactionInfo IRegisterPaymentHandler.DoRegisterPaymentRequest(bool p0)
        {
            //make register payment request synchronous to your backend and return the Transaction info
            return new TransactionInfo("walletUrl");
        }
    }

    public class SwishHandler : Java.Lang.Object, IRegisterPaymentHandler
    {
        public new void Dispose()
        {
            //do nothing
        }

        TransactionInfo IRegisterPaymentHandler.DoRegisterPaymentRequest(bool p0)
        {
            //make register payment request synchronous to your backend and return the Transaction info
            return new TransactionInfo("walletUrl");
        }
    }
}

