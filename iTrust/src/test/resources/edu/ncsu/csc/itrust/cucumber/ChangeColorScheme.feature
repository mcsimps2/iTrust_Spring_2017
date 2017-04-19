Feature: Change Color Scheme
	As a user of any kind
	I want to be able to change the colors in iTrust
	
Background:
	Given I am at the iTrust login screen
	Given the databses have been reset
	
Scenario Outline: Change the colors
	Given I have logged in as HCP <hcpMID> with password <pw>
	When I click on the settings page
	And I set the color scheme to <scheme>
	And I save the color changes
	Then the colors have been changed to <color>
Examples:
	| hcpMID | pw | scheme | color |
	| 9000000012 | pw | Dark | #050505 |
	| 9000000012 | pw | Muted Midtones | #55699a |
	| 9000000012 | pw | Rainbow | #2DFF1C |
	| 9000000012 | pw | I Dare You | #ff7700 |