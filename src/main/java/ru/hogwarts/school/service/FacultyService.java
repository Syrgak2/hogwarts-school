package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {
    private FacultyRepository facultyRepo;

    Logger logger = LoggerFactory.getLogger(FacultyService.class);

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepo = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        logger.trace("Wos invoked method for create faculty");
        return facultyRepo.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        logger.trace("Wos invoked method for get faculty");
        return facultyRepo.findById(id).get();
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.trace("Wos invoked method for update faculty");
        return facultyRepo.save(faculty);
    }

    public void removeFaculty(Long id) {
        logger.trace("Wos invoked method for remove faculty");
        facultyRepo.deleteById(id);
    }

    public List<Faculty> getFacultiesByColor(String color) {
        logger.trace("Wos invoked method for get faculties by color");
        return facultyRepo.findByColorContainsIgnoreCase(color);
    }

    public List<Faculty> getFacultiesByName(String name) {
        logger.trace("Wos invoked method for get faculties by name");
        return facultyRepo.findByNameContainsIgnoreCase(name);
    }

    public List<Student> getStudents(Long id) {
        logger.trace("Wos invoked method for get faculty students ");
        return findFaculty(id).getStudents();
    }
}
