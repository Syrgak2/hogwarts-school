package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentAvatar;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentAvatarRepository;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentAvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.ByteArrayOutputStream;
import java.nio.file.Files;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.hogwarts.school.constans.Constants.*;

@WebMvcTest
public class StudentControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private StudentAvatarRepository studentAvatarRepository;

    @MockBean
    private FacultyRepository facultyRepository;

    @SpyBean
    private StudentService studentService;

    @SpyBean
    private FacultyService facultyService;

    @MockBean
    private StudentAvatarService studentAvatarService;

    @InjectMocks
    private StudentController studentController;

    @Test
    public void testAddStudent() throws Exception {
//        Given
        JSONObject studentObject = new JSONObject();
        studentObject.put("name", STUDENT_1.getName());
        studentObject.put("age", STUDENT_1.getAge());

//        When
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT_1);
//Then
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/students")
                        .content(studentObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_1.getId()))
                .andExpect(jsonPath("$.name").value(STUDENT_1.getName()))
                .andExpect(jsonPath("$.age").value(STUDENT_1.getAge()));
    }

    @Test
    public void findStudentTest()throws Exception {
//        When
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT_1));
//Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + STUDENT_1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_1.getId()))
                .andExpect(jsonPath("$.name").value(STUDENT_1.getName()))
                .andExpect(jsonPath("$.age").value(STUDENT_1.getAge()));
    }

    @Test
    public void editeFacultyTest() throws Exception{
//        Given
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("id", STUDENT_1.getId());
        facultyObject.put("name", STUDENT_1.getName());
        facultyObject.put("age", STUDENT_1.getAge());

//        When
        when(studentRepository.findById(anyLong())).thenReturn(Optional.of(STUDENT_1));
        when(studentRepository.save(any(Student.class))).thenReturn(STUDENT_1);
//Then
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/students")
                        .content(facultyObject.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(STUDENT_1.getId()))
                .andExpect(jsonPath("$.name").value(STUDENT_1.getName()))
                .andExpect(jsonPath("$.age").value(STUDENT_1.getAge()));

    }

    @Test
    public void removeFacultyTest() throws Exception{
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/students/" + STUDENT_1.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        Mockito.verify(studentService, Mockito.times(1)).removeStudent(STUDENT_1.getId());
    }

    @Test
    public void filterByAgeOrAgeBetween() throws Exception{
//        Тестирует когда передан возраст для сортировки
        whenFilterByAge();
//        Тестирует когда передан min and max age для сортировки между ними
        whenFilterBetweenAge();
    }

    private void whenFilterByAge() throws Exception{
//        When
        when(studentRepository.findStudentsByAge(anyInt())).thenReturn(STUDENT_SORTED_LIST);
//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?age=" + STUDENT_1.getAge())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(STUDENT_SORTED_LIST.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(STUDENT_SORTED_LIST.get(1).getName())));
    }

    private void whenFilterBetweenAge() throws Exception{
//        When
        when(studentRepository.findByAgeBetween(anyInt(), anyInt())).thenReturn(STUDENT_SORTED_LIST);
//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students?min=20&max=20")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name", is(STUDENT_SORTED_LIST.get(0).getName())))
                .andExpect(jsonPath("$[1].name", is(STUDENT_SORTED_LIST.get(1).getName())));
    }
    @Test
    public void getFacultyTest() throws Exception{
//        Given
        STUDENT_2.setFaculty(FACULTY_2);
//        When
        when(studentRepository.findById(STUDENT_2.getId())).thenReturn(Optional.of(STUDENT_2));
//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + STUDENT_2.getId() + "/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(FACULTY_2.getName())));
    }

    @Test
    public void uploadAvatarTest() throws Exception{
//        Given
        ClassPathResource resource = new ClassPathResource("test-images/1.png");
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        MockMultipartFile file = new MockMultipartFile("avatar", "avatar.png", "image/png", fileContent);
//        When
        when(studentAvatarService.uploadAvatar(anyLong(), any(MockMultipartFile.class))).thenReturn(true);
//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/students/1/avatar/post")
                        .file(file)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());
    }

    @Test
    public void downloadAvatarPreviewTest() throws Exception {
//        Given
        ClassPathResource resource = new ClassPathResource("test-images/1.png");
        byte[] fileContent = Files.readAllBytes(resource.getFile().toPath());
        String mediaType = "image/png";
        StudentAvatar avatar = new StudentAvatar(1L, resource.toString(), fileContent.length, mediaType, fileContent, STUDENT_2);
//        When
        when(studentAvatarService.findAvatar(anyLong())).thenReturn(avatar);

//        Then
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/students/" + STUDENT_2.getId() + "/avatar/preview"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.parseMediaType(mediaType)))
                .andReturn().getResponse();
    }

}
