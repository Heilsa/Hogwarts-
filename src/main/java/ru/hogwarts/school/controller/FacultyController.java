package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/faculty")
@Tag(name = "Факультеты", description = "API для управления факультетами Хогвартса")
public class FacultyController {

    private static final Logger logger = LoggerFactory.getLogger(FacultyController.class);

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
    public Faculty getFaculty(@PathVariable Long id) {
        return facultyService.getFaculty(id);
    }

    @PutMapping
    @Operation(summary = "Обновить данные факультета")
    public Faculty updateFaculty(@RequestBody Faculty faculty) {
        return facultyService.updateFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить факультет")
    public void deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping("/color/{color}")
    @Operation(summary = "Фильтр факультетов по цвету")
    public Collection<Faculty> getFacultiesByColor(@PathVariable String color) {
        return facultyService.getFacultiesByColor(color);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить все факультеты")
    public Collection<Faculty> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @GetMapping("/search")
    @Operation(summary = "Найти факультеты по имени или цвету (без учета регистра)")
    public Collection<Faculty> findFacultiesByNameOrColor(@RequestParam String nameOrColor) {
        return facultyService.findFacultiesByNameOrColor(nameOrColor);
    }

    @GetMapping("/{id}/students")
    public ResponseEntity<List<Student>> getFacultyStudents(@PathVariable Long id) {
        List<Student> students = facultyService.getFacultyStudents(id);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/longest-name")
    @Operation(summary = "Получить самое длинное название факультета")
    public ResponseEntity<String> getLongestFacultyName() {
        logger.info("Request to get longest faculty name");
        String longestName = facultyService.getLongestFacultyName();
        return ResponseEntity.ok(longestName);
    }
}