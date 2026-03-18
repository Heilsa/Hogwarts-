package ru.hogwarts.school.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import javax.persistence.EntityNotFoundException;  // ВАЖНО: для Spring Boot 2.x используем javax
import java.util.Arrays;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
class StudentControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /student/{id} - получение студента по ID")
    void getStudentByIdTest() throws Exception {
        // given
        Long id = 1L;
        Student student = new Student(id, "Гермиона Грейнджер", 18);

        when(studentService.getStudent(id)).thenReturn(student);

        // when & then
        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Гермиона Грейнджер"))
                .andExpect(jsonPath("$.age").value(18));
    }

    @Test
    @DisplayName("GET /student/{id} - студент не найден")
    void getStudentByIdNotFoundTest() throws Exception {
        // given
        Long id = 999L;

        when(studentService.getStudent(id)).thenThrow(new EntityNotFoundException("Студент не найден"));

        // when & then
        mockMvc.perform(get("/student/{id}", id))
                .andExpect(status().isNotFound());  // 404 Not Found
    }

    @Test
    @DisplayName("PUT /student - обновление студента")
    void updateStudentTest() throws Exception {
        // given
        Long id = 1L;
        Student studentToUpdate = new Student(id, "Рональд Уизли", 18);

        when(studentService.updateStudent(any(Student.class))).thenReturn(studentToUpdate);

        // when & then - ИСПРАВЛЕНО
        mockMvc.perform(put("/student")  // PUT, а не POST
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentToUpdate)))
                .andExpect(status().isOk())  // 200 OK
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Рональд Уизли"))
                .andExpect(jsonPath("$.age").value(18));

        verify(studentService, times(1)).updateStudent(any(Student.class));
    }

    @Test
    @DisplayName("POST /student - создание студента")
    void createStudentTest() throws Exception {
        // given
        Student studentToCreate = new Student(null, "Гарри Поттер", 17);
        Student createdStudent = new Student(1L, "Гарри Поттер", 17);

        when(studentService.createStudent(any(Student.class))).thenReturn(createdStudent);

        // when & then
        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));
    }

    @Test
    @DisplayName("DELETE /student/{id} - удаление студента")
    void deleteStudentTest() throws Exception {
        // given
        Long id = 1L;
        doNothing().when(studentService).deleteStudent(id);

        // when & then
        mockMvc.perform(delete("/student/{id}", id))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(id);
    }

    @Test
    @DisplayName("GET /student/age/{age} - фильтр по возрасту")
    void getStudentsByAgeTest() throws Exception {
        // given
        int age = 17;
        Collection<Student> students = Arrays.asList(
                new Student(1L, "Гарри Поттер", 17),
                new Student(2L, "Рон Уизли", 17)
        );

        when(studentService.getStudentsByAge(age)).thenReturn(students);

        // when & then
        mockMvc.perform(get("/student/age/{age}", age))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Рон Уизли"));
    }

    @Test
    @DisplayName("GET /student/all - получение всех студентов")
    void getAllStudentsTest() throws Exception {
        // given
        Collection<Student> students = Arrays.asList(
                new Student(1L, "Гарри Поттер", 17),
                new Student(2L, "Гермиона Грейнджер", 18),
                new Student(3L, "Рон Уизли", 17)
        );

        when(studentService.getAllStudents()).thenReturn(students);

        // when & then
        mockMvc.perform(get("/student/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"));
    }

    @Test
    @DisplayName("GET /student/age/between - поиск по диапазону возраста")
    void getStudentsByAgeBetweenTest() throws Exception {
        // given
        int minAge = 16;
        int maxAge = 18;
        Collection<Student> students = Arrays.asList(
                new Student(1L, "Гарри Поттер", 17),
                new Student(2L, "Гермиона Грейнджер", 18)
        );

        when(studentService.getStudentsByAgeBetween(minAge, maxAge)).thenReturn(students);

        // when & then
        mockMvc.perform(get("/student/age/between")
                        .param("minAge", String.valueOf(minAge))
                        .param("maxAge", String.valueOf(maxAge)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("GET /student/{id}/faculty - получение факультета студента")
    void getStudentFacultyTest() throws Exception {
        // given
        Long id = 1L;
        Faculty faculty = new Faculty(1L, "Гриффиндор", "Красный");

        when(studentService.getStudentFaculty(id)).thenReturn(faculty);

        // when & then
        mockMvc.perform(get("/student/{id}/faculty", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));
    }

    @Test
    @DisplayName("POST /student/{studentId}/faculty/{facultyId} - назначение факультета")
    void assignStudentToFacultyTest() throws Exception {
        // given
        Long studentId = 1L;
        Long facultyId = 1L;
        Faculty faculty = new Faculty(facultyId, "Гриффиндор", "Красный");
        Student updatedStudent = new Student(studentId, "Гарри Поттер", 17);
        updatedStudent.setFaculty(faculty);

        when(studentService.assignStudentToFaculty(studentId, facultyId)).thenReturn(updatedStudent);

        // when & then
        mockMvc.perform(post("/student/{studentId}/faculty/{facultyId}", studentId, facultyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.faculty.id").value(facultyId))
                .andExpect(jsonPath("$.faculty.name").value("Гриффиндор"));
    }
}