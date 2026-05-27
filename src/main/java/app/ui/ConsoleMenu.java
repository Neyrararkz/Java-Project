package app.ui;

import app.model.Course;
import app.model.Enrollment;
import app.model.Student;
import app.model.Teacher;
import app.service.CourseService;
import app.service.EnrollmentService;
import app.service.StudentService;
import app.service.TeacherService;
import app.util.InputHelper;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ConsoleMenu {
    private final StudentService studentService;
    private final TeacherService teacherService;
    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    public ConsoleMenu() {
        this.studentService = new StudentService();
        this.teacherService = new TeacherService();
        this.courseService = new CourseService();
        this.enrollmentService = new EnrollmentService();
    }

    public void start() {
        boolean running = true;

        while (running) {
            printMainMenu();
            int choice = InputHelper.readInt("Choose option: ");

            switch (choice) {
                case 1 -> manageStudents();
                case 2 -> manageTeachers();
                case 3 -> manageCourses();
                case 4 -> manageEnrollments();
                case 0 -> {
                    running = false;
                    System.out.println("Application closed.");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void printMainMenu() {
        System.out.println();
        System.out.println("===== COURSE ENROLLMENT MANAGER =====");
        System.out.println("1. Manage students");
        System.out.println("2. Manage teachers");
        System.out.println("3. Manage courses");
        System.out.println("4. Manage enrollments");
        System.out.println("0. Exit");
    }

    private void manageStudents() {
        boolean back = false;

        while (!back) {
            System.out.println();
            System.out.println("===== STUDENTS =====");
            System.out.println("1. Add student");
            System.out.println("2. Show all students");
            System.out.println("3. Find student by ID");
            System.out.println("4. Update student");
            System.out.println("5. Delete student");
            System.out.println("6. Search students by name");
            System.out.println("0. Back");

            int choice = InputHelper.readInt("Choose option: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> showAllStudents();
                case 3 -> findStudentById();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> searchStudents();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addStudent() {
        try {
            String name = InputHelper.readString("Name: ");
            String surname = InputHelper.readString("Surname: ");
            String email = InputHelper.readString("Email: ");
            String groupName = InputHelper.readString("Group name: ");
            int courseYear = InputHelper.readInt("Course year: ");

            Student student = studentService.createStudent(name, surname, email, groupName, courseYear);

            System.out.println("Student created successfully:");
            System.out.println(student);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void showAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();

            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                students.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void findStudentById() {
        try {
            int id = InputHelper.readInt("Student ID: ");
            Optional<Student> student = studentService.getStudentById(id);

            if (student.isPresent()) {
                System.out.println(student.get());
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void updateStudent() {
        try {
            int id = InputHelper.readInt("Student ID to update: ");
            String name = InputHelper.readString("New name: ");
            String surname = InputHelper.readString("New surname: ");
            String email = InputHelper.readString("New email: ");
            String groupName = InputHelper.readString("New group name: ");
            int courseYear = InputHelper.readInt("New course year: ");

            boolean updated = studentService.updateStudent(id, name, surname, email, groupName, courseYear);

            if (updated) {
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void deleteStudent() {
        try {
            int id = InputHelper.readInt("Student ID to delete: ");
            boolean deleted = studentService.deleteStudent(id);

            if (deleted) {
                System.out.println("Student deleted successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void searchStudents() {
        try {
            String query = InputHelper.readString("Search query: ");
            List<Student> students = studentService.searchStudentsByName(query);

            if (students.isEmpty()) {
                System.out.println("No students found.");
            } else {
                students.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void manageTeachers() {
        boolean back = false;

        while (!back) {
            System.out.println();
            System.out.println("===== TEACHERS =====");
            System.out.println("1. Add teacher");
            System.out.println("2. Show all teachers");
            System.out.println("3. Find teacher by ID");
            System.out.println("4. Update teacher");
            System.out.println("5. Delete teacher");
            System.out.println("6. Search teachers by name");
            System.out.println("0. Back");

            int choice = InputHelper.readInt("Choose option: ");

            switch (choice) {
                case 1 -> addTeacher();
                case 2 -> showAllTeachers();
                case 3 -> findTeacherById();
                case 4 -> updateTeacher();
                case 5 -> deleteTeacher();
                case 6 -> searchTeachers();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addTeacher() {
        try {
            String name = InputHelper.readString("Name: ");
            String surname = InputHelper.readString("Surname: ");
            String email = InputHelper.readString("Email: ");
            String department = InputHelper.readString("Department: ");
            String position = InputHelper.readString("Position: ");

            Teacher teacher = teacherService.createTeacher(name, surname, email, department, position);

            System.out.println("Teacher created successfully:");
            System.out.println(teacher);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void showAllTeachers() {
        try {
            List<Teacher> teachers = teacherService.getAllTeachers();

            if (teachers.isEmpty()) {
                System.out.println("No teachers found.");
            } else {
                teachers.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void findTeacherById() {
        try {
            int id = InputHelper.readInt("Teacher ID: ");
            Optional<Teacher> teacher = teacherService.getTeacherById(id);

            if (teacher.isPresent()) {
                System.out.println(teacher.get());
            } else {
                System.out.println("Teacher not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void updateTeacher() {
        try {
            int id = InputHelper.readInt("Teacher ID to update: ");
            String name = InputHelper.readString("New name: ");
            String surname = InputHelper.readString("New surname: ");
            String email = InputHelper.readString("New email: ");
            String department = InputHelper.readString("New department: ");
            String position = InputHelper.readString("New position: ");

            boolean updated = teacherService.updateTeacher(id, name, surname, email, department, position);

            if (updated) {
                System.out.println("Teacher updated successfully.");
            } else {
                System.out.println("Teacher not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void deleteTeacher() {
        try {
            int id = InputHelper.readInt("Teacher ID to delete: ");
            boolean deleted = teacherService.deleteTeacher(id);

            if (deleted) {
                System.out.println("Teacher deleted successfully.");
            } else {
                System.out.println("Teacher not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void searchTeachers() {
        try {
            String query = InputHelper.readString("Search query: ");
            List<Teacher> teachers = teacherService.searchTeachersByName(query);

            if (teachers.isEmpty()) {
                System.out.println("No teachers found.");
            } else {
                teachers.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void manageCourses() {
        boolean back = false;

        while (!back) {
            System.out.println();
            System.out.println("===== COURSES =====");
            System.out.println("1. Add course");
            System.out.println("2. Show all courses");
            System.out.println("3. Find course by ID");
            System.out.println("4. Update course");
            System.out.println("5. Delete course");
            System.out.println("6. Search courses by title");
            System.out.println("0. Back");

            int choice = InputHelper.readInt("Choose option: ");

            switch (choice) {
                case 1 -> addCourse();
                case 2 -> showAllCourses();
                case 3 -> findCourseById();
                case 4 -> updateCourse();
                case 5 -> deleteCourse();
                case 6 -> searchCourses();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void addCourse() {
        try {
            showTeachersBeforeCourseAction();

            String title = InputHelper.readString("Title: ");
            String description = InputHelper.readString("Description: ");
            int teacherId = InputHelper.readInt("Teacher ID: ");
            int durationWeeks = InputHelper.readInt("Duration in weeks: ");

            Course course = courseService.createCourse(title, description, teacherId, durationWeeks);

            System.out.println("Course created successfully:");
            System.out.println(course);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void showAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();

            if (courses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                courses.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void findCourseById() {
        try {
            int id = InputHelper.readInt("Course ID: ");
            Optional<Course> course = courseService.getCourseById(id);

            if (course.isPresent()) {
                System.out.println(course.get());
            } else {
                System.out.println("Course not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void updateCourse() {
        try {
            showTeachersBeforeCourseAction();

            int id = InputHelper.readInt("Course ID to update: ");
            String title = InputHelper.readString("New title: ");
            String description = InputHelper.readString("New description: ");
            int teacherId = InputHelper.readInt("New teacher ID: ");
            int durationWeeks = InputHelper.readInt("New duration in weeks: ");

            boolean updated = courseService.updateCourse(id, title, description, teacherId, durationWeeks);

            if (updated) {
                System.out.println("Course updated successfully.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void deleteCourse() {
        try {
            int id = InputHelper.readInt("Course ID to delete: ");
            boolean deleted = courseService.deleteCourse(id);

            if (deleted) {
                System.out.println("Course deleted successfully.");
            } else {
                System.out.println("Course not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void searchCourses() {
        try {
            String query = InputHelper.readString("Search query: ");
            List<Course> courses = courseService.searchCoursesByTitle(query);

            if (courses.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                courses.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void manageEnrollments() {
        boolean back = false;

        while (!back) {
            System.out.println();
            System.out.println("===== ENROLLMENTS =====");
            System.out.println("1. Enroll student to course");
            System.out.println("2. Show all enrollments");
            System.out.println("3. Find enrollment by ID");
            System.out.println("4. Update enrollment");
            System.out.println("5. Delete enrollment");
            System.out.println("6. Show courses of student");
            System.out.println("7. Show students of course");
            System.out.println("0. Back");

            int choice = InputHelper.readInt("Choose option: ");

            switch (choice) {
                case 1 -> enrollStudentToCourse();
                case 2 -> showAllEnrollments();
                case 3 -> findEnrollmentById();
                case 4 -> updateEnrollment();
                case 5 -> deleteEnrollment();
                case 6 -> showCoursesOfStudent();
                case 7 -> showStudentsOfCourse();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private void enrollStudentToCourse() {
        try {
            showStudentsBeforeEnrollmentAction();
            showCoursesBeforeEnrollmentAction();

            int studentId = InputHelper.readInt("Student ID: ");
            int courseId = InputHelper.readInt("Course ID: ");

            Enrollment enrollment = enrollmentService.enrollStudent(studentId, courseId);

            System.out.println("Student enrolled successfully:");
            System.out.println(enrollment);
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void showAllEnrollments() {
        try {
            List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

            if (enrollments.isEmpty()) {
                System.out.println("No enrollments found.");
            } else {
                enrollments.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void findEnrollmentById() {
        try {
            int id = InputHelper.readInt("Enrollment ID: ");
            Optional<Enrollment> enrollment = enrollmentService.getEnrollmentById(id);

            if (enrollment.isPresent()) {
                System.out.println(enrollment.get());
            } else {
                System.out.println("Enrollment not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void updateEnrollment() {
        try {
            showStudentsBeforeEnrollmentAction();
            showCoursesBeforeEnrollmentAction();

            int id = InputHelper.readInt("Enrollment ID to update: ");
            int studentId = InputHelper.readInt("New student ID: ");
            int courseId = InputHelper.readInt("New course ID: ");

            boolean updated = enrollmentService.updateEnrollment(id, studentId, courseId);

            if (updated) {
                System.out.println("Enrollment updated successfully.");
            } else {
                System.out.println("Enrollment not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void deleteEnrollment() {
        try {
            int id = InputHelper.readInt("Enrollment ID to delete: ");
            boolean deleted = enrollmentService.deleteEnrollment(id);

            if (deleted) {
                System.out.println("Enrollment deleted successfully.");
            } else {
                System.out.println("Enrollment not found.");
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void showCoursesOfStudent() {
        try {
            int studentId = InputHelper.readInt("Student ID: ");
            List<Enrollment> enrollments = enrollmentService.getCoursesOfStudent(studentId);

            if (enrollments.isEmpty()) {
                System.out.println("This student has no courses.");
            } else {
                enrollments.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void showStudentsOfCourse() {
        try {
            int courseId = InputHelper.readInt("Course ID: ");
            List<Enrollment> enrollments = enrollmentService.getStudentsOfCourse(courseId);

            if (enrollments.isEmpty()) {
                System.out.println("This course has no students.");
            } else {
                enrollments.forEach(System.out::println);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("Validation error: " + e.getMessage());
        }

        InputHelper.pause();
    }

    private void showTeachersBeforeCourseAction() throws SQLException {
        List<Teacher> teachers = teacherService.getAllTeachers();

        System.out.println();
        System.out.println("Available teachers:");

        if (teachers.isEmpty()) {
            System.out.println("No teachers found. Please add a teacher first.");
        } else {
            teachers.forEach(System.out::println);
        }

        System.out.println();
    }

    private void showStudentsBeforeEnrollmentAction() throws SQLException {
        List<Student> students = studentService.getAllStudents();

        System.out.println();
        System.out.println("Available students:");

        if (students.isEmpty()) {
            System.out.println("No students found. Please add a student first.");
        } else {
            students.forEach(System.out::println);
        }

        System.out.println();
    }

    private void showCoursesBeforeEnrollmentAction() throws SQLException {
        List<Course> courses = courseService.getAllCourses();

        System.out.println();
        System.out.println("Available courses:");

        if (courses.isEmpty()) {
            System.out.println("No courses found. Please add a course first.");
        } else {
            courses.forEach(System.out::println);
        }

        System.out.println();
    }
}