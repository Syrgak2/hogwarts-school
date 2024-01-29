package ru.hogwarts.school.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.constans.Constants.*;
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void addStudent() {
//        Given
        when(studentRepository.save(STUDENT_1)).thenReturn(STUDENT_1);
//        When
        Student actual = studentService.addStudent(STUDENT_1);
//        Then
        assertEquals(STUDENT_1, actual);
    }

    @Test
    public void findStudent() {
//        given
        when(studentRepository.findById(anyLong())).thenReturn((Optional.of(STUDENT_1)));
//        When
        Student actual = studentService.findStudent(STUDENT_1.getId());
//        Then
        assertEquals(STUDENT_1, actual);
    }

    @Test
    public void updateStudent() {
//        Given
        when(studentRepository.save(STUDENT_1)).thenReturn(STUDENT_1);
//        When
        Student excepted = studentService.updateStudent(STUDENT_1);
//        Then
        assertEquals(STUDENT_1, excepted);
    }

    @Test
    public void filterStudent() {
//        Given
        when(studentRepository.findStudentsByAge(anyInt())).thenReturn(STUDENT_SORTED_LIST);
//        When
        List<Student> excepted = studentService.filterStudentByAge(22);
//        Then
        assertEquals(STUDENT_SORTED_LIST, excepted);
    }

    @Test
    public void whenFilterBetweenAge() {
//        Given
        when(studentRepository.findByAgeBetween(anyInt(), anyInt())).thenReturn(STUDENT_SORTED_LIST);
//        When
        List<Student> excepted = studentService.findByAgeBetween(10, 22);
//        Then
        assertEquals(STUDENT_SORTED_LIST, excepted);
    }
}
