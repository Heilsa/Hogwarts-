package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;

@RestController
@RequestMapping("/faculty")
@Tag(name = "Факультеты", description = "API для управления факультетами Хогвартса")
public class FacultyController {

    private final FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @Operation(summary = "Создать новый факультет")
    public Faculty createFaculty(@RequestBody Faculty faculty) {
        return facultyService.createFaculty(faculty);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить факультет по ID")
    public Faculty getFaculty(
            @Parameter(description = "ID факультета", example = "1")
            @PathVariable Long id) {
        return facultyService.getFaculty(id);
    }

    @PutMapping
    @Operation(summary = "Обновить данные факультета")
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить факультет")
    public void deleteFaculty(
            @Parameter(description = "ID факультета", example = "1")
            @PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("/color/{color}")
    @Operation(summary = "Фильтр факультетов по цвету")
    public Collection<Faculty> getFacultiesByColor(
            @Parameter(description = "Цвет для фильтрации", example = "красный")
            @PathVariable String color) {
        return facultyService.getFacultiesByColor(color);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все факультеты")
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }
}