Feature: View Patient's Obstetrics Records
	As an HCP
	I want to be able to view the obstetrics record for a patient
	
Background:
	Given I am at the iTrust login screen
	Given the databases have been reset
	
Scenario Outline: Check Records Display Values
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on the link for patient with pid <pid>
	Then an obstetrics record appears with date <date>
Examples:
	| hcpMID | pw | patientName | pid | date |
	| 9000000012 | pw | Baby | 5 | March 1, 2005 |
	
Scenario Outline: Ineligable Obstetrics Patient
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on the link for patient with pid <pid>
	And I click the button to make the patient eligable for obstetrics care
	Then no obstetrics records appear
Examples:
	| hcpMID | pw | patientName | pid |
	| 9000000012 | pw | Andy | 2 |
	
Scenario Outline: Non OB/GYN HCP
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on the link for patient with pid <pid>
	Then the add record button will not be displayed
Examples:
	| hcpMID | pw | patientName | pid |
	| 9000000000 | pw | Baby | 5 |

Scenario Outline: View Obstetric Record
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on the link for patient with pid <pid>
	And I click on the first obstetrics record
	Then the following data will be displayed: <initDate>; <lmp>; <edd>; <weeksPreg>
	And there will be <numPreg> prior pregnancies

Examples:
	| hcpMID | pw | patientName | pid | initDate | lmp | edd | weeksPreg | numPreg |
	| 9000000012 | pw | Baby | 5 |  March 1, 2005 | January 3, 2005 | December 6, 2005 | 8 | 1 |

Scenario Outline: Select Wrong Patient
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <wrongName>
	And click on the link for patient with pid <pid>
	And I decide to select another patient
	And I search for the patient with name <rightName>
	And click on the link for patient with pid <pid2>
	Then no obstetrics records appear
Examples:
	| hcpMID | pw | wrongName | pid | rightName | pid2 |
	| 9000000012 | pw | Andy | 2 | Fozzie | 22 |