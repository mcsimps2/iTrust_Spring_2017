Feature: View Patient's Fitness Data
	As an HCP
	I want to be able to select a date on a patient’s fitness calendar
	So I can view a patient’s fitness data for that day

Background:
	Given I am at the iTrust login screen
	And I have logged in with MID 9000000000 and password pw
	And I have pulled up Patient 1's fitness calendar

Scenario Outline: Viewing Patient Data for a Date of the Current Month
	Given the patient has populated fitness data for the current month day <dayOfTheMonth>
	When I click on the day <dayOfTheMonth>
	Then I can see the patient’s fitness data for that date

Examples:
	| dayOfTheMonth |
	| 3 |
	| 8 |
	| 12 |
	| 25 |

Scenario Outline: Viewing Patient Data for a Date from Last Month
	Given the patient has populated fitness data for last month during the day <dayOfTheMonth>
	When I click on the previous button to go to the previous month
	And I click on the previous month day <dayOfTheMonth>
	Then I can see the patient’s fitness data for that date
Examples:
	| dayOfTheMonth |
	| 15 |
	| 2 |
	| 18 |