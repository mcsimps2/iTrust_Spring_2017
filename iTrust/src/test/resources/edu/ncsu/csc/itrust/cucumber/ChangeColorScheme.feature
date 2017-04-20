Feature: Change Color Scheme
	As a user of any kind
	I want to be able to change the colors in iTrust
	
Background:
	Given I am at the iTrust login screen
	Given the databases have been reset
	
Scenario Outline: Change the colors
	Given I have logged in as HCP <hcpMID> with password <pw>
	When I click on the settings page
	And I set the color scheme to <scheme>
	And I save the color changes
	Then the colors have been changed to <color>
Examples:
	| hcpMID | pw | scheme | color |
	| 9000000012 | pw | Dark | DARK |
	| 9000000012 | pw | Muted Midtones | MUTED_MIDTONES |
	| 9000000012 | pw | Rainbow | RAINBOW |
	| 9000000012 | pw | I Dare You | I_DARE_YOU |
	| 9000000012 | pw | Default | DEFAULT |