#Author: nranthon

Feature: Messaging Inbox
As a user
I want the inbox to properly mark read messages as read

Scenario: Marking as read
	Given I am logged in as HCP 1
	When I open the messaging tab and click on the messaging inbox
	And I click on an unread message
	And I press the back button on the web browser
	Then the previously opened message will be marked as read

