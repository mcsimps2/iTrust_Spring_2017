INSERT INTO ndcodes(Code, Description) VALUES
('00904-2407','Tetracycline'),
('08109-6','Aspirin'),
('64764-1512','Prioglitazone'),
('54868-4985', 'Citalopram Hydrobromide'),
('00060-431', 'Benzoyl Peroxide'),
('0338-1021-41', 'Penicillin'),
('0603-5801-04', 'Sulfasalazine'),
('0440-7026-30', 'Acetaminophen And Codeine')


ON DUPLICATE KEY UPDATE Code = Code;
