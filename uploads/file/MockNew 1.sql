drop database interview_managerment;
create database interview_managerment;

INSERT INTO `interview_managerment`.`benefit` (`benefit_id`, `benefit_name`) VALUES ('1', 'Lunch');
INSERT INTO `interview_managerment`.`benefit` (`benefit_id`, `benefit_name`) VALUES ('2', '25-day leave');
INSERT INTO `interview_managerment`.`benefit` (`benefit_id`, `benefit_name`) VALUES ('3', 'Healthcare insurance');
INSERT INTO `interview_managerment`.`benefit` (`benefit_id`, `benefit_name`) VALUES ('4', 'Hybrid working');
INSERT INTO `interview_managerment`.`benefit` (`benefit_id`, `benefit_name`) VALUES ('5', 'Travel');

INSERT INTO `interview_managerment`.`high_level` (`high_level_id`, `high_level_name`) VALUES ('1', 'High school');
INSERT INTO `interview_managerment`.`high_level` (`high_level_id`, `high_level_name`) VALUES ('2', 'Bachlor\'s Degree');
INSERT INTO `interview_managerment`.`high_level` (`high_level_id`, `high_level_name`) VALUES ('3', 'Master Degree');
INSERT INTO `interview_managerment`.`high_level` (`high_level_id`, `high_level_name`) VALUES ('4', 'PHD');


INSERT INTO `interview_managerment`.`level` (`level_id`, `level_name`) VALUES ('1', 'Fresher');
INSERT INTO `interview_managerment`.`level` (`level_id`, `level_name`) VALUES ('2', 'Junior');
INSERT INTO `interview_managerment`.`level` (`level_id`, `level_name`) VALUES ('3', 'Senior');
INSERT INTO `interview_managerment`.`level` (`level_id`, `level_name`) VALUES ('4', 'Leader');
INSERT INTO `interview_managerment`.`level` (`level_id`, `level_name`) VALUES ('5', 'Manager');
INSERT INTO `interview_managerment`.`level` (`level_id`, `level_name`) VALUES ('6', 'Vice Head');


INSERT INTO `interview_managerment`.`skills` (`skill_id`, `skill_name`) VALUES ('1', 'Java');
INSERT INTO `interview_managerment`.`skills` (`skill_id`, `skill_name`) VALUES ('2', 'Nodejs');
INSERT INTO `interview_managerment`.`skills` (`skill_id`, `skill_name`) VALUES ('3', '.net');
INSERT INTO `interview_managerment`.`skills` (`skill_id`, `skill_name`) VALUES ('4', 'C++');
INSERT INTO `interview_managerment`.`skills` (`skill_id`, `skill_name`) VALUES ('5', 'Business analysis');
INSERT INTO `interview_managerment`.`skills` (`skill_id`, `skill_name`) VALUES ('6', 'Communication');


