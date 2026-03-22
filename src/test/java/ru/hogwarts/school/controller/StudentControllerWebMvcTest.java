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

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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
    @DisplayName("POST /student - создание студента")
    void createStudentTest() throws Exception {
        // given
        Student studentToCreate = new Student();
        studentToCreate.setName("Гарри Поттер");
        studentToCreate.setAge(17);

        Student createdStudent = new Student();
        createdStudent.setId(1L);
        createdStudent.setName("Гарри Поттер");
        createdStudent.setAge(17);

        when(studentService.createStudent(any(Student.class))).thenReturn(createdStudent);

        // when & then
        mockMvc.perform(post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));

        verify(studentService, times(1)).createStudent(any(Student.class));
    }

    @Test
    @DisplayName("GET /student/{id} - получение студента по ID")
    void getStudentByIdTest() throws Exception {
        // given
        Long id = 1L;
        Student student = new Student();
        student.setId(id);
        student.setName("Гермиона Грейнджер");
        student.setAge(18);

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
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("PUT /student - обновление студента")
    void updateStudentTest() throws Exception {
        // given
        Long id = 1L;
        Student studentToUpdate = new Student();
        studentToUpdate.setId(id);
        studentToUpdate.setName("Рональд Уизли");
        studentToUpdate.setAge(18);

        when(studentService.updateStudent(any(Student.class))).thenReturn(studentToUpdate);

        // when & then
        mockMvc.perform(put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(studentToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Рональд Уизли"))
                .andExpect(jsonPath("$.age").value(18));

        verify(studentService, times(1)).updateStudent(any(Student.class));
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

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Рон Уизли");
        student2.setAge(17);

        Collection<Student> students = Arrays.asList(student1, student2);

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
        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гермиона Грейнджер");
        student2.setAge(18);

        Student student3 = new Student();
        student3.setId(3L);
        student3.setName("Рон Уизли");
        student3.setAge(17);

        Collection<Student> students = Arrays.asList(student1, student2, student3);

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

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Гарри Поттер");
        student1.setAge(17);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Гермиона Грейнджер");
        student2.setAge(18);

        Collection<Student> students = Arrays.asList(student1, student2);

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
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        when(studentService.getStudentFaculty(id)).thenReturn(faculty);

        // when & then
        mockMvc.perform(get("/student/{id}/faculty", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));
    }

    @Test
    @DisplayName("GET /student/{id}/faculty - факультет не найден")
    void getStudentFacultyNotFoundTest() throws Exception {
        // given
        Long id = 1L;
        when(studentService.getStudentFaculty(id)).thenReturn(null);

        // when & then
        mockMvc.perform(get("/student/{id}/faculty", id))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /student/{studentId}/faculty/{facultyId} - назначение факультета")
    void assignStudentToFacultyTest() throws Exception {
        // given
        Long studentId = 1L;
        Long facultyId = 1L;

        Faculty faculty = new Faculty();
        faculty.setId(facultyId);
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        Student updatedStudent = new Student();
        updatedStudent.setId(studentId);
        updatedStudent.setName("Гарри Поттер");
        updatedStudent.setAge(17);
        updatedStudent.setFaculty(faculty);

        when(studentService.assignStudentToFaculty(studentId, facultyId)).thenReturn(updatedStudent);

        // when & then
        mockMvc.perform(post("/student/{studentId}/faculty/{facultyId}", studentId, facultyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(studentId))
                .andExpect(jsonPath("$.name").value("Гарри Поттер"))
                .andExpect(jsonPath("$.age").value(17));

        // Проверяем, что сервис был вызван
        verify(studentService, times(1)).assignStudentToFaculty(studentId, facultyId);
    }

    @Test
    @DisplayName("POST /student/{studentId}/faculty/{facultyId} - студент или факультет не найдены")
    void assignStudentToFacultyNotFoundTest() throws Exception {
        // given
        Long studentId = 1L;
        Long facultyId = 1L;

        when(studentService.assignStudentToFaculty(studentId, facultyId)).thenReturn(null);

        // when & then
        mockMvc.perform(post("/student/{studentId}/faculty/{facultyId}", studentId, facultyId))
                .andExpect(status().isNotFound());
    }
}