package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FacultyService {
    private final Map<Long, Faculty> facultyMap = new HashMap<>();

    private long lastId = 0;

    public Faculty addFaculty(Faculty faculty) {
        facultyMap.put(++lastId, faculty);
        faculty.setId(lastId);
        return faculty;
    }

    public Faculty findFaculty(Long id) {
        return facultyMap.get(id);
    }

    public Faculty updateFaculty(Faculty faculty) {
        if (facultyMap.containsKey(faculty.getId())) {
            facultyMap.put(faculty.getId(), faculty);
            return faculty;
        }
        return null;
    }

    public Faculty removeFaculty(Long id) {
        if (facultyMap.containsKey(id)) {
            return facultyMap.remove(id);
        }
        return null;
    }

    public List<Faculty> filterFacultyByColor(String color) {
        List<Faculty> tmp = new ArrayList<>();
        for (Faculty faculty : facultyMap.values()) {
            if (faculty.getColor().equals(color)) {
                tmp.add(faculty);
            }
        }
        return tmp;
    }
}
