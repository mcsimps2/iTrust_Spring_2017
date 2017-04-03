Feature: Add/Edit childbirth records to office visit
	As an HCP
	I want to be able to add childbirth data to an office visit for a patient

Background:
	Given the databases have been reset
	Given I am at the iTrust login screen

Scenario: Childbirth and Newborns tabs exist
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	Then the childbirth tab is there
	And the newborns tab is there

Scenario: Obstetrics history displayed on childbirth page
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	Then the patient's obstetrics history is present

Scenario Outline: Add childbirth data to office visit
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And I select <birthMethod> for Childbirth Method
	And I enter <pit> for Pitocin, <N2O> for Nitrous Oxide, <peth> for Pethidine, <epi> for Epidural Anaesthesia, and <MgSO4> for Magnesium Sulfate
	And click Save on the childbirth tab
	Then the childbirth tab has those fields
	And the childbirth visit is in the database
	And a success message appears

Examples:
	| birthMethod                     | pit | N2O | peth | epi | MgSO4 |
	| Vaginal Delivery                | 100 | 0   | 0    | 0   | 0     |
	| Vaginal Delivery Vacuum Assist  | 0   | 100 | 0    | 0   | 0     |
	| Vaginal Delivery Forceps Assist | 0   | 0   | 100  | 0   | 0     |
	| Caesarean section               | 0   | 0   | 0    | 100 | 0     |
	| Miscarriage                     | 0   | 0   | 0    | 0   | 100   |

Scenario: Add newborns
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And I select Vaginal Delivery for Childbirth Method
	And I enter 0 for all childbirth drug fields
	And click Save on the childbirth tab
	And I check the number of newborns in the table
	And I enter 2017-4-2 for Date and 10:00 for Time
	And I select Male for Sex
	And click Add Newborn on the newborns tab
	And I enter 2017-4-2 for Date and 10:01 for Time
	And I select Female for Sex
	And click Add Newborn on the newborns tab
	Then the newborns are in the database
	And 2 more newborns exist in the newborns table
	And a success message appears

Scenario: Update and Delete newborn data
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And I select Vaginal Delivery for Childbirth Method
	And I enter 0 for all childbirth drug fields
	And click Save on the childbirth tab
	And I enter 2017-4-2 for Date and 10:00 for Time
	And I select Male for Sex
	And click Add Newborn on the newborns tab
	And I enter 2017-4-2 for Date and 10:01 for Time
	And I select Female for Sex
	And click Add Newborn on the newborns tab
	And I check the number of newborns in the table
	And click the first Delete button in the newborns table
	And I check that there is one fewer newborn in the table
	And click the first Edit button in the newborns table
	And I enter 2017-4-2 for Date and 10:02 for Time
	And I select Female for Sex
	And click Update Newborn
	Then the first newborn in the table should have those field values
	And the same number of newborns should be in the table

Scenario: Non-OB/GYN HCP
	Given I have logged in as OBGYN with MID 9000000000 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	Then a message indicates that only OBGYN HCPs can edit obstetrics data
	And the childbirth form fields are disabled
	And the newborns form fields are disabled

Scenario: Add newborn before childbirth visit info
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And I go to the create new office visit page
	And I enter a date to the office visit date field
	And I click Save to save the office visit
	And I enter 2017-4-2 for Date and 10:00 for Time
	And I select Male for Sex
	And click Add Newborn on the newborns tab
	Then a message says I must add childbirth data first and no newborn data is added