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
            String values = String.format("NULL, '%s', '%s', '%s', '%s', %d",us.GetName(), us.GetUname(), us.GetMail(), us.GetPass(), us.GetUType());

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

    // TO DELETE USER FROM USER DATABASE
    public int delete_user(int user_id) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE AND EXECUTE SQL QUERY
            String statement = String.format("DELETE FROM userdata WHERE uid = '%s'", user_id);
            PreparedStatement ps = con.prepareStatement(statement);
            ps.executeUpdate();

            // CLOSE THE CONNECTION
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

    // FUNCTION TO INITIALIZE USER DATA
    public void get_user_data (User user) {

        try {
            int[] attendance;

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
                attendance = get_attendance(user.GetUID());
                user.SetAttendance(attendance[0]);
                user.SetTotalAttendance(attendance[1]);
            }

            // CLOSE THE CONNECTION
            con.close();

        } catch (Exception ignored) { }
    }

    // FUNCTION TO GET ATTENDANCE DATA OF A USER
    public int[] get_attendance (int user_id) {

        try {
            int[] attendance = new int[2];

            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE AND EXECUTE SQL QUERY
            Statement st = con.createStatement();
            ResultSet rs;

            String query_1 = String.format("SELECT COUNT(uid) FROM attendance_data WHERE uid = %d AND absent = 0", user_id);
            rs = st.executeQuery(query_1);
            rs.next();
            attendance[0] = rs.getInt(1);

            String query_2 = String.format("SELECT COUNT(uid) FROM attendance_data WHERE uid = %d", user_id);
            rs = st.executeQuery(query_2);
            rs.next();
            attendance[1] = rs.getInt(1);

            // CLOSE THE CONNECTION
            con.close();

            // RETURNS AN ARRAY CONTAINING
            // ATTENDANCE AND TOTAL WORKING DAYS
            return attendance;

        } catch (Exception ignored) { }

        return null;
    }
}