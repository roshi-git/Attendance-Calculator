import java.util.Scanner;

// THIS CLASS HANDLES CRUD OPERATIONS ON ACCOUNTS IN THE DATABASE
public class AccountHandler {

    Scanner sc = new Scanner(System.in);
    String input;

    // THIS FUNCTION HANDLES THE SIGN-UP PROCESS
    public int signup(User us) {

        DBHandler db = new DBHandler();
        int exists = 1;

        // ENTER YOUR NAME
        System.out.print("Enter your name: ");
        input = sc.next();
        us.SetName(input);

        // ENTER YOUR E-MAIL ID
        while (exists == 1) {
            System.out.print("Enter your E-mail ID: ");
            input = sc.next();

            String query = String.format("select exists(select * from userdata where email='%s')", input);
            exists = db.query_user(query);

            if(exists == 1)
                System.out.println("An account already exists with this e-mail ID. Please enter a different one.");
        }
        us.SetMail(input);

        // ENTER YOUR USERNAME
        exists = 1;
        while (exists == 1) {
            System.out.print("Enter a username: ");
            input = sc.next();

            String query = String.format("select exists(select * from userdata where uname='%s')", input);
            exists = db.query_user(query);

            if(exists == 1)
                System.out.println("This username is already taken. Please enter a different one.");
        }
        us.SetUName(input);

        // ENTER YOUR ACCOUNT PASSWORD
        System.out.print("Enter a password: ");
        input = sc.next();

        int pass_ok = 0;
        while(pass_ok == 0) {
            System.out.print("Re-enter your password: ");
            if(!input.equals(sc.next())) {
                System.out.println("Password does not match!");
            }
            else
                pass_ok = 1;
        }
        us.SetPass(input);

        // ENTER DATA INTO DATABASE AND THEN EXIT THIS FUNCTION
        return db.adduser(us);
    }

    // THIS FUNCTION HANDLES THE LOG-IN PROCESS
    public int login(User us) {

        DBHandler db = new DBHandler();

        int exists = 0;
        while (exists == 0) {
            System.out.print("Enter username: ");
            us.SetUName(sc.next());

            System.out.print("Enter password: ");
            us.SetPass(sc.next());

            String query = String.format("select exists(select * from userdata where uname='%s' and password='%s')", us.GetUname(), us.GetPass());
            // CHECK IF INPUT USERNAME AND PASSWORD COMBINATION EXISTS IN DATABASE
            exists = db.query_user(query);

            // IF EXISTS IS 0, INCORRECT COMBINATION OF USERNAME AND PASSWORD
            // IF EXISTS IS 1, THERE EXISTS A CORRECT COMBINATION OF BOTH
            if (exists == 0)
                System.out.println("Incorrect username or password.");
        }

        return exists;
    }

    public int delete_account(User us) {

        String option;

        System.out.println("Are you sure you want to delete your account? (Y/N): ");
        option = sc.next().toUpperCase();
        if (option.equals("N"))
            return 1;
        else {
            DBHandler db = new DBHandler();
            return db.delete_user(us);
        }
    }
}
