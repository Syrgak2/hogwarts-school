package ru.hogwarts.school.contrloller;

import org.h2.command.dml.MergeUsing;
import org.junit.jupiter.api.BeforeEach;
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
import ru.hogwarts.school.model.StudentAvatar;
import ru.hogwarts.school.service.FacultyService;
import ru.hogwarts.school.service.StudentAvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
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

	@BeforeEach
	public void setUp() {
		studentServices.addStudent(STUDENT_1);
		studentServices.addStudent(STUDENT_2);
		studentServices.addStudent(STUDENT_3);
	}


	@Test
	public void contextLoads() throws Exception {
		assertThat(studentController).isNotNull();
	}

	@Test
	public void testPostStudent() throws Exception{
//		When
		ResponseEntity<Student> response = testRestTemplate.postForEntity(
				HOST + port + "/students",
				STUDENT_1,
				Student.class
		);
//		Then
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertEquals(response.getBody(), STUDENT_1);
	}

	@Test
	public void testGetStudent() {
//		When
		ResponseEntity<Student> response = testRestTemplate.getForEntity(
				HOST + port + "/students/" + STUDENT_1.getId(),
				Student.class
		);
//		Then
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody())).isEqualTo(STUDENT_1);
	}

	@Test
	public void testEditeStudent() {
//		Given
		studentServices.addStudent(new Student(1L, "jdfskj", 1));
//		When
		ResponseEntity<Student> response = testRestTemplate.postForEntity(
				HOST + port + "/students", STUDENT_1,
				Student.class
		);
//		Then
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertThat(Objects.requireNonNull(response.getBody())).isEqualTo(STUDENT_1);
	}

	@Test
	public void testRemoveStudent() {
//		When
		ResponseEntity<Student> removeResponse = testRestTemplate.exchange(
				HOST + port + "/students/" + STUDENT_1.getId(),
				HttpMethod.DELETE,
				null,
				Student.class
		);

//		Then
		assertThat(removeResponse.getStatusCode().value()).isEqualTo(200);

//		Дополнительный тест
//		Пытаемся, получить только что удаленный объект.
//		Должно вернуть статус 500
//		Иначе тест провален.
		ResponseEntity<Student> response = testRestTemplate.getForEntity(
				HOST + port + "/students/" + STUDENT_1.getId(),
				Student.class
		);
		assertThat(response.getStatusCode().value()).isEqualTo(500);
	}

	@Test
	public void tesFilterByAge() {
//		Given
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
//		Given
		List<Student> excepted = new ArrayList<>();
		excepted.add(STUDENT_2);
		ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
		ResponseEntity<List<Student>> response = testRestTemplate.exchange(
				HOST + port + "/students/filter?age=" + STUDENT_2.getAge(),
				HttpMethod.GET,
				null,
				responseType
		);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertEquals(excepted, response.getBody());
	}

	private void testWhenFilterByMinMaxAge() {
		ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
		ResponseEntity<List<Student>> response = testRestTemplate.exchange(
				HOST + port + "/students/filter?min=" + 21 + "&max=" + 25,
				HttpMethod.GET,
				null,
				responseType
		);
		assertThat(response.getStatusCode().value()).isEqualTo(200);
		assertEquals(STUDENT_SORTED_LIST, response.getBody());
	}

	@Test
	public void testGetFaculty() {
//		given
		facultyService.addFaculty(FACULTY_1);
		STUDENT_4.setFaculty(FACULTY_1);
		studentServices.addStudent(STUDENT_4);

//		When
		ResponseEntity<Faculty> response = testRestTemplate.getForEntity(
				HOST + port + "/students/" + STUDENT_4.getId() + "/faculty",
				Faculty.class
		);
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
				HOST + port + "/students/" + STUDENT_4.getId() + "/avatar/post",
				requestEntity,
				String.class
		);
//		Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}


	private void testDownloadAvatarPreview() {
		ResponseEntity<byte[]> response = testRestTemplate.getForEntity(
				HOST + port + "/students/" + STUDENT_4.getId() + "/avatar/preview",
				byte[].class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	private void testDownloadAvatar() {
		ResponseEntity<byte[]> response = testRestTemplate.getForEntity(
				HOST + port + "/students/" + STUDENT_4.getId() + "/avatar",
				byte[].class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

	@Test
	public void testGetAmountOfStudents() {
		ResponseEntity<Integer> response = testRestTemplate.getForEntity(
				HOST + port + "/students/count",
				Integer.class
		);

		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(3, response.getBody());
	}

	@Test
	public void testGetAverageAge() {
//		Given
		Integer actual = (int) STUDENT_LIST.stream()
				.mapToInt(Student::getAge)
				.average()
				.orElse(0.0);
//		When
		ResponseEntity<Integer> response = testRestTemplate.getForEntity(
				HOST + port + "/students/average",
				Integer.class
		);
//		Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(actual, response.getBody());
	}

	@Test
	public void testGetLastFiveStudent() {
//		Given
		ParameterizedTypeReference<List<Student>> responseType = new ParameterizedTypeReference<List<Student>>() {};
//		When
		ResponseEntity<List<Student>> response = testRestTemplate.exchange(
				HOST + port + "/students/lastStudents",
				HttpMethod.GET,
				null,
				responseType
		);
//		Then
		assertEquals(HttpStatus.OK, response.getStatusCode());
	}

}
