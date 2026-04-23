package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
        logger.debug("StudentService initialized with repositories");
    }

    public Student getStudent(Long id) {
        logger.info("Was invoked method for get student by id = {}", id);
        Student student = studentRepository.findById(id).orElse(null);
        if (student == null) {
            logger.warn("Student with id = {} not found", id);
        } else {
            logger.debug("Student found: {}", student.getName());
        }
        return student;
    }

    public Student updateStudent(Student student) {
        logger.info("Was invoked method for update student with id = {}", student.getId());
        if (student.getId() == null) {
            logger.error("Cannot update student: id is null");
            return null;
        }
        Student updated = studentRepository.save(student);
        logger.debug("Student updated successfully: {}", updated.getName());
        return updated;
    }

    public Student createStudent(Student student) {
        logger.info("Was invoked method for create student with name = {}", student.getName());
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty faculty = facultyRepository.findById(student.getFaculty().getId()).orElse(null);
            student.setFaculty(faculty);
            logger.debug("Student assigned to faculty: {}", faculty != null ? faculty.getName() : "none");
        }
        Student created = studentRepository.save(student);
        logger.info("Student created successfully with id = {}", created.getId());
        return created;
    }

    public void deleteStudent(Long id) {
        logger.info("Was invoked method for delete student with id = {}", id);
        if (!studentRepository.existsById(id)) {
            logger.warn("Cannot delete: student with id = {} not found", id);
            return;
        }
        studentRepository.deleteById(id);
        logger.debug("Student with id = {} deleted successfully", id);
    }

    public Collection<Student> getAllStudents() {
        logger.info("Was invoked method for get all students");
        Collection<Student> students = studentRepository.findAll();
        logger.debug("Found {} students", students.size());
        return students;
    }

    public Collection<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for get students by age = {}", age);
        Collection<Student> students = studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(java.util.stream.Collectors.toList());
        logger.debug("Found {} students with age {}", students.size(), age);
        return students;
    }

    public Collection<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        logger.info("Was invoked method for get students between ages {} and {}", minAge, maxAge);
        Collection<Student> students = studentRepository.findByAgeBetween(minAge, maxAge);
        logger.debug("Found {} students in age range", students.size());
        return students;
    }

    public Faculty getStudentFaculty(Long studentId) {
        logger.info("Was invoked method for get faculty of student with id = {}", studentId);
        Student student = getStudent(studentId);
        if (student == null) {
            logger.warn("Student with id = {} not found, cannot get faculty", studentId);
            return null;
        }
        Faculty faculty = student.getFaculty();
        if (faculty == null) {
            logger.debug("Student with id = {} has no faculty assigned", studentId);
        } else {
            logger.debug("Student belongs to faculty: {}", faculty.getName());
        }
        return faculty;
    }

    public Student assignStudentToFaculty(Long studentId, Long facultyId) {
        logger.info("Was invoked method for assign student {} to faculty {}", studentId, facultyId);
        Student student = getStudent(studentId);
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);

        if (student == null) {
            logger.error("Cannot assign: student with id = {} not found", studentId);
            return null;
        }
        if (faculty == null) {
            logger.error("Cannot assign: faculty with id = {} not found", facultyId);
            return null;
        }

        student.setFaculty(faculty);
        Student updated = studentRepository.save(student);
        logger.info("Student {} assigned to faculty {} successfully", student.getName(), faculty.getName());
        return updated;
    }

    public Integer getCountOfStudents() {
        logger.info("Was invoked method for get count of students");
        Integer count = studentRepository.getCountOfStudents();
        logger.debug("Total students count: {}", count);
        return count;
    }

    public Double getAverageAgeOfStudents() {
        logger.info("Was invoked method for get average age of students");
        Double average = studentRepository.getAverageAgeOfStudents();
        logger.debug("Average student age: {}", average);
        return average;
    }

    public List<Student> getLastNStudents(int count) {
        logger.info("Was invoked method for get last {} students", count);
        List<Student> students = studentRepository.getLastNStudents(count);
        logger.debug("Retrieved {} students", students.size());
        return students;
    }

    public List<String> getNamesStartingWithA() {
        logger.info("Was invoked method for get names starting with 'A'");

        List<String> result = studentRepository.findAll().stream()
                .map(Student::getName)                          // берем имена
                .filter(name -> name != null && name.startsWith("А")) // фильтруем на "А"
                .map(String::toUpperCase)                       // преобразуем в верхний регистр
                .sorted()                                       // сортируем в алфавитном порядке
                .collect(Collectors.toList());

        logger.debug("Found {} names starting with 'A'", result.size());
        return result;
    }
}