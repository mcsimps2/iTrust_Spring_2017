Feature: Patient Profile Pictures
	As an HCP
	I want to be able to upload a profile picture of a patient
	So that I can see who the patient is in their demographics

Background:
	Given I am at the iTrust login screen
	And I have logged in with MID 9000000000 and password pw

Scenario Outline: Profile Picture Appears in Patient Information
	When I navigate to Patient Information
	And I search for the patient with name <patientName> 
	And click on the link for patient with pid <pid>
	Then a profile picture appears

Examples:
	| patientName | pid |
	| Trend | 25 |
	| Thane | 105 |
	| Rock | 405 |
	
Scenario Outline: Profile Picture Appears in Basic Health Information
	When I navigate to Basic Health Information
	And I search for the patient with name <patientName> 
	And click on the link for patient with pid <pid>
	Then a profile picture appears

Examples:
	| patientName | pid |
	| Random | 1 |
	| Andy | 2 |
	
Scenario Outline: No profile picture displayed for patients with no health info, Error Path
	When I navigate to Basic Health Information
	And I search for the patient with name <patientName> 
	And click on the link for patient with pid <pid>
	Then no profile picture appears
Examples:
	| patientName | pid |
	| Trend | 25 |
	| Christina | 305 |
	
Scenario Outline: Upload Profile Picture
	When I navigate to Patient Information
	And I search for the patient with name Andy
	And click on the link for patient with pid 2
	And I upload a profile picture <profilePicture>
	Then the profile picture is saved in the database

Examples:
	| profilePicture |
	| testing-files/sample_profilepictures/profile01.jpg |
	| testing-files/sample_profilepictures/profile02.jpg |

Scenario Outline: No Submission Error Path
	When I navigate to Patient Information
	And I search for the patient with name Fake
	And click on the link for patient with pid 314159
	And I upload a picture <profilePicture> but don't submit anything
	Then the database is unchanged
	
Examples:
	| profilePicture |
	| testing-files/sample_profilepictures/profile01.jpg |
	| testing-files/sample_profilepictures/profile02.jpg |