INSERT INTO `interview_managerment`.`user` (`user_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `date_of_birth`, `department`, `email`, `full_name`, `gender`, `note`, `password`, `phone_number`, `role`, `status`, `user_name`) VALUES ('0', 'Hoàng Thái', '2022-04-01', 'Hoàng Thái', '2022-05-01', 'Nam Trực, Nam Định', '1997-06-04', '0', 'hvinhht05@gmail.com', 'Hoàng Thế Vinh', '0', 'Good', '123456', '0902540245', '0', '0', 'vinhht02');
INSERT INTO `interview_managerment`.`user` (`user_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `date_of_birth`, `department`, `email`, `full_name`, `gender`, `note`, `password`, `phone_number`, `role`, `status`, `user_name`) VALUES ('1', 'Thái Hoàng', '2021-05-12', 'Thái Hoàng', '2021-05-12', 'Lê Chân  Hải Phòng', '1995-05-12', '1', 'datpt02@gmail.com', 'Phạm Thành Đạt', '0', 'Good', '123456', '0901540287', '1', '1', 'datpt11');
INSERT INTO `interview_managerment`.`user` (`user_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `date_of_birth`, `department`, `email`, `full_name`, `gender`, `note`, `password`, `phone_number`, `role`, `status`, `user_name`) VALUES ('2', 'Minh Trang', '2020-07-07', 'Minh Trang', '2020-07-07', 'Kiến Xương Thái Bình', '2000-09-19', '2', 'thaomt05@gmail.com', 'Mai Thị Thảo', '1', 'Good', '123456', '0901420877', '2', '2', 'thaomt09');
INSERT INTO `interview_managerment`.`user` (`user_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `date_of_birth`, `department`, `email`, `full_name`, `gender`, `note`, `password`, `phone_number`, `role`, `status`, `user_name`) VALUES ('3', 'Thảo Mai', '2019-05-05', 'Thảo Mai', '2019-05-05', 'Hoàng Mai Hà Nội', '2001-12-12', '3', 'maint47@gmail.com', 'Nguyễn Thảo Mai', '1', 'Good', '123456', '0902820858', '3', '3', 'maint19');
INSERT INTO `interview_managerment`.`user` (`user_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `date_of_birth`, `department`, `email`, `full_name`, `gender`, `note`, `password`, `phone_number`, `role`, `status`, `user_name`) VALUES ('4', 'Minh Hoàng', '2022-01-08', 'Minh Hoàng', '2022-01-08', 'Diễn Châu Nghệ An', '2002-10-15', '4', 'trungpt12@gmail.com', 'Phạm Thành Trung', '0', 'Bad', '123456', '0958820852', '2', '3', 'trungpt05');

INSERT INTO `interview_managerment`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `cv_attachment`, `date_of_birth`, `email`, `full_name`, `gender`, `note`, `phone_number`, `current_position`, `status`, `year_of_experience`, `high_level_id`, `user_id`) VALUES ('2', 'Hữu Minh', '2024-01-05', 'Hữu Minh', '2024-01-05', 'Từ Sơn Bắc Ninh', 'FA_FJB_Course Information', '2004-12-10', 'huuminh25@gmail.com', 'Nguyễn Hữu Minh', '0', 'Good', '0942530512', '1', '2', '1', '2', '2');
INSERT INTO `interview_managerment`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `cv_attachment`, `date_of_birth`, `email`, `full_name`, `gender`, `note`, `phone_number`, `current_position`, `status`, `year_of_experience`, `high_level_id`, `user_id`) VALUES ('3', 'Thanh Thư', '2024-01-03', 'Thanh Thư', '2024-01-03', 'Sầm Sơn Thanh Hóa', 'Untitled 3', '1992-09-15', 'thanhthu002@gmail.com', 'Phạm Thanh Thư', '1', 'Good', '0942545534', '2', '3', '5', '3', '3');
INSERT INTO `interview_managerment`.`candidate` (`candidate_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `address`, `cv_attachment`, `date_of_birth`, `email`, `full_name`, `gender`, `note`, `phone_number`, `current_position`, `status`, `year_of_experience`, `high_level_id`, `user_id`) VALUES ('4', 'Chiến Thắng', '2024-01-07', 'Chiến Thắng', '2024-01-07', 'Kim Sơn Ninh Bình', 'Postman-win64-Setup', '1996-05-04', 'chienthang25@gmail.com', 'Hoàng Chiến Thắng', '0', 'Good', '0934511588', '3', '2', '1', '2', '1');


