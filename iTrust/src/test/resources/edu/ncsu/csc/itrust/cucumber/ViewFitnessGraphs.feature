Feature: View Fitness Graphs
	As an HCP
	I want to be able to create summary reports for a patient’s fitness data
	So I can visualize their daily step counts, daily delta step counts, and weekly step count averages

Background:
	Given I am at the iTrust login screen
	And I have logged in with MID 9000000000 and password pw
	And I have pulled up the patient calendar for patient with name Random

Scenario: View Summary Reports
	When I click the button to view summary reports
	And I enter start date 2017-02-02 and end date 2017-03-25
	And I click generate
	Then I can see three graphs of daily step counts, daily deltas, and weekly average step counts

Scenario: Change Dates in Summary Reports
	When I click the button to view summary reports
	And I change the default dates to be the past month
	And I click generate
	Then the graphs are changed to reflect the past month
	
Scenario Outline: Use Invalid Dates in Summary Reports
	When I click the button to view summary reports
	And I change the defaults dates to start on <day1> and end on <day2>
	And I click generate
	Then I get an error message about invalid dates
Examples:
	| day1 | day2 |
	| 5 | 1 |
	| 28 | 20|
	| 8 | 2 |