package ru.hogwarts.school.contrloller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
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
                FACULTY_1,
                Faculty.class
        );
//       Then
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(FACULTY_1, response.getBody());
    }

    @Test
    public void testGetFaculty() {
//        Given
        facultyService.addFaculty(FACULTY_1);
//        When
        ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
                HOST + port + "/faculty/" + FACULTY_1.getId(),
                Faculty.class
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FACULTY_1, response.getBody());
    }

    @Test
    public void testEditeFaculty() {
//        Given
        facultyService.addFaculty(new Faculty(1L, "fasd", "adsa"));
        HttpEntity<Faculty> requestEntity = new HttpEntity<>(FACULTY_1);
//        When
        ResponseEntity<Faculty> response = testRestTemplate.exchange(
                HOST + port + "/faculty",
                HttpMethod.PUT,
                requestEntity,
                Faculty.class
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FACULTY_1, response.getBody());
    }

    @Test
    public void tesRemoveFaculty() {
//        Given
        facultyService.addFaculty(FACULTY_1);
//        When
        ResponseEntity<Faculty> removeResponse = testRestTemplate.exchange(
                HOST + port + "/faculty/" + FACULTY_1.getId(),
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
                HOST + port + "/faculty/" + FACULTY_1.getId(),
                Faculty.class
        );
        assertThat(response.getStatusCode().value()).isEqualTo(500);
    }

    @Test
    public void testFilterByColorOrName() {
//        Given
        facultyService.addFaculty(FACULTY_1);
        facultyService.addFaculty(FACULTY_2);
        facultyService.addFaculty(FACULTY_3);
//        Когда передан параметр color
        testWhenFilterByColor();
//        Когда передан параметр name
        testWhenFilterByName();
    }

    private void testWhenFilterByName() {
//        When
        ParameterizedTypeReference<List<Faculty>> responseType = new ParameterizedTypeReference<List<Faculty>>() {};
        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(
                HOST + port + "/faculty?name=" + FACULTY_1.getName(),
                HttpMethod.GET,
                null,
                responseType
        );
//        Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(FACULTY_1, Objects.requireNonNull(response.getBody()).get(0));
    }

    private void testWhenFilterByColor() {
//        When
        ParameterizedTypeReference<List<Faculty>> responseType = new ParameterizedTypeReference<List<Faculty>>() {};
        ResponseEntity<List<Faculty>> response = testRestTemplate.exchange(
                HOST + port + "/faculty?color=" + COLOR_FOR_FILTER,
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
        facultyService.addFaculty(FACULTY_1);
        STUDENT_2.setFaculty(FACULTY_1);
        STUDENT_1.setFaculty(FACULTY_1);
        studentService.addStudent(STUDENT_1);
        studentService.addStudent(STUDENT_2);
        List<Student> students = new ArrayList<>();
        students.add(STUDENT_1);
        students.add(STUDENT_2);

//        When
        ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
        ResponseEntity<List<Student>> response = testRestTemplate.exchange(
                HOST + port + "/faculty/" + FACULTY_1.getId() + "/students",
                HttpMethod.GET,
                null,
                responseType
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(students, response.getBody());

    }
}
