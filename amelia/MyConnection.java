package amelia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class MyConnection {
    final static String DATABASE_URL = "jdbc:mysql://localhost:3306/Customers";

    /**
     * Returns a fresh database connection each time it's called.
     */
    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(DATABASE_URL, "root", "W29Nh5TKc");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