INSERT INTO `interview_managerment`.`candidate_skill` (`candidate_skill_id`, `candidate_id`, `skill_id`) VALUES ('1', '1', '1');
INSERT INTO `interview_managerment`.`candidate_skill` (`candidate_skill_id`, `candidate_id`, `skill_id`) VALUES ('2', '2', '3');
INSERT INTO `interview_managerment`.`candidate_skill` (`candidate_skill_id`, `candidate_id`, `skill_id`) VALUES ('3', '3', '2');

    
INSERT INTO `interview_managerment`.`job` (`job_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `end_date`, `salary_from`, `salary_to`, `start_date`, `status`, `job_title`, `working_address`) VALUES ('1', 'Hải Hoàng', '2024-01-05', 'Hải Hoàng', '2024-01-05', 'Thiết kế, triển khai ứng dụng dựa trên ngôn ngữ lập trình Java', '2024-01-15', '15000000', '25000000', '2024-01-08', '1', 'Java', 'Hà Nội');
INSERT INTO `interview_managerment`.`job` (`job_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `end_date`, `salary_from`, `salary_to`, `start_date`, `status`, `job_title`, `working_address`) VALUES ('2', 'Minh Đức', '2024-01-06', 'Minh Đức', '2024-01-06', 'Thiết kế, triển khai ứng dụng dựa trên ngôn ngữ lập trình C#', '2024-01-15', '10000000', '20000000', '2024-01-12', '2', 'C#', 'Hải Phòng');
INSERT INTO `interview_managerment`.`job` (`job_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `end_date`, `salary_from`, `salary_to`, `start_date`, `status`, `job_title`, `working_address`) VALUES ('3', 'Thái Minh', '2024-01-07', 'Thái Minh', '2024-01-07', 'Thiết kế, triển khai ứng dụng dựa trên ngôn ngữ lập trình C++', '2024-01-20', '12000000', '18000000', '2024-01-13', '3', 'C++', 'Đà Nẵng');
INSERT INTO `interview_managerment`.`job` (`job_id`, `created_by`, `created_date`, `last_modified_by`, `last_modified_date`, `description`, `end_date`, `salary_from`, `salary_to`, `start_date`, `status`, `job_title`, `working_address`) VALUES ('4', 'Hải Linh', '2024-01-02', 'Hải Linh', '2024-01-02', 'Thiết kế, triển khai ứng dụng dựa trên ngôn ngữ lập trình Python', '2024-01-30', '18000000', '30000000', '2024-01-20', '2', 'Python', 'Hồ Chí Minh');


INSERT INTO `interview_managerment`.`candidate_job` (`candidate_job_id`, `candidate_id`, `job_id`) VALUES ('1', '1', '1');
INSERT INTO `interview_managerment`.`candidate_job` (`candidate_job_id`, `candidate_id`, `job_id`) VALUES ('2', '2', '2');
INSERT INTO `interview_managerment`.`candidate_job` (`candidate_job_id`, `candidate_id`, `job_id`) VALUES ('3', '3', '3');
INSERT INTO `interview_managerment`.`candidate_job` (`candidate_job_id`, `candidate_id`, `job_id`) VALUES ('4', '4', '4');

INSERT INTO `interview_managerment`.`job_benefit` (`job_benefit_id`, `benefit_id`, `job_id`) VALUES ('1', '1', '1');
INSERT INTO `interview_managerment`.`job_benefit` (`job_benefit_id`, `benefit_id`, `job_id`) VALUES ('2', '2', '2');
INSERT INTO `interview_managerment`.`job_benefit` (`job_benefit_id`, `benefit_id`, `job_id`) VALUES ('3', '3', '3');
INSERT INTO `interview_managerment`.`job_benefit` (`job_benefit_id`, `benefit_id`, `job_id`) VALUES ('4', '4', '4');


INSERT INTO `interview_managerment`.`job_level` (`job_level_id`, `job_id`, `level_id`) VALUES ('1', '1', '1');
INSERT INTO `interview_managerment`.`job_level` (`job_level_id`, `job_id`, `level_id`) VALUES ('2', '2', '2');
INSERT INTO `interview_managerment`.`job_level` (`job_level_id`, `job_id`, `level_id`) VALUES ('3', '3', '3');
INSERT INTO `interview_managerment`.`job_level` (`job_level_id`, `job_id`, `level_id`) VALUES ('4', '4', '4');

INSERT INTO `interview_managerment`.`offer` (`job_id`, `approver`, `basic_salary`, `contract_period_from`, `contract_period_to`, `contract_type`, `gender`, `due_date`, `note`, `current_position`, `status`, `candidate_id`, `level_id`, `recruiter_id`) VALUES ('1', 'Minh Thành', '10000000', '2024-01-05', '2025-01-05', '2', '0', '2024-01-05', 'Important', '1', '1', '2', '1', '1');
INSERT INTO `interview_managerment`.`offer` (`job_id`, `approver`, `basic_salary`, `contract_period_from`, `contract_period_to`, `contract_type`, `gender`, `due_date`, `note`, `current_position`, `status`, `candidate_id`, `level_id`, `recruiter_id`) VALUES ('2', 'Hà Giang', '15000000', '2024-01-02', '2025-01-02', '3', '1', '2024-01-02', 'Important', '2', '2', '3', '2', '2');
INSERT INTO `interview_managerment`.`offer` (`job_id`, `approver`, `basic_salary`, `contract_period_from`, `contract_period_to`, `contract_type`, `gender`, `due_date`, `note`, `current_position`, `status`, `candidate_id`, `level_id`, `recruiter_id`) VALUES ('3', 'Minh Thương', '20000000', '2024-01-06', '2025-01-06', '2', '1', '2024-01-06', 'Important', '3', '1', '4', '1', '3');

INSERT INTO `interview_managerment`.`schedule` (`schedule_id`, `location`, `meeting_id`, `note`, `result`, `schedule_time`, `schedule_time_from`, `schedule_time_to`, `status`, `title`, `candidate_id`, `offer_id`, `recruiter_owner`) VALUES ('1', 'FA', 'A205', 'ABC', 'Pass', '2024-01-05', '2024-01-05 08:00', '2024-01-05 09:00', '1', 'Interview title 1', '2', '1', '1');
INSERT INTO `interview_managerment`.`schedule` (`schedule_id`, `location`, `meeting_id`, `note`, `result`, `schedule_time`, `schedule_time_from`, `schedule_time_to`, `status`, `title`, `candidate_id`, `offer_id`, `recruiter_owner`) VALUES ('2', 'FA', 'A401', 'CDE', 'Pass', '2024-01-02', '2024-01-02 14:00', '2024-01-02 15:00', '2', 'Interview title 2', '3', '2', '2');
INSERT INTO `interview_managerment`.`schedule` (`schedule_id`, `location`, `meeting_id`, `note`, `result`, `schedule_time`, `schedule_time_from`, `schedule_time_to`, `status`, `title`, `candidate_id`, `offer_id`, `recruiter_owner`) VALUES ('3', 'FA', 'A502', 'FGH', 'Fail', '2024-01-06', '2024-01-06 09:00', '2024-01-06 10:00', '1', 'Interview title 3', '4', '3', '3');

INSERT INTO `interview_managerment`.`user_schedule` (`job_schedule_id`, `schedule_id`, `user_id`) VALUES ('1', '1', '1');
INSERT INTO `interview_managerment`.`user_schedule` (`job_schedule_id`, `schedule_id`, `user_id`) VALUES ('2', '2', '2');
INSERT INTO `interview_managerment`.`user_schedule` (`job_schedule_id`, `schedule_id`, `user_id`) VALUES ('3', '3', '3');

INSERT INTO `interview_managerment`.`job_schedule` (`job_schedule_id`, `job_id`, `schedule_id`) VALUES ('1', '1', '1');
INSERT INTO `interview_managerment`.`job_schedule` (`job_schedule_id`, `job_id`, `schedule_id`) VALUES ('2', '2', '2');
INSERT INTO `interview_managerment`.`job_schedule` (`job_schedule_id`, `job_id`, `schedule_id`) VALUES ('3', '3', '3');


INSERT INTO `interview_managerment`.`job_skill` (`job_skill_id`, `job_id`, `skill_id`) VALUES ('1', '1', '1');
INSERT INTO `interview_managerment`.`job_skill` (`job_skill_id`, `job_id`, `skill_id`) VALUES ('2', '2', '2');
INSERT INTO `interview_managerment`.`job_skill` (`job_skill_id`, `job_id`, `skill_id`) VALUES ('3', '3', '3');
