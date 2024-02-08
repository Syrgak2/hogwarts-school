package ru.hogwarts.school.contrloller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentAvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.hogwarts.school.constans.Constants.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@TestPropertySource(locations = "classpath:application-test.properties")
class StudentControllerTests {
	@LocalServerPort
	private Integer port;

	@Autowired
	private StudentController studentController;

	@Autowired
	private StudentService studentServices;

	@Autowired
	FacultyService facultyService;

	@Autowired
	StudentAvatarService avatarService;

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
		assertThat(Objects.requireNonNull(response.getBody())).isEqualTo(STUDENT_1);
	}

	@Test
	public void testEditeStudent() {
//		Given
		studentServices.addStudent(new Student(1L, "jdfskj", 1));
//		When
		ResponseEntity<Student> response = testRestTemplate.postForEntity("http://localhost:" + port + "/students", STUDENT_1, Student.class);
//		Then
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody())).isEqualTo(STUDENT_1);
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
		assertEquals(Objects.requireNonNull(response.getBody()).get(0), STUDENT_2);
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
//		Then
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody())).isEqualTo(FACULTY_1);
	}


//	Тестирует endpoints
//  /{id}/avatar/post (загрузка avatar)
//  /{id}/avatar/preview (отправка из базы)
//	/{id}/avatar (отправка из локального диска)
	@Test
	public void testUploadGetFindAvatar() throws IOException {
//		 Given
		studentServices.addStudent(STUDENT_4);
		testUploadAvatar();
		testDownloadAvatarPreview();
		testDownloadAvatar();

//		Удаляет файл после теста
		avatarService.removeAvatar(STUDENT_4.getId());

	}

	private void testUploadAvatar() {
//		Подготовка тела
		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("avatar", new ClassPathResource("test-images/1.png"));

//		Подготовка заголовок
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, httpHeaders);

//		When
		ResponseEntity<String> response = testRestTemplate.postForEntity(
				"http://localhost:" + port + "/students/" + STUDENT_4.getId() + "/avatar/post",
				requestEntity,
				String.class
		);
//		Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}


	private void testDownloadAvatarPreview() {
		ResponseEntity<byte[]> response = testRestTemplate.getForEntity(
				"http://localhost:" + port + "/students/" + STUDENT_4.getId() + "/avatar/preview",
				byte[].class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	private void testDownloadAvatar() {
		ResponseEntity<byte[]> response = testRestTemplate.getForEntity(
				"http://localhost:" + port + "/students/" + STUDENT_4.getId() + "/avatar",
				byte[].class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
