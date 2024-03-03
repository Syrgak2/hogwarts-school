package ru.hogwarts.school.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FacultyService {
    private FacultyRepository facultyRepo;
    @Autowired
    StudentService studentService;

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

    public List<Faculty> findAll() {
        return facultyRepo.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.trace("Wos invoked method for update faculty");
        return facultyRepo.save(faculty);
    }

    public void removeFaculty(Long id) {
              logger.trace("Wos invoked method for remove faculty");
        Faculty faculty = findFaculty(id);
        List<Student> students = faculty.getStudents();
        for (Student element : students) {
            element.setFaculty(null);
        }
        studentService.saveAll(students);
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

    public String getLongestName() {
        Optional<String> longestName = findAll().stream()
                .map(Faculty::getName)
                .max(Comparator.comparingInt(String::length));
        return longestName.get();
    }
}
