package ru.hogwarts.school.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.StudentAvatar;
import ru.hogwarts.school.service.StudentAvatarService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/avatar")
public class AvatarTController {
    @Autowired
    private StudentAvatarService avatarService;

    @GetMapping
    public ResponseEntity<List<StudentAvatar>> getAllAvatar(@RequestParam("page") Integer pageNumber,
                                                            @RequestParam("size") Integer pageSize) {
        return ResponseEntity.ok(avatarService.findAll(pageNumber, pageSize));
    }
}
