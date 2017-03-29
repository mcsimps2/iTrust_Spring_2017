Feature: Add obstetrics records to office visit
	As an HCP
	I want to be able to add obstetrics data to an office visit for a patient

Background:
	Given the databases have been reset
	Given I am at the iTrust login screen
	And I have logged in as OBGYN with MID 9000000012 and password pw

Scenario: obstetrics and ultrasound tabs exist
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	Then the obstetrics tab is there
	And the ultrasound tab is there

Scenario: add obstetrics data to office visit
	Then this scenario is not implemented yet

Scenario: add ultrasound with images
	Then this scenario is not implemented yet

Scenario: add ultrasound without images
	Then this scenario is not implemented yet

Scenario: schedule appointment
	Then this scenario is not implemented yet

Scenario: non OBGYN HCP
	Then this scenario is not implemented yet

Scenario: bad obstetrics input
	Then this scenario is not implemented yet

Scenario: bad ultrasound input
	Then this scenario is not implemented yet

Scenario: add ultrasound before office visit info
	Then this scenario is not implemented yet