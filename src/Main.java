import java.util.Scanner;

public class Main {

    // Main class..
    public static void main(String[] args) {

        GUI g = new GUI();
        int option;
        int ec; // Exit code

        while (true) {

            // LOAD MAIN MENU
            g.main_menu();

            option = 0;
            // BUSY WAIT AT MAIN MENU
            while (option == 0) { option = g.get_main_menu_option(); }

            // CHECK WHICH BUTTON WAS PRESSED
            switch (option) {

                // SIGN-UP
                case 1 -> {

                    User us = new User();
                    AccountHandler ac = new AccountHandler();
                    g.sign_up_menu(us, ac);
                    ec = 0;

                    // STUCK AT LOG-IN UNTIL CREDENTIALS ARE CORRECT
                    while (ec != 2) { ec = g.get_signing_up(); }
                    System.out.println("Sign-up closed.");
                }

                // LOG-IN
                case 2 -> {

                    User us = new User();
                    AccountHandler ac = new AccountHandler();
                    g.login_menu(us, ac);
                    ec = 0;

                    // STUCK AT LOG-IN UNTIL CREDENTIALS ARE CORRECT
                    while (ec == 0) { ec = g.get_logging_in(); }

                    if (ec == 1) {
                        System.out.println("Log-in successful!");
                        ec = LoggedIn(us);
                    }

                    if(ec == 2)
                        System.out.println("Logged out.");
                    else
                        System.out.println("Log-in closed.");
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