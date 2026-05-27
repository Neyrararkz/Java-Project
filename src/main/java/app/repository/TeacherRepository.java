package app.repository;

import app.db.DatabaseConnection;
import app.model.Teacher;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TeacherRepository implements CrudRepository<Teacher> {
    @Override
    public Teacher create(Teacher teacher) throws SQLException {
        String sql = """
                INSERT INTO teachers (name, surname, email, department, position)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getSurname());
            statement.setString(3, teacher.getEmail());
            statement.setString(4, teacher.getDepartment());
            statement.setString(5, teacher.getPosition());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    teacher.setId(generatedKeys.getInt(1));
                }
            }

            return teacher;
        }
    }

    @Override
    public List<Teacher> findAll() throws SQLException {
        String sql = """
                SELECT id, name, surname, email, department, position
                FROM teachers
                ORDER BY id
                """;

        List<Teacher> teachers = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                teachers.add(mapResultSetToTeacher(resultSet));
            }
        }

        return teachers;
    }

    @Override
    public Optional<Teacher> findById(int id) throws SQLException {
        String sql = """
                SELECT id, name, surname, email, department, position
                FROM teachers
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToTeacher(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Teacher teacher) throws SQLException {
        String sql = """
                UPDATE teachers
                SET name = ?, surname = ?, email = ?, department = ?, position = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, teacher.getName());
            statement.setString(2, teacher.getSurname());
            statement.setString(3, teacher.getEmail());
            statement.setString(4, teacher.getDepartment());
            statement.setString(5, teacher.getPosition());
            statement.setInt(6, teacher.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = """
                DELETE FROM teachers
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    public List<Teacher> searchByName(String query) throws SQLException {
        String sql = """
                SELECT id, name, surname, email, department, position
                FROM teachers
                WHERE LOWER(name) LIKE LOWER(?) OR LOWER(surname) LIKE LOWER(?)
                ORDER BY id
                """;

        List<Teacher> teachers = new ArrayList<>();
        String searchPattern = "%" + query + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchPattern);
            statement.setString(2, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    teachers.add(mapResultSetToTeacher(resultSet));
                }
            }
        }

        return teachers;
    }

    private Teacher mapResultSetToTeacher(ResultSet resultSet) throws SQLException {
        return new Teacher(
                resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("surname"),
                resultSet.getString("email"),
                resultSet.getString("department"),
                resultSet.getString("position")
        );
    }
}