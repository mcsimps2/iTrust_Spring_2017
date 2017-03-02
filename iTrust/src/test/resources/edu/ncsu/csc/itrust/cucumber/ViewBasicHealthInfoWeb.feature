Feature: View Basic Health Information
As an HCP
I want to view past basic health information

Scenario: Test the display of basic health info
	Given user is on the login screen
	When user enters 9000000000 in the MID field and pw in the Password field and clicks login
	And user clicks Patient Info then Basic Health Information
	And user enters 1 in the search field
	And user clicks on the first patient shown
	
	Then the basic health info page is displayed