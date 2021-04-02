package com.piasample;

import android.util.Log;

import com.facebook.react.ReactActivity;

import androidx.activity.result.ActivityResultLauncher;
import eu.nets.pia.ProcessResult;
import eu.nets.pia.card.CardProcessActivityLauncherInput;
import eu.nets.pia.card.CardProcessActivityResultContract;
import eu.nets.pia.card.PayPalActivityLauncherInput;
import eu.nets.pia.card.PayPalActivityResultContract;
import eu.nets.pia.card.PaytrailActivityLauncherInput;
import eu.nets.pia.card.PaytrailActivityResultContract;
import eu.nets.pia.wallets.MobileWallet;
import eu.nets.pia.wallets.MobileWalletError;
import eu.nets.pia.wallets.MobileWalletListener;

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

public class MainActivity extends ReactActivity implements MobileWalletListener {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "PiaSample";
    }

    ActivityResultLauncher<PayPalActivityLauncherInput> payPalActivityLauncher = registerForActivityResult(
            new PayPalActivityResultContract(),
            this::transactionCompleteResult
    );

    ActivityResultLauncher<PaytrailActivityLauncherInput> paytrailActivityLauncher = registerForActivityResult(
            new PaytrailActivityResultContract(),
            this::transactionCompleteResult
    );

    ActivityResultLauncher<CardProcessActivityLauncherInput> cardPaymentActivityLauncher = registerForActivityResult(
            new CardProcessActivityResultContract(),
            this::transactionCompleteResult
    );

    /* Here in the overridden methods we need to access the SDKModule to pass the result back to App.js*/
 @Override
    public void onMobileWalletAppSwitchFailure(MobileWallet mobileWallet, MobileWalletError mobileWalletError) {
        RNSDKPackage reactPackage = (RNSDKPackage) this.getReactNativeHost().getReactInstanceManager().getPackages().get(2);
        reactPackage.sdkModule.returnSuccessfulRedirectResult("ERROR");
    }

    @Override
    public void onMobileWalletRedirect(MobileWallet mobileWallet) {
        RNSDKPackage reactPackage = (RNSDKPackage) this.getReactNativeHost().getReactInstanceManager().getPackages().get(2);
        reactPackage.sdkModule.returnSuccessfulRedirectResult("SUCCESS");
    }

    @Override
    public void onMobileWalletRedirectInterrupted(MobileWallet mobileWallet) {
        RNSDKPackage reactPackage = (RNSDKPackage) this.getReactNativeHost().getReactInstanceManager().getPackages().get(2);
        reactPackage.sdkModule.returnInterruption("INTERRUPTED");
    }

    void transactionCompleteResult(ProcessResult result) {
        RNSDKPackage reactPackage = (RNSDKPackage) this.getReactNativeHost().getReactInstanceManager().getPackages().get(2);

        if (result instanceof ProcessResult.Success) {

            /*
             *Transaction ID from SDK for rollback call
             */
            String transactionId = ((ProcessResult.Success) result).getTransactionID();

            reactPackage.sdkModule.returnSuccess();

        } else if (result instanceof ProcessResult.Failure) {
            /*
             *Failure message to be displayed in Confirmation page
             */
            String message = ((ProcessResult.Failure) result).getProcessError().toString();

            reactPackage.sdkModule.returnFailure(message);


        } else if (result instanceof ProcessResult.Cancellation) {

            ProcessResult.Cancellation cancellation = (ProcessResult.Cancellation) result;
            /*
             *Cancellation message to be displayed in Confirmation page
             */
            String message =  cancellation.getReason();
            /*
             *Transaction ID from SDK for rollback call
             */
            String transactionId = cancellation.getTransactionID();

            reactPackage.sdkModule.returnCancellation(message);


        }
    }




}
