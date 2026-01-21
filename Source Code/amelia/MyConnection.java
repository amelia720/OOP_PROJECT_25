package amelia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * This class handles connecting to the MySQL database.
 * <p>
 * It provides a method to open a connection to my local database called "Customers".
 * The connection uses the MySQL JDBC driver.
 * <p>
 * Note: This class is abstract because it only contains a static utility method and don't need to make an object of it.
 */
public abstract class MyConnection 
{
    /**
     * The URL used to connect to the MySQL database.
     */
    public static final String DATABASE_URL = "jdbc:mysql://localhost:3306/Customers";

    /**
     * Creates and returns a new connection to the database.
     * <p>
     * It loads the JDBC driver and connects to the database
     * using the root user and password provided.
     * 
     * If it doesn't work, it prints the error and returns null.
     *
     *@return a Connection object if successful, or {@code null} if it fails
     */
    public static Connection getConnection() 
    {
        try 
        {
            // Load the MySQL JDBC driver so Java can talk to the database
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Try to connect to the database using the URL, username, and password
            return DriverManager.getConnection(DATABASE_URL, "root", "1234");
        } 
        catch (ClassNotFoundException | SQLException e) 
        {
            // If the driver isn't found or connection fails, show the error
            e.printStackTrace();

            // Return null to show that it didn’t connect
            return null;
        }
    }

}
