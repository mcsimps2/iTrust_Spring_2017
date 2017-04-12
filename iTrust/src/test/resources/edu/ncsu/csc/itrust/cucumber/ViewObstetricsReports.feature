Feature: view patient's obstetrics reports
	As an HCP
	I want to be able to view obstetrics reports for a patient

Scenario: generate report from new obstetrics record
	Given I am on the obstetrics records page for patient 1 as an OBGYN HCP
	When I create a new obstetrics record with LMP 2017-04-11, RH false, and genetic potential for miscarriage false
	And I click generate report on the first obstetrics record in the table
	Then the page displays correctly