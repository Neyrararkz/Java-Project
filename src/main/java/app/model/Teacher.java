package app.model;

public class Teacher extends User {
    private String department;
    private String position;

    public Teacher() {
    }

    public Teacher(int id, String name, String surname, String email, String department, String position) {
        super(id, name, surname, email);
        this.department = department;
        this.position = position;
    }

    @Override
    public String getRole() {
        return "Teacher";
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    } 

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "Teacher ID: " + getId()
                + " | " + getFullName()
                + " | Email: " + getEmail()
                + " | Department: " + department
                + " | Position: " + position;
    }
}