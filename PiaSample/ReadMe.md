## Required Maven URLs and Credentials

Please add to the **gradle.properties** file in PROJECT level the values for the following variables as provided by Nets: 

```
MAVEN_URL_PIA=provided value for URL to PIA SDK

MAVEN_REPO_USERNAME=providedUsername
MAVEN_REPO_PASSWORD=providedPassword

```
## Required Merchant BackEnd BaseURL and Merchant ID

Please add to the **gradle.properties** file in APP level the values for the following variables as provided by Nets: 

```

#test merchant ID
MERCHANT_ID_TEST= "YOUR TEST NETAXEPT MERCHANT ID HERE"
MERCHANT_BACKEND_URL_TEST="https://yourtestbackendbaseurlhere/"
#prod merchant ID
MERCHANT_ID_PROD="YOUR PRODUCTION NETAXEPT MERCHANT ID HERE"
MERCHANT_BACKEND_URL_PROD="https://yourproductionbackendbaseurlhere/"

```
