package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentAvatar;
import ru.hogwarts.school.service.StudentAvatarService;
import ru.hogwarts.school.service.StudentService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentAvatarService studentAvatarService;

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        Student student = studentService.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editeStudent(@RequestBody Student student) {
        Student foundStudent = studentService.updateStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> removeStudent(@PathVariable long id) {
        studentService.removeStudent(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Student>> filterByAge(@RequestParam(required = false) Integer age,
                                                     @RequestParam(required = false) Integer min,
                                                     @RequestParam(required = false) Integer max) {
        if (age != null) {
            return ResponseEntity.ok(studentService.filterStudentByAge(age));
        }
        if (min != null && max != null) {
            return ResponseEntity.ok(studentService.findByAgeBetween(min, max));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping
    ResponseEntity<List<Student>> getStudentsByPage(@RequestParam("page") Integer pageNumber,
                                                    @RequestParam("size") Integer pageSize) {
        return ResponseEntity.ok(studentService.findAllStudents(pageNumber, pageSize));
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        if (id != null) {
            return ResponseEntity.ok(studentService.getStudentsFaculty(id));
        }
        return ResponseEntity.notFound().build();
    }


    @PostMapping(value = "/{id}/avatar/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 500) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        studentAvatarService.uploadAvatar(id, avatar);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/avatar/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        StudentAvatar avatar = studentAvatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}/avatar")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        studentAvatarService.getAvatar(id, response);
    }

    @GetMapping("count")
    public ResponseEntity<Integer> getAmountOfStudents() {
        return ResponseEntity.ok(studentService.countStudents());
    }

    @GetMapping("average")
    public ResponseEntity<Integer> getAverageAge() {
        return ResponseEntity.ok(studentService.countAverageAge());
    }

    @GetMapping("lastStudents")
    public ResponseEntity<List<Student>> getLAstFiveStudents() {
        return ResponseEntity.ok(studentService.findLastFiveStudents());
    }
}
