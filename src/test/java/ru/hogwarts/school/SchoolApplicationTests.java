package ru.hogwarts.school;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentService;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.hogwarts.school.constans.Constants.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
class SchoolApplicationTests {
	@LocalServerPort
	private Integer port;

	@Autowired
	private StudentController studentController;

	@Autowired
	private StudentService studentServices;

	@Autowired
	FacultyService facultyService;

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Test
	public void contextLoads() throws Exception {
		assertThat(studentController).isNotNull();
	}

	@Test
	public void postStudent() throws Exception{
//		When
		ResponseEntity<Student> response = testRestTemplate.postForEntity("http://localhost:" + port + "/students", STUDENT_1, Student.class);
//		Then
		Assertions.assertThat(response).isNotNull();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	public void testGetStudent() {
//		Given
		studentServices.addStudent(STUDENT_1);
//		When
		ResponseEntity<Student> response = testRestTemplate.getForEntity("http://localhost:" + port + "/students/" + STUDENT_1.getId(), Student.class);
//		Then
		assertThat(response).isNotNull();
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(STUDENT_1.getName());
		assertThat(Objects.requireNonNull(response.getBody()).getAge()).isEqualTo(STUDENT_1.getAge());
	}

	@Test
	public void testEditeStudent() {
//		Given
		studentServices.addStudent(new Student(1L, "jdfskj", 1));
//		When
		ResponseEntity<Student> response = testRestTemplate.postForEntity("http://localhost:" + port + "/students", STUDENT_1, Student.class);
//		Then
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(STUDENT_1.getName());
	}

	@Test
	public void testRemoveStudent() {
//		given
		studentServices.addStudent(STUDENT_1);
//		When
		testRestTemplate.delete("http://localhost:" + port + "/students/" + STUDENT_1.getId());
//		Then
		ResponseEntity<Student> response = testRestTemplate.getForEntity("http://localhost:" + port + "/students/" + STUDENT_1.getId(), Student.class);
		assertThat(response.getStatusCode().value()).isEqualTo(500);
	}

	@Test
	public void tesFilterByAge() {
//		Given
		studentServices.addStudent(STUDENT_1);
		studentServices.addStudent(STUDENT_2);
		studentServices.addStudent(STUDENT_3);
		STUDENT_1.setFaculty(null);
		STUDENT_2.setFaculty(null);
		STUDENT_3.setFaculty(null);

//		When
//		Тестирует когда передана age
		testWhenFilterByAge();
//		Тестирует когда передана min и max
		testWhenFilterByMinMaxAge();
	}

	private void testWhenFilterByAge() {
		ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
		ResponseEntity<List<Student>> response = testRestTemplate.exchange("http://localhost:" + port + "/students?age=" + STUDENT_2.getAge(),
				HttpMethod.GET,
				null,
				responseType);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertEquals(Objects.requireNonNull(response.getBody()).get(0).getName(), STUDENT_2.getName());
	}

	private void testWhenFilterByMinMaxAge() {
		ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
		ResponseEntity<List<Student>> response = testRestTemplate.exchange("http://localhost:" + port + "/students?min=" + 21 + "&max=" + 25,
				HttpMethod.GET,
				null,
				responseType);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertEquals(response.getBody(), STUDENT_SORTED_LIST);
	}

	@Test
	public void testGetFaculty() {
//		given
		facultyService.addFaculty(FACULTY_1);
		STUDENT_4.setFaculty(FACULTY_1);
		studentServices.addStudent(STUDENT_4);

//		When
		ResponseEntity<Faculty> response = testRestTemplate.getForEntity("http://localhost:" + port + "/students/" + STUDENT_4.getId() + "/faculty", Faculty.class);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody()).getName()).isEqualTo(FACULTY_1.getName());

	}

}
