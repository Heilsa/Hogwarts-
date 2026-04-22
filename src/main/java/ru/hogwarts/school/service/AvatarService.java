package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.AvatarRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class AvatarService {

    private static final Logger logger = LoggerFactory.getLogger(AvatarService.class);

    @Value("${avatars.directory.path}")
    private String avatarsDir;

    private final AvatarRepository avatarRepository;
    private final StudentService studentService;

    public AvatarService(AvatarRepository avatarRepository, StudentService studentService) {
        this.avatarRepository = avatarRepository;
        this.studentService = studentService;
        logger.debug("AvatarService initialized with avatars directory: {}", avatarsDir);
    }

    public void uploadAvatar(Long studentId, MultipartFile file) throws IOException {
        logger.info("Was invoked method for upload avatar for student with id = {}", studentId);

        Student student = studentService.getStudent(studentId);
        if (student == null) {
            logger.error("Student with id = {} not found, cannot upload avatar", studentId);
            throw new RuntimeException("Student not found");
        }

        Path uploadPath = Paths.get(avatarsDir);
        if (!Files.exists(uploadPath)) {
            logger.debug("Creating avatars directory: {}", avatarsDir);
            Files.createDirectories(uploadPath);
        }

        String fileName = studentId + "." + getFileExtension(file.getOriginalFilename());
        Path filePath = uploadPath.resolve(fileName);

        logger.debug("Saving avatar to: {}", filePath);
        file.transferTo(filePath.toFile());

        Avatar avatar = avatarRepository.findByStudentId(studentId).orElse(new Avatar());
        avatar.setStudent(student);
        avatar.setFilePath(filePath.toString());
        avatar.setFileSize(file.getSize());
        avatar.setMediaType(file.getContentType());
        avatar.setData(file.getBytes());

        avatarRepository.save(avatar);
        logger.info("Avatar uploaded successfully for student {}", student.getName());
    }

    public Avatar getAvatarByStudentId(Long studentId) {
        logger.info("Was invoked method for get avatar by student id = {}", studentId);
        Optional<Avatar> avatar = avatarRepository.findByStudentId(studentId);
        if (avatar.isPresent()) {
            logger.debug("Avatar found for student id = {}", studentId);
            return avatar.get();
        } else {
            logger.warn("Avatar not found for student id = {}", studentId);
            return null;
        }
    }

    public byte[] getAvatarFromFileSystem(Long studentId) throws IOException {
        logger.info("Was invoked method for get avatar file by student id = {}", studentId);
        Avatar avatar = getAvatarByStudentId(studentId);
        if (avatar == null) {
            logger.warn("Avatar not found for student id = {}", studentId);
            return null;
        }

        Path filePath = Paths.get(avatar.getFilePath());
        if (!Files.exists(filePath)) {
            logger.error("Avatar file not found at path: {}", avatar.getFilePath());
            return null;
        }

        byte[] data = Files.readAllBytes(filePath);
        logger.debug("Avatar file read successfully, size: {} bytes", data.length);
        return data;
    }

    public Page<Avatar> getAllAvatars(int page, int size) {
        logger.info("Was invoked method for get all avatars, page = {}, size = {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<Avatar> avatarPage = avatarRepository.findAll(pageable);
        logger.debug("Found {} avatars on page {} of {}",
                avatarPage.getContent().size(),
                page,
                avatarPage.getTotalPages());
        return avatarPage;
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex + 1) : "";
    }
}