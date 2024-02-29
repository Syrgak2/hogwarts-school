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
import static org.mockito.ArgumentMatchers.anyString;
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
        when(facultyRepository.save(FACULTY_FOR_POST_PUT)).thenReturn(FACULTY_FOR_POST_PUT);
//        When
        Faculty excepted = facultyService.addFaculty(FACULTY_FOR_POST_PUT);
//        Then
        assertEquals(FACULTY_FOR_POST_PUT, excepted);
    }

    @Test
    public void findFaculty() {
//        Given
        when(facultyRepository.findById(FACULTY_FOR_POST_PUT.getId())).thenReturn(Optional.of(FACULTY_FOR_POST_PUT));
//        When
        Faculty excepted = facultyService.findFaculty(FACULTY_FOR_POST_PUT.getId());
//        Then
        assertEquals(FACULTY_FOR_POST_PUT, excepted);
    }

    @Test
    public void updateFaculty() {
//        Given
        when(facultyRepository.save(FACULTY_FOR_POST_PUT)).thenReturn(FACULTY_FOR_POST_PUT);
//        When
        Faculty excepted = facultyService.updateFaculty(FACULTY_FOR_POST_PUT);
//        Then
        assertEquals(FACULTY_FOR_POST_PUT, excepted);
    }

    @Test
    public void filterFaculty() {
//        Given
        when(facultyRepository.findByColorContainsIgnoreCase(anyString())).thenReturn(FACULTY_SORTED_BY_COLOR);
//        When
        List<Faculty> excepted = facultyService.getFacultiesByColor(FACULTY_3_FOR_FILTER.getColor());
//        Then
        assertEquals(FACULTY_SORTED_BY_COLOR, excepted);
    }

    @Test
    public void whenFilterByName() {
//        Given
        when(facultyRepository.findByNameContainsIgnoreCase(anyString())).thenReturn(FACULTY_SORTED_BY_COLOR);
//        When
        List<Faculty> excepted = facultyService.getFacultiesByName(FACULTY_3_FOR_FILTER.getName());
//        Then
        assertEquals(FACULTY_SORTED_BY_COLOR, excepted);
    }
}
