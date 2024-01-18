package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.Map;

public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();

    private long lastId = 0;

    public Faculty addFaculty(Faculty faculty) {
        facultyMap.put(++lastId, faculty);
        faculty.setId(lastId);
        return faculty;
    }

    public Faculty findeFaculty(Long id) {
        return facultyMap.get(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    public Faculty removeFaculty(Long id) {
        return      facultyMap.remove(id);
    }
}
