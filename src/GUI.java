import javax.swing.*;
import java.awt.*;

public class GUI extends JFrame {

    // X-BOUNDS MACROS
    int x_label = 50, x_field = 150;
    // DIMENSIONS FOR FRAME
    int width = 500, height = 500;
    // STATE VARIABLES **Volatile because they get
    // used by multiple threads. This ensures a proper
    // busy wait state between different states**
    private volatile int logging_in = 0;    // 0 - WRONG CREDENTIALS, 1 - SUCCESS, 9 - EXIT, -1 - DB ERROR
    private volatile int signing_up = 0;    // 0 - BUSY WAIT, 2 - SIGN-UP COMPLETE/CLOSED, 3 - INVALID EMAIL, 4 - INVALID USERNAME
    private volatile int main_menu_option = 0;  // 0 - BUSY WAIT, 1 - SIGN-UP, 2 - LOG-IN

    public void set_main_menu_option (int main_menu_option) { this.main_menu_option = main_menu_option; }
    public int get_main_menu_option () { return main_menu_option; }

    public void set_logging_in (int logging_in) { this.logging_in = logging_in; }
    public int get_logging_in () { return logging_in; }

    public void set_signing_up (int signing_up) { this.signing_up = signing_up; }
    public int get_signing_up () { return signing_up; }

