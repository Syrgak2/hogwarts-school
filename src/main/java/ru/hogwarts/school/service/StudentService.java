package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.Map;

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
        studentMap.put(student.getId(), student);
        return student;
    }

    public Student removeStudent(long id) {
        return studentMap.remove(id);
    }

}
