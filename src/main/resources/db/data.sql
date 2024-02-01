INSERT INTO skills (skill_name)
VALUES ("Java"),
       ("Nodejs"),
       (".net"),
       ("C++"),
       ("Business analysis"),
       ("Communication");

INSERT INTO high_level (high_level_name)
VALUES ("High school"),
       ("Bachlor's Degree"),
       ("Master Degree"),
       ("PHD");

INSERT INTO user (department, gender, role, status, created_date, date_of_birth, last_modified_date, address, created_by, email, full_name, last_modified_by, note, phone_number, password, user_name)
VALUES (0, 0, 0, 0, '2000-01-01', '2000-01-01', '2000-01-01', "Nam Dinh", '2000-01-01', "duongduc1520@gmail.com", "duong duc", '2000-01-01', "good", "0364854020" ,"1","admin1"),
       (1, 1, 1, 1, '2000-01-01', '2000-01-01', '2000-01-01', "Nam Dinh", '2000-01-01', "duongduc1521@gmail.com", "hoang anh", '2000-01-01', "good", "0364854021","1" ,"admin2"),
       (2, 1, 2, 2, '2000-01-01', '2000-01-01', '2000-01-01', "Nam Dinh", '2000-01-01', "duongduc1522@gmail.com", "long", '2000-01-01', "good", "0364854022" ,"1","admin3"),
       (3, 0, 3, 3, '2000-01-01', '2000-01-01', '2000-01-01', "Nam Dinh", '2000-01-01', "duongduc1523@gmail.com", "duong duc3", '2000-01-01', "good", "0364854023","1","admin4");

INSERT INTO interview_managerment.candidate
( current_position, date_of_birth, gender, high_level_id, status, user_id, year_of_experience, created_date, last_modified_date, note, address, created_by, cv_attachment, email, full_name, last_modified_by, phone_number)
VALUES( 1, '2000-12-12', 1, 1, 1, 1, 20, NULL, NULL, '2', '2', '200', '3', 'thant200@gmail.com', 'av', NULL, '0378820975'),
      ( 2, '2000-12-12', 1, 1, 2, 2, 20, NULL, NULL, '2', '2', '200', '3', 'tiendat18072002@gmail.com', 'av', NULL, '0308809975'),
      ( 3, '2000-12-12', 1, 1, 3, 3, 20, NULL, NULL, '2', '2', '200', '3', 'tiendat18072004@gmail.com', 'av', NULL, '0377829975'),
      ( 2, '2000-12-12', 1, 1, 3, 3, 20, NULL, NULL, '2', '2', '200', '3', 'tiendat18072000@gmail.com', 'av', NULL, '0375829975')
INSERT INTO interview_managerment.user
(date_of_birth, department, gender, `role`, status, created_date, last_modified_date, address, created_by, email, full_name, last_modified_by, note, password, phone_number, user_name)
VALUES('2000-01-09', 1, 0, 1, 'ACTIVE',  NULL, NULL, 'Hai Duong', 'Admin', 'thanh562000@gmail.com', 'Nguyen Dinh Thanh', NULL, NULL, '12345', '0378829976', 'ThanhND'),
      ('2000-01-09', 1, 1, 1, 'ACTIVE',  NULL, NULL, 'Hai Duong', 'Admin', 'hoang562001@gmail.com', 'Nguyen Dinh Thanh', NULL, NULL, '12345', '0378829976', 'ThanhND1'),
      ('2000-01-09', 1, 1, 1, 'ACTIVE',  NULL, NULL, 'Hai Duong', 'Admin', 'long654@gmail.com', 'Nguyen Duy Tang', NULL, NULL, '12345', '0378829976', 'TangND'),
      ('2000-01-09', 1, 1, 1, 'ACTIVE',  NULL, NULL, 'Hai Duong', 'Admin', 't562003@gmail.com', 'Nguyen Duy tuy', NULL, NULL, '12345', '0378829976', 'TuyND'),
      ('2000-01-09', 1, 1, 1, 'ACTIVE',  NULL, NULL, 'Hai Duong', 'Admin', 'thanh562004@gmail.com', 'Nguyen Duy Thien', NULL, NULL, '12345', '0378829976', 'ThienND'),
      ('2000-01-09', 1, 1, 1, 'ACTIVE',  NULL, NULL, 'Hai Duong', 'Admin', 'thanh562005@gmail.com', 'Nguyen Duy THong', NULL, NULL, '12345', '0378829976', 'ThongND');


INSERT INTO interview_managerment.candidate(current_position, date_of_birth, gender, high_level_id, status, user_id, year_of_experience, created_date, last_modified_date, note, address, created_by, cv_attachment, email, full_name, last_modified_by, phone_number)
VALUES( 1, '2000-12-12', 1, 1, 1, 1, 20, NULL, NULL, '2', '2', '200', '3', 'thant200@gmail.com', 'av', NULL, '0378820975'),
      ( 2, '2000-12-12', 1, 1, 2, 2, 20, NULL, NULL, '2', '2', '200', '3', 'tiendat18072002@gmail.com', 'av', NULL, '0308809975'),
      ( 3, '2000-12-12', 1, 1, 3, 3, 20, NULL, NULL, '2', '2', '200', '3', 'tiendat18072004@gmail.com', 'av', NULL, '0377829975');

INSERT INTO skills(skill_name)
values ('Java'),
       ('Nodejs'),
       ('.net'),
       ('C++'),
       ('Business analysis'),
       ('Communication');


INSERT INTO benefit(benefit_name)
values ('Lunch'),
       ('25-day leave'),
       ('Heathcare leave'),
       ('Hybird working'),
       ('Travel');


INSERT INTO level(level_name)
values ('Fresher'),
       ('Junior'),
       ('Senior'),
       ('Leader'),
       ('Manager'),
       ('Vice Head');


INSERT INTO job( salary_from, salary_to, description, created_by, job_title, status, working_address, start_date, end_date)
values ( 2000.00, 3000.00, 'good', 'System', 'HR IT', 'Closed', 'ha noi', '2000-02-15', '2021-02-15'),
       ( 2500.00, 3500.00, 'good', 'System', 'Backend Developer', 'Open', 'ha noi', '2000-02-15', '2024-02-15'),
       ( 2200.00, 3000.00, 'good', 'System', 'Frontend Developer', 'Open', 'ha noi', '2000-02-15', '2024-02-15'),
       ( 2500.00, 3500.00, 'good', 'System', 'Project Manager', 'Draft','ha noi', '2000-02-15', '2021-02-15');


INSERT INTO job_benefit(benefit_id, job_id)
values (1, 1),
       (2, 1),
       (4, 1),

       (2, 2),
       (3, 3);


INSERT INTO job_level(job_id, level_id)
values (1, 1),
       (1, 2),
       (1, 4),
       (2, 3),
       (2, 5),
       (3, 6);


INSERT INTO job_skill(job_id, skill_id)
values (1, 1),
       (1, 2),
       (1, 4),
       (2, 3),
       (2, 5),
       (3, 6);

