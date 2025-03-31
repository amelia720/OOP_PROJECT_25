package amelia;

public class ProjectInfo {

    private final String projectTitle;
    private final String description;
    private final String studentName;
    private final String studentNumber;
    private final String courseCode;

    public ProjectInfo() {
        this.projectTitle = "ShopManager Pro";
        this.description = "Welcome to my project for the Object Oriented Software Development module.\n"
                         + "This project aims to implement a system that manages customer purchases.";
        this.studentName = "Amelia Hamulewicz";
        this.studentNumber = "C00296605";
        this.courseCode = "CW_KCSOF_B";
    }

    public String getProjectTitle() {
        return projectTitle;
    }

    public String getDescription() {
        return description;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getCourseCode() {
        return courseCode;
    }
}
