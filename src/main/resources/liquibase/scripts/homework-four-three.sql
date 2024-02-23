-- liquibase formatted sql

-- changeset karybekov:1
CREATE INDEX student_name_index ON student (name);
CREATE INDEX faculty_name_color_index ON faculty (name);

-- changeset karybekov:2
DROP INDEX faculty_name_color_index;

-- changeset karybekov:3
CREATE INDEX faculty_name_color_index ON faculty(name, color);