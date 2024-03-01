package ru.hogwarts.school.constans;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.List;

public class Constants {
    public static final String HOST = "http://localhost:";
    public static final Student STUDENT_1 = new Student(1L, "AStudent1", 22);

    public static final Student STUDENT_2 = new Student(2L, "AStudent2", 20);
    public static final Student STUDENT_3 = new Student(3L, "aStudent3", 22);

    public static final Student STUDENT_4 = new Student(4L, "Student4", 18);
    public static final Student STUDENT_FOR_AVATAR = new Student(5L, "StudentForUpdate", 18);

    public static final List<Student> STUDENT_SORTED_LIST = new ArrayList<>(List.of(
            STUDENT_1,
            STUDENT_3
    ));

    public static final List<Student> STUDENT_LIST = new ArrayList<>(List.of(
            STUDENT_1,
            STUDENT_2,
            STUDENT_3,
            STUDENT_4,
            STUDENT_FOR_AVATAR
    ));

    public static final List<Student> STUDENT_NAME_START_A = new ArrayList<>(List.of(
            STUDENT_1,
            STUDENT_2,
            STUDENT_3
    ));


    public static final Faculty FACULTY_FOR_POST_PUT = new Faculty(1L, "Faculty1", "gold");
    public static final Faculty FACULTY_2_FOR_REMOVE = new Faculty(2L, "Faculty2", "blue");
    public static final Faculty FACULTY_3_FOR_FILTER = new Faculty(3L, "Faculty3", "black");
    public static final Faculty FACULTY_4_FOR_GET_STUDENTS = new Faculty(4L, "Faculty4", "green");


    public static final List<Faculty> FACULTY_SORTED_BY_COLOR = new ArrayList<>(List.of(
            FACULTY_3_FOR_FILTER
    ));

    public static final List<Faculty> FACULTY_LIST = new ArrayList<>(List.of(
            FACULTY_FOR_POST_PUT,
            FACULTY_2_FOR_REMOVE,
            FACULTY_3_FOR_FILTER
    ));

}
