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

Scenario Outline: add obstetrics data to office visit
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And enter <fhr> for FHR, <mult> for multiplicity, and <llp> for low-lying placenta
	And click Save on the obstetrics office visit tab
	Then the obstetrics tab should have a FHR of <fhr>, a multiplicity of <mult>, and <llp> for low-lying placenta
Examples:
	| fhr | mult | llp |
	| 130 | 1    | no  |
	| 130 | 1    | yes |

Scenario: add ultrasound without images
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And enter 130 for FHR, 1 for multiplicity, and no for low-lying placenta
	And click Save on the obstetrics office visit tab
	And I check the number of ultrasounds already in the table
	And I enter 1 for CRL, 1 for BPD, 1 for HC, 1 for FL, 1 for OFD, 1 for AC, 1 for HL, and 1 for EFW
	And click Add Fetus Data on the ultrasound office visit tab
	And I enter 2 for CRL, 2 for BPD, 2 for HC, 2 for FL, 2 for OFD, 2 for AC, 2 for HL, and 2 for EFW
	And click Add Fetus Data on the ultrasound office visit tab
	Then two more ultrasounds exist in the ultrasound table than before

Scenario: add ultrasound with images
	Then this scenario is not implemented yet

Scenario: update and delete ultrasound data
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