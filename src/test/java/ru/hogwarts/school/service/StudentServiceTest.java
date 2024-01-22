package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.hogwarts.school.constans.Constants.*;

public class StudentServiceTest {
    private StudentService studentService;

    @BeforeEach
    public void setUp() {
        studentService = new StudentService();
    }

    @Test
    public void addStudent() {
//        When
        Student actual = studentService.addStudent(STUDENT_1);
//        Then
        assertEquals(STUDENT_1, actual);
    }

    @Test
    public void findStudent() {
//        given
        studentService.addStudent(STUDENT_1);
//        When
        Student actual = studentService.findStudent(STUDENT_1.getId());
//        Then
        assertEquals(STUDENT_1, actual);
    }

    @Test
    public void removeStudent() {
//        Given
        studentService.addStudent(STUDENT_1);
//        When
        Student actual = studentService.removeStudent(STUDENT_1.getId());
//        Then
        assertEquals(STUDENT_1, actual);
    }

    @Test
    public void updateStudent() {
//        Given
        studentService.addStudent(STUDENT_FOR_EDITE);
//        When
        Student excepted = studentService.updateStudent(STUDENT_1);
//        Then
        assertEquals(STUDENT_1, excepted);
    }

    @Test
    public void filterStudent() {
//        Given
        studentService.addStudent(STUDENT_1);
        studentService.addStudent(STUDENT_2);
        studentService.addStudent(STUDENT_3);
//        When
        List<Student> excepted = studentService.filterStudentByAge(22);
//        Then
        assertEquals(STUDENT_LIST, excepted);
    }

    @Test
    public void whenStudentNull() {
//        When
        Student removeActual = studentService.removeStudent(0L);
        Student updateActual = studentService.updateStudent(STUDENT_1);
//        Then
        assertNull(removeActual);
        assertNull(updateActual);
    }
}
