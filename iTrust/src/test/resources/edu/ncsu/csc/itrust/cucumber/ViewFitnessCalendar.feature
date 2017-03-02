Feature: View Fitness Data Calendar
	As a HCP
	I want to be able to enter the MID of a patient or their name
	So I can bring up their fitness calendar and data
	
Background:
	Given I am at the iTrust login screen

Scenario Outline: Search by Name
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Patient Fitness Data
	When I search for the patient with name <patientName>
	And click on their link
	Then their fitness calendar comes up
Examples:
	| hcpMID | pw | patientName |
	| 9000000000 | pw | Random |
	| 9000000003 | pw |  Zappic |
	| 9000000007 | pw | Princess |
	
Scenario Outline: Check for Add Data Links in Calendar
	Given I have logged in as HCP 9000000003 with password pw
	Given they have no fitness data for day <day>
	Given I have pulled up Patient 1's fitness calendar
	When I find that day in their calendar
	Then it has an Add Data link
Examples:
	| day |
	| 1 |
	| 5 |
	| 10 |
	| 28 |
	
Scenario Outline: Check for Edit/View Data Links in Calendar
	Given they have fitness data for day <day>
	Given I have logged in as HCP 9000000003 with password pw
	Given I have pulled up Patient 1's fitness calendar
	When I find that day in their calendar
	Then it has a View/Edit Data link
Examples:
	| day |
	| 2 |
	| 15 |
	| 27 |
		