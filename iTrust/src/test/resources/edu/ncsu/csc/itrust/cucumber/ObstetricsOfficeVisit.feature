Feature: Add obstetrics records to office visit
	As an HCP
	I want to be able to add obstetrics data to an office visit for a patient

Background:
	Given the databases have been reset
	Given I am at the iTrust login screen

Scenario: obstetrics and ultrasound tabs exist
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	Then the obstetrics tab is there
	And the ultrasound tab is there

Scenario Outline: add obstetrics data to office visit
	Given I have logged in as OBGYN with MID 9000000012 and password pw
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
	Given I have logged in as OBGYN with MID 9000000012 and password pw
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
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And enter 130 for FHR, 1 for multiplicity, and no for low-lying placenta
	And click Save on the obstetrics office visit tab
	And I enter 1 for CRL, 1 for BPD, 1 for HC, 1 for FL, 1 for OFD, 1 for AC, 1 for HL, and 1 for EFW
	And click Add Fetus Data on the ultrasound office visit tab
	And upload the file test.jpg
	And upload the file test.png
	And upload the file test.pdf
	Then the ultrasound images were uploaded successfully

Scenario: update and delete ultrasound data
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And enter 130 for FHR, 1 for multiplicity, and no for low-lying placenta
	And click Save on the obstetrics office visit tab
	And I enter 1 for CRL, 1 for BPD, 1 for HC, 1 for FL, 1 for OFD, 1 for AC, 1 for HL, and 1 for EFW
	And click Add Fetus Data on the ultrasound office visit tab
	And I enter 2 for CRL, 2 for BPD, 2 for HC, 2 for FL, 2 for OFD, 2 for AC, 2 for HL, and 2 for EFW
	And click Add Fetus Data on the ultrasound office visit tab
	And I check the number of ultrasounds already in the table
	And click the first Delete button in the ultrasound table
	And I check that there is one fewer ultrasound in the table
	And I check the number of ultrasounds already in the table
	And click the first Edit button in the ultrasound table
	And I enter 3 for CRL, 3 for BPD, 3 for HC, 3 for FL, 3 for OFD, 3 for AC, 3 for HL, and 3 for EFW
	And click Update Fetus Data
	Then the same number of ultrasounds should be in the table

Scenario: schedule appointment
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And I go to the create new office visit page
	And I enter a date to the office visit date field
	And I click Save to save the office visit
	And I enter a weight of 150, blood pressure of 120/80, FHR of 130, multiplicity of 1, and a Google Calendar ID
	And click Save on the obstetrics office visit tab
	Then the visit was updated successfully and an appointment was scheduled

Scenario: non OBGYN HCP
	Given I have logged in as OBGYN with MID 9000000000 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	Then a message indicates that only OBGYN HCPs can edit obstetrics data
	And the obstetrics form fields are disabled
	And the ultrasound form fields are disabled

Scenario: add ultrasound before office visit info
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And I go to the create new office visit page
	And I enter a date to the office visit date field
	And I click Save to save the office visit
	And I enter 1 for CRL, 1 for BPD, 1 for HC, 1 for FL, 1 for OFD, 1 for AC, 1 for HL, and 1 for EFW
	And click Add Fetus Data on the ultrasound office visit tab
	Then a message says I must add obstetrics data first and no ultrasound data is added