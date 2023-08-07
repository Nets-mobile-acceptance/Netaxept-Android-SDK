*** Settings ***
Resource  HomePage.robot

*** Variables ***
${SETTINGS_CUSTOMER_ID}       id=eu.nets.pia.sample:id/customer_id_label
${SETTINGS_CHANGE_ID}         id=eu.nets.pia.sample:id/change_customer_id
${SETTINGS_CHANGE_ID_ALERT}   id=eu.nets.pia.sample:id/alertTitle
${SETTINGS_CHANGE_ID_CANCEL}  id=android:id/button2
${SETTINGS_CHANGE_ID_SUBMIT}  id=android:id/button1
${SETTINGS_CHANGE_ID_INPUT}   class=android.widget.EditText

*** Keywords ***
Open Settings View for userID: 
  [Arguments]  ${text}
  Click Element  ${HOMEPAGE_SETTINGS_BUTTON}
  Wait Until Page Contains Element  ${SETTINGS_CUSTOMER_ID}
  Element Text Should Be  ${SETTINGS_CUSTOMER_ID}  ${text}

Verify that UserID has beed changed to:
  [Arguments]  ${text}
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