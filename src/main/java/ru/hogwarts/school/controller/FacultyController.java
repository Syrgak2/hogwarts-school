package ru.hogwarts.school.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {
    private final FacultyService service;

    public FacultyController(FacultyService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable long id) {
        Faculty faculty = service.findFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return service.addFaculty(faculty);
    }

    @PutMapping
    public ResponseEntity<Faculty> editeFaculty(@RequestBody Faculty faculty) {
        Faculty foundFaculty = service.updateFaculty(faculty);
        if (foundFaculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> removeFaculty(@PathVariable Long id) {
        service.removeFaculty(id);

        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Faculty>> filterByColorOrName(@RequestParam(required = false) String color,
                                                             @RequestParam(required = false) String name) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(service.getFacultiesByColor(color));
        }
        if (name != null && !name.isBlank()) {
            return ResponseEntity.ok(service.getFacultiesByName(name));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getStudents(@PathVariable Long id) {
        if (id != null) {
            return ResponseEntity.ok(service.getStudents(id));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }

    @GetMapping("/longestName")
    public ResponseEntity<String> getLongestFacultyName() {
        return ResponseEntity.ok(service.getLongestName());
    }

}
