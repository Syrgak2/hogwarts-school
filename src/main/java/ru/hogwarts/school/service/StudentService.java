package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepo;

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
        return studentRepo.findStudentsByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        return studentRepo.findByAgeBetween(min, max);
    }

    public Faculty getStudentsFaculty(Long id) {
        return findStudent(id).getFaculty();
    }

    public Integer countStudents() {
        return studentRepo.countStudents();
    }

    public Integer countAverageAge() {
        return studentRepo.countAverageAge();
    }

    public List<Student> findLastFiveStudents() {
        return studentRepo.finFirst5ByOrderByIdDesc();
    }
}
