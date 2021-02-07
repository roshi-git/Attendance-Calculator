import java.sql.*;

public class DBConnector {

    // CONNECTS THE DATABASE TO THE PROGRAM
    public Connection connect () {

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

    // CREATES THE DATABASE IF IT IS
    // NOT ALREADY PRESENT IN THE SYSTEM
    public void create_db (String uname, String pass) {

        // URL AT WHICH THE DATABASE IS BEING SERVED
        String url = "jdbc:mysql://localhost/";

        // TRY TO CONNECT TO THE DATABASE BEING SERVED
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, uname, pass);
            System.out.println("Connected to database.");

            Statement st = con.createStatement();

            // CREATE THE DATABASE
            String query = "CREATE DATABASE att_calc";
            st.executeUpdate(query);

            con.close();
            System.out.println("Database created.");

        } catch (Exception e) {
            System.out.println("Database creation failed. Please check if the dependencies are available.");
        }
    }

    // CREATES THE TABLES IF THEY ARE
    // NOT ALREADY PRESENT IN THE SYSTEM
    public void create_tb (String uname, String pass) {

        // URL AT WHICH THE DATABASE IS BEING SERVED
        String url = "jdbc:mysql://localhost:3306/att_calc";

        // TRY TO CONNECT TO THE DATABASE BEING SERVED
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection(url, uname, pass);
            System.out.println("Connected to database.");

            Statement st = con.createStatement();

            // CREATE THE TABLES
            // attendance_data
            String query_t1 = "CREATE TABLE `att_calc`.`attendance_data` (" +
                    "`uid` INT NOT NULL, `date` DATE NOT NULL, " +
                    "`absent` INT NOT NULL, PRIMARY KEY (`uid`, `date`)) " +
                    "ENGINE = InnoDB";

            // userdata
            String query_t2 = "CREATE TABLE `att_calc`.`new_table` (" +
                    "`uid` INT NOT NULL AUTO_INCREMENT, `name` VARCHAR(45) NOT NULL, " +
                    "`uname` VARCHAR(45) NOT NULL, `email` VARCHAR(45) NOT NULL, " +
                    "`password` VARCHAR(45) NOT NULL, `u_type` INT NOT NULL, " +
                    "`manages` INT NOT NULL, `join_date` DATE NOT NULL, PRIMARY KEY (`uid`), " +
                    "  UNIQUE INDEX `uname_UNIQUE` (`uname` ASC) VISIBLE, " +
                    "  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE) " +
                    "ENGINE = InnoDB";

            st.executeUpdate(query_t1);
            st.executeUpdate(query_t2);

            con.close();
            System.out.println("Tables created.");

        } catch (Exception e) {
            System.out.println("Connection failed.");
        }
    }
}