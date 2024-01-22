package ru.hogwarts.school.constans;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final Student STUDENT_1 = new Student(1L, "Student1", 22);
    public static final Student STUDENT_2 = new Student(2L, "Student2", 21);
    public static final Student STUDENT_3 = new Student(3L, "Student2", 22);
    public static final Student STUDENT_FOR_EDITE = new Student(1L, "StudentForUpdate", 25);

    public static final List<Student> STUDENT_LIST = new ArrayList<>(List.of(
            STUDENT_1,
            STUDENT_3
    ));


    public static final Faculty FACULTY_1 = new Faculty(1L, "Faculty1", "gold");
    public static final Faculty FACULTY_2 = new Faculty(2L, "Faculty2", "blue");
    public static final Faculty FACULTY_3 = new Faculty(3L, "Faculty3", "gold");
    public static final Faculty FACULTY_FOR_EDITE = new Faculty(1L, "FacultyForEdite", "black");
    public static final String COLOR_FOR_FILTER = "gold";

    public static final List<Faculty> FACULTY_LIST = new ArrayList<>(List.of(
            FACULTY_1,
            FACULTY_3
    ));

}