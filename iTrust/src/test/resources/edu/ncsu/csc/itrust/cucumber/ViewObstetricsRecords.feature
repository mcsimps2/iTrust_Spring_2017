Feature: View Patient's Obstetrics Records
	As an HCP
	I want to be able to view the obstetrics record for a patient
	
Background:
	Given I am at the iTrust login screen
	
Scenario Outline: Check Records Display Values
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with pid <patientName>
	And click on their link
	Then <numRecords> obstetrics records appear with dates <dates>
Examples:
	| hcpMID | pw | patientName | numRecords | dates |
	| 9000000000 | pw | Random | -1 | TODO |
	
Scenario Outine: Ineligable Obstetrics Patient
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with pid <pid>
	And click on their link
	And I click the button to make the patient eligable for obstetrics care
	Then <numRecords> obstetrics records appear with dates <dates>
Examples:
	| hcpMID | pw | patientName | numRecords | dates |
	| 9000000000 | pw | Zappic | 0 | null |

Scenario Outline: View Obstetric Record
	Given I have logged in as HCP <hcpMID> with password <pw>
	Given I have navigated to Patient Info -> Obstetrics Records
	When I search for the patient with pid <patientName>
	And click on their link
	And I click on the first obstetrics record
	Then the following data will be displayed: <initDate>, <lmp>, <edd>, <weeksPreg>
	And there will be <numPreg> prior pregnancies

Examples:
	| hcpMID | pw | patientName | initDate | lmp | edd | weeksPreg | numPreg |
	| 9000000000 | pw | Random | TODO | TODO | TODO | TODO | TODO |