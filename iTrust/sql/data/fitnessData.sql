INSERT INTO fitness (pid, dateOfData, steps) VALUES(1,CONCAT(ADDDATE(CURDATE(),1)), 500);
INSERT INTO fitness (pid, dateOfData, steps, caloriesBurned) VALUES(1, CONCAT(ADDDATE(CURDATE(),0)), 9255, 5000);
INSERT INTO fitness (pid, dateOfData, steps, distance) VALUES(1, CONCAT(ADDDATE(CURDATE(),-1)), 5252, 3.25);
INSERT INTO fitness (pid, dateOfData, steps, distance, caloriesBurned) VALUES (1, CONCAT(ADDDATE(CURDATE(),3)), 1, 5.5, 56);
INSERT INTO fitness (pid, dateOfData, steps) VALUES(2,CONCAT(ADDDATE(CURDATE(),1)), 500);
INSERT INTO fitness (pid, dateOfData, steps, caloriesBurned) VALUES(2, CONCAT(ADDDATE(CURDATE(),0)), 12345, 5321);
INSERT INTO fitness (pid, dateOfData, steps, distance) VALUES(2, CONCAT(ADDDATE(CURDATE(),-1)), 5555, 5.53);
INSERT INTO fitness (pid, dateOfData, steps, distance, caloriesBurned) VALUES (2, CONCAT(ADDDATE(CURDATE(),-3)), 10, 50.5, 5600);

INSERT INTO fitness (pid, dateOfData, steps) VALUES (1,'2016-12-06',58);
INSERT INTO fitness (pid, dateOfData, steps) VALUES (1,'2016-12-07',60);
INSERT INTO fitness (pid, dateOfData, steps) VALUES (1,'2016-12-08',62);
INSERT INTO fitness (pid, dateOfData, steps) VALUES (1,'2016-12-10',64);