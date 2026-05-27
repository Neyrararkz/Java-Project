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

import app.model.Student;
import app.service.StudentService;

import java.sql.SQLException;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        StudentService studentService = new StudentService();

        try {
            String testEmail = "test" + System.currentTimeMillis() + "@email.com";

            Student createdStudent = studentService.createStudent(
                    "Amina",
                    "Saparova",
                    testEmail,
                    "242P",
                    2
            );

            System.out.println("Created student:");
            System.out.println(createdStudent);

            System.out.println();

            System.out.println("All students:");
            List<Student> students = studentService.getAllStudents();

            for (Student student : students) {
                System.out.println(student);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }
    }
}