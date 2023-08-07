# PiA - Netaxept Android SDK v2.7.2
----
![Logo](readme-files/NetsLogo.jpg)

PiA Netaxept Android SDK is a library that provides the native In-App interaction of performing the Netaxept payment directly from an app on the Android device and minimizes PCI DSS requirements for you.

**PiA - Netaxept iOS SDK** can also be found [here](https://github.com/Nets-mobile-acceptance/Netaxept-iOS-SDK)

Detailed documentation can be found [here](https://htmlpreview.github.io/?https://github.com/Nets-mobile-acceptance/Netaxept-Android-SDK/blob/master/documentation/START%20-%20Overview%20of%20Netaxept%20Android%20SDK.html)

| ![](readme-files/demo_pay_with_new_card.gif)  | ![](readme-files/demo_pay_with_saved_card.gif) |
| --- | --- |


# Installation
----
In your `build.gradle` application level file, add:
```gradle
implementation('eu.nets.pia:pia-sdk:2.7.2') { transitive = true; changing=true; }
```

**Important:** for the release version of your _.apk_, add the following rules in your application's `proguard-rules.pro` file:
```pro
# Rules required by Card.Io library
-keep class eu.nets.pia.cardio.** { *; }
-dontwarn eu.nets.pia.cardio.**
```

**Note:** the library is available through both `jcenter()` and `mavenCentral()` repositories.

# Frequently Asked Questions
---
If you encounter any blockers when integrating the `Netaxept - Android SDK`, feel free to check the [Frequently Asked Questions](FAQs.md) page. If any of these answers does not fit your question, don't hesitate to [contact us](#contact).


# Requirements
----
**Minimum supported Android version is 5.0** - Due to PCI DSS requirements and known vulnerabilities in secure protocols less than TLS 1.2, TLS 1.2 is the only secure protocol available when connecting towards Netaxept. For this reason, we don't support the Android versions lower than 5.0 which are NOT enabled/supported TLS 1.2 by default. For more information, you can check the details from Netaxept.


# Permissions
----
These permissions are handled inside the binary, and your integration won't require any additional changes.

**PiA SDK** will require the internet permissions to be fully operational.

```xml
<uses-permission android:name="android.permission.INTERNET" />
```

**CardIo** library integrated by PiA SDK will also require permission for Camera and Vibrate.

```xml
 <uses-permission android:name="android.permission.CAMERA" />
 <uses-permission android:name="android.permission.VIBRATE" />
```

# Examples
----
![](readme-files/sample_screenshots.png)

We have provided a [Sample Application](PiaSample/) to help you understand the use cases of the SDK functionalities. All you need to do is to setup your Back-End solution with Netaxept ([see more](https://github.com/Nets-mobile-acceptance/Netaxept-Sample-Backend)), get the source code (check [ReadMe](PiaSample/ReadMe.md) on how to do basic setup) and then run it.


# Project Status
---
Supported payment methods:
- Cards: Visa, Mastercard, American Express, Diners, JCB, Maestro, Dankort, Forbrugsforeningen
- S-Business
- PayPal
- Vipps
- Swish
- Paytrail direct bank payments
- MobilePay

# Automation
---
All automated UI tests will be added in /automation directory at project root. 
Automation is done with Robot framework and Appium

Automation follows Page Object Pattern meanning all steps/keywords and UI element queries/locators will be defined around app screens in automation/Pages dir.

### Install dependencies:
- Android Studio (2022.3.1) - download from [official website](https://developer.android.com/studio)
- Brew - package manager for MacOS [official site](https://brew.sh)
- Python 3.11.4_1 - install with `brew install python`
- Robot framework (6.1.1) - install with `pip3 install robotframework`
- Robot-appium client - install with `pip3 install robotframework-appiumlibrary`
- Appium Server (2.0.1) 
    - install npm: `brew install npm`
    - install Appium: `npm i --location=global appium`
- Appium Inspector (2023.7.1) - install from [github page](https://github.com/appium/appium-inspector/releases)

### Run Automation:
```bash
cd automation # make sure you are in PROJECT_ROOT/automation directory
appium --base-path /wd/hub > output.txt & # run Appium server in separate process and move output to txt file (or you could run it in sepate terminal)
robot test_file.robot
```

# Contact
----
If you have any question or feedback, please contact us via email: [in-app-support@nets.eu](mailto:in-app-support@nets.eu)



# License
----

Please check [License file](PiA-Netaxept-SDK-License.md).
