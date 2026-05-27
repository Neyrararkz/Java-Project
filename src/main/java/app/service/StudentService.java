package app.service;

import app.model.Student;
import app.repository.StudentRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class StudentService {
    private final StudentRepository studentRepository;

    public StudentService() {
        this.studentRepository = new StudentRepository();
    }

    public Student createStudent(String name, String surname, String email, String groupName, int courseYear) throws SQLException {
        validateStudentData(name, surname, email, groupName, courseYear);

        Student student = new Student(0, name, surname, email, groupName, courseYear);

        return studentRepository.create(student);
    }

    public List<Student> getAllStudents() throws SQLException {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(int id) throws SQLException {
        validateId(id);

        return studentRepository.findById(id);
    }

    public boolean updateStudent(int id, String name, String surname, String email, String groupName, int courseYear) throws SQLException {
        validateId(id);
        validateStudentData(name, surname, email, groupName, courseYear);

        Student student = new Student(id, name, surname, email, groupName, courseYear);

        return studentRepository.update(student);
    }

    public boolean deleteStudent(int id) throws SQLException {
        validateId(id);

        return studentRepository.delete(id);
    }

    public List<Student> searchStudentsByName(String query) throws SQLException {
        if (isBlank(query)) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        return studentRepository.searchByName(query);
    }

    private void validateStudentData(String name, String surname, String email, String groupName, int courseYear) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (isBlank(surname)) {
            throw new IllegalArgumentException("Surname cannot be empty");
        }

        if (isBlank(email) || !email.contains("@")) {
            throw new IllegalArgumentException("Email is invalid");
        }

        if (isBlank(groupName)) {
            throw new IllegalArgumentException("Group name cannot be empty");
        }

        if (courseYear < 1 || courseYear > 4) {
            throw new IllegalArgumentException("Course year must be from 1 to 4");
        }
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID must be positive");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}