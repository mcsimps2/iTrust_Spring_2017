Feature: Add Patient's Obstetrics Records
	As an HCP
	I want to be able to add an obstetrics initialization record for a patient
	
Background:
	Given I am at the iTrust login screen
	And I have logged in with MID 9000000012 and password pw
	
Scenario Outline: Add Obstetrics Record
	Given I am viewing obstetrics records for patient with name <name>
	When I click Add New Record
	And I enters an lmp in for the LMP field
	And I enter a prior pregnancy with values: conception year <cYear>, weeks pregnant <weeksPreg>, hours in labor <hrsLabor>, weight gain <weightGain>, delivery type <deliveryType>, multiplicity <mult>
	And I click add pregnancy
	And I click Save Record
	Then I am redirected to obstetrics records page
	And a success message appears indicating save successful
Examples:
	| name | cYear | weeksPreg | hrsLabor | weightGain | deliveryType | mult |
	| Baby | 2010 | 38 | 12 | 32 | Vaginal Delivery | 1 |

Scenario Outline: Add Obstetrics Record with Invalid Input
	Given I am viewing obstetrics records for patient with name <name>
	When I click Add New Record
	And I enters an lmp in for the LMP field
	And I enter a prior pregnancy with values: conception year <cYear>, weeks pregnant <weeksPreg>, hours in labor <hrsLabor>, weight gain <weightGain>, delivery type <deliveryType>, multiplicity <mult>
	And I click add pregnancy
	Then the following error message appears: <err>
Examples:
	| name | cYear | weeksPreg | hrsLabor | weightGain | deliveryType | mult | err |
	| Baby | 2010 | -1 | 12 | 32 | Vaginal Delivery | 1 | Error Message |