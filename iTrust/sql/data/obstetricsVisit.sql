INSERT INTO officeVisit(patientMID, visitDate, locationID, apptTypeID, household_smoking_status, patient_smoking_status, notes, blood_pressure, weight)
VALUES (1,  "2017-02-22 00:00:00", 1, 7, 1, 3, 'This is a note1', "120/80",  105.1),
       (1,  "2017-04-22 00:00:00", 1, 7, 1, 3, 'This is a note2', "120/80",  125.1),
       (5,  "2017-04-22 00:00:00", 1, 7, 1, 3, 'This is a note3', "150/100", 180.0),
       (5,  "2017-02-22 00:00:00", 1, 7, 1, 3, 'This is a note4', "150/100", 135.0),
       (5,  "2017-04-22 00:00:00", 1, 7, 1, 3, 'This is a note5', "150/100", 180.0),
       (20, "2017-02-22 00:00:00", 1, 7, 1, 3, 'This is a note6', "120/100", 150.0),
       (20, "2017-04-22 00:00:00", 1, 7, 1, 3, 'This is a note7', "120/100", 155.0);

INSERT INTO obstetricsVisit(officeVisitID, obstetricsInitID, weeksPregnant, fhr, multiplicity, lowLyingPlacentaObserved, imageType)
VALUES (51, 3, 22, 130, 1, false, "image.jpg"),
       (52, 3, 30, 135, 1, false, "image1.jpg"),
       (53, 1, 22,  42, 2,  true, "image2.jpg"),
       (54, 1, 22,  42, 2,  true, "image2.jpg"),
       (55, 1, 30,  48, 2,  true, "image3.jpg"),
       (56, 4, 22,  170, 2,  true, "image2.jpg"),
       (57, 4, 30,  170, 2,  true, "image3.jpg");