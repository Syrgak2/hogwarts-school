package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.Student;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findStudentsByAge(int age);

    List<Student> findByAgeBetween(int min, int max);

    @Query(value = "SELECT COUNT(*) FROM student", nativeQuery = true)
    Integer countStudents();

    @Query(value = "SELECT AVG(age) FROM student", nativeQuery = true)
    Integer countAverageAge();

    @Query(value = "SELECT * FROM student ORDER BY id DESC LIMIT 5", nativeQuery = true)
    List<Student> finFirst5ByOrderByIdDesc();

    @Query(value = "SELECT * FROM student WHERE UPPER(SUBSTRING(name, 1, 1)) = 'A'", nativeQuery = true)
    List<Student> findWhoseNameStartsA();
}
