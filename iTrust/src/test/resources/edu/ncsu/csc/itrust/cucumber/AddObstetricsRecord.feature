Feature: Add Patient's Obstetrics Records
	As an HCP
	I want to be able to add an obstetrics initialization record for a patient
	
Background:
	Given I am at the iTrust login screen
	And I have logged in as OBGYN with MID 9000000012 and password pw
	
Scenario Outline: Add Obstetrics Record
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <name>
	And click on the link for patient with pid <pid>
	And I click Add New Record
	And I enter an lmp in for the LMP field
	And I enter a prior pregnancy with values: conception year <cYear>, weeks pregnant <weeksPreg>, hours in labor <hrsLabor>, weight gain <weightGain>, delivery type <deliveryType>, multiplicity <mult>
	And I click add pregnancy
	And I click Save Record
	Then I am redirected to obstetrics records page
	And a success message appears indicating save successful
Examples:
	| name | pid | cYear | weeksPreg | hrsLabor | weightGain | deliveryType | mult |
	| Baby | 5 | 2010 | 38 | 12 | 32 | Vaginal Delivery | 1 |

Scenario Outline: Add Obstetrics Record with Invalid Input
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <name>
	And click on the link for patient with pid <pid>
	When I click Add New Record
	And I enter an lmp in for the LMP field
	And I enter a prior pregnancy with values: conception year <cYear>, weeks pregnant <weeksPreg>, hours in labor <hrsLabor>, weight gain <weightGain>, delivery type <deliveryType>, multiplicity <mult>
	And I click add pregnancy
	Then there is an error message appears about invalid input
Examples:
	| name | pid | cYear | weeksPreg | hrsLabor | weightGain | deliveryType | mult |
	| Baby | 5 | 2010 | -1 | 12 | 32 | Vaginal Delivery | 1 |
	
Scenario Outline: Cancel Add Obstetrics Record
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <name>
	And click on the link for the patient with pid <pid>
	And I click Add New Record
	And I enter an lmp in for the LMP field
	And I enter a prior pregnancy with values: conception year <cYear>, weeks pregnant <weeksPreg>, hours in labor <hrsLabor>, weight gain <weightGain>, delivery type <deliveryType>, multiplicity <mult>
	And I click add pregnancy
	And I click cancel
	And I click Add New Record
	Then all the fields are empty
Examples:
	| name | pid | cYear | weeksPreg | hrsLabor | weightGain | deliveryType | mult |
	| Baby | 5 | 2010 | 38 | 12 | 32 | Vaginal Delivery | 1 |