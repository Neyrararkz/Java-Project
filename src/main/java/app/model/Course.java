package app.model;

public class Course {
    private int id;
    private String title;
    private String description;
    private int teacherId;
    private String teacherName;
    private int durationWeeks;

    public Course() {
    }

    public Course(int id, String title, String description, int teacherId, int durationWeeks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teacherId = teacherId;
        this.durationWeeks = durationWeeks;
    }

    public Course(int id, String title, String description, int teacherId, String teacherName, int durationWeeks) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.teacherId = teacherId;
        this.teacherName = teacherName;
        this.durationWeeks = durationWeeks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    } 

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    } 

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    } 

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    } 

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    } 

    public int getDurationWeeks() {
        return durationWeeks;
    }

    public void setDurationWeeks(int durationWeeks) {
        this.durationWeeks = durationWeeks;
    }

    @Override
    public String toString() {
        return "Course ID: " + id
                + " | Title: " + title
                + " | Teacher: " + (teacherName == null ? "Teacher ID " + teacherId : teacherName)
                + " | Duration: " + durationWeeks + " weeks"
                + " | Description: " + description;
    }
}