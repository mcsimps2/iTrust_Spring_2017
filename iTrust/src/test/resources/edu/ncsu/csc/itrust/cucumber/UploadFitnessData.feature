Feature: Upload Fitness Data
	As an HCP
	I want to be able to upload a Fitbit export CSV file
	So I can add and update fitness data for a patient

Background:
	Given I'm at the iTrust login screen
	And I have logged in with MID: 9000000000 and password: pw
	And I have pulled up the patient calendar for Patient 1 with name Random
	
Scenario Outline: Verifying File Upload
	When I click the upload fitness data button
	And I upload the file <file>
	Then the data in the file is added to the patient’s fitness data

Examples:
	| file                                                      |
	| testing-files/sample_fitnessData/fitbit_export_HW3.csv    |
	| testing-files/sample_fitnessData/fitbit_export_valid1.csv |
	| testing-files/sample_fitnessData/fitbit_export_valid2.csv |
	
Scenario Outline: Invalid File Format
	When I click the upload fitness data button
	And I upload the file <file>
	Then the file is not uploaded
Examples:
	| file                                                           |
	| testing-files/sample_fitnessData/fitbit_export_invalid_HW3.csv |
	| testing-files/sample_fitnessData/fitbit_export_invalid1.csv    |
	| testing-files/sample_fitnessData/fitbit_export_invalid2.csv    |
	| testing-files/sample_fitnessData/fitbit_export_invalid3.csv    |
	| testing-files/sample_fitnessData/fitbit_export_invalid4.csv    |
	| testing-files/sample_fitnessData/fitbit_export_invalid5.csv    |
	| testing-files/sample_fitnessData/fitbit_export_invalid6.csv    |
	| testing-files/sample_fitnessData/fitbit_export_invalid7.csv    |