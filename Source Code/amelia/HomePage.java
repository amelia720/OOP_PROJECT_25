package amelia;

/**
 * This class stores information about my project and myself.
 * It's used to display details like the project title, what it's about,
 * my name, student number, and course code.
 */
public class HomePage 
{
    private final String projectTitle;
    private final String description;
    private final String studentName;
    private final String studentNumber;
    private final String courseCode;

    /**
     * Sets up my homepage details when the app starts.
     * All the information is filled in by default here.
     */
    public HomePage() 
    {
        this.projectTitle = "ShopManager Pro";
        this.description = "Welcome to my project for the Object Oriented Software Development module.\n"
                         + "This project aims to implement a system that manages customer purchases.";
        this.studentName = "Amelia Hamulewicz";
        this.studentNumber = "C00296605";
        this.courseCode = "CW_KCSOF_B";
    }

    /**
     * @return The title of my project
     */
    public String getProjectTitle() 
    {
        return projectTitle;
    }

    /**
     * @return A short description of what my project is about
     */
    public String getDescription() 
    {
        return description;
    }

    /**
     * @return My name
     */
    public String getStudentName() 
    {
        return studentName;
    }

    /**
     * @return My student number
     */
    public String getStudentNumber() 
    {
        return studentNumber;
    }

    /**
     * @return The course code I'm studying under
     */
    public String getCourseCode() 
    {
        return courseCode;
    }
}
