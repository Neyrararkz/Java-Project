package app.service;

import app.model.Teacher;
import app.repository.TeacherRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class TeacherService {
    private final TeacherRepository teacherRepository;

    public TeacherService() {
        this.teacherRepository = new TeacherRepository();
    }

    public Teacher createTeacher(String name, String surname, String email, String department, String position) throws SQLException {
        validateTeacherData(name, surname, email, department, position);

        Teacher teacher = new Teacher(0, name, surname, email, department, position);

        return teacherRepository.create(teacher);
    }

    public List<Teacher> getAllTeachers() throws SQLException {
        return teacherRepository.findAll();
    }

    public Optional<Teacher> getTeacherById(int id) throws SQLException {
        validateId(id);

        return teacherRepository.findById(id);
    }

    public boolean updateTeacher(int id, String name, String surname, String email, String department, String position) throws SQLException {
        validateId(id);
        validateTeacherData(name, surname, email, department, position);

        Teacher teacher = new Teacher(id, name, surname, email, department, position);

        return teacherRepository.update(teacher);
    }

    public boolean deleteTeacher(int id) throws SQLException {
        validateId(id);

        return teacherRepository.delete(id);
    }

    public List<Teacher> searchTeachersByName(String query) throws SQLException {
        if (isBlank(query)) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        return teacherRepository.searchByName(query);
    }

    private void validateTeacherData(String name, String surname, String email, String department, String position) {
        if (isBlank(name)) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        if (isBlank(surname)) {
            throw new IllegalArgumentException("Surname cannot be empty");
        }

        if (isBlank(email) || !email.contains("@")) {
            throw new IllegalArgumentException("Email is invalid");
        }

        if (isBlank(department)) {
            throw new IllegalArgumentException("Department cannot be empty");
        }

        if (isBlank(position)) {
            throw new IllegalArgumentException("Position cannot be empty");
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