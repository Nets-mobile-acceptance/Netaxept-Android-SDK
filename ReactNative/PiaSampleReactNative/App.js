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

import React, { Component } from 'react';
import { Platform, StyleSheet, Text, View, Button, ToastAndroid } from 'react-native';
import { NativeModules } from 'react-native';

const piaSDKModule = NativeModules.PiaSDK;

const netsProduction = {

   static let backendUrlProd: String = "YOUR PRODUCTION BACKEND BASE URL HERE"
   static let merchantIdProd: String = "YOUR PRODUCTION NETAXEPT MERCHANT ID HERE"

};

const netsTest = {

      static let backendUrlTest: String = "YOUR TEST BACKEND BASE URL HERE"
      static let merchantIdTest: String = "YOUR TEST NETAXEPT MERCHANT ID HERE"
      static let tokenIdTest: String = "YOUR TEST TOKED ID"
      static let schemeIdTest: String = "YOUR TEST CARD PROVIDER NAME"
      static let expiryDateTest: String = "YOUR TEST CARD EXPIRY DATE"

};


type Props = {};
export default class App extends Component<Props> {
  render() {

    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>Welcome to Pia Sample app React Native!</Text>
        <Text style={styles.instructions}>Check our basic implementation here!</Text>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.pay} title="Buy" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.startCardPaymentWithOnlyVisa} title="Start CardPayment With Only Visa" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.sBusiness} title="S-Business" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.saveCard} title="Save Card" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.payViaPaypal} title="Paypal" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.payViaMobilePay} title="MobilePay Payment" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.paySavedCardWithSkipConfirm} title="Pay 10 EUR - Saved Card (Skip Confirmation)" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.paySavedCardWithoutSkipConfirm} title="Pay 10 EUR - Saved Card" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.payViaPaytrailNordea} title="Paytrail Nordea" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.paySavedCardWithoutSkipConfirmCustomImage} title="Pay 10 EUR - Saved Card (Custom Image)" />
        </View>
        <View style={styles.button}>
          <Button style={styles.button} onPress={this.paySavedCardWithSkipConfirmCustomImage} title="Pay 10 EUR - Saved Card (Skip Confirmation custom Image)" />
        </View>
      </View>
    );
  }

  pay = () => {
    //for pay with new card, set only the MechantInfo and Order info objects
    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/true);
    piaSDKModule.buildOrderInfo(1, "EUR");

    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.start((saveCardBool) => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"storeCard": ' + saveCardBool + ',"orderNumber": "PiaSDK-Android","customerId": "000003","amount": {"currencyCode": "EUR", "totalAmount": "100","vatAmount": 0}}'
      }).then((response) => response.json())
        .then((responseJson) => {
          console.log('onResponse' + responseJson.transactionId)
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  sBusiness = () => {
    //for pay with new card, set only the MechantInfo and Order info objects
    piaSDKModule.buildMerchantInfo(netsTest.sBusinessMerchantIdTest,  /*isTestMode*/true, /*isCvcRequired*/true);
    piaSDKModule.buildOrderInfo(1, "EUR");

    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.startSBusinessCard((saveCardBool) => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.sBusinessMerchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"storeCard": ' + saveCardBool + ',"orderNumber": "PiaSDK-Android","customerId": "000003","amount": {"currencyCode": "EUR", "totalAmount": "100","vatAmount": 0}}'
      }).then((response) => response.json())
        .then((responseJson) => {
          console.log('onResponse' + responseJson.transactionId)
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }



  saveCard = () => {
    //for save card only MerchantInfo object is required
    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/true);


    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });


    piaSDKModule.saveCard(() => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"storeCard": true,"orderNumber": "PiaSDK-Android","customerId": "000003","amount": {"currencyCode": "EUR", "totalAmount": "1","vatAmount": 0}}'
      }).then((response) => response.json())
        .then((responseJson) => {
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  payViaPaypal = () => {
    //for PayPal set only the MerchantInfo object
    piaSDKModule.buildMerchantInfo(netsProduction.merchantIdProd, /*isTestMode*/false, /*isCvcRequired*/true);


    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.startPayPalProcess(() => {
      fetch(netsProduction.backendUrlProd + "v2/payment/" + netsProduction.merchantIdProd + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"storeCard": true,"orderNumber": "PiaSDK-Android","customerId": "000003","amount": {"currencyCode": "DKK", "totalAmount": "100","vatAmount": 0}, "method": {"id":"PayPal"}}'
      }).then((response) => response.json())
        .then((responseJson) => {
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  payViaMobilePay = () => {

    let walletType = "MobilePay"

    //Set wallet type before initiating the wallet payment using startWalletPayment
    piaSDKModule.setWalletType(walletType);


    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('Interruption', ToastAndroid.SHORT);
      piaSDKModule.handleSDKResult().then((result) => {
        ToastAndroid.show(result, ToastAndroid.SHORT);
      }).catch((error) => {
        ToastAndroid.show(error, ToastAndroid.SHORT);
      });
    });



    //Same api for initiating all wallet payments
    piaSDKModule.startWalletPayment(() => {

      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"amount":{"currencyCode":"EUR","totalAmount":1000,"vatAmount":0},"customerId":"000013","method":{"id":"MobilePay"},"orderNumber":"PiaSDK-Android","paymentMethodActionList":"[{PaymentMethod:MobilePay}]","redirectUrl":"com.piasample://piasdk_mobilepay","storeCard":false}'

      }).then((response) => response.json())
        .then((responseJson) => {
          piaSDKModule.openWalletApp(responseJson.walletUrl);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.openWalletApp(null);
        });
    });
  }

  paySavedCardWithSkipConfirm = () => {
    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/false);

    piaSDKModule.buildOrderInfo(1, "EUR");
    piaSDKModule.buildTokenCardInfo(netsTest.tokenIdTest, netsTest.schemeIdTest, netsTest.expiryDateTest, false);
    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.startSkipConfirmation(() => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"customerId":"000012","orderNumber":"PiaSDK-Android","amount": {"currencyCode": "EUR", "vatAmount":0, "totalAmount":"1000"},"method": {"id":"EasyPayment","displayName":"","fee":""},"cardId":"492500******0004","storeCard": true,"merchantId":"","token":"","serviceTyp":"","paymentMethodActionList":"","phoneNumber":"","currencyCode":"","redirectUrl":"","language":""}'

      }).then((response) => response.json())
        .then((responseJson) => {
          console.log('onResponse: ' + responseJson)
          console.log('onResponse' + responseJson.transactionId)
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  paySavedCardWithoutSkipConfirm = () => {

    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/true);

    piaSDKModule.buildOrderInfo(10, "EUR");
    piaSDKModule.buildTokenCardInfo(netsTest.tokenIdTest, netsTest.schemeIdTest, netsTest.expiryDateTest, true);
    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.startTokenPayment(() => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"customerId":"000012","orderNumber":"PiaSDK-Android","amount": {"currencyCode": "EUR", "vatAmount":0, "totalAmount":"1000"},"method": {"id":"EasyPayment","displayName":"","fee":""},"cardId":"492500******0004","storeCard": true,"merchantId":"","token":"","serviceTyp":"","paymentMethodActionList":"","phoneNumber":"","currencyCode":"","redirectUrl":"","language":""}'
      }).then((response) => response.json())
        .then((responseJson) => {
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  paySavedCardWithoutSkipConfirmCustomImage = () => {

    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/true);

    piaSDKModule.buildOrderInfo(10, "EUR");
    piaSDKModule.buildTokenCardInfo(netsTest.tokenIdTest, netsTest.schemeIdTest, netsTest.expiryDateTest, true);
    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.startTokenPaymentCustomImage(() => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"customerId":"000012","orderNumber":"PiaSDK-Android","amount": {"currencyCode": "EUR", "vatAmount":0, "totalAmount":"1000"},"method": {"id":"EasyPayment","displayName":"","fee":""},"cardId":"492500******0004","storeCard": true,"merchantId":"","token":"","serviceTyp":"","paymentMethodActionList":"","phoneNumber":"","currencyCode":"","redirectUrl":"","language":""}'
      }).then((response) => response.json())
        .then((responseJson) => {
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  paySavedCardWithSkipConfirmCustomImage = () => {
    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/false);

    piaSDKModule.buildOrderInfo(1, "EUR");
    piaSDKModule.buildTokenCardInfo(netsTest.tokenIdTest, netsTest.schemeIdTest, netsTest.expiryDateTest, false);
    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.startSkipConfirmationCustomImage(() => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"customerId":"000012","orderNumber":"PiaSDK-Android","amount": {"currencyCode": "EUR", "vatAmount":0, "totalAmount":"1000"},"method": {"id":"EasyPayment","displayName":"","fee":""},"cardId":"492500******0004","storeCard": true,"merchantId":"","token":"","serviceTyp":"","paymentMethodActionList":"","phoneNumber":"","currencyCode":"","redirectUrl":"","language":""}'

      }).then((response) => response.json())
        .then((responseJson) => {
          console.log('onResponse: ' + responseJson)
          console.log('onResponse' + responseJson.transactionId)
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  payViaPaytrailNordea = () => {
    piaSDKModule.buildOrderInfo(10, "EUR");
    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/false);


    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    var orderId = this.getOrderId();
    console.log('orderId ' + orderId);

    piaSDKModule.startPaytrailProcess(() => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"amount":{"currencyCode":"EUR","totalAmount":1000,"vatAmount":0},"customerTown":"Helsinki","customerPostCode":"00510","customerLastName":"Buyer","customerId":"000012","customerAddress1":"Testaddress","customerCountry":"FI","customerEmail":"bill.buyer@nets.eu","customerFirstName":"Bill","customerId":"000013","method":{"id":"PaytrailNordea"},"orderNumber":' + orderId + ',"storeCard":false}'
      }).then((response) => response.json())
        .then((responseJson) => {
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });
  }

  startCardPaymentWithOnlyVisa = () => {

    piaSDKModule.buildMerchantInfo(netsTest.merchantIdTest, /*isTestMode*/true, /*isCvcRequired*/true);
    piaSDKModule.buildOrderInfo(1, "EUR");

    //set the payment result promise
    piaSDKModule.handleSDKResult().then((result) => {
      ToastAndroid.show(result, ToastAndroid.SHORT);
    }).catch((error) => {
      ToastAndroid.show('CANCEL OR ERROR', ToastAndroid.SHORT);
    });

    piaSDKModule.startCardPaymentWithOnlyVisa((saveCardBool) => {
      fetch(netsTest.backendUrlTest + "v2/payment/" + netsTest.merchantIdTest + "/register", {
        method: 'POST',
        headers: {
          'Accept': 'application/json;charset=utf-8;version=2.0',
          'Content-Type': 'application/json;charset=utf-8;version=2.0'
        },
        body: '{"storeCard": ' + saveCardBool + ',"orderNumber": "PiaSDK-Android","customerId": "000003","amount": {"currencyCode": "EUR", "totalAmount": "100","vatAmount": 0}}'
      }).then((response) => response.json())
        .then((responseJson) => {
          console.log('onResponse' + responseJson.transactionId)
          piaSDKModule.buildTransactionInfo(responseJson.transactionId, responseJson.redirectOK, null);
        })
        .catch((error) => {
          console.error(error);
          piaSDKModule.buildTransactionInfo(null, null, null);
        });
    });

  }

  getOrderId() {
    var checkDigit = -1;
    var multipliers = [7, 3, 1];
    var multiplierIndex = 0;
    var sum = 0;

    // Storing random positive integers in an array. '1' is appended in the beginning of the
    // order number in order to differentiate between Android and iOS (0 for iOS and 1 for Android)
    var ds = (new Date()).toISOString().replace(/[^0-9]/g, "")
    console.log('dateTime ' + ds);

    var orderNumber = "1" + ds;

    //Sum of the product of each element of randomNumber and multipliers in right to left manner
    for (var i = orderNumber.length - 1; i >= 0; i--) {
      if (multiplierIndex == 3) {
        multiplierIndex = 0;
      }
      var value = orderNumber.charAt(i);
      sum += value * multipliers[multiplierIndex];
      multiplierIndex++;
    }

    //The sum is then subtracted from the next highest ten
    checkDigit = 10 - sum % 10;

    if (checkDigit == 10) {
      checkDigit = 0;
    }
    return orderNumber + checkDigit;
  }

}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    marginTop: 5,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
  button: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 10,
    marginTop: 10
  }
});
