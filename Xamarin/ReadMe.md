# Xamarin Integration Guide
---

## Purpose
This document provides the basic information to include the **Netaxept - Android SDK** (JAVA native) in your Xamarin application. Please check below the instructions on how to get started.

## Prerequisites
Need-to-know basics on how to get started:
+ IDE: Visual Studio
+ SDK public APIs can be found in the [documentation](../documentation)
+ Android SDK minimum supported API version is 21
+ The SDK requires external dependencies (some of them can be found on NuGet package manager, and some of them need to be included manually as Binding Libraries)

We have provided a [PiaSampleXamarin](PiaSampleXamarin) application which integrates the `PiASDK` Xamarin Binding Library and implements basic functionalities.

## Step-by-step instructions
1. Include `.dll` files in your Xamarin application
    + In your solution explorer, Right click on the application's References folder: **Add Reference** - **Assemblies** - **Browse** - Go to the `DLL Files` folder and select all files and click **OK**
2. Include required external dependencies
    + In your solution explorer, Right click on the application's References folder: **Manage NuGet Packages** - Search and install the following libraries:
        + `Xamarin.Android.Support.Constraint.Layout`
        + `Naxam.Retrofit2.ConvertGson.Droid`

**Note:** If the external dependencies are not available through NuGet Package Manager, you need to manually create separate binding libraries and include the generated `.dll` files in your application. Check the `pom.xml` file from [jCenter()](http://jcenter.bintray.com/eu/nets/pia/pia-sdk/) for required dependencies.

## Example

**Note:** For Xamarin support, the keys for the Bundle will be available inside the `PiaSDK` class, and not in the `PiaActivity` like it's stated in our official documentation. This is because the library is closed-source, and the obfuscation prevents Xamarin Binding Library to access the fields from PiaActivity class.

+ Initialize the RegisterPaymentHandler

```java
  public class Handler : Java.Lang.Object, IRegisterPaymentHandler
    {
        public void Dispose()
        {
            //do nothing
        }

        TransactionInfo IRegisterPaymentHandler.DoRegisterPaymentRequest(bool p0)
        {
            //make register payment request synchronous to your backend and return the Transaction info
            return new TransactionInfo("transactionId", "redirectOk", "'redirectCancel");
        }
    }
```

+ Handle the SDK Result
```java
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
                    }
                    else
                    {
                        Toast.MakeText(this, "ERROR", ToastLength.Short).Show();
                    }
                }
                else
                {
                    Toast.MakeText(this, "CANCELED", ToastLength.Short).Show();
                }
            }
        }
```
+ Start the SDK
The SDK can be started in 4 ways, depending on the content of the Bundle
    1. Pay with new card:
        ```java
        MerchantInfo merchant = new MerchantInfo("merchant_id", false);
        OrderInfo order = new OrderInfo(1, "DKK");
        
        Bundle bundle = new Bundle();
        bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
        bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
        
        PiaSDK.Instance.Start(this, bundle, new Handler());
        ```

    2. Save Card
        ```java
        MerchantInfo merchant = new MerchantInfo("merchant_id", false);
        
        Bundle bundle = new Bundle();
        bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
        
        PiaSDK.Instance.Start(this, bundle, new Handler());
        ```
    3. Pay With saved card
        ``` java
        MerchantInfo merchant = new MerchantInfo("merchant_id", false);
        OrderInfo order = new OrderInfo(1, "DKK");
        TokenCardInfo tokenCardInfo = new TokenCardInfo("4925********0004", "0822", true, false);
        
        Bundle bundle = new Bundle();
        bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
        bundle.PutParcelable(PiaSDK.BundleOrderInfo, order);
        bundle.PutParcelable(PiaSDK.BundleTokenCardInfo, tokenCardInfo);
        
        PiaSDK.Instance.Start(this, bundle, new Handler());
        ```

    4. Pay with PayPal
    
        ```java
        MerchantInfo merchant = new MerchantInfo("merchant_id", false);
        
        Bundle bundle = new Bundle();
        bundle.PutParcelable(PiaSDK.BundleMerchantInfo, merchant);
        
        PiaSDK.Instance.StartPayPalProcess(this, bundle, new Handler());
        ```

For technical information, please check our detailed overview documentation on our [GitHub Page](../).