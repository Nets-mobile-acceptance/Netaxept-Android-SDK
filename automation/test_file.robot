*** Settings ***
Documentation  Login with userID 101, start and abort changing userID process, change userID to 202
Library  AppiumLibrary
Resource  Pages/AppPage.robot
Resource  Pages/LoginPage.robot
Resource  Pages/HomePage.robot
Resource  Pages/SettingsPage.robot

*** Test Cases ***
Should send keys to search box and then check the value
  Open Test Application
  Login as user with id:  000101
  Open Settings View for userID:  000101
  Open Change UserID view
  Cancel Changing UserID action, so userID is still:  000101
  Change UserID to:  000202
  Verify that UserID has beed changed to:  000202