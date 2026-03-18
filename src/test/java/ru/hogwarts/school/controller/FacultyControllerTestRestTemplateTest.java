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
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FacultyControllerTestTemplateTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl;

    @BeforeAll
    void setUpBaseUrl() {
        baseUrl = "http://localhost:" + port + "/faculty";
        System.out.println("Базовый URL: " + baseUrl);
    }

    @BeforeEach
    void setUp() {
        facultyRepository.deleteAll();
        System.out.println("Очищено перед тестом");
    }

    @Test
    @DisplayName("POST /faculty - создание факультета")
    void createFacultyTest() {
        // given
        Faculty faculty = new Faculty();
        faculty.setName("Гриффиндор");
        faculty.setColor("Красный");

        // when
        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                baseUrl,
                faculty,
                Faculty.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Гриффиндор");
        assertThat(response.getBody().getColor()).isEqualTo("Красный");
    }

    @Test
    @DisplayName("GET /faculty/{id} - получение факультета по ID")
    void getFacultyByIdTest() {
        // given
        Faculty savedFaculty = restTemplate.postForObject(
                baseUrl,
                new Faculty(null, "Слизерин", "Зеленый"),
                Faculty.class
        );

        // when
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/" + savedFaculty.getId(),
                Faculty.class
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(savedFaculty.getId());
        assertThat(response.getBody().getName()).isEqualTo("Слизерин");
        assertThat(response.getBody().getColor()).isEqualTo("Зеленый");
    }

    @Test
    @DisplayName("GET /faculty/all - получение всех факультетов")
    void getAllFacultiesTest() {
        // given
        restTemplate.postForEntity(baseUrl, new Faculty(null, "Гриффиндор", "Красный"), Faculty.class);
        restTemplate.postForEntity(baseUrl, new Faculty(null, "Слизерин", "Зеленый"), Faculty.class);

        // when
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl + "/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("GET /faculty/color/{color} - фильтр по цвету")
    void getFacultiesByColorTest() {
        // given
        restTemplate.postForEntity(baseUrl, new Faculty(null, "Гриффиндор", "Красный"), Faculty.class);
        restTemplate.postForEntity(baseUrl, new Faculty(null, "Красный факультет", "Красный"), Faculty.class);
        restTemplate.postForEntity(baseUrl, new Faculty(null, "Синий факультет", "Синий"), Faculty.class);

        // when
        ResponseEntity<Collection<Faculty>> response = restTemplate.exchange(
                baseUrl + "/color/Красный",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        // then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
        assertThat(response.getBody()).allMatch(f -> f.getColor().equals("Красный"));
    }

    @Test
    @DisplayName("PUT /faculty - обновление факультета")
    void updateFacultyTest() {
        // given
        Faculty savedFaculty = restTemplate.postForObject(
                baseUrl,
                new Faculty(null, "Когтевран", "Синий"),
                Faculty.class
        );

        savedFaculty.setName("Равенкло");
        savedFaculty.setColor("Бронзовый");

        // when
        restTemplate.put(baseUrl, savedFaculty);

        // then
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/" + savedFaculty.getId(),
                Faculty.class
        );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Равенкло");
        assertThat(response.getBody().getColor()).isEqualTo("Бронзовый");
    }

    @Test
    @DisplayName("DELETE /faculty/{id} - удаление факультета")
    void deleteFacultyTest() {
        // given
        Faculty savedFaculty = restTemplate.postForObject(
                baseUrl,
                new Faculty(null, "Пуффендуй", "Желтый"),
                Faculty.class
        );

        Long facultyId = savedFaculty.getId();

        // when
        restTemplate.delete(baseUrl + "/" + facultyId);

        // then - проверяем только через репозиторий
        // Это самый надежный способ, не зависит от API
        assertThat(facultyRepository.findById(facultyId)).isEmpty();
    }

    @Test
    @DisplayName("GET /faculty/search - поиск по имени или цвету")
    void findFacultiesByNameOrColorTest() {
        // given
        restTemplate.postForEntity(baseUrl, new Faculty(null, "Гриффиндор", "Красный"), Faculty.class);
        restTemplate.postForEntity(baseUrl, new Faculty(null, "Слизерин", "Зеленый"), Faculty.class);

        // when - поиск по имени
        ResponseEntity<Collection<Faculty>> responseByName = restTemplate.exchange(
                baseUrl + "/search?nameOrColor=Гриффиндор",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        // then
        assertThat(responseByName.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByName.getBody()).isNotNull();
        assertThat(responseByName.getBody().size()).isEqualTo(1);
        assertThat(responseByName.getBody().iterator().next().getName()).isEqualTo("Гриффиндор");

        // when - поиск по цвету
        ResponseEntity<Collection<Faculty>> responseByColor = restTemplate.exchange(
                baseUrl + "/search?nameOrColor=Красный",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<Collection<Faculty>>() {}
        );

        // then
        assertThat(responseByColor.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseByColor.getBody()).isNotNull();
        assertThat(responseByColor.getBody().size()).isEqualTo(1);
    }
}