Feature: View Patient's Obstetrics Records
	As an HCP
	I want to be able to view the obstetrics record for a patient
	
Background:
	Given I am at the iTrust login screen
	
Scenario Outline: Check Records Display Values
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on their link
	Then an obstetrics record appears with date <date>
Examples:
	| hcpMID | pw | patientName | date |
	| 9000000012 | pw | Random | 2017-03-16 |
	
Scenario Outline: Ineligable Obstetrics Patient
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on their link
	And I click the button to make the patient eligable for obstetrics care
	Then no obstetrics records appear
Examples:
	| hcpMID | pw | patientName |
	| 9000000012 | pw | Andy |
	
Scenario Outline: Non OB/GYN HCP
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on their link
	Then the add record button will not be displayed
Examples:
	| hcpMID | pw | patientName |
	| 9000000000 | pw | Random |

Scenario Outline: View Obstetric Record
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <patientName>
	And click on their link
	And I click on the first obstetrics record
	Then the following data will be displayed: <initDate>, <lmp>, <edd>, <weeksPreg>
	And there will be <numPreg> prior pregnancies

Examples:
	| hcpMID | pw | patientName | initDate | lmp | edd | weeksPreg | numPreg |
	| 9000000012 | pw | Random | March 16 | January 1 | December 21 | 10 | 2 |

Scenario Outline: Select Wrong Patient
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with name <wrongName>
	And click on their link
	And I decide to select another patient
	And I search for the patient with name <rightName>
	And click on their link
	Then no obstetrics records appear
Examples:
	| hcpMID | pw | wrongName | rightName |
	| 9000000012 | pw | Andy | Fozzie |