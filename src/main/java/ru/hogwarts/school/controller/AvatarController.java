package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.model.Avatar;
import ru.hogwarts.school.service.AvatarService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/avatar")
@Tag(name = "Аватары", description = "API для управления аватарами студентов")
public class AvatarController {

    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/{studentId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Загрузить аватар для студента")
    public ResponseEntity<String> uploadAvatar(
            @PathVariable Long studentId,
            @RequestParam MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
        return ResponseEntity.ok("Аватар успешно загружен");
    }

    @GetMapping("/{id}/download")
    @Operation(summary = "Скачать аватар по ID")
    public ResponseEntity<byte[]> downloadAvatar(@PathVariable Long id) {
        Avatar avatar = avatarService.getAvatarByStudentId(id);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(avatar.getMediaType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + id + ".png")
                .body(avatar.getData());
    }

    // ========== НОВЫЙ ЭНДПОИНТ С ПАГИНАЦИЕЙ ДЛЯ ДОМАШНЕГО ЗАДАНИЯ ==========
    @GetMapping
    @Operation(summary = "Получить все аватары с пагинацией")
    public ResponseEntity<Page<Avatar>> getAllAvatars(
            @Parameter(description = "Номер страницы (начиная с 0)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Количество элементов на странице", example = "10")
            @RequestParam(defaultValue = "10") int size) {

        Page<Avatar> avatars = avatarService.getAllAvatars(page, size);
        return ResponseEntity.ok(avatars);
    }
}