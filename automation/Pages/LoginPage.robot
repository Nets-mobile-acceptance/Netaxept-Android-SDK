*** Settings ***
Resource  AppPage.robot
Resource  HomePage.robot
Resource  SettingsPage.robot

*** Variables ***
${LOGIN_INPUT}                id=eu.nets.pia.sample:id/customer_id_et
${LOGIN_BUTTON}               id=eu.nets.pia.sample:id/sign_up_btn

*** Keywords ***
Login as user with id:
  [Arguments]  ${query}
  Input Text  ${LOGIN_INPUT}   ${query}
  Click Element  ${LOGIN_BUTTON}
  Wait Until Page Contains Element  ${HOMEPAGE_PRODUCT_IMAGE}