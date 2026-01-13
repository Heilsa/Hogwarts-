 package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;

@RestController
@RequestMapping("/student")
@Tag(name = "Студенты", description = "API для управления студентами Хогвартса")  // Исправлено: Студенты
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Создать нового студента",
            description = "Создает нового студента и возвращает его с присвоенным ID")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить студента по ID")
    public Student getStudent(
            @Parameter(description = "ID студента", example = "1")
            @PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping
    @Operation(summary = "Обновить данные студента")
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить студента")
    public void deleteStudent(
            @Parameter(description = "ID студента", example = "1")
            @PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/age/{age}")
    @Operation(summary = "Фильтр студентов по возрасту")
    public Collection<Student> getStudentsByAge(
            @Parameter(description = "Возраст для фильтрации", example = "17")
            @PathVariable int age) {
        return studentService.getStudentsByAge(age);
    }

    @GetMapping("/all")
    @Operation(summary = "Получить всех студентов")
    public Collection<Student> getAllStudents() {
        return studentService.getAllStudents();
    }
}