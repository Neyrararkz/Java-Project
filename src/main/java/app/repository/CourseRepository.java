package app.repository;

import app.db.DatabaseConnection;
import app.model.Course;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CourseRepository implements CrudRepository<Course> {
    @Override
    public Course create(Course course) throws SQLException {
        String sql = """
                INSERT INTO courses (title, description, teacher_id, duration_weeks)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, course.getTitle());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getTeacherId());
            statement.setInt(4, course.getDurationWeeks());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    course.setId(generatedKeys.getInt(1));
                }
            }

            return course;
        }
    }

    @Override
    public List<Course> findAll() throws SQLException {
        String sql = """
                SELECT
                    c.id,
                    c.title,
                    c.description,
                    c.teacher_id,
                    CONCAT(t.name, ' ', t.surname) AS teacher_name,
                    c.duration_weeks
                FROM courses c
                LEFT JOIN teachers t ON c.teacher_id = t.id
                ORDER BY c.id
                """;

        List<Course> courses = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                courses.add(mapResultSetToCourse(resultSet));
            }
        }

        return courses;
    }

    @Override
    public Optional<Course> findById(int id) throws SQLException {
        String sql = """
                SELECT
                    c.id,
                    c.title,
                    c.description,
                    c.teacher_id,
                    CONCAT(t.name, ' ', t.surname) AS teacher_name,
                    c.duration_weeks
                FROM courses c
                LEFT JOIN teachers t ON c.teacher_id = t.id
                WHERE c.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToCourse(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Course course) throws SQLException {
        String sql = """
                UPDATE courses
                SET title = ?, description = ?, teacher_id = ?, duration_weeks = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, course.getTitle());
            statement.setString(2, course.getDescription());
            statement.setInt(3, course.getTeacherId());
            statement.setInt(4, course.getDurationWeeks());
            statement.setInt(5, course.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = """
                DELETE FROM courses
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    public List<Course> searchByTitle(String query) throws SQLException {
        String sql = """
                SELECT
                    c.id,
                    c.title,
                    c.description,
                    c.teacher_id,
                    CONCAT(t.name, ' ', t.surname) AS teacher_name,
                    c.duration_weeks
                FROM courses c
                LEFT JOIN teachers t ON c.teacher_id = t.id
                WHERE LOWER(c.title) LIKE LOWER(?)
                ORDER BY c.id
                """;

        List<Course> courses = new ArrayList<>();
        String searchPattern = "%" + query + "%";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, searchPattern);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    courses.add(mapResultSetToCourse(resultSet));
                }
            }
        }

        return courses;
    }

    private Course mapResultSetToCourse(ResultSet resultSet) throws SQLException {
        return new Course(
                resultSet.getInt("id"),
                resultSet.getString("title"),
                resultSet.getString("description"),
                resultSet.getInt("teacher_id"),
                resultSet.getString("teacher_name"),
                resultSet.getInt("duration_weeks")
        );
    }
}