package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/student")
@Tag(name = "Студенты", description = "API для управления студентами Хогвартса")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    @Operation(summary = "Создать нового студента")
    public Student createStudent(@RequestBody Student student) {
        return studentService.createStudent(student);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить студента по ID")
    public Student getStudent(@PathVariable Long id) {
        return studentService.getStudent(id);
    }

    @PutMapping
    @Operation(summary = "Обновить данные студента")
    public Student updateStudent(@RequestBody Student student) {
        return studentService.updateStudent(student);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить студента")
    public void deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping("/age/{age}")
    @Operation(summary = "Фильтр студентов по возрасту")
    public Collection<Student> getStudentsByAge(@PathVariable int age) {
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
            @RequestParam int minAge,
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
            @PathVariable Long studentId,
            @PathVariable Long facultyId) {
        Student student = studentService.assignStudentToFaculty(studentId, facultyId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/count")
    @Operation(summary = "Получить количество всех студентов")
    public ResponseEntity<Integer> getCountOfStudents() {
        Integer count = studentService.getCountOfStudents();
        return ResponseEntity.ok(count);
    }

    @GetMapping("/average-age")
    @Operation(summary = "Получить средний возраст студентов")
    public ResponseEntity<Double> getAverageAgeOfStudents() {
        Double averageAge = studentService.getAverageAgeOfStudents();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/last/{count}")
    @Operation(summary = "Получить последних N студентов")
    public ResponseEntity<List<Student>> getLastNStudents(
            @Parameter(description = "Количество студентов", example = "5")
            @PathVariable int count) {
        List<Student> students = studentService.getLastNStudents(count);
        return ResponseEntity.ok(students);
    }
}