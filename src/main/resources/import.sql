INSERT INTO lecture (id, name, lecturer, description) VALUES (1, 'spring start', 'kim', 'spring basic start');

INSERT INTO lecture (id, name, lecturer, description) VALUES (2, 'JPA start', 'lee', 'jpa basic start');

-- lecture_schedule 데이터
INSERT INTO lecture_schedule (id, lecture_id, date, start_time, end_time, capacity, current_capacity) VALUES (1, 1, CURRENT_DATE() + 1, '22:00:00', '23:00:00', 30, 0);

INSERT INTO lecture_schedule (id, lecture_id, date, start_time, end_time, capacity, current_capacity) VALUES (2, 1, CURRENT_DATE() + 1, '22:00:00', '23:00:00', 30, 0);

INSERT INTO lecture_schedule (id, lecture_id, date, start_time, end_time, capacity, current_capacity) VALUES (3, 2, CURRENT_DATE() + 1, '22:00:00', '23:00:00', 30, 0);