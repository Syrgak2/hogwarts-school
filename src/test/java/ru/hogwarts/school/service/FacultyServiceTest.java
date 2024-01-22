package ru.hogwarts.school.service;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static ru.hogwarts.school.constans.Constants.*;

@ExtendWith(MockitoExtension.class)
public class FacultyServiceTest {
    @Mock
    FacultyRepository facultyRepository;

    @InjectMocks
    FacultyService facultyService;



    @Test
    public void addFaculty() {
//        Given
        when(facultyRepository.save(FACULTY_1)).thenReturn(FACULTY_1);
//        When
        Faculty excepted = facultyService.addFaculty(FACULTY_1);
//        Then
        assertEquals(FACULTY_1, excepted);
    }

    @Test
    public void findFaculty() {
//        Given
        when(facultyRepository.findById(FACULTY_1.getId())).thenReturn(Optional.of(FACULTY_1));
//        When
        Faculty excepted = facultyService.findFaculty(FACULTY_1.getId());
//        Then
        assertEquals(FACULTY_1, excepted);
    }

    @Test
    public void updateFaculty() {
//        Given
        when(facultyRepository.save(FACULTY_1)).thenReturn(FACULTY_1);
//        When
        Faculty excepted = facultyService.updateFaculty(FACULTY_1);
//        Then
        assertEquals(FACULTY_1, excepted);
    }

    @Test
    public void filterFaculty() {
//        Given
        when(facultyRepository.findAll()).thenReturn(FACULTY_LIST);
//        When
        List<Faculty> excepted = facultyService.filterFacultyByColor(COLOR_FOR_FILTER);
//        Then
        assertEquals(FACULTY_SORTED_LIST, excepted);
    }
}
