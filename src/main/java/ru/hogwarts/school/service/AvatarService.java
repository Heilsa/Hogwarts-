package ru.hogwarts.school.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class AvatarService {

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    @Value("${avatars.directory.path}")
    private String avatarsDir;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        Student student = studentService.getStudent(studentId);

        Path uploadPath = Paths.get(avatarsDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String fileName = studentId + "." + getExtension(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);
        Files.write(filePath, file.getBytes());

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
    }

    public Avatar getAvatarByStudentId(Long studentId) {
        return avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new RuntimeException("Аватар не найден"));
    }

    // ========== НОВЫЙ МЕТОД С ПАГИНАЦИЕЙ ДЛЯ ДОМАШНЕГО ЗАДАНИЯ ==========
    public Page<Avatar> getAllAvatars(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return avatarRepository.findAll(pageable);
    }

    private String getExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }
}