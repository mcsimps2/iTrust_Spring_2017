Feature: view patient's obstetrics reports
	As an HCP
	I want to be able to view obstetrics reports for a patient

Background:
	Given I am at the iTrust login screen
	Given the databases have been reset

Scenario: view report and go back
	And I have logged in as HCP 9000000012 with password pw
	And I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name Random
	And click on the link for patient with pid 1
	And I click Generate Report on the first entry
	And I click Done
	Then I am redirected to obstetrics records page

Scenario: generate report from new obstetrics record
	And I have logged in as HCP 9000000012 with password pw
	And I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name Random
	And click on the link for patient with pid 1
	When I click Add New Record
	And I enter an lmp in for the LMP field
	And I click Save Record
	And I click Generate Report on the first entry
	Then the report displays correctly