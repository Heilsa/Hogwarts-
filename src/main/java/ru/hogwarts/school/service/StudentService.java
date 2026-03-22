package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class StudentService {

    private final StudentRepository studentRepository;
    private final FacultyRepository facultyRepository; // Добавьте это поле

    @Autowired
    public StudentService(StudentRepository studentRepository,
                          FacultyRepository facultyRepository) { // Добавьте в конструктор
        this.studentRepository = studentRepository;
        this.facultyRepository = facultyRepository; // Инициализируйте поле
    }

    public Student getStudent(Long id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public Student createStudent(Student student) {
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
                .toList();
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
}