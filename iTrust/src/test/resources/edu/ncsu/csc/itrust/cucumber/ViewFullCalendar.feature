Feature: view full calendar
	As a patient
	I want to view my calendar
	So I know when my appointments are

	Scenario: view calendar
		Given user logs in with username 1 and password pw
		When user goes to the full calendar page
		Then the page appears normally