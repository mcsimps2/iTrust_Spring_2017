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
	And I select <visitType> for Visit Type
	And I enter <pit> for Pitocin, <N2O> for Nitrous Oxide, <peth> for Pethidine, <epi> for Epidural Anaesthesia, <MgSO4> for Magnesium Sulfate, and <rh> for RH
	And click Save on the childbirth tab
	Then the childbirth tab has those fields
	And the childbirth visit is in the database
	And a success message appears

Examples:
	| birthMethod                     | visitType                 | pit | N2O | peth | epi | MgSO4 | rh |
	| Vaginal Delivery                | Emergency appointment     | 100 | 0   | 0    | 0   | 0     | 0  |
	| Vaginal Delivery Vacuum Assist  | Emergency appointment     | 0   | 100 | 0    | 0   | 0     | 1  |
	| Vaginal Delivery Forceps Assist | Emergency appointment     | 0   | 0   | 100  | 0   | 0     | 2  |
	| Caesarean section               | Pre-scheduled appointment | 0   | 0   | 0    | 100 | 0     | 3  |
	| Miscarriage                     | Emergency appointment     | 0   | 0   | 0    | 0   | 100   | 4  |

Scenario: Add newborns
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And I select Vaginal Delivery for Childbirth Method
	And I select Emergency appointment for Visit Type
	And I enter 0 for all childbirth drug fields
	And click Save on the childbirth tab
	And I check the number of newborns in the table
	And I enter 2017-4-2 for Date and 10:00 AM for Time
	And I select Male for Sex
	And I enter Michael for First Name
	And click Add Newborn on the newborns tab
	And I enter 2017-4-2 for Date and 10:01 AM for Time
	And I select Female for Sex
	And I enter Michelle for First Name
	And click Add Newborn on the newborns tab
	Then 2 more newborns exist in the newborns table
	And a success message appears
	And the newborns are in the database

Scenario: Update and Delete newborn data
	Given I have logged in as OBGYN with MID 9000000012 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	And I select Vaginal Delivery for Childbirth Method
	And I select Emergency appointment for Visit Type
	And I enter 0 for all childbirth drug fields
	And click Save on the childbirth tab
	And I enter 2017-4-2 for Date and 10:00 AM for Time
	And I select Male for Sex
	And I enter Michael for First Name
	And click Add Newborn on the newborns tab
	And I enter 2017-4-2 for Date and 10:01 AM for Time
	And I select Female for Sex
	And I enter Michelle for First Name
	And click Add Newborn on the newborns tab
	And I check the number of newborns in the table
	And click the first Delete button in the newborns table
	And I check that there is one fewer newborn in the table
	And click the first Edit button in the newborns table
	And I enter 2017-4-2 for Date and 10:02 AM for Time
	And I select Female for Sex
	And I enter Uber for First Name
	And click Update Newborn
	Then the same number of newborns should be in the table

Scenario: Non-OB/GYN HCP
	Given I have logged in as OBGYN with MID 9000000000 and password pw
	When I navigate to Office Visit -> Document Office Visit
	And I search for the patient with name Random
	And click on the link for patient with pid 1
	And click on the first office visit on the office visits page
	Then a message indicates that only OBGYN HCPs can edit childbirth data
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
	Then the newborns form fields are disabled
	And a message says I must save childbirth data first