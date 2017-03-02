Feature: Enter and edit data for a patient
	As an HCP
	I want to be able to select a date on a patient’s fitness calendar
	So I can manually enter and edit their fitness information for that day

Background:
	Given I am at the iTrust login screen
	And I have logged in with MID 9000000000 and password pw
	And I have pulled up the patient calendar for patient with name Random

Scenario Outline: Manually enter data
	Given the day <dayOfTheMonth> has no fitness data
	When I click on the day <dayOfTheMonth>
	And I enter the following values: <calsB> Calories Burned, <steps> Steps, <miles> Distance, <floors> Floors, <minSed> Minutes Sedentary, <minLight> Minutes Lightly Active, <minFair> Minutes Fairly Active, <minHigh> Minutes Very Active, <calsA> Activity Calories
	And I click Save
	Then a success message appears
	And the information is saved
	
Examples:
| dayOfTheMonth | calsB | steps | miles | floors | minSed | minLight | minFair | minHigh | calsA |
| 13 | 1000 | 500 | 2 | 14 | 180 | 200 | 300 |400 | 500 |
| 1 | 500 | 1200 | 20 | 18 | 181 | 300 | 200 |100 | 200 |
| 28 | 2000 | 500 | 2 | 14 | 180 | 200 | 300 |400 | 1000 |

Scenario Outline: Edit existing data
	Given the patient has populated fitness data for the day <dayOfTheMonth>
	When I click on the day <dayOfTheMonth>
	And I enter the following values: <calsB> Calories Burned, <steps> Steps, <miles> Distance, <floors> Floors, <minSed> Minutes Sedentary, <minLight> Minutes Lightly Active, <minFair> Minutes Fairly Active, <minHigh> Minutes Very Active, <calsA> Activity Calories
	And I click Save
	Then a success message appears
	And the information is saved
	
Examples:
| dayOfTheMonth | calsB | steps | miles | floors | minSed | minLight | minFair | minHigh | calsA |
| 27 | 1000 | 500 | 2 | 14 | 180 | 200 | 300 |400 | 500 |
| 5 | 500 | 1200 | 20 | 18 | 181 | 300 | 200 |100 | 200 |
| 6 | 2000 | 500 | 2 | 14 | 180 | 200 | 300 |400 | 1000 |

Scenario Outline: Invalid Data
	Given the day <dayOfTheMonth> has no fitness data
	When I click on the day <dayOfTheMonth>
	And I enter the following invalid values: <calsB> Calories Burned, <steps> Steps, <miles> Distance, <floors> Floors, <minSed> Minutes Sedentary, <minLight> Minutes Lightly Active, <minFair> Minutes Fairly Active, <minHigh> Minutes Very Active, <calsA> Activity Calories
	And I click Save
	Then the information is not saved
Examples:
	| dayOfTheMonth | calsB | steps | miles | floors | minSed | minLight | minFair | minHigh | calsA |
	| 27 | 1000 | 500 | 2 | abc | 180 | 200 | 300 |400 | 500 |
	| 5 | 500 | def | 20 | 18 | 181 | 300 | 200 |100 | 200 |
	| 6 | 2000 | 500 | 2 | 14 | 180 | 30hijk30s | 300 |400 | 1000 |