import java.sql.*;

public class DBConnector {

    // CONNECTS THE DATABASE TO THE PROGRAM
    public Connection connect() {

        // URL AT WHICH THE DATABASE IS BEING SERVED
        String url = "jdbc:mysql://localhost:3306/att_calc";
        // DATABASE CREDENTIALS
        String user = "admin", pass = "admin_pass";

        // TRY TO CONNECT TO THE DATABASE BEING SERVED
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to database.");

            // THE CONNECTION IS RETURNED TO THE
            // FUNCTION THAT WISHES TO CONNECT
            return con;

        } catch (Exception e) {
            System.out.println("Connection failed.");
            return null;
        }
    }
}