package ru.hogwarts.school.controller;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentControllerTestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private StudentRepository studentRepository;

    private String baseUrl;

    @BeforeAll
    void setUpBaseUrl() {
        // ИСПРАВЛЕНО: /student (как в контроллере)
        baseUrl = "http://localhost:" + port + "/student";
        System.out.println("Базовый URL: " + baseUrl);
    }

    @BeforeEach
    void setUp() {
        studentRepository.deleteAll();
        System.out.println("Очищено перед тестом");
    }

    @Test
    @DisplayName("POST /student - создание студента")
    void createStudentTest() {
        System.out.println("Тест createStudentTest, URL: " + baseUrl);

        Student student = new Student();
        student.setName("Гарри Поттер");
        student.setAge(17);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                baseUrl,
                student,
                Student.class
        );

        System.out.println("Статус ответа: " + response.getStatusCode());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
    }

    @Test
    @DisplayName("GET /student/{id} - получение студента по ID")
    void getStudentByIdTest() {
        // Сначала создаем студента
        Student savedStudent = restTemplate.postForObject(
                baseUrl,
                new Student(null, "Гермиона Грейнджер", 18),
                Student.class
        );

        System.out.println("Создан студент с ID: " + savedStudent.getId());

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/" + savedStudent.getId(),
                Student.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Гермиона Грейнджер");
    }

    @Test
    @DisplayName("GET /student/all - получение всех студентов")
    void getAllStudentsTest() {
        restTemplate.postForEntity(baseUrl, new Student(null, "Студент 1", 20), Student.class);
        restTemplate.postForEntity(baseUrl, new Student(null, "Студент 2", 21), Student.class);

        // ИСПРАВЛЕНО: используем /all
        ResponseEntity<Collection<Student>> response = restTemplate.exchange(
                baseUrl + "/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Student>>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("PUT /student - обновление студента")
    void updateStudentTest() {
        Student savedStudent = restTemplate.postForObject(
                baseUrl,
                new Student(null, "Рон Уизли", 17),
                Student.class
        );

        savedStudent.setName("Рональд Уизли");
        savedStudent.setAge(18);

        restTemplate.put(baseUrl, savedStudent);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                baseUrl + "/" + savedStudent.getId(),
                Student.class
        );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Рональд Уизли");
    }

    @Test
    @DisplayName("DELETE /student/{id} - удаление студента")
    void deleteStudentTest() {
        // given
        Student savedStudent = restTemplate.postForObject(
                baseUrl,
                new Student(null, "Драко Малфой", 17),
                Student.class
        );

        Long studentId = savedStudent.getId();

        // when
        restTemplate.delete(baseUrl + "/" + studentId);

        // then - проверяем напрямую в БД
        boolean exists = studentRepository.existsById(studentId);
        assertThat(exists).isFalse();

    }
}