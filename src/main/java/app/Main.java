// package app;

// import app.db.DatabaseConnection;

// import java.sql.Connection;
// import java.sql.SQLException;

// public class Main {
//     public static void main(String[] args) {
//         try (Connection connection = DatabaseConnection.getConnection()) {
//             System.out.println("Database connected successfully!");
//         } catch (SQLException e) {
//             System.out.println("Database connection error: " + e.getMessage());
//         }
//     }
// }

package app;

import app.model.Course;
import app.model.Enrollment;
import app.model.Student;
import app.model.Teacher;
import app.service.CourseService;
import app.service.EnrollmentService;
import app.service.StudentService;
import app.service.TeacherService;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StudentService studentService = new StudentService();
        TeacherService teacherService = new TeacherService();
        CourseService courseService = new CourseService();
        EnrollmentService enrollmentService = new EnrollmentService();

        try {
            String studentEmail = "student" + System.currentTimeMillis() + "@email.com";
            String teacherEmail = "teacher" + System.currentTimeMillis() + "@email.com";

            Student student = studentService.createStudent(
                    "Nora",
                    "Sultanova",
                    studentEmail,
                    "242P",
                    2
            );

            Teacher teacher = teacherService.createTeacher(
                    "Murat",
                    "Akhmetov",
                    teacherEmail,
                    "Software Development",
                    "Senior Java Teacher"
            );

            Course course = courseService.createCourse(
                    "Java OOP",
                    "Object-oriented programming with Java",
                    teacher.getId(),
                    10
            );

            Enrollment enrollment = enrollmentService.enrollStudent(
                    student.getId(),
                    course.getId()
            );

            System.out.println("Created enrollment:");
            System.out.println(enrollment);

            System.out.println();

            System.out.println("All enrollments:");
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

            for (Enrollment item : enrollments) {
                System.out.println(item);
            }

            System.out.println();

            System.out.println("Courses of student:");
            List<Enrollment> studentCourses = enrollmentService.getCoursesOfStudent(student.getId());

            for (Enrollment item : studentCourses) {
                System.out.println(item);
            }

            System.out.println();

            System.out.println("Students of course:");
            List<Enrollment> courseStudents = enrollmentService.getStudentsOfCourse(course.getId());

            for (Enrollment item : courseStudents) {
                System.out.println(item);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }
}