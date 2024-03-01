package ru.hogwarts.school.contrloller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.hogwarts.school.constans.Constants.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
public class FacultyControllerTest {
    @LocalServerPort
    private Integer port;

    @Autowired
    FacultyController facultyController;

    @Autowired
    FacultyService facultyService;

    @Autowired
    StudentService studentService;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void contextLoads() throws Exception {
        assertThat(facultyController).isNotNull();
    }

    @Test
    public void testCreateFaculty() throws IOException {
//        When
        ResponseEntity<Faculty> response = testRestTemplate.postForEntity(
                HOST + port + "/faculty",
                FACULTY_FOR_POST_PUT,
                Faculty.class
        );
//       Then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(FACULTY_FOR_POST_PUT, response.getBody());
    }

    @Test
    public void testGetFaculty() {
//        When
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                HOST + port + "/faculty/" + 1,
                Faculty.class
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FACULTY_FOR_POST_PUT, response.getBody());
    }

    @Test
    public void testEditeFaculty() {
//        Given
        Faculty faculty = new Faculty(1L, "TEST", "TEST");
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(faculty);
//        When
        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                HOST + port + "/faculty",
                HttpMethod.PUT,
                requestEntity,
                Faculty.class
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(faculty, response.getBody());
    }

    @Test
    public void tesRemoveFaculty() {
//        Given
        facultyService.addFaculty(FACULTY_2_FOR_REMOVE);
        Long id = facultyService.getFacultiesByName(FACULTY_2_FOR_REMOVE.getName()).get(0).getId();
//        When
        ResponseEntity<Faculty> removeResponse = testRestTemplate.exchange(
                HOST + port + "/faculty/" + id,
                HttpMethod.DELETE,
                null,
                Faculty.class
        );

//		Then
        assertThat(removeResponse.getStatusCode().value()).isEqualTo(200);

//		Дополнительный тест
//		Пытаемся, получить только что удаленный объект.
//		Должно вернуть статус 500
//		Иначе тест провален.
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                HOST + port + "/faculty/" + FACULTY_2_FOR_REMOVE.getId(),
                Faculty.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    public void testFilterByColorOrName() {
//        Given
        facultyService.addFaculty(FACULTY_3_FOR_FILTER);
//        Когда передан параметр color
        testWhenFilterByColor();
//        Когда передан параметр name
        testWhenFilterByName();
    }

    private void testWhenFilterByName() {
//        When
        ParameterizedTypeReference<List<Faculty>> responseType = new ParameterizedTypeReference<List<Faculty>>() {};
        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(
                HOST + port + "/faculty?name=" + FACULTY_3_FOR_FILTER.getName(),
                HttpMethod.GET,
                null,
                responseType
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FACULTY_3_FOR_FILTER, Objects.requireNonNull(response.getBody()).get(0));
    }

    private void testWhenFilterByColor() {
//        Given

//        When
        ParameterizedTypeReference<List<Faculty>> responseType = new ParameterizedTypeReference<List<Faculty>>() {};
        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(
                HOST + port + "/faculty?color=" + FACULTY_3_FOR_FILTER.getColor(),
                HttpMethod.GET,
                null,
                responseType
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FACULTY_SORTED_BY_COLOR, response.getBody());
    }

    @Test
    public void testGetStudents() {
//        Given
        facultyService.addFaculty(FACULTY_4_FOR_GET_STUDENTS);
        STUDENT_4.setFaculty(FACULTY_4_FOR_GET_STUDENTS);
        studentService.addStudent(STUDENT_4);
        List<Student> students = new ArrayList<>();
        students.add(STUDENT_4);

//        When
        ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                HOST + port + "/faculty/" + FACULTY_4_FOR_GET_STUDENTS.getId() + "/students",
                HttpMethod.GET,
                null,
                responseType
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, response.getBody());

    }

    @Test
    public void getLongestFacultyNameTest() {
//        Given
        facultyService.addFaculty(FACULTY_3_FOR_FILTER);
        String excepted = FACULTY_3_FOR_FILTER.getName();
//        When
        ResponseEntity<String> response = testRestTemplate.exchange(
                HOST + port + "/faculty/longestName",
                HttpMethod.GET,
                null,
                String.class
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(excepted, response.getBody());
    }
}
