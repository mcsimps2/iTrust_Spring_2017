#Author: nranthon

Feature: Personnel Record
As a user
I want to be able to edit my records

Scenario: Changing information
	Given That I am logged in as HCP 1
	When I open the personal info tab and click on the my demographics link
	And I edit the last name field
	And I press the edit personnel records button
	And I got to the home page
	Then my last name should be properly changed

