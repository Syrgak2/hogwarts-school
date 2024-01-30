package ru.hogwarts.school.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.service.StudentAvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class StudentAvatarController {
    private final StudentAvatarService studentAvatarService;

    public StudentAvatarController(StudentAvatarService studentAvatarService) {
        this.studentAvatarService = studentAvatarService;
    }

    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 500) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        studentAvatarService.uploadAvatar(id, avatar);

        return ResponseEntity.ok().build();
    }
}
