package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import javax.persistence.EntityNotFoundException;  // Добавь этот импорт
import java.util.Collection;

@RestController
@RequestMapping("/student")
@Tag(name = "Студенты", description = "API для управления студентами Хогвартса")
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

    @GetMapping("/age/between")
    @Operation(summary = "Найти студентов по возрастному диапазону")
    public Collection<Student> getStudentsByAgeBetween(
            @Parameter(description = "Минимальный возраст", example = "10")
            @RequestParam int minAge,
            @Parameter(description = "Максимальный возраст", example = "20")
            @RequestParam int maxAge) {
        return studentService.getStudentsByAgeBetween(minAge, maxAge);
    }

    @GetMapping("/{id}/faculty")
    public ResponseEntity<Faculty> getStudentFaculty(@PathVariable Long id) {
        Faculty faculty = studentService.getStudentFaculty(id);
        if (faculty == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(faculty);
    }

    @PostMapping("/{studentId}/faculty/{facultyId}")
    @Operation(summary = "Назначить факультет студенту")
    public ResponseEntity<Student> assignStudentToFaculty(
            @Parameter(description = "ID студента", example = "1")
            @PathVariable Long studentId,
            @Parameter(description = "ID факультета", example = "1")
            @PathVariable Long facultyId) {
        Student student = studentService.assignStudentToFaculty(studentId, facultyId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleEntityNotFound(EntityNotFoundException e) {
        return ResponseEntity.notFound().build();
    }
}