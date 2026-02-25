package ru.hogwarts.school.repository;

import ru.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findByFacultyId(Long facultyId);
}