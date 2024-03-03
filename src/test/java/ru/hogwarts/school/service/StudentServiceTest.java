package ru.hogwarts.school.service;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.constans.Constants.*;
@ExtendWith(MockitoExtension.class)
public class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private PageRequest pageRequest;

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

    @Test
    public void testCountStudents() {
//        Given
        when(studentRepository.countStudents()).thenReturn(5);
//        When
        Integer excepted = studentService.countStudents();
//        Then
        assertEquals(5, excepted);
    }

    @Test
    public void testCountAverageAge() {
//        Given
        when(studentRepository.countAverageAge()).thenReturn(20);
//        When
        Integer excepted = studentService.countAverageAge();
//        Then
        assertEquals(20, excepted);
    }

    @Test
    public void testFindLastFiveStudents() {
//        Given
        when(studentRepository.finFirst5ByOrderByIdDesc()).thenReturn(STUDENT_SORTED_LIST);
//        When
        List<Student> excepted = studentService.findLastFiveStudents();
//        Then
        assertEquals(STUDENT_SORTED_LIST, excepted);
    }

    @Test
    public void findAllStudentsTest() {
//        Given
        PageImpl<Student> page = new PageImpl<>(STUDENT_LIST);

        when(studentRepository.findAll(any(PageRequest.class))).thenReturn(page);
//        When
        List<Student> excepted = studentService.findAllStudents(1, 1);
//        Then
        assertEquals(excepted.get(0), STUDENT_LIST.get(0));
    }

    @Test
    public void FindWhoseNameStartATest() {
//        Given
//         Меняет 'a' в имени студента на 'A' для проверки
        STUDENT_NAME_START_A.get(2).setName("AStudent3");
        when(studentRepository.findWhoseNameStartsA()).thenReturn(STUDENT_NAME_START_A);
//        When
        List<Student> actual = studentService.findWhoseNameStartsA();
//        Then
        assertEquals(STUDENT_NAME_START_A, actual);
    }

    @Test
    public void getAverageAgeTest() {
//        Given
        double sum = 0;
        for (Student student : STUDENT_LIST) {
            sum += student.getAge();
        }
        Double excepted = sum / STUDENT_LIST.size();
        when(studentRepository.findAll()).thenReturn(STUDENT_LIST);
//        when
        double actual = studentService.getAverageAge();
//        Then
        assertEquals(excepted, actual);
    }
}
