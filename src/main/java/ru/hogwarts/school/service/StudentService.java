package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepo;
    Logger logger = LoggerFactory.getLogger(StudentService.class);

    public StudentService(StudentRepository studentRepo) {
        this.studentRepo = studentRepo;
    }

    public Student addStudent(Student student) {
        logger.trace("Wos invoked method for save student");
        return studentRepo.save(student);
    }

    public Student findStudent(long id) {
        logger.trace("Wos invoked method for find student");
        return studentRepo.findById(id).get();
    }

    public Student updateStudent(Student student) {
        logger.trace("Wos invoked method for edite student");
        return studentRepo.save(student);
    }

    public void removeStudent(long id) {
        logger.trace("Wos invoked method for remove student");
        studentRepo.deleteById(id);
    }

    public List<Student> filterStudentByAge(int age) {
        logger.trace("Wos invoked method for filter students by age");
        return studentRepo.findStudentsByAge(age);
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.trace("Wos invoked method for filter students between age");
        return studentRepo.findByAgeBetween(min, max);
    }

    public Faculty getStudentsFaculty(Long id) {
        logger.trace("Wos invoked method for get students faculty");
        return findStudent(id).getFaculty();
    }

    public Integer countStudents() {
        logger.trace("Wos invoked method for get students amount ");
        return studentRepo.countStudents();
    }

    public Integer countAverageAge() {
        logger.trace("Wos invoked method for count students average age");
        return studentRepo.countAverageAge();
    }

    public List<Student> findLastFiveStudents() {
        logger.trace("Wos invoked method for find last five students");
        return studentRepo.finFirst5ByOrderByIdDesc();
    }

    public List<Student> findAllStudents(Integer pageNumber, Integer pageSize) {
        logger.trace("Wos invoked method for get all students");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return studentRepo.findAll(pageRequest).getContent();
    }

    public List<Student> findAll() {
        return studentRepo.findAll();
    }

    public List<Student> findWhoseNameStartsA() {
//        Сортировку вынес на уровень бд
        return studentRepo.findWhoseNameStartsA().stream()
                .peek(student -> {
                    String capitalized = student.getName().substring(0, 1).toUpperCase() + student.getName().substring(1);
                    student.setName(capitalized);
                })
                .sorted(Comparator.comparing(Student::getName))
                .collect(Collectors.toList());
    }

    public Double getAverageAge() {
        return findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0);
    }
}