    // MAIN MENU
    public void main_menu () {

        // SET PROGRAM TO BUSY WAIT STATE
        set_main_menu_option(0);
        // CREATE THE WINDOW
        JFrame frame = new JFrame();

        // CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton sign_up_b = new JButton("Sign-up");
        JButton log_in_b = new JButton("Log-in");
        JButton exit_b = new JButton("Exit");

        // ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        sign_up_b.setBounds(150,100,200,50);
        frame.add(sign_up_b);
        log_in_b.setBounds(150,200,200,50);
        frame.add(log_in_b);
        exit_b.setBounds(150,300,200,50);
        frame.add(exit_b);

        // DO STUFF WHEN BUTTONS ARE PRESSED
        sign_up_b.addActionListener(ae -> {
            set_main_menu_option(1);
            frame.dispose();
        });
        log_in_b.addActionListener(ae -> {
            set_main_menu_option(2);
            frame.dispose();
        });
        exit_b.addActionListener(ae -> {
            frame.dispose();
            System.exit(0);
        });

        // DISPLAY THE WINDOW
        frame.setSize(width,height);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    // SIGN-UP MENU
    public void sign_up_menu (User us, AccountHandler ac) {

        // SET PROGRAM TO BUSY WAIT STATE
        set_signing_up(0);
        // CREATE THE WINDOW
        JFrame frame = new JFrame();

        // CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton sign_up_b = new JButton("Sign-up");
        JButton main_menu_b = new JButton("Main Menu");
        JTextField name_f = new JTextField();
        JTextField email_f = new JTextField();
        JTextField username_f = new JTextField();
        JPasswordField pass_f = new JPasswordField();
        JPasswordField re_pass_f = new JPasswordField();
        JLabel name_l = new JLabel("Name:");
        JLabel email_l = new JLabel("E-mail:");
        JLabel username_l = new JLabel("Username:");
        JLabel pass_l = new JLabel("Password:");
        JLabel re_pass_l = new JLabel("Re-enter password:");
        JLabel messageLabel = new JLabel();

        // ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        name_l.setBounds(x_label,100,75,25);
        frame.add(name_l);
        name_f.setBounds(x_field,100,200,25);
        frame.add(name_f);

        email_l.setBounds(x_label,150,75,25);
        frame.add(email_l);
        email_f.setBounds(x_field,150,200,25);
        frame.add(email_f);

        username_l.setBounds(x_label,200,75,25);
        frame.add(username_l);
        username_f.setBounds(x_field,200,200,25);
        frame.add(username_f);

        pass_l.setBounds(x_label,250,75,25);
        frame.add(pass_l);
        pass_f.setBounds(x_field,250,200,25);
        frame.add(pass_f);

        re_pass_l.setBounds(x_label,300,75,25);
        frame.add(re_pass_l);
        re_pass_f.setBounds(x_field,300,200,25);
        frame.add(re_pass_f);

        sign_up_b.setBounds(150,350,100,25);
        sign_up_b.setFocusable(false);

        main_menu_b.setBounds(300,350,100,25);
        main_menu_b.setFocusable(false);

        messageLabel.setBounds(50,400,300,35);
        messageLabel.setFont(new Font(null,Font.BOLD,12));
        frame.add(messageLabel);

        // DO STUFF WHEN SIGN-UP BUTTON IS PRESSED
        sign_up_b.addActionListener(ae -> {
            int dup_exists = 1;
            // CHECK FOR EMPTY FIELDS
            if (name_f.getText().isEmpty() || name_f.getText().isBlank()
                    || email_f.getText().isEmpty() || email_f.getText().isBlank()
                    || username_f.getText().isEmpty() || username_f.getText().isBlank()
                    || String.valueOf(pass_f.getPassword()).isEmpty() || String.valueOf(pass_f.getPassword()).isBlank())
                messageLabel.setText("Please fill up empty fields.");
            // IF NO EMPTY FIELDS
            else {
                us.SetName(name_f.getText());
                us.SetMail(email_f.getText());
                us.SetUName(username_f.getText());
                us.SetPass(String.valueOf(pass_f.getPassword()));
                // CHECK IF RE_ENTERED PASSWORD MATCHES
                if (!us.GetPass().equals(String.valueOf(re_pass_f.getPassword())))
                    messageLabel.setText("Passwords do not match!");
                else {
                    // CHECK IF DUPLICATES EXIST IN DATABASE
                    // IF YES, MAIN STAYS STUCK UNTIL NEW EMAIL
                    // AND USERNAMES ARE ENTERED
                    dup_exists = ac.signup(us);
                    set_signing_up(dup_exists);
                    if( dup_exists == 2)
                        frame.dispose();
                    else if (dup_exists == 3)
                        messageLabel.setText("E-mail is already being used!");
                    else if (dup_exists == 4)
                        messageLabel.setText("Username already taken! Enter a different one.");
                }
            }
        });
        frame.add(sign_up_b);

        main_menu_b.addActionListener(ae -> {
            frame.dispose();
            set_signing_up(2);
        });
        frame.add(main_menu_b);

        // DISPLAY THE WINDOW
        frame.setSize(width,height);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    // LOG-IN MENU
    public void login_menu(User us, AccountHandler ac) {

        // SET PROGRAM TO BUSY WAIT STATE
        set_logging_in(0);
        // CREATE THE WINDOW
        JFrame frame = new JFrame();

        // CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton loginButton = new JButton("Log-in");
        JButton main_menu_b = new JButton("Main Menu");
        JTextField username_f = new JTextField();
        JPasswordField pass_f = new JPasswordField();
        JLabel username_l = new JLabel("Username:");
        JLabel pass_l = new JLabel("Password:");
        JLabel messageLabel = new JLabel();

        // ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        username_l.setBounds(x_label,100,75,25);
        frame.add(username_l);
        username_f.setBounds(x_field,100,200,25);
        frame.add(username_f);

        pass_l.setBounds(x_label,150,75,25);
        frame.add(pass_l);
        pass_f.setBounds(x_field,150,200,25);
        frame.add(pass_f);

        loginButton.setBounds(150,200,100,25);
        loginButton.setFocusable(false);

        main_menu_b.setBounds(300,200,100,25);
        main_menu_b.setFocusable(false);

        messageLabel.setBounds(50,250,250,35);
        messageLabel.setFont(new Font(null,Font.BOLD,18));
        frame.add(messageLabel);

        // DO STUFF WHEN BUTTONS ARE PRESSED
        loginButton.addActionListener(ae -> {

            us.SetUName(username_f.getText());
            us.SetPass(String.valueOf(pass_f.getPassword()));

            // SETS logging_in TO 0 IF UNSUCCESSFUL ELSE,
            // SETS IT TO 1 (I.E. THE LOOP IN MAIN BREAKS)
            set_logging_in(ac.login(us));

            if( logging_in == 1)
                frame.dispose();
            else
                messageLabel.setText("Incorrect username or password.");
        });
        frame.add(loginButton);

        main_menu_b.addActionListener(ae -> {
            frame.dispose();
            set_logging_in(9);
        });
        frame.add(main_menu_b);

        // DISPLAY THE WINDOW
        frame.setSize(width,height);
        frame.setLayout(null);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}