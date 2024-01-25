package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

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
        return facultyRepo.findFacultiesByColor(color);
    }
}
