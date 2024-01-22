package ru.hogwarts.school.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private FacultyRepository facultyRepo;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepo = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepo.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepo.findById(id).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepo.save(faculty);
    }

    public void removeFaculty(Long id) {
        facultyRepo.deleteById(id);
    }

    public List<Faculty> filterFacultyByColor(String color) {
        List<Faculty> faculties = new ArrayList<>(facultyRepo.findAll());
        return faculties.stream()
                .filter(e -> e.getColor().equals(color))
                .collect(Collectors.toList());
    }
}
