package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {
    private final Map<Long, Student> studentMap = new HashMap<>();
    private long lastId = 0;

    public Student addStudent(Student student) {
        studentMap.put(++lastId, student);
        student.setId(lastId);
        return student;
    }

    public Student findStudent(long id) {
        return studentMap.get(id);
    }

    public Student updateStudent(Student student) {
        if (studentMap.containsKey(student.getId())) {
            studentMap.put(student.getId(), student);
            return student;
        }
        return null;
    }

    public Student removeStudent(long id) {
        if (studentMap.containsKey(id)) {
            return studentMap.remove(id);
        }
        return null;
    }

    public List<Student> filterStudentByAge(int age) {
        List<Student> tmp = new ArrayList<>();
        for (long i = 1; i <= lastId; i++) {
            Student student = studentMap.get(i);
            if (student.getAge() == age) {
                tmp.add(student);
            }
        }
        return tmp;
    }

}
