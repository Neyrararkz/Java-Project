package app.repository;

import app.db.DatabaseConnection;
import app.model.Student;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StudentRepository implements CrudRepository<Student> {
    @Override
    public Student create(Student student) throws SQLException {
        String sql = """
                INSERT INTO students (name, surname, email, group_name, course_year)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getSurname());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getGroupName());
            statement.setInt(5, student.getCourseYear());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    student.setId(generatedKeys.getInt(1));
                }
            }

            return student;
        }
    }

    @Override
    public List<Student> findAll() throws SQLException {
        String sql = """
                SELECT id, name, surname, email, group_name, course_year
                FROM students
                ORDER BY id
                """;

        List<Student> students = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                students.add(mapResultSetToStudent(resultSet));
            }
        }

        return students;
    }

    @Override
    public Optional<Student> findById(int id) throws SQLException {
        String sql = """
                SELECT id, name, surname, email, group_name, course_year
                FROM students
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToStudent(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Student student) throws SQLException {
        String sql = """
                UPDATE students
                SET name = ?, surname = ?, email = ?, group_name = ?, course_year = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, student.getName());
            statement.setString(2, student.getSurname());
            statement.setString(3, student.getEmail());
            statement.setString(4, student.getGroupName());
            statement.setInt(5, student.getCourseYear());
            statement.setInt(6, student.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = """
                DELETE FROM students
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    public List<Student> searchByName(String query) throws SQLException {
        String sql = """
                SELECT id, name, surname, email, group_name, course_year
                FROM students
                WHERE LOWER(name) LIKE LOWER(?) OR LOWER(surname) LIKE LOWER(?)
                ORDER BY id
                """;

        List<Student> students = new ArrayList<>();
        String searchPattern = "%" + query + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    students.add(mapResultSetToStudent(resultSet));
                }
            }
        }

        return students;
    }

    private Student mapResultSetToStudent(ResultSet resultSet) throws SQLException {
        return new Student(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getString("group_name"),
                resultSet.getInt("course_year")
        );
    }
}