# React Native Integration Guide
---
## This is a reference source code of an application (under MIT license) using the SDK, provided for demo purpose!

## Purpose
This document provides the basic information to include the **Netaxept - Android SDK** (JAVA native) in your React Native application. Please check below the instructions on how to get started.

## Prerequisites
Need-to-know basics on how to get started:
+ IDE: Visual Studio Code
+ SDK public APIs can be found in the [documentation](../documentation)
+ Android SDK minimum supported API version is 21
+ The **PiA - Netaxept Android SDK** is available through both `jcenter()` and `mavenCentral()` repositories

We have provided a `PiaSampleReactNative` application which integrates the `PiASDK` native library and uses a sample **Bridge** between JavaScript and Java code.

## Step-by-step instructions
### Create the React Native Bridge
1. Add dependency for **PiaSDK**
    + In your **android** folder, in `build.gradle` application level file, add:
```gradle
implementation('eu.nets.pia:pia-sdk:2.7.2') { transitive = true; }
```
2. Create a _.java_ class in your Android folder which extends `ReactContextBaseJavaModule`. Make sure to override the `getName()` method, and return a proper String
3. Create a _.java_ class in your Android folder which extends `ReactPackage`. In the array returned by the `createNativeModules()` method add a new instance of the class created at the previous step
4. In your `MainApplication` class, in the array returned by `getPackages` method add a new instance of the class created at previous the step

### Configure the React Native Bridge

1. In your class which extends the `ReactContextBaseJavaModule`, make sure to use the correct imports. Please check the official [documentation](../documentation) to find the right paths for classes
2. Implement the `ActivityEventListener` in your class definition and override the `onActivityResult` method to receive the SDK result
3. Add class variables for the following: `MerchantInfo`, `OrderInfo`, `TokenCardInfo`, `TransactionInfo` (these will be used to launch the SDK), and `Promise` (this will be used to deliver the payment result to your react-native application)
4. Create setter methods for these objects, annotated with `@ReactMethod`, which will instantiate the local variables with the given parameters
5. Create a method, annotated with `@ReactMethod`, which will receive a `Callback` as parameter (it will be used to notify the application to make the `registerPayment` call) and will start the SDK. Check the official documentation to see which parameters are required for each SDK use case

**Important:** When the SDK is invoking the `doRegisterPaymentRequest` handler, an synchronous API call to your backend is required. Here, you have two options:
+ Make the API call from the bridge Java class (you may need to add dependencies for networking library and write the HTTP calls in Java native code)
+ Use thread syncronization in the bridge class: on a final Object call `.wait()` until the react-native app calls the registerPayment and sets the TransactionInfo variable in the bridge. Then, call `.notify()` on the Object to release the thread, and the payment process will continue (check our [sample application](PiaSampleReactNative/) for a full example)

**Note:** Don’t forget to clear the class variables after each payment process.

## Example

Below you can find an overview on how to create and call the bridge from your react-native application. For a detailed implementation, check the attached sample application.

+ Required imports
```java
import eu.nets.pia.data.model.MerchantInfo;
import eu.nets.pia.data.model.OrderInfo;
import eu.nets.pia.data.model.PiaResult;
import eu.nets.pia.data.model.SchemeType;
import eu.nets.pia.data.model.TokenCardInfo;
import eu.nets.pia.data.model.TransactionInfo;
import eu.nets.pia.ui.main.PiaActivity;
import eu.nets.pia.PiaSDK;
import eu.nets.pia.RegisterPaymentHandler;
```

+ Defining bridge class

```java
public class SDKModule extends ReactContextBaseJavaModule implements ActivityEventListener{

private final Object threadSynchronizator = new Object();
//define here local variables for MerchantInfo, OrderInfo, TransactionInfo, Promise

public SDKModule(final ReactApplicationContext reactContext) {
    super(reactContext);
    reactContext.addActivityEventListener(this);
}

@Override
public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
    //hanndle the SDK result, and send it to the application through the Promise variable
    //also here, you can clear the class variables, as they are no longer needed
}

@Override
public void onNewIntent(Intent intent) {
    //nothing is required here
}

@Override
public String getName() {
    return "PiaSDK";
}

//setters for the class variables

}
```
+ Start SDK method in the bridge

```java
@ReactMethod
public void start(final Callback registerPaymentCallback) {
    Bundle bundle = new Bundle();
    if (merchantInfo != null) {
        bundle.putParcelable(PiaActivity.BUNDLE_MERCHANT_INFO, merchantInfo);
    }
    if (orderInfo != null) {
        bundle.putParcelable(PiaActivity.BUNDLE_ORDER_INFO, orderInfo);
    }
    if (tokenCardInfo != null) {
        bundle.putParcelable(PiaActivity.BUNDLE_TOKEN_CARD_INFO, tokenCardInfo);
    }
    
    PiaSDK.getInstance().start(getCurrentActivity(), bundle, new RegisterPaymentHandler() {
            @Override
            public TransactionInfo doRegisterPaymentRequest(final boolean saveCard) {
                //notify the application to call registerPayment; the TransactionInfo will be set in the specific setter for this variable
                registerPaymentCallback.invoke(saveCard);
                try {
                    //when the buildTransactionInfo() method will be called, the thread will resume
                    return getTransactionInfo();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return null;
            }
        }
    });
}

private TransactionInfo getTransactionInfo() throws InterruptedException {
    synchronized (threadSynchronizator) {
        while (transactionInfo == null) {
            //block the current thread until the appli
            cation will set the required TransactionInfo object
            threadSynchronizator.wait();
        }
    }
    return transactionInfo;
}

@ReactMethod
public void buildTransactionInfo(String transactionId, String redirectOK, String redirectCancel){
    synchronized (threadSynchronizator) {
        if(transactionId == null){
            transactionInfo = null;
        } else {
            transactionInfo = new TransactionInfo(transactionId, redirectOK);
        }
        //release the thread to continue with the payment
        threadSynchronizator.notify();
    }
}
```
+ Call bridge methods from application

```javascript
saveCard = () => {
    //for save card only MerchantInfo object is required
    NativeModules.PiaSDK.buildMerchantInfo("MERCHANT_ID", true, true);
    //set the payment result promise
    NativeModules.PiaSDK.handleSDKResult().then(()=>{
        ToastAndroid.show('SUCCESS', ToastAndroid.SHORT);
    }).catch((error) =>{
        ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    NativeModules.PiaSDK.start((saveCardBool) => {
    //make register payment request
    fetch('BASE_URL/v1/payment/{MERCHANT_ID}/register', {
        method: 'POST',
        headers: {
        'Accept': 'application/json',
        },
        body: requestString
    }).then((response) => response.json())
      .then((responseJson) => {
            NativeModules.PiaSDK.buildTransactionInfo(responseJson.transactionId ,responseJson.redirectOK);
        })
      .catch((error) => {
            console.error(error);
            NativeModules.PiaSDK.buildTransactionInfo(null ,null);
        });
    });
}
```


