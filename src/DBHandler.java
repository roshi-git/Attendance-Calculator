import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

// This class handles transaction with the database
public class DBHandler {

    // CONNECTOR TO MYSQL DATABASE
    DBConnector dbc = new DBConnector();

    // Function for inserting into database
    public int adduser(User us) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();
            // PREPARE SQL QUERY
            String values = String.format("NULL, '%s', '%s', '%s', '%s', %d", us.GetName(), us.GetUname(), us.GetMail(), us.GetPass(), us.GetUType());
            PreparedStatement ps = con.prepareStatement(String.format("insert into userdata values (%s)", values));
            // EXECUTE QUERY AND CLOSE THE CONNECTION
            ps.executeUpdate();
            con.close();

            return 2;

        } catch (Exception e) {
            return -1;
        }
    }

    // FUNCTION TO FIND PRE-EXISTING ENTRIES IN DATABASE
    public int query_user(String query) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE AND EXECUTE SQL QUERY
            Statement st =con.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();

            // RETURNS 1 IF THERE ALREADY EXISTS AN ENTRY AND 0 IF NOT
            return rs.getInt(1);

        } catch (Exception e) {
            return -1;
        }
    }

    // Function to delete from database
    public int delete_user(User us) {
        try {
            Connection con = dbc.connect();
            String statement = String.format("DELETE FROM userdata WHERE uname = '%s'", us.GetUname());
            PreparedStatement ps = con.prepareStatement(statement);
            ps.executeUpdate();

            con.close();
            return 0;

        } catch (Exception e) {
            return 1;
        }
    }

    // FUNCTION FOR MARKING ATTENDANCE BY EMPLOYEE
    public void mark_attendance (User user) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // TO GET TODAY'S DATE
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            // PREPARE AND EXECUTE SQL QUERY
            String values = String.format("%d, '%s', %d", user.GetUID(), dtf.format(now), 0);
            PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO attendance_data VALUES (%s)", values));
            ps.executeUpdate();

            // CLOSE THE CONNECTION
            con.close();

        } catch (Exception e) {
            System.out.println("Failed to mark attendance.");
        }

    }

    // TO CHECK IF EMPLOYEE HAS ALREADY MARKED THEIR ATTENDANCE TODAY
    public int check_attendance_today (User user) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // TO GET TODAY'S DATE
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now = LocalDateTime.now();

            // PREPARE AND EXECUTE SQL QUERY
            String query_1 = String.format("SELECT date FROM attendance_data WHERE uid = %d", user.GetUID());
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query_1);
            rs.next();

            // IF EMPLOYEE HAS ALREADY MARKED THEIR ATTENDANCE TODAY
            if (rs.getString(1).equals(dtf.format(now)))
                return 1;
            else
                return 0;

        } catch (Exception e) {
            System.out.println("Date could not be checked");
            return -1;}

    }

    // Function to show users (only for development purposes)
    public int show_users() {

        try {
            // CONNECT TO DATABASE AND EXECUTE QUERY
            Connection con = dbc.connect();
            String Query = "SELECT * FROM userdata";
            String name, uname, email, pass;
            Statement st =con.createStatement();
            ResultSet rs = st.executeQuery(Query);

            while(rs.next()){
                int s1 = rs.getInt(1);
                name = rs.getString(2);
                uname = rs.getString(3);
                email = rs.getString(4);
                pass = rs.getString(5);
                System.out.println("UID: " + s1 + " NAME: "+ name + " UNAME: " + uname + " E-MAIL: " + email + " PASSWORD: " + pass + "\n");
            }
            con.close();
            return 0;

        } catch (Exception e) {
            return 1;
        }
    }

    // FUNCTION TO INITIALIZE USER DATA
    public void get_user_data (User user) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE AND EXECUTE SQL QUERY
            String query_1 = String.format("SELECT * FROM userdata WHERE uname = '%s'", user.GetUname());
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query_1);
            rs.next();

            // INITIALIZE USER DATA, EXCEPT THEIR ATTENDANCE
            user.SetUID(rs.getInt(1));
            user.SetName(rs.getString(2));
            user.SetMail(rs.getString(4));
            user.SetUType(rs.getInt(6));

            // IF USER IS JUST AN EMPLOYEE, INITIALIZE THEIR ATTENDANCE DATA
            if (user.GetUType() != 1) {
                String query_2 = String.format("SELECT COUNT(uid) FROM attendance_data WHERE uid = %d AND absent = 0", user.GetUID());
                rs = st.executeQuery(query_2);
                rs.next();
                user.SetAttendance(rs.getInt(1));

                String query_3 = String.format("SELECT COUNT(uid) FROM attendance_data WHERE uid = %d", user.GetUID());
                rs = st.executeQuery(query_3);
                rs.next();
                user.SetTotalAttendance(rs.getInt(1));
            }

            // CLOSE THE CONNECTION
            con.close();

        } catch (Exception ignored) { }
    }
}