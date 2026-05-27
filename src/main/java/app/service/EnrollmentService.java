package app.service;

import app.model.Enrollment;
import app.repository.CourseRepository;
import app.repository.EnrollmentRepository;
import app.repository.StudentRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class EnrollmentService {
    private final EnrollmentRepository enrollmentRepository;
    private final StudentRepository studentRepository;
    private final CourseRepository courseRepository;

    public EnrollmentService() {
        this.enrollmentRepository = new EnrollmentRepository();
        this.studentRepository = new StudentRepository();
        this.courseRepository = new CourseRepository();
    }

    public Enrollment enrollStudent(int studentId, int courseId) throws SQLException {
        validateId(studentId, "Student ID");
        validateId(courseId, "Course ID");
        validateStudentExists(studentId);
        validateCourseExists(courseId);

        if (enrollmentRepository.existsByStudentAndCourse(studentId, courseId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);

        return enrollmentRepository.create(enrollment);
    }

    public List<Enrollment> getAllEnrollments() throws SQLException {
        return enrollmentRepository.findAll();
    }

    public Optional<Enrollment> getEnrollmentById(int id) throws SQLException {
        validateId(id, "Enrollment ID");

        return enrollmentRepository.findById(id);
    }

    public boolean updateEnrollment(int id, int studentId, int courseId) throws SQLException {
        validateId(id, "Enrollment ID");
        validateId(studentId, "Student ID");
        validateId(courseId, "Course ID");
        validateStudentExists(studentId);
        validateCourseExists(courseId);

        Optional<Enrollment> existingEnrollment = enrollmentRepository.findById(id);

        if (existingEnrollment.isEmpty()) {
            return false;
        }

        Enrollment current = existingEnrollment.get();

        if ((current.getStudentId() != studentId || current.getCourseId() != courseId)
                && enrollmentRepository.existsByStudentAndCourse(studentId, courseId)) {
            throw new IllegalArgumentException("Student is already enrolled in this course");
        }

        Enrollment enrollment = new Enrollment();
        enrollment.setId(id);
        enrollment.setStudentId(studentId);
        enrollment.setCourseId(courseId);

        return enrollmentRepository.update(enrollment);
    }

    public boolean deleteEnrollment(int id) throws SQLException {
        validateId(id, "Enrollment ID");

        return enrollmentRepository.delete(id);
    }

    public List<Enrollment> getCoursesOfStudent(int studentId) throws SQLException {
        validateId(studentId, "Student ID");
        validateStudentExists(studentId);

        return enrollmentRepository.findByStudentId(studentId);
    }

    public List<Enrollment> getStudentsOfCourse(int courseId) throws SQLException {
        validateId(courseId, "Course ID");
        validateCourseExists(courseId);

        return enrollmentRepository.findByCourseId(courseId);
    }

    private void validateStudentExists(int studentId) throws SQLException {
        if (studentRepository.findById(studentId).isEmpty()) {
            throw new IllegalArgumentException("Student with this ID does not exist");
        }
    }

    private void validateCourseExists(int courseId) throws SQLException {
        if (courseRepository.findById(courseId).isEmpty()) {
            throw new IllegalArgumentException("Course with this ID does not exist");
        }
    }

    private void validateId(int id, String fieldName) {
        if (id <= 0) {
            throw new IllegalArgumentException(fieldName + " must be positive");
        }
    }
}