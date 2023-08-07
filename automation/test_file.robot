*** Settings ***
Documentation  Login with userID 101, start and abort changing userID process, change userID to 202
Library  AppiumLibrary
Resource  Pages/AppPage.robot
Resource  Pages/LoginPage.robot
Resource  Pages/HomePage.robot
Resource  Pages/SettingsPage.robot

*** Test Cases ***
Should send keys to search box and then check the value
  [Documentation]  You need to speficy initial 6-digit user id and new user id (also 6-digit) to run this test.
  Open Test Application
  Login as user with id:  ${INITIAL_ID}
  Open Settings View for userID:  ${INITIAL_ID}
  Open Change UserID view
  Cancel Changing UserID action, so userID is still:  ${INITIAL_ID}
  Change UserID to:  ${NEW_ID}
  Verify that UserID has beed changed to:  ${NEW_ID}