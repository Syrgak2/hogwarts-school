package ru.hogwarts.school.service;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.model.StudentAvatar;
import ru.hogwarts.school.repository.StudentAvatarRepository;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
@Transactional
public class StudentAvatarService {
    @Value("${student.avatar.dir.path}")
    private String avatarDir;

    Logger logger = LoggerFactory.getLogger(StudentAvatarService.class);

    private final StudentService studentService;
    private final StudentAvatarRepository avatarRepo;

    public StudentAvatarService(StudentService studentService, StudentAvatarRepository avatarRepo) {
        this.studentService = studentService;
        this.avatarRepo = avatarRepo;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.trace("Wos invoked method for upload avatar");
        Path filePath = Path.of(avatarDir, studentId + "." + getExtension(Objects.requireNonNull(file.getOriginalFilename())));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);

        try (
                InputStream is = file.getInputStream();
                OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            bis.transferTo(bos);
        }
        saveAvatarToDataBase(studentId, filePath, file);
    }

    public void getAvatar(Long studentId, HttpServletResponse response) throws IOException {
        logger.trace("Wos invoked method for get avatar");
        StudentAvatar avatar = findAvatar(studentId);

        Path path = Path.of(avatar.getFilePath());

        try (
                InputStream is = Files.newInputStream(path);
                OutputStream os = response.getOutputStream();
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                BufferedOutputStream bos = new BufferedOutputStream(os, 1024);
        ) {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            bis.transferTo(os);
        }
    }

    public StudentAvatar findAvatar(Long studentId) {
        logger.trace("Wos invoked method for get avatar from database");
        return avatarRepo.findByStudentId(studentId).orElse(new StudentAvatar());
    }

    public Boolean removeAvatar(Long studentId) throws IOException {
        logger.trace("Wos invoked method for remove avatar");
        StudentAvatar avatar = findAvatar(studentId);
        if (avatar != null) {
            logger.warn("avatar is null");
        }
        Path path = Path.of(avatar.getFilePath());
        try {
            Files.delete(path);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }

    public List<StudentAvatar> findAll(Integer pageNumber, Integer pageSize) {
        logger.trace("Wos invoked method for get all avatars");
        PageRequest page = PageRequest.of(pageNumber - 1, pageSize);
        return avatarRepo.findAll(page).getContent();
    }


    private String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    private byte[] generateImagePreview(Path filePath) throws IOException {
        if (filePath == null) {
            logger.warn("filePath is null");
        }
        assert filePath != null;
        try (
                InputStream is = Files.newInputStream(filePath);
                BufferedInputStream bis = new BufferedInputStream(is, 1024);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ) {
            BufferedImage image = ImageIO.read(bis);

            int height = image.getHeight() / (image.getWidth() / 100);
            BufferedImage preview = new BufferedImage(100, height, image.getType());
            Graphics2D graphics = preview.createGraphics();
            graphics.drawImage(image, 0, 0, 100, height, null);
            graphics.dispose();

            ImageIO.write(preview, getExtension(filePath.getFileName().toString()), baos);
            return baos.toByteArray();
        }
    }

    private void saveAvatarToDataBase(Long studentId, Path filePath, MultipartFile file) throws IOException {
        Student student = studentService.findStudent(studentId);
        if (student == null) {
            logger.warn("Students is null");
        }
        StudentAvatar avatar = findAvatar(studentId);
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(generateImagePreview(filePath));

        avatarRepo.save(avatar);
    }
}
