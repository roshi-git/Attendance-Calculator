import java.util.Scanner;

// THIS CLASS HANDLES CRUD OPERATIONS ON ACCOUNTS IN THE DATABASE
public class AccountHandler {

    Scanner sc = new Scanner(System.in);

    // THIS FUNCTION HANDLES THE SIGN-UP PROCESS
    public int signup(User user) {

        DBHandler db = new DBHandler();
        int exists = 1;
        String query;

        // CHECK FOR E-MAIL IF ALREADY BEING USED
        query = String.format("select exists(select * from userdata where email='%s')", user.GetMail());
        exists = db.query_user(query);
        if(exists == 1)
            return 3;

        // CHECK FOR USERNAME IF ALREADY BEING USED
        query = String.format("select exists(select * from userdata where uname='%s')", user.GetUname());
        exists = db.query_user(query);
        if(exists == 1)
            return 4;

        // ENTER DATA INTO DATABASE AND THEN EXIT THIS FUNCTION (return 0 IMPLIES SUCCESS)
        return db.adduser(user);
    }

    // THIS FUNCTION HANDLES THE LOG-IN PROCESS
    public int login(User user) {

        DBHandler db = new DBHandler();
        int exists;

        String query = String.format("select exists(select * from userdata where uname='%s' and password='%s')", user.GetUname(), user.GetPass());
        // CHECK IF INPUT USERNAME AND PASSWORD COMBINATION EXISTS IN DATABASE
        exists = db.query_user(query);
        // IF EXISTS IS 0, INCORRECT COMBINATION OF USERNAME AND PASSWORD
        // IF EXISTS IS 1, THERE EXISTS A CORRECT COMBINATION OF BOTH
        if (exists == 0)
            System.out.println("Incorrect username or password.");
        else
            db.get_user_data(user);

        return exists;
    }

    public int delete_account(int user_id) {

        String option;

        System.out.println("Are you sure you want to delete your account? (Y/N): ");
        option = sc.next().toUpperCase();
        if (option.equals("N"))
            return 1;
        else {
            DBHandler db = new DBHandler();
            return db.delete_user(user_id);
        }
    }
}