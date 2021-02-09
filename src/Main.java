import java.sql.Connection;

public class Main {

    // MAIN CLASS
    public static void main(String[] args) throws Exception {

        // CHECK IF DATABASE IS AVAILABLE
        DBConnector dbc = new DBConnector();
        Connection  con = dbc.connect();
        if (con == null) {
            // DB Credentials
            String uname = "admin";
            String pass = "admin_pass";

            System.out.println("Database is not found. Attempting to create database..");
            dbc.create_db(uname, pass);
            dbc.create_tb(uname, pass);
        }
        else
            System.out.println("Database found.");

        // CONSTRUCT GUI AND RUN THE APP
        GUI g = new GUI();
    }
}