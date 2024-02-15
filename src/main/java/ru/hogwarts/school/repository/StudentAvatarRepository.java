package ru.hogwarts.school.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.hogwarts.school.model.StudentAvatar;

import java.util.Optional;

@Repository
public interface StudentAvatarRepository extends JpaRepository<StudentAvatar, Long> {
    Optional<StudentAvatar> findByStudentId(Long id);
}
