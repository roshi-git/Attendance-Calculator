import java.sql.*;

public class DBConnector {

    // CONNECTS THE DATABASE TO THE PROGRAM
    public Connection connect() {

        String url = "jdbc:mysql://localhost:3306/att_calc";
        String user = "admin", pass = "admin_pass";

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, user, pass);
            System.out.println("Connected to database.");
            return con;

        } catch (Exception e) {
            System.out.println("Connection failed.");
            return null;
        }
    }
}