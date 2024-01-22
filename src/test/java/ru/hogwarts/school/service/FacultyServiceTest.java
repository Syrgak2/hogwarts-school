package ru.hogwarts.school.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static ru.hogwarts.school.constans.Constants.*;

public class FacultyServiceTest {
    FacultyService facultyService;
    FacultyRepository facultyRepository;

    @BeforeEach
    public void setUp() {
        facultyService = new FacultyService(facultyRepository);
    }

    @Test
    public void addFaculty() {
//        When
        Faculty excepted = facultyService.addFaculty(FACULTY_1);
//        Then
        assertEquals(FACULTY_1, excepted);
    }

    @Test
    public void findFaculty() {
//        Given
        facultyService.addFaculty(FACULTY_1);
//        When
        Faculty excepted = facultyService.findFaculty(FACULTY_1.getId());
//        Then
        assertEquals(FACULTY_1, excepted);
    }

    @Test
    public void updateFaculty() {
//        Given
        facultyService.addFaculty(FACULTY_FOR_EDITE);
//        When
        Faculty excepted = facultyService.updateFaculty(FACULTY_1);
//        Then
        assertEquals(FACULTY_1, excepted);
    }

//    @Test
//    public void removeFaculty() {
////        Given
//        facultyService.addFaculty(FACULTY_1);
////        When
////        Faculty excepted = facultyService.removeFaculty(FACULTY_1.getId());
////        Then
////        assertEquals(FACULTY_1, excepted);
//    }

    @Test
    public void filterFaculty() {
//        Given
        facultyService.addFaculty(FACULTY_1);
        facultyService.addFaculty(FACULTY_2);
        facultyService.addFaculty(FACULTY_3);
//        When
        List<Faculty> excepted = facultyService.filterFacultyByColor(COLOR_FOR_FILTER);
//        Then
        assertEquals(FACULTY_LIST, excepted);
    }

    @Test
    public void whenFacultyNull() {
//        When
//        Faculty removeActual = facultyService.removeFaculty(0L);
        Faculty updateActual = facultyService.updateFaculty(FACULTY_1);
//        Then
//        assertNull(removeActual);
        assertNull(updateActual);
    }
}
