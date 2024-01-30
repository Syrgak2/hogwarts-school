package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.hogwarts.school.model.StudentAvatar;

import java.util.Optional;

public interface StudentAvatarRepository extends JpaRepository<StudentAvatar, Long> {
    Optional<StudentAvatar> findByStudentId(Long id);
}
