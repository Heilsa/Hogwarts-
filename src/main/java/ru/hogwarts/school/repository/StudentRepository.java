package ru.hogwarts.school.repository;

import ru.hogwarts.school.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {

    List<Student> findByAgeBetween(int minAge, int maxAge);

    List<Student> findByFacultyId(Long facultyId);

    @Query("SELECT COUNT(s) FROM Student s")
    Integer getCountOfStudents();

    @Query("SELECT AVG(s.age) FROM Student s")
    Double getAverageAgeOfStudents();

    @Query("SELECT s FROM Student s ORDER BY s.id DESC")
    List<Student> getLastNStudents(@Param("limit") int limit);
}