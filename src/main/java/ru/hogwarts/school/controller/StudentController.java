package ru.hogwarts.school.controller;

import org.apache.catalina.valves.StuckThreadDetectionValve;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService service;

    public StudentController(StudentService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudent(@PathVariable long id) {
        Student student = service.findStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return service.addStudent(student);
    }

    @PutMapping
    public ResponseEntity<Student> editeStudent(@RequestBody Student student) {
        Student foundStudent = service.updateStudent(student);
        if (foundStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(foundStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Student> removeStudent(@PathVariable long id) {
        Student student = service.removeStudent(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/filter/{age}")
    public List<Student> filterByAge(@PathVariable int age) {
        return service.filterStudentByAge(age);
    }
}
