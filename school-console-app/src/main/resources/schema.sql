DROP TABLE IF EXISTS schedule.groups CASCADE;
DROP TABLE IF EXISTS schedule.students CASCADE;
DROP TABLE IF EXISTS schedule.courses CASCADE;
DROP TABLE IF EXISTS schedule.students_courses CASCADE;

CREATE SCHEMA IF NOT EXISTS schedule;

CREATE TABLE IF NOT EXISTS schedule.groups
(
    group_id SERIAL NOT NULL,
    group_name character varying(10) NOT NULL,
    CONSTRAINT groups_pkey PRIMARY KEY (group_id)
);

CREATE TABLE IF NOT EXISTS schedule.students
(
    student_id SERIAL NOT NULL,
    group_id integer,
    first_name character varying(50) NOT NULL,
    last_name character varying(50) NOT NULL,
    CONSTRAINT students_pkey PRIMARY KEY (student_id),
    CONSTRAINT group_id FOREIGN KEY (group_id)
        REFERENCES schedule.groups (group_id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS schedule.courses
(
    course_id SERIAL NOT NULL,
    course_name character varying(50) NOT NULL,
    course_description character varying,
    CONSTRAINT courses_pkey PRIMARY KEY (course_id)
);

CREATE TABLE IF NOT EXISTS schedule.students_courses
(
    student_id integer NOT NULL,
    course_id integer NOT NULL,
    CONSTRAINT students_courses_pkey PRIMARY KEY (student_id, course_id),
    CONSTRAINT course_id FOREIGN KEY (course_id) REFERENCES schedule.courses (course_id)
    ON UPDATE NO ACTION
    ON DELETE CASCADE,
    CONSTRAINT student_id FOREIGN KEY (student_id) REFERENCES schedule.students (student_id)
    ON UPDATE NO ACTION
    ON DELETE CASCADE
);
