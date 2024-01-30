package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.StudentAvatar;
import ru.hogwarts.school.service.StudentAvatarService;

import java.io.IOException;

@RestController
@RequestMapping("/avatar")
public class StudentAvatarController {
    private final StudentAvatarService studentAvatarService;

    public StudentAvatarController(StudentAvatarService studentAvatarService) {
        this.studentAvatarService = studentAvatarService;
    }

    @PostMapping(value = "/{id}/post", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAvatar(@PathVariable Long id, @RequestParam MultipartFile avatar) throws IOException {
        if (avatar.getSize() > 1024 * 500) {
            return ResponseEntity.badRequest().body("File is too big");
        }
        studentAvatarService.uploadAvatar(id, avatar);

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/{id}/preview")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        StudentAvatar avatar = studentAvatarService.findAvatar(id);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    @GetMapping(value = "/{id}")
    public void downloadAvatar(@PathVariable Long id, HttpServletResponse response) throws IOException {
        studentAvatarService.getAvatar(id, response);

    }
}
