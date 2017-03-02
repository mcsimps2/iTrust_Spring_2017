Feature: make an appointment request
	As a patient
	I want to make an appointment with my health care provider
	So I can get something fixed

	Scenario: making an appointment request goes to the correct HCP
		Given patient with username 1 and password pw has requested an appointment with HCP ID 9000000003 for 01/20/2050 with keyword testone in the comment
		When I log in as user 9000000003 with password pw
		And I check my appointment requests
		Then I have an appointment request for 01/20/2050 with keyword testone
	
	Scenario: making an appointment request does not go to the incorrect HCP
		Given patient with username 1 and password pw has requested an appointment with HCP ID 9000000003 for 01/20/2050 with keyword testtwo in the comment
		When I log in as user 9000000000 with password pw
		And I check my appointment requests
		Then I do not have an appointment request for 01/20/2050 with keyword testtwo
