import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// This class handles transaction with the database
public class DBHandler {

    // CONNECTOR TO MYSQL DATABASE
    DBConnector dbc = new DBConnector();

    // FUNCTION FOR ADDING USERS INTO DATABASE
    public int adduser(User us) {

        // TO GET TODAY'S DATE
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE SQL QUERY
            String values = String.format("NULL, '%s', '%s', '%s', '%s', %d, %d, '%s'",
                            us.GetName(), us.GetUname(), us.GetMail(),
                            us.GetPass(), us.GetUType(), 0, dtf.format(now));

            PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO userdata VALUES (%s)", values));

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
    public void delete_user(int user_id) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE AND EXECUTE SQL QUERY
            String query_1 = String.format("DELETE FROM userdata WHERE uid = '%s'", user_id);
            PreparedStatement ps = con.prepareStatement(query_1);
            ps.executeUpdate();

            String query_2 = String.format("DELETE FROM attendance_data WHERE uid = '%s'", user_id);
            ps = con.prepareStatement(query_2);
            ps.executeUpdate();

            System.out.println("User removed.");
            // CLOSE THE CONNECTION
            con.close();

        } catch (Exception ignored) {}
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
            String query = String.format("SELECT max(date) FROM attendance_data WHERE uid = %d", user.GetUID());
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);
            rs.next();

            System.out.println("Last attended on: " + rs.getString(1));

            // IF EMPLOYEE HAS ALREADY MARKED THEIR ATTENDANCE TODAY
            if (rs.getString(1).equals(dtf.format(now)))
                return 1;
            else
                return 0;

        } catch (Exception e) {
            System.out.println("Date could not be checked");
            return -1;
        }
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

        } catch (Exception ignored) {}

        return null;
    }

    // TO GET LIST OF ALL EMPLOYEES
    public List<Employee> get_emp_list () {

        // LIST OF EMPLOYEES
        List<Employee> emp_list = new ArrayList<>();

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE AND EXECUTE SQL QUERY
            String query = "SELECT uid, name, email, manager, join_date FROM userdata";
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(query);

            System.out.println("Fetched employee details.");

            while (rs.next()) {
                // INIT EMPLOYEE DATA
                Employee employee = new Employee();
                employee.user_id = rs.getInt(1);
                employee.name = rs.getString(2);
                employee.email = rs.getString(3);
                employee.managed_by = rs.getInt(4);
                employee.join_date = rs.getString(5);

                // ADD EMPLOYEE DATA TO LIST
                emp_list.add(employee);
            }
            return emp_list;

        } catch (Exception e) {
            System.out.println("Could not fetch employee list.");
        }

        return null;
    }

    // TO MANAGE AND UN-MANAGE EMPLOYEES
    public void ch_manager (int emp_id, int manager) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            // PREPARE AND EXECUTE SQL QUERY
            String query = String.format("UPDATE userdata SET manager = '%s' WHERE uid = '%s'", manager, emp_id);
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeUpdate();

            System.out.println("Changed manager.");
            // CLOSE THE CONNECTION
            con.close();

        } catch (Exception ignored) {}
    }

    public int reval_attendance () {
        // FROM JOIN DATE TILL CURRENT DATE ADD ATTENDANCE AS ABSENT (1) IN ATTENDANCE
        // DATABASE, FOR ALL EMPLOYEES, SUCH THAT UID, DATE PAIR IS UNIQUE
        DBHandler db = new DBHandler();
        List<Employee> emp_list;

        int new_entries_added = 0;

        // TO GET TODAY'S DATE
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate now = LocalDate.now();

        // GET LIST OF EMPLOYEES
        emp_list = db.get_emp_list();

        // FOR ALL EMPLOYEES, DO STUFF
        for (Employee e: emp_list) {
            // FROM JOIN DATE TO CURRENT DATE, MARK AS ABSENT
            int i = 0;
            LocalDate date = LocalDate.parse(e.join_date);
            while (date.isBefore(now)) {

                // IF NOT SUNDAY, ADD ENTRY AS ABSENT
                if (!date.getDayOfWeek().toString().equals("SUNDAY")) {
                    try {
                        // CONNECT TO DATABASE
                        Connection con = dbc.connect();

                        // PREPARE AND EXECUTE SQL QUERY
                        String values = String.format("%d, '%s', %d", e.user_id, dtf.format(date), 1);
                        PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO attendance_data VALUES (%s)", values));
                        ps.executeUpdate();

                        System.out.println("Added entry no.: " + new_entries_added);
                        new_entries_added++;

                        // CLOSE THE CONNECTION
                        con.close();
                    }
                    // IF ATTENDANCE ALREADY EXISTS FOR
                    // THAT DATE FOR A PARTICULAR USER
                    catch (Exception ignored) {}
                }

                // INCREMENT DATE
                i++;
                date = date.plusDays(i);
            }
        }

    return new_entries_added;
    }
}