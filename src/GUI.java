import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class GUI extends JFrame {

    //region OBJECTS AND VARIABLE DECLARATIONS
    // STATUS VARIABLES
    private int logging_in = 0;    // 0 - WRONG CREDENTIALS, 1 - SUCCESS, -1 - DB ERROR

    // GUI RELATED OBJECTS AND VARS
    JFrame frame = new JFrame();
    JPanel main_panel = new JPanel();
    JPanel main_menu = new JPanel();
    JPanel sign_up_menu = new JPanel();
    JPanel log_in_menu = new JPanel();
    JPanel emp_log_in_menu = new JPanel();
    JPanel emg_log_in_menu = new JPanel();
    JPanel emp_list_menu = new JPanel();
    JPanel manage_menu = new JPanel();
    CardLayout cl = new CardLayout();

    // X-BOUNDS MACROS
    int x_label = 50, x_field = 200;
    // DIMENSIONS FOR FRAME
    int width = 500, height = 600;

    // PREDICTOR CLASS
    Predictor p = new Predictor(3);
    List<Predictor.Instance> instances;

    //endregion

    //region GUI CONSTRUCTION
    public GUI () throws Exception {

        // TRAIN MODEL
        instances = Predictor.readDataSet("D:\\WORK\\CODING\\JAVA\\Att_Calc\\dataset\\training_data.txt");
        p.train(instances);

        // LOAD GUI
        main_panel.setLayout(cl);
        main_menu(main_menu);
        main_panel.add(main_menu, "1");
        main_panel.add(sign_up_menu, "2");
        main_panel.add(log_in_menu, "3");
        main_panel.add(emp_log_in_menu, "4");
        main_panel.add(emg_log_in_menu, "5");
        main_panel.add(emp_list_menu, "6");
        main_panel.add(manage_menu,"7");

        cl.show(main_panel, "1");

        frame.add(main_panel);
        frame.setSize(width,height);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    //endregion

    //region MENUS

    // MAIN MENU
    public void main_menu (JPanel panel) {

        // SHOW PANEL 1 FROM THE CARD LAYOUT
        cl.show(main_panel, "1");

        //region CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton sign_up_b = new JButton("Sign-up");
        JButton log_in_b = new JButton("Log-in");
        JButton exit_b = new JButton("Exit");
        //endregion

        //region ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        sign_up_b.setBounds(150,100,200,50);
        panel.add(sign_up_b);
        log_in_b.setBounds(150,200,200,50);
        panel.add(log_in_b);
        exit_b.setBounds(150,300,200,50);
        panel.add(exit_b);
        //endregion

        //region BUTTONS
        // LOAD SIGN UP MENU WHEN Sign-Up BUTTON IS PRESSED
        sign_up_b.addActionListener(ae -> {
            User user = new User();
            AccountHandler ac = new AccountHandler();
            sign_up_menu(user, ac, sign_up_menu);
        });
        // LOAD SIGN UP MENU WHEN Sign-Up BUTTON IS PRESSED
        log_in_b.addActionListener(ae -> {
            User user = new User();
            AccountHandler ac = new AccountHandler();
            login_menu(user, ac, log_in_menu);
        });
        // EXIT WHEN Exit BUTTON IS PRESSED
        exit_b.addActionListener(ae -> {
            System.exit(0);
        });
        //endregion

        panel.setLayout(null);
    }

    // SIGN-UP MENU
    public void sign_up_menu (User user, AccountHandler ac, JPanel panel) {

        // SHOW PANEL 2 FROM THE CARD LAYOUT
        cl.show(main_panel, "2");

        //region CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton sign_up_b = new JButton("Sign-up");
        JButton main_menu_b = new JButton("Main Menu");
        JTextField name_f = new JTextField();
        JTextField email_f = new JTextField();
        JTextField username_f = new JTextField();
        JPasswordField pass_f = new JPasswordField();
        JPasswordField re_pass_f = new JPasswordField();
        JCheckBox user_type = new JCheckBox();
        JLabel name_l = new JLabel("Name:");
        JLabel email_l = new JLabel("E-mail:");
        JLabel username_l = new JLabel("Username:");
        JLabel user_type_l = new JLabel("Employee Manager?:");
        JLabel pass_l = new JLabel("Password:");
        JLabel re_pass_l = new JLabel("Re-enter password:");
        JLabel messageLabel = new JLabel();
        //endregion

        //region ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        name_l.setBounds(x_label,100,75,25);
        panel.add(name_l);
        name_f.setBounds(x_field,100,200,25);
        panel.add(name_f);

        email_l.setBounds(x_label,150,75,25);
        panel.add(email_l);
        email_f.setBounds(x_field,150,200,25);
        panel.add(email_f);

        username_l.setBounds(x_label,200,75,25);
        panel.add(username_l);
        username_f.setBounds(x_field,200,200,25);
        panel.add(username_f);

        user_type_l.setBounds(x_label,250,200,25);
        panel.add(user_type_l);
        user_type.setBounds(x_field,250,75,25);
        panel.add(user_type);

        pass_l.setBounds(x_label,300,75,25);
        panel.add(pass_l);
        pass_f.setBounds(x_field,300,200,25);
        panel.add(pass_f);

        re_pass_l.setBounds(x_label,350,150,25);
        panel.add(re_pass_l);
        re_pass_f.setBounds(x_field,350,200,25);
        panel.add(re_pass_f);

        sign_up_b.setBounds(150,400,100,25);
        sign_up_b.setFocusable(false);

        main_menu_b.setBounds(300,400,100,25);
        main_menu_b.setFocusable(false);

        messageLabel.setBounds(50,50,300,35);
        messageLabel.setFont(new Font(null,Font.BOLD,12));
        panel.add(messageLabel);
        //endregion

        //region BUTTONS
        // SET USER TYPE AS EMPLOYEE MANAGER IF CHECKED
        user_type.addItemListener(ie -> user.SetUType(1));
        // DO STUFF WHEN SIGN-UP BUTTON IS PRESSED
        sign_up_b.addActionListener(ae -> {
            int dup_exists;
            // CHECK FOR EMPTY FIELDS
            if (name_f.getText().isEmpty() || name_f.getText().isBlank()
                    || email_f.getText().isEmpty() || email_f.getText().isBlank()
                    || username_f.getText().isEmpty() || username_f.getText().isBlank()
                    || String.valueOf(pass_f.getPassword()).isEmpty() || String.valueOf(pass_f.getPassword()).isBlank())
                messageLabel.setText("Please fill up empty fields.");
            // IF NO EMPTY FIELDS
            else {
                user.SetName(name_f.getText());
                user.SetMail(email_f.getText());
                user.SetUName(username_f.getText());
                user.SetPass(String.valueOf(pass_f.getPassword()));
                // CHECK IF RE_ENTERED PASSWORD MATCHES
                if (!user.GetPass().equals(String.valueOf(re_pass_f.getPassword())))
                    messageLabel.setText("Passwords do not match!");
                else {
                    // CHECK IF DUPLICATES EXIST IN DATABASE
                    // IF YES, MAIN STAYS STUCK UNTIL NEW EMAIL
                    // AND USERNAMES ARE ENTERED
                    dup_exists = ac.signup(user);
                    if( dup_exists == 2) {
                        System.out.println("Sign-up successful!");
                        main_menu(main_menu);
                    }
                    else if (dup_exists == 3)
                        messageLabel.setText("E-mail is already being used!");
                    else if (dup_exists == 4)
                        messageLabel.setText("Username already taken! Enter a different one.");
                }
            }
        });
        panel.add(sign_up_b);
        // OPEN MAIN MENU WHEN MAIN MENU IS PRESSED
        main_menu_b.addActionListener(ae -> main_menu(main_menu));
        panel.add(main_menu_b);
        //endregion

        panel.setLayout(null);
    }

    // LOG-IN MENU
    public void login_menu(User user, AccountHandler ac, JPanel panel) {

        // SHOW PANEL 3 FROM THE CARD LAYOUT
        cl.show(main_panel, "3");

        //region CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton loginButton = new JButton("Log-in");
        JButton main_menu_b = new JButton("Main Menu");
        JTextField username_f = new JTextField();
        JPasswordField pass_f = new JPasswordField();
        JLabel username_l = new JLabel("Username:");
        JLabel pass_l = new JLabel("Password:");
        JLabel messageLabel = new JLabel();
        //endregion

        //region ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        username_l.setBounds(x_label,100,75,25);
        panel.add(username_l);
        username_f.setBounds(x_field,100,200,25);
        panel.add(username_f);

        pass_l.setBounds(x_label,150,75,25);
        panel.add(pass_l);
        pass_f.setBounds(x_field,150,200,25);
        panel.add(pass_f);

        loginButton.setBounds(150,200,100,25);
        loginButton.setFocusable(false);

        main_menu_b.setBounds(300,200,100,25);
        main_menu_b.setFocusable(false);

        messageLabel.setBounds(50,250,300,35);
        messageLabel.setFont(new Font(null,Font.BOLD,13));
        panel.add(messageLabel);
        //endregion

        //region BUTTONS
        // LOG-IN WHEN Log-in BUTTON IS PRESSED
        loginButton.addActionListener(ae -> {

            user.SetUName(username_f.getText());
            user.SetPass(String.valueOf(pass_f.getPassword()));

            // SETS logging_in TO 0 IF UNSUCCESSFUL ELSE,
            // SETS IT TO 1 (I.E. THE LOOP IN MAIN BREAKS)
            // SET LOGGED IN TO 1, TO KEEP USER LOGGED IN
            logging_in = ac.login(user);

            if( logging_in == 1) {
                System.out.println("Log-in successful!");
                if (user.GetUType() == 1)
                    logged_in_menu_emp_mgr(user, emg_log_in_menu);
                else
                    logged_in_menu_emp(user, emp_log_in_menu);
            }
            else
                messageLabel.setText("Incorrect username or password.");
        });
        panel.add(loginButton);
        // GO TO MAIN MENU WHEN Main Menu BUTTON IS PRESSED
        main_menu_b.addActionListener(ae -> {
            main_menu(main_menu);
        });
        panel.add(main_menu_b);
        //endregion

        panel.setLayout(null);
    }

    // LOGGED IN MENU FOR EMPLOYEE
    public void logged_in_menu_emp (User user, JPanel panel) {

        DBHandler db = new DBHandler();

        // SHOW PANEL 4 FROM THE CARD LAYOUT
        cl.show(main_panel, "4");

        //region CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton check_attendance = new JButton("Check Attendance");
        JButton mark_attendance = new JButton("Mark Attendance");
        JButton log_out = new JButton("Log out");
        JLabel attendance_count = new JLabel();
        JLabel logged_in_as = new JLabel();
        //endregion

        //region ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        attendance_count.setBounds(100,100,300,25);
        attendance_count.setHorizontalAlignment(JLabel.CENTER);
        panel.add(attendance_count);

        check_attendance.setBounds(200,150,150,25);
        panel.add(check_attendance);

        mark_attendance.setBounds(200,200,150,25);
        panel.add(mark_attendance);

        log_out.setBounds(200,300,150,25);
        panel.add(log_out);

        logged_in_as.setBounds(100,350,300,25);
        logged_in_as.setText(String.format("Logged in as: %s", user.GetName()));
        panel.add(logged_in_as);
        //endregion

        //region BUTTONS
        // LOG OUT WHEN Log out BUTTON IS PRESSED
        log_out.addActionListener(ae -> {
            main_menu(main_menu);
        });
        // SHOW ATTENDANCE AND ATTENDANCE PERCENTAGE WHEN CLICKED
        check_attendance.addActionListener(ae -> {
            db.get_user_data(user);
            int att_percentage = Math.round(((float)user.GetAttendance()/user.GetTotalAttendance()) * 100);
            String text = String.format("Attendance: %d out of %d (%d%%)", user.GetAttendance(), user.GetTotalAttendance(), att_percentage);
            attendance_count.setText(text);
        });
        // MARK EMPLOYEE AS PRESENT WHEN THIS BUTTON IS PRESSED
        mark_attendance.addActionListener(ae -> {
            db.mark_attendance(user);
            mark_attendance.setEnabled(false);
        });
        // IF EMPLOYEE HAS ALREADY MARKED THEIR ATTENDANCE,
        // DISABLE mark_attendance BUTTON
        if (db.check_attendance_today(user) == 1)
            mark_attendance.setEnabled(false);
        //endregion

        panel.setLayout(null);
    }

    // LOGGED IN MENU FOR EMPLOYEE MANAGER
    public void logged_in_menu_emp_mgr (User user, JPanel panel) {

        // SHOW PANEL 5 FROM THE CARD LAYOUT
        cl.show(main_panel, "5");

        //region CREATE BUTTONS, LABELS, AND TEXT FIELDS
        JButton check_attendance = new JButton("Check Attendance");
        JButton predict_presence = new JButton("Predict Presence");
        JButton manage_employee = new JButton("Manage Employee");
        JButton show_emp_list = new JButton("Employee List");
        JButton log_out = new JButton("Log out");
        JTextField employee_id_f = new JTextField();
        JLabel message_label = new JLabel();
        JLabel employee_id_l = new JLabel("Employee ID:");
        JLabel logged_in_as = new JLabel();
        //endregion

        //region ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        message_label.setBounds(50,50,400,25);
        message_label.setHorizontalAlignment(JLabel.CENTER);
        panel.add(message_label);

        employee_id_l.setBounds(100,100,100,25);
        panel.add(employee_id_l);
        employee_id_f.setBounds(200,100,150,25);
        panel.add(employee_id_f);

        check_attendance.setBounds(200,150,150,25);
        panel.add(check_attendance);

        predict_presence.setBounds(200,200,150,25);
        panel.add(predict_presence);

        show_emp_list.setBounds(200,250,150,25);
        panel.add(show_emp_list);

        manage_employee.setBounds(200,300,150,25);
        panel.add(manage_employee);

        log_out.setBounds(200,350,150,25);
        panel.add(log_out);

        logged_in_as.setBounds(100,400,300,25);
        logged_in_as.setText(String.format("Logged in as: %s", user.GetName()));
        panel.add(logged_in_as);
        //endregion

        //region BUTTONS
        // LOG OUT WHEN Log out BUTTON IS PRESSED
        log_out.addActionListener(ae -> main_menu(main_menu));
        // SHOW ATTENDANCE AND ATTENDANCE PERCENTAGE WHEN CLICKED
        check_attendance.addActionListener(ae -> {
            DBHandler db = new DBHandler();
            int uid = Integer.parseInt(employee_id_f.getText());

            int[] attendance = db.get_attendance(uid);
            int att_percentage = Math.round(((float)attendance[0]/attendance[1]) * 100);

            String text = String.format("Attendance: %d out of %d (%d%%)", attendance[0], attendance[1], att_percentage);
            message_label.setText(text);
        });
        // PREDICT PRESENCE WHEN CLICKED
        predict_presence.addActionListener( ae -> {
            DBHandler db = new DBHandler();
            int uid = Integer.parseInt(employee_id_f.getText());

            int[] attendance = db.get_attendance(uid);
            int att_percentage = Math.round(((float)attendance[0]/attendance[1]) * 100);

            // TO GET TOMORROW'S DATE
            LocalDate date = LocalDate.now().plusDays(1);
            String dow = date.getDayOfWeek().toString();
            int dom = date.getDayOfMonth();

            // TO GET DAY NUMBER
            int dow_number;
            switch (dow) {
                case ("MONDAY") -> dow_number = 1;
                case ("TUESDAY") -> dow_number = 2;
                case ("WEDNESDAY") -> dow_number = 3;
                case ("THURSDAY") -> dow_number = 4;
                case ("FRIDAY") -> dow_number = 5;
                case ("SATURDAY") -> dow_number = 6;
                default -> dow_number = 7;
            }

            if (dow_number == 7)
                System.out.println("Sunday no work.");
            else {
                // CREATE SAMPLE FOR PREDICTION
                int[] x = {att_percentage, dow_number, dom};

                // CALL PREDICTION FUNCTION
                double prob = 1 - p.classify(x);

                // DISPLAY PREDICTION
                String att_status;
                if (prob < 0.5) {
                    att_status = "ABSENT";
                    prob = 1 - prob;
                }
                else if (prob > 0.5)
                    att_status = "PRESENT";
                else
                    att_status = "UNCERTAIN";

                int att_prob_percent = (int)(prob * 100);
                message_label.setText(String.format("Attendance next working day is - %s (%d%% likely)", att_status, att_prob_percent));
            }
        });
        // SHOW EMPLOYEE LIST WHEN CLICKED
        show_emp_list.addActionListener(ae -> emp_list_menu(user, emp_list_menu));
        // REMOVE EMPLOYEE WHEN CLICKED
        manage_employee.addActionListener(ae -> manage_menu(user, manage_menu));
        //endregion

        panel.setLayout(null);
    }

    // EMPLOYEE LIST MENU
    public void emp_list_menu (User user, JPanel panel) {

        // SHOW PANEL 6 FROM THE CARD LAYOUT
        cl.show(main_panel, "6");

        JPanel sub_panel = new JPanel();
        JButton back = new JButton("Back");

        //region TABLE CREATION
        // CREATE A DYNAMIC TABLE
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);

        DBHandler db = new DBHandler();
        List<Employee> emp_list = db.get_emp_list();

        // ADD COLUMNS TO THE TABLE
        tableModel.addColumn("Employee ID");
        tableModel.addColumn("Managed By");
        tableModel.addColumn("Name");
        tableModel.addColumn("E-mail ID");

        // ADD ROWS TO TABLE VIEW OF EMPLOYEE DATA
        for (Employee emp : emp_list) {
            String[] emp_data = {Integer.toString(emp.user_id),
                                Integer.toString(emp.managed_by),
                                emp.name, emp.email};

            tableModel.insertRow(0, emp_data);
        }

        // RESIZE, ORIENT, AND ADD TABLE TO SUB-PANEL
        table.setPreferredScrollableViewportSize(new Dimension(400, 250));
        table.setBounds(50, 0, 400, 250);
        sub_panel.add(new JScrollPane(table));
        //endregion

        //region ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        back.setBounds(200,350,100,25);
        panel.add(back);

        sub_panel.setBounds(0, 0, 500, 300);
        panel.add(sub_panel);
        //endregion

        // GO BACK TO EMPLOYEE MANAGER LOG-IN MENU WHEN CLICKED
        back.addActionListener( ae -> logged_in_menu_emp_mgr(user, emg_log_in_menu));

        panel.setLayout(null);
    }

    // EMPLOYEES UNDER SUPERVISION LIST
    public void manage_menu (User user, JPanel panel) {

        // SHOW PANEL 7 FROM THE CARD LAYOUT
        cl.show(main_panel, "7");

        JPanel sub_panel = new JPanel();
        JButton back = new JButton("Back");
        JButton manage = new JButton("Manage");
        JButton un_manage = new JButton("Un-manage");
        JButton remove = new JButton("Remove");
        JLabel message_label = new JLabel();

        //region TABLE CREATION
        // CREATE A DYNAMIC TABLE
        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);

        DBHandler db = new DBHandler();
        List<Employee> emp_list = db.get_emp_list();

        // ADD COLUMNS TO THE TABLE
        tableModel.addColumn("Employee ID");
        tableModel.addColumn("Managed By");
        tableModel.addColumn("Name");
        tableModel.addColumn("E-mail ID");

        // ADD ROWS TO TABLE VIEW OF EMPLOYEE DATA
        for (Employee emp : emp_list) {
            String[] emp_data = {Integer.toString(emp.user_id),
                                Integer.toString(emp.managed_by),
                                emp.name, emp.email};

            tableModel.insertRow(0, emp_data);
        }

        // RESIZE, ORIENT, AND ADD TABLE TO SUB-PANEL
        table.setPreferredScrollableViewportSize(new Dimension(400, 250));
        table.setBounds(50, 0, 400, 250);
        sub_panel.add(new JScrollPane(table));
        //endregion

        //region ORIENTATION AND RESIZING, AND ADDING OF ITEMS TO FRAME
        sub_panel.setBounds(0, 0, 500, 300);
        panel.add(sub_panel);

        manage.setBounds(100,300,150,25);
        panel.add(manage);

        un_manage.setBounds(300,300,150,25);
        panel.add(un_manage);

        remove.setBounds(200,350,150,25);
        panel.add(remove);

        back.setBounds(200,400,150,25);
        panel.add(back);

        message_label.setBounds(200,450,150,25);
        panel.add(message_label);
        //endregion

        //region BUTTONS
        // MANAGE SELECTED EMPLOYEES
        manage.addActionListener( ae -> {

            int emp_id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
            int manager_id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 1).toString());

            // IF EMPLOYEE IS BEING MANAGED ALREADY
            if (manager_id != 0)
                message_label.setText("Employee is being managed already.");
            // IF EMPLOYEE IS NOT BEING MANAGED
            else {
                if (emp_id == user.GetUID())
                    message_label.setText("You cannot manage yourself.");
                else {
                    db.ch_manager(emp_id, user.GetUID());
                    tableModel.setValueAt(user.GetUID(), table.getSelectedRow(), 1);
                    message_label.setText("Success");
                }
            }
        });

        // UN-MANAGE SELECTED EMPLOYEE
        un_manage.addActionListener( ae -> {

            int emp_id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
            int manager_id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 1).toString());

            // IF EMPLOYEE IS BEING MANAGED ALREADY
            if (manager_id != 0) {
                // IF YOU ARE MANAGING THE EMPLOYEE
                if (manager_id == user.GetUID()) {
                    db.ch_manager(emp_id, 0);
                    tableModel.setValueAt(0, table.getSelectedRow(), 1);
                    message_label.setText("Success");
                }
                else
                    message_label.setText("Invalid operation!");
            }

            // IF EMPLOYEE IS NOT BEING MANAGED
            else
                message_label.setText("Invalid operation!");
        });

        // REMOVE SELECTED EMPLOYEES
        remove.addActionListener( ae -> {

            int emp_id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 0).toString());
            int manager_id = Integer.parseInt(tableModel.getValueAt(table.getSelectedRow(), 1).toString());

            // IF EMPLOYEE IS BEING MANAGED
            if (manager_id != 0) {
                // IF YOU ARE MANAGING THE SELECTED EMPLOYEE
                if (manager_id == user.GetUID()) {
                    db.delete_user(emp_id);
                    tableModel.removeRow(table.getSelectedRow());
                    message_label.setText("Successfully removed employee");
                }
                // IF YOU ARE NOT MANAGING THE SELECTED EMPLOYEE
                else
                    message_label.setText("You cannot remove this employee.");
            }
            // IF EMPLOYEE IS NOT BEING MANAGED
            else {
                if (emp_id == user.GetUID())
                    message_label.setText("You cannot remove yourself.");
                else {
                    db.delete_user(emp_id);
                    tableModel.removeRow(table.getSelectedRow());
                    message_label.setText("Successfully removed employee.");
                }
            }
        });

        // GO BACK TO EMPLOYEE MANAGER MENU
        back.addActionListener( ae -> logged_in_menu_emp_mgr(user, emg_log_in_menu));
        //endregion

        panel.setLayout(null);

    }

    //endregion
}