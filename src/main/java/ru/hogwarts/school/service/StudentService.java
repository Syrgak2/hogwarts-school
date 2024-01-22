package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private StudentRepository studentRepo;

    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student addStudent(Student student) {
        return studentRepo.save(student);
    }

    public Student findStudent(long id) {
        return studentRepo.findById(id).get();
    }

    public Student updateStudent(Student student) {
        return studentRepo.save(student);
    }

    public void removeStudent(long id) {
        studentRepo.deleteById(id);
    }

    public List<Student> filterStudentByAge(int age) {
        List<Student> students = new ArrayList<>(studentRepo.findAll());
        return students.stream()
                .filter(e -> e.getAge() == age)
                .collect(Collectors.toList());
    }

}
