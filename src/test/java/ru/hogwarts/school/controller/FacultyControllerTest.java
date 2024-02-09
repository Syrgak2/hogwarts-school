package ru.hogwarts.school.controller;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentAvatarService;
import ru.hogwarts.school.service.StudentService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.hogwarts.school.constans.Constants.*;

@WebMvcTest
public class FacultyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private FacultyService facultyService;

    @MockBean
    private StudentService studentservice;

    @MockBean
    private StudentAvatarService studentAvatarService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void testAddFaculty() throws Exception {
//        Given
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", FACULTY_1.getName());
        facultyObject.put("color", FACULTY_1.getColor());

//        When
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY_1);
//Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_1.getId()))
                .andExpect(jsonPath("$.name").value(FACULTY_1.getName()))
                .andExpect(jsonPath("$.color").value(FACULTY_1.getColor()));
    }

    @Test
    public void findFacultyTest()throws Exception {
//        Given
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", FACULTY_1.getName());
        facultyObject.put("color", FACULTY_1.getColor());

//        When
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(FACULTY_1));
//Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + FACULTY_1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_1.getId()))
                .andExpect(jsonPath("$.name").value(FACULTY_1.getName()))
                .andExpect(jsonPath("$.color").value(FACULTY_1.getColor()));
    }

    @Test
    public void editeFacultyTest() throws Exception{
//        Given
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", FACULTY_1.getId());
        facultyObject.put("name", FACULTY_1.getName());
        facultyObject.put("color", FACULTY_1.getColor());

//        When
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(FACULTY_1));
        when(facultyRepository.save(any(Faculty.class))).thenReturn(FACULTY_1);
//Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(FACULTY_1.getId()))
                .andExpect(jsonPath("$.name").value(FACULTY_1.getName()))
                .andExpect(jsonPath("$.color").value(FACULTY_1.getColor()));

    }

    @Test
    public void removeFacultyTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/" + FACULTY_1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(facultyService, Mockito.times(1)).removeFaculty(FACULTY_1.getId());
    }

    @Test
    public void filterByColorOrName() throws Exception{
//        Тестирует когда передан параметр name
        whenFilterByName();
//        Тестирует когда передан параметр name
        whenFilterByColor();

    }

    private void whenFilterByName() throws  Exception{
        //        Given
        List<Faculty> excepted = new ArrayList<>();
        excepted.add(FACULTY_1);
//        When
        when(facultyRepository.findByNameContainsIgnoreCase(anyString())).thenReturn(excepted);
//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?name=" + FACULTY_1.getName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(FACULTY_1.getName())));
    }

    private void whenFilterByColor() throws Exception{
//        When
        when(facultyRepository.findByColorContainsIgnoreCase(anyString())).thenReturn(FACULTY_SORTED_LIST);
//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty?color=" + COLOR_FOR_FILTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(FACULTY_SORTED_LIST.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(FACULTY_SORTED_LIST.get(1).getName())));
    }

    @Test
    public void getStudentsTest() throws Exception{
//        Given
        List<Student> excepted = new ArrayList<>();
        excepted.add(STUDENT_1);
        excepted.add(STUDENT_2);
        FACULTY_1.setStudents(excepted);
//        When
        when(facultyRepository.findById(anyLong())).thenReturn(Optional.of(FACULTY_1));
//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/" + FACULTY_1.getId() + "/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(excepted.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(excepted.get(1).getName())));

    }
}
