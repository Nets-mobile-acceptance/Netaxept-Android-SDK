*** Settings ***
Documentation  Login with userID 101, start and abort changing userID process, change userID to 202
Library  AppiumLibrary

*** Variables ***
${ANDROID_AUTOMATION_NAME}    UIAutomator2
${ANDROID_APP}                ${CURDIR}/../apk/pia-sdk-android-release-sample-2.7.2.apk
${ANDROID_PLATFORM_NAME}      Android
${ANDROID_PLATFORM_VERSION}   %{ANDROID_PLATFORM_VERSION=13}
${LOGIN_INPUT}                id=eu.nets.pia.sample:id/customer_id_et
${LOGIN_BUTTON}               id=eu.nets.pia.sample:id/sign_up_btn
${HOMEPAGE_PRODUCT_IMAGE}     id=eu.nets.pia.sample:id/products_imgv
${HOMEPAGE_SETTINGS_BUTTON}   id=eu.nets.pia.sample:id/settings_item
${SETTINGS_CUSTOMER_ID}       id=eu.nets.pia.sample:id/customer_id_label
${SETTINGS_CHANGE_ID}         id=eu.nets.pia.sample:id/change_customer_id
${SETTINGS_CHANGE_ID_ALERT}   id=eu.nets.pia.sample:id/alertTitle
${SETTINGS_CHANGE_ID_CANCEL}  id=android:id/button2
${SETTINGS_CHANGE_ID_SUBMIT}  id=android:id/button1
${SETTINGS_CHANGE_ID_INPUT}   class=android.widget.EditText

*** Test Cases ***
Should send keys to search box and then check the value
  Open Test Application
  Login as user with id:  000101
  Open Settings View for userID:  000101
  Open Change UserID view
  Cancel Changing UserID action, so userID is still:  000101
  Change UserID to:  000202

*** Keywords ***
Open Test Application
  Open Application  http://127.0.0.1:4723/wd/hub  automationName=${ANDROID_AUTOMATION_NAME}
  ...  platformName=${ANDROID_PLATFORM_NAME}  platformVersion=${ANDROID_PLATFORM_VERSION}
  ...  app=${ANDROID_APP}  appPackage=eu.nets.pia.sample  appActivity=.ui.activity.main.MainActivity

Login as user with id:
  [Arguments]  ${query}
  Input Text  ${LOGIN_INPUT}   ${query}
  Click Element  ${LOGIN_BUTTON}
  Wait Until Page Contains Element  ${HOMEPAGE_PRODUCT_IMAGE}

Open Settings View for userID: 
  [Arguments]  ${text}
  Click Element  ${HOMEPAGE_SETTINGS_BUTTON}
  Wait Until Page Contains Element  ${SETTINGS_CUSTOMER_ID}
  Element Text Should Be  ${SETTINGS_CUSTOMER_ID}  ${text}

Open Change UserID view
  Click Element  ${SETTINGS_CHANGE_ID}
  Wait Until Page Contains Element  ${SETTINGS_CHANGE_ID_ALERT}

Cancel Changing UserID action, so userID is still: 
  [Arguments]  ${text}
  Click Element  id=android:id/button2
  Wait Until Page Contains Element  ${SETTINGS_CUSTOMER_ID}
  Element Text Should Be  ${SETTINGS_CUSTOMER_ID}  ${text}

Change UserID to:
  [Arguments]  ${text}
  Click Element  ${SETTINGS_CHANGE_ID}
  Input Text  ${SETTINGS_CHANGE_ID_INPUT}   ${text}
  Click Element  ${SETTINGS_CHANGE_ID_SUBMIT}
  Wait Until Page Contains Element  ${SETTINGS_CUSTOMER_ID}
  Element Text Should Be  ${SETTINGS_CUSTOMER_ID}  ${text}
