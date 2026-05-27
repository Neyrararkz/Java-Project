package app.repository;

import app.db.DatabaseConnection;
import app.model.Enrollment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EnrollmentRepository implements CrudRepository<Enrollment> {
    @Override
    public Enrollment create(Enrollment enrollment) throws SQLException {
        String sql = """
                INSERT INTO enrollments (student_id, course_id)
                VALUES (?, ?)
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, enrollment.getStudentId());
            statement.setInt(2, enrollment.getCourseId());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    enrollment.setId(generatedKeys.getInt(1));
                }
            }

            return enrollment;
        }
    }

    @Override
    public List<Enrollment> findAll() throws SQLException {
        String sql = """
                SELECT
                    e.id,
                    e.student_id,
                    e.course_id,
                    CONCAT(s.name, ' ', s.surname) AS student_name,
                    c.title AS course_title,
                    e.enrolled_at
                FROM enrollments e
                JOIN students s ON e.student_id = s.id
                JOIN courses c ON e.course_id = c.id
                ORDER BY e.id
                """;

        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                enrollments.add(mapResultSetToEnrollment(resultSet));
            }
        }

        return enrollments;
    }

    @Override
    public Optional<Enrollment> findById(int id) throws SQLException {
        String sql = """
                SELECT
                    e.id,
                    e.student_id,
                    e.course_id,
                    CONCAT(s.name, ' ', s.surname) AS student_name,
                    c.title AS course_title,
                    e.enrolled_at
                FROM enrollments e
                JOIN students s ON e.student_id = s.id
                JOIN courses c ON e.course_id = c.id
                WHERE e.id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEnrollment(resultSet));
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Enrollment enrollment) throws SQLException {
        String sql = """
                UPDATE enrollments
                SET student_id = ?, course_id = ?
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, enrollment.getStudentId());
            statement.setInt(2, enrollment.getCourseId());
            statement.setInt(3, enrollment.getId());

            return statement.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = """
                DELETE FROM enrollments
                WHERE id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            return statement.executeUpdate() > 0;
        }
    }

    public boolean existsByStudentAndCourse(int studentId, int courseId) throws SQLException {
        String sql = """
                SELECT COUNT(*)
                FROM enrollments
                WHERE student_id = ? AND course_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);
            statement.setInt(2, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    public List<Enrollment> findByStudentId(int studentId) throws SQLException {
        String sql = """
                SELECT
                    e.id,
                    e.student_id,
                    e.course_id,
                    CONCAT(s.name, ' ', s.surname) AS student_name,
                    c.title AS course_title,
                    e.enrolled_at
                FROM enrollments e
                JOIN students s ON e.student_id = s.id
                JOIN courses c ON e.course_id = c.id
                WHERE e.student_id = ?
                ORDER BY e.id
                """;

        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, studentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    enrollments.add(mapResultSetToEnrollment(resultSet));
                }
            }
        }

        return enrollments;
    }

    public List<Enrollment> findByCourseId(int courseId) throws SQLException {
        String sql = """
                SELECT
                    e.id,
                    e.student_id,
                    e.course_id,
                    CONCAT(s.name, ' ', s.surname) AS student_name,
                    c.title AS course_title,
                    e.enrolled_at
                FROM enrollments e
                JOIN students s ON e.student_id = s.id
                JOIN courses c ON e.course_id = c.id
                WHERE e.course_id = ?
                ORDER BY e.id
                """;

        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, courseId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    enrollments.add(mapResultSetToEnrollment(resultSet));
                }
            }
        }

        return enrollments;
    }

    private Enrollment mapResultSetToEnrollment(ResultSet resultSet) throws SQLException {
        Timestamp timestamp = resultSet.getTimestamp("enrolled_at");

        return new Enrollment(
                resultSet.getInt("id"),
                resultSet.getInt("student_id"),
                resultSet.getInt("course_id"),
                resultSet.getString("student_name"),
                resultSet.getString("course_title"),
                timestamp == null ? null : timestamp.toLocalDateTime()
        );
    }
}