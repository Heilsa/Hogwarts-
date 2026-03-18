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
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
class FacultyControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FacultyService facultyService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /faculty - создание факультета")
    void createFacultyTest() throws Exception {
        // given
        Faculty facultyToCreate = new Faculty(null, "Гриффиндор", "Красный");
        Faculty createdFaculty = new Faculty(1L, "Гриффиндор", "Красный");

        when(facultyService.createFaculty(any(Faculty.class))).thenReturn(createdFaculty);

        // when & then
        mockMvc.perform(post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facultyToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Гриффиндор"))
                .andExpect(jsonPath("$.color").value("Красный"));

        verify(facultyService, times(1)).createFaculty(any(Faculty.class));
    }

    @Test
    @DisplayName("GET /faculty/{id} - получение факультета по ID")
    void getFacultyByIdTest() throws Exception {
        // given
        Long id = 1L;
        Faculty faculty = new Faculty(id, "Слизерин", "Зеленый");

        when(facultyService.getFaculty(id)).thenReturn(faculty);

        // when & then
        mockMvc.perform(get("/faculty/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.name").value("Слизерин"))
                .andExpect(jsonPath("$.color").value("Зеленый"));
    }

    @Test
    @DisplayName("PUT /faculty - обновление факультета")
    void updateFacultyTest() throws Exception {
        // given
        Faculty facultyToUpdate = new Faculty(1L, "Равенкло", "Бронзовый");

        when(facultyService.updateFaculty(any(Faculty.class))).thenReturn(facultyToUpdate);

        // when & then
        mockMvc.perform(put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(facultyToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Равенкло"))
                .andExpect(jsonPath("$.color").value("Бронзовый"));

        verify(facultyService, times(1)).updateFaculty(any(Faculty.class));
    }

    @Test
    @DisplayName("DELETE /faculty/{id} - удаление факультета")
    void deleteFacultyTest() throws Exception {
        // given
        Long id = 1L;
        doNothing().when(facultyService).deleteFaculty(id);

        // when & then
        mockMvc.perform(delete("/faculty/{id}", id))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(id);
    }

    @Test
    @DisplayName("GET /faculty/color/{color} - фильтр по цвету")
    void getFacultiesByColorTest() throws Exception {
        // given
        String color = "Красный";
        Collection<Faculty> faculties = Arrays.asList(
                new Faculty(1L, "Гриффиндор", "Красный"),
                new Faculty(2L, "Красный факультет", "Красный")
        );

        when(facultyService.getFacultiesByColor(color)).thenReturn(faculties);

        // when & then
        mockMvc.perform(get("/faculty/color/{color}", color))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"))
                .andExpect(jsonPath("$[1].name").value("Красный факультет"));
    }

    @Test
    @DisplayName("GET /faculty/all - получение всех факультетов")
    void getAllFacultiesTest() throws Exception {
        // given
        Collection<Faculty> faculties = Arrays.asList(
                new Faculty(1L, "Гриффиндор", "Красный"),
                new Faculty(2L, "Слизерин", "Зеленый"),
                new Faculty(3L, "Когтевран", "Синий")
        );

        when(facultyService.getAllFaculties()).thenReturn(faculties);

        // when & then
        mockMvc.perform(get("/faculty/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"));
    }

    @Test
    @DisplayName("GET /faculty/search - поиск по имени или цвету")
    void findFacultiesByNameOrColorTest() throws Exception {
        // given
        String query = "Гриффиндор";
        Collection<Faculty> faculties = Collections.singletonList(
                new Faculty(1L, "Гриффиндор", "Красный")
        );

        when(facultyService.findFacultiesByNameOrColor(query)).thenReturn(faculties);

        // when & then
        mockMvc.perform(get("/faculty/search")
                        .param("nameOrColor", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].name").value("Гриффиндор"));
    }

    @Test
    @DisplayName("GET /faculty/{id}/students - получение студентов факультета")
    void getFacultyStudentsTest() throws Exception {
        // given
        Long id = 1L;
        List<Student> students = Arrays.asList(
                new Student(1L, "Гарри Поттер", 17),
                new Student(2L, "Гермиона Грейнджер", 18)
        );

        when(facultyService.getFacultyStudents(id)).thenReturn(students);

        // when & then
        mockMvc.perform(get("/faculty/{id}/students", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$[0].name").value("Гарри Поттер"))
                .andExpect(jsonPath("$[1].name").value("Гермиона Грейнджер"));
    }
}