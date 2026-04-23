package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.Collection;
import java.util.List;

@Service
public class FacultyService {

    private static final Logger logger = LoggerFactory.getLogger(FacultyService.class);

    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;

    public FacultyService(FacultyRepository facultyRepository,
                          StudentRepository studentRepository) {
        this.facultyRepository = facultyRepository;
        this.studentRepository = studentRepository;
        logger.debug("FacultyService initialized");
    }

    public Faculty getFaculty(Long id) {
        logger.info("Was invoked method for get faculty by id = {}", id);
        Faculty faculty = facultyRepository.findById(id).orElse(null);
        if (faculty == null) {
            logger.warn("Faculty with id = {} not found", id);
        } else {
            logger.debug("Faculty found: {}", faculty.getName());
        }
        return faculty;
    }

    public Faculty updateFaculty(Faculty faculty) {
        logger.info("Was invoked method for update faculty with id = {}", faculty.getId());
        if (faculty.getId() == null) {
            logger.error("Cannot update faculty: id is null");
            return null;
        }
        Faculty updated = facultyRepository.save(faculty);
        logger.debug("Faculty updated successfully: {}", updated.getName());
        return updated;
    }

    public Faculty createFaculty(Faculty faculty) {
        logger.info("Was invoked method for create faculty with name = {}", faculty.getName());
        Faculty created = facultyRepository.save(faculty);
        logger.info("Faculty created successfully with id = {}", created.getId());
        return created;
    }

    public void deleteFaculty(Long id) {
        logger.info("Was invoked method for delete faculty with id = {}", id);
        if (!facultyRepository.existsById(id)) {
            logger.warn("Cannot delete: faculty with id = {} not found", id);
            return;
        }
        facultyRepository.deleteById(id);
        logger.debug("Faculty with id = {} deleted successfully", id);
    }

    public Collection<Faculty> getAllFaculties() {
        logger.info("Was invoked method for get all faculties");
        Collection<Faculty> faculties = facultyRepository.findAll();
        logger.debug("Found {} faculties", faculties.size());
        return faculties;
    }

    public Collection<Faculty> getFacultiesByColor(String color) {
        logger.info("Was invoked method for get faculties by color = {}", color);
        Collection<Faculty> faculties = facultyRepository.findAll().stream()
                .filter(faculty -> faculty.getColor().equalsIgnoreCase(color))
                .collect(java.util.stream.Collectors.toList());  // ← исправлено
        logger.debug("Found {} faculties with color {}", faculties.size(), color);
        return faculties;
    }

    public Collection<Faculty> findFacultiesByNameOrColor(String nameOrColor) {
        logger.info("Was invoked method for find faculties by name or color = {}", nameOrColor);
        Collection<Faculty> faculties = facultyRepository.findByNameIgnoreCaseOrColorIgnoreCase(
                nameOrColor, nameOrColor
        );
        logger.debug("Found {} faculties matching '{}'", faculties.size(), nameOrColor);
        return faculties;
    }

    public List<Student> getFacultyStudents(Long facultyId) {
        logger.info("Was invoked method for get students of faculty with id = {}", facultyId);
        if (!facultyRepository.existsById(facultyId)) {
            logger.warn("Faculty with id = {} not found", facultyId);
            return List.of();
        }
        List<Student> students = studentRepository.findByFacultyId(facultyId);
        logger.debug("Faculty has {} students", students.size());
        return students;
    }

    public String getLongestFacultyName() {
        logger.info("Was invoked method for get longest faculty name");

        String longestName = facultyRepository.findAll().stream()
                .map(Faculty::getName)
                .filter(name -> name != null)
                .max((name1, name2) -> Integer.compare(name1.length(), name2.length()))
                .orElse("No faculties found");

        logger.debug("Longest faculty name: '{}' (length: {})", longestName, longestName.length());
        return longestName;
    }


}