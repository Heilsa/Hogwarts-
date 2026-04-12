package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository) {
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository;
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student createStudent(Student student) {
        if (student.getFaculty() != null && student.getFaculty().getId() != null) {
            Faculty faculty = facultyRepository.findById(student.getFaculty().getId()).orElse(null);
            student.setFaculty(faculty);
        }
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Collection<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Collection<Student> getStudentsByAge(int age) {
        return studentRepository.findAll().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());  // ← заменили toList() на collect(Collectors.toList())
    }

    public Collection<Student> getStudentsByAgeBetween(int minAge, int maxAge) {
        return studentRepository.findByAgeBetween(minAge, maxAge);
    }

    public Faculty getStudentFaculty(Long studentId) {
        Student student = getStudent(studentId);
        return student != null ? student.getFaculty() : null;
    }

    public Student assignStudentToFaculty(Long studentId, Long facultyId) {
        Student student = getStudent(studentId);
        Faculty faculty = facultyRepository.findById(facultyId).orElse(null);

        if (student == null || faculty == null) {
            return null;
        }

        student.setFaculty(faculty);
        return studentRepository.save(student);
    }

    public Integer getCountOfStudents() {
        return studentRepository.getCountOfStudents();
    }

    public Double getAverageAgeOfStudents() {
        return studentRepository.getAverageAgeOfStudents();
    }

    public List<Student> getLastNStudents(int count) {
        return studentRepository.getLastNStudents(count);
    }
}