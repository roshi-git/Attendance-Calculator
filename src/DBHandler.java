import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

// This class handles transaction with the database
public class DBHandler {

    DBConnector dbc = new DBConnector();

    // Function for inserting into database
    public int adduser(User us) {

        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();
            // PREPARE SQL QUERY
            String values = String.format("NULL, '%s', '%s', '%s', '%s'", us.GetName(), us.GetUname(), us.GetMail(), us.GetPass());
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
            Connection con = dbc.connect();
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

    // Function for updating attendance
    public int update_att() {

        return 0;
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
}