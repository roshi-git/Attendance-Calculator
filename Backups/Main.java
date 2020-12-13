import java.util.Scanner;

public class Main {

    // Main class..
    public static void main(String[] args) {

        System.out.println("It workin");
        Scanner sc = new Scanner(System.in);
        int option;
        int ec; // Exit code

        loop: while (true) {

            System.out.println("1. Sign-Up\n2. Log-In\n3. Show Users\n4. Exit");
            System.out.print("Select one option: ");
            option = sc.nextInt();

            switch (option) {

                case 1 -> {
                    // Sign up
                    User us = new User();
                    AccountHandler ac = new AccountHandler();
                    ec = ac.signup(us);

                    if(ec == 0)
                        System.out.println("Sign-up successful!");
                    else
                        System.out.println("Sign-up failed.");
                }

                case 2 -> {
                    // Log-in
                    User us = new User();
                    AccountHandler ac = new AccountHandler();

                    ec = ac.login(us);
                    if (ec == 0) {
                        System.out.println("Log-in successful!");
                        ec = LoggedIn(us);
                    }

                    if(ec == 2)
                        System.out.println("Logged out.");
                    else
                        System.out.println("Unknown error occurred.");
                }

                case 3 -> {
                    // Show users in DB
                    DBHandler db = new DBHandler();
                    ec = db.show_users();

                    if (ec == 1)
                        System.out.println("Unable to show database.");
                }

                case 4 -> {
                    // Exit
                    System.out.print("Closing app..");
                    break loop;
                }
                
                // Exception
                default -> System.out.print("Invalid Input!");
            }
        }
    }

    public static int LoggedIn(User us) {
        // Do logged in stuff
        int option, ec = 0;
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.printf("Logged in as %s%n", us.GetUname());
            System.out.println("1. Show profile\n2. Account settings\n3. Delete account\n4. Log-out");
            System.out.print("Select one option: ");
            option = sc.nextInt();

            switch (option) {
                case 1 -> {
                    // Show Profile
                }
                case 2 -> {
                    // Change account related stuff
                }
                case 3 -> {
                    // Delete account
                    AccountHandler ac = new AccountHandler();

                    ec = ac.delete_account(us);
                    if(ec == 1)
                        System.out.println("Account deletion aborted.");
                    else {
                        System.out.println("Account successfully deleted.");
                        return 2;
                    }
                }
                case 4 -> {
                    System.out.println("Logging out...");
                    return 2;
                }
            }
        }
    }
}
