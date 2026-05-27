package app.model;

public class Student extends User {
    private String groupName;
    private int courseYear;

    public Student() {
    }

    public Student(int id, String name, String surname, String email, String groupName, int courseYear) {
        super(id, name, surname, email);
        this.groupName = groupName;
        this.courseYear = courseYear;
    }

    @Override
    public String getRole() {
        return "Student";
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getCourseYear() {
        return courseYear;
    }

    public void setCourseYear(int courseYear) {
        this.courseYear = courseYear;
    }

    @Override
    public String toString() {
        return "Student ID: " + getId()
                + " | " + getFullName()
                + " | Email: " + getEmail()
                + " | Group: " + groupName
                + " | Course: " + courseYear;
    }
}