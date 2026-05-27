package app.service;

import app.model.Course;
import app.repository.CourseRepository;
import app.repository.TeacherRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;

    public CourseService() {
        this.courseRepository = new CourseRepository();
        this.teacherRepository = new TeacherRepository();
    }

    public Course createCourse(String title, String description, int teacherId, int durationWeeks) throws SQLException {
        validateCourseData(title, teacherId, durationWeeks);
        validateTeacherExists(teacherId);

        Course course = new Course(0, title, description, teacherId, durationWeeks);

        return courseRepository.create(course);
    }

    public List<Course> getAllCourses() throws SQLException {
        return courseRepository.findAll();
    }

    public Optional<Course> getCourseById(int id) throws SQLException {
        validateId(id);

        return courseRepository.findById(id);
    }

    public boolean updateCourse(int id, String title, String description, int teacherId, int durationWeeks) throws SQLException {
        validateId(id);
        validateCourseData(title, teacherId, durationWeeks);
        validateTeacherExists(teacherId);

        Course course = new Course(id, title, description, teacherId, durationWeeks);

        return courseRepository.update(course);
    }

    public boolean deleteCourse(int id) throws SQLException {
        validateId(id);

        return courseRepository.delete(id);
    }

    public List<Course> searchCoursesByTitle(String query) throws SQLException {
        if (isBlank(query)) {
            throw new IllegalArgumentException("Search query cannot be empty");
        }

        return courseRepository.searchByTitle(query);
    }

    private void validateCourseData(String title, int teacherId, int durationWeeks) {
        if (isBlank(title)) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }

        if (teacherId <= 0) {
            throw new IllegalArgumentException("Teacher ID must be positive");
        }

        if (durationWeeks <= 0) {
            throw new IllegalArgumentException("Duration must be positive");
        }
    }

    private void validateTeacherExists(int teacherId) throws SQLException {
        if (teacherRepository.findById(teacherId).isEmpty()) {
            throw new IllegalArgumentException("Teacher with this ID does not exist");
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