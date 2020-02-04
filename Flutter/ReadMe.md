# Flutter Integration Guide
---

## Purpose
This document provides the basic information to include the **Netaxept - Android SDK** (JAVA native) in your Flutter application. Please check below the instructions on how to get started.

## Prerequisites
Need-to-know basics on how to get started:
+ IDE: Android Studio (VS Code, IntelliJ)
+ SDK public APIs can be found in the [documentation](https://github.com/Nets-mobile-acceptance/Netaxept-Android-SDK/tree/master/documentation)
+ Android SDK minimum supported API version is 21
+ The **PiA - Netaxept Android SDK** is available through both `jcenter()` and `mavenCentral()` repositories
+ Basic knowledge of Native Android languages (Java/Kotlin)

We have provided a `PiaSampleFlutter` application which integrates the `PiASDK` native library and uses a sample [Platform Specific Code](https://flutter.dev/docs/development/platform-integration/platform-channels) between _dart_ and Java code.

## Step-by-step instructions
Assuming that you have your Flutter application structure ready, here are the things you need to consider:
1. Open an instance of _Android Studio_ and open the _android_ folder inside the Flutter app
    +  Add dependency for **PiaSDK**: in `build.gradle` application level file, add:
        ```gradle
implementation('eu.nets.pia:pia-sdk:1.6.0') { transitive = true; }
        ```
    + HTTP requests needs to be done from Native code (at least the RegisterPayment synchronous call). For this you may need to add dependency for a newtorking library (e.g. [Retrofit](https://square.github.io/retrofit/))
    + Open _MainActivity.java_ and add Java/Kotlin code to handle SDK communication (launch, registerPayment, handle result). Please check the official [documentation](https://github.com/Nets-mobile-acceptance/Netaxept-Android-SDK/tree/master/documentation) on how to implement this.
    + Inside the _MainActivity_ you need to configure a **MethodChannel** to listen for method calls from your `dart` code. 
    ```java
            //register a MethodChannel to listen for method calls from dart code
            new MethodChannel(getFlutterView(), PAY_NEW_CARD_CHANNEL).setMethodCallHandler(
                    (call, result) -> {
                        this.paymentCallback = result;
                        //call specific method; you can handle all cases here: new card, saved card, paypal, etc
                        switch (call.method) {
                            case "payWithNewCard":
                                payWithNewCard();
                                break;
                            case "payWithPayPal":
                                payWithPayPal();
                                break;
                        }
                    });
    ```
2. Open an instance of Android Studio and open the Flutter app root folder
    + Upon user interraction, you need to invoke a method on the method channel, specifying the concrete method to call via the String identifier:
    ```dart
        void _launchSDK(String method) async {
            try {
              //call method from MainActivity to launch SDK
              //the result will be delivered after the payment process is finished
              String result = await platform.invokeMethod(method);
              //handle your result
            } on PlatformException catch (e) {
              //method cannot be invoked; handle exception
            }
        }
        
        ...
        onPressed:() {_launchSDK('payWithNewCard');},
        ...
    ```
    + Ignore all missing imports from the _MainActivity_ class. The external dependencies are still included in your project, but they are not visible from the Flutter application.
    
### Native Module inside Flutter application

In order to link the Flutter app with the Native PiA SDK library, the communication between them needs to be made in native code. Here are some hints on how to do it:

+ Register a MethodChannel to listen for any method calls invoked from `dart` code through a channel ID. Based on the method name String, you can call several methods: pay with new card, pay with saved card, pay with PayPal. Here you need to store in a local variable the `MethodChannel.Result` object to be used later.
+ Registering a payment needs to be done synchronously when the RegisterPaymentHandler is called. The order details to be used in this register API call can be provided by your `dart` code as parameters when the specific method is invoked through MethodChannel.  
+ Handling the result will be made in the `onActivityResult` of the MainActivity class. You can deliver the result to your `dart` code through the local saved variable `MethodChannel.Result`.
+ Don't forget to clear all local variables after the payment process is completed. 

## Example

Please check our sample implementation of the [Flutter application](PiaSampleFlutter) including `PiaSDK` native library. For any questions, please don't hesitate to contact us.

**How to run the sample project**:
+ Open the **PiaSampleFlutter** in Android Studio
+ **Get dependencies** if the popup "Pubspec file has been edited" will appear
+ Open **PiaSampleFlutter/android** folder in a separate instance of Android Studio and *Build* the project
+ Run the **PiaSampleFlutter** application