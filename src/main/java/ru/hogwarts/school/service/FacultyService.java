package ru.hogwarts.school.service;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;

@Service
public class FacultyService {
    private FacultyRepository facultyRepo;
    @Autowired
    StudentService studentService;

    public FacultyService(FacultyRepository facultyRepository) {
        this.facultyRepo = facultyRepository;
    }

    public Faculty addFaculty(Faculty faculty) {
        return facultyRepo.save(faculty);
    }

    public Faculty findFaculty(Long id) {
        return facultyRepo.findById(id).get();
    }

    public List<Faculty> findAll() {
        return facultyRepo.findAll();
    }

    public Faculty updateFaculty(Faculty faculty) {
        return facultyRepo.save(faculty);
    }

    public void removeFaculty(Long id) {
        Faculty faculty = findFaculty(id);
        List<Student> students = faculty.getStudents();
        for (Student element : students) {
            element.setFaculty(null);
        }
        studentService.saveAll(students);
        facultyRepo.deleteById(id);
    }

    public List<Faculty> getFacultiesByColor(String color) {
        return facultyRepo.findByColorContainsIgnoreCase(color);
    }

    public List<Faculty> getFacultiesByName(String name) {
        return facultyRepo.findByNameContainsIgnoreCase(name);
    }

    public List<Student> getStudents(Long id) {
        return findFaculty(id).getStudents();
    }
}
