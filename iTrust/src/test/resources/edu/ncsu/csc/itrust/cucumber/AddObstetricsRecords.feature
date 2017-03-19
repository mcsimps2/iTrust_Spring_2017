Feature: Add Patient's Obstetrics Records
	As an HCP
	I want to be able to add an obstetrics initialization record for a patient
	
Background:
	Given I am at the iTrust login screen
	And I have logged in with MID 9000000000 and password pw
	
Scenario Outline: Add Obstetrics Record
	Given I am viewing obstetrics records for patient with name <name>
	When I click Add New Record
	And I enter the value <lmp> in for the LMP field
	And I enter a prior pregnancy with values: conception year <cYear>, weeks pregnant <weeksPreg>, hours in labor <hrsLabor>, weight gain <weightGain>, delivery type <deliveryType>, multiplicity <mult>
	And I click add pregnancy
	And I click Save Record
	Then I am redirected to obstetrics records page
	And a success message appears indicating save successful
Examples:
	| name | lmp | cYear | weeksPreg | hrsLabor | weightGain | deliveryType | mult |
	| Random | TODO
	