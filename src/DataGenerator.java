import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class DataGenerator {

    DBConnector dbc = new DBConnector();

    public void user_generator () {

        String name, uname, email, password;
        name = "dummy";
        uname = "uname";
        email = "uname";
        password = "qwerty";

        // TO GET TODAY'S DATE
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Random rand = new Random();
        int r;

        String idx;
        try {
            // CONNECT TO DATABASE
            Connection con = dbc.connect();

            int i;
            for (i = 0; i < 10; i ++) {

                // MAKE EACH USER UNIQUE
                idx = Integer.toString(i);

                // MAKE RANDOM JOIN DATES (BEFORE TODAY)
                r = 1 + rand.nextInt(90);
                LocalDateTime now = LocalDateTime.now().minusDays(r);

                // PREPARE SQL QUERY
                String values = String.format("NULL, '%s', '%s', '%s', '%s', %d, %d, '%s'",
                                name, (uname + idx), (email + idx + "@gmail.com"),
                                password, 0, 0, dtf.format(now));
                PreparedStatement ps = con.prepareStatement(String.format("INSERT INTO userdata VALUES (%s)", values));

                // EXECUTE QUERY AND CLOSE THE CONNECTION
                ps.executeUpdate();
            }
            con.close();

        } catch (Exception ignored) {}
    }

    public void attendance_generator () {

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

        // RANDOM CLASS
        Random rand = new Random();
        int r;

        // FOR ALL EMPLOYEES, DO STUFF
        for (Employee e: emp_list) {
            // FROM JOIN DATE TO CURRENT DATE, MARK AS ABSENT
            int i = 0;
            LocalDate date = LocalDate.parse(e.join_date);
            while (date.isBefore(now)) {

                // IF NOT SUNDAY, ADD ENTRY AS ABSENT
                if (!date.getDayOfWeek().toString().equals("SUNDAY")) {
                    try {
                        // RANDOM ATTENDANCE
                        r = rand.nextInt(2);

                        // CONNECT TO DATABASE
                        Connection con = dbc.connect();

                        // PREPARE AND EXECUTE SQL QUERY
                        String values = String.format("%d, '%s', %d", e.user_id, dtf.format(date), r);
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
    }

    public void dataset_generator () {

        Random rand = new Random();
        String data;
        int uid, attendance, dow, dom, pres_abs;
        int idx;
        try {
            FileWriter fw = new FileWriter("dataset/dataset.txt");

            for (idx = 0; idx < 1000; idx ++) {

                uid = 1000 + rand.nextInt(1000);
                attendance = rand.nextInt(101);
                pres_abs = rand.nextInt(101);
                dow = (1 + rand.nextInt(6));
                dom = (1 + rand.nextInt(31));

                if (attendance >=70)
                    pres_abs += 30;
                else if (attendance >= 50)
                    pres_abs += 0;
                else if (attendance >= 30)
                    pres_abs -= 30;
                else
                    pres_abs -= 50;

                double p = (double) pres_abs/100;

                if (p > 1)
                    pres_abs = (int)(Math.floor(p));
                else if (p > 0.5)
                    pres_abs = (int)(Math.ceil(p));
                else if (p > 0)
                    pres_abs = (int)(Math.floor(p));
                else
                    pres_abs = (int)(Math.ceil(p));

                data = String.format("%d %d %d %d %d\n",
                        uid, attendance, dow, dom, pres_abs);

                fw.write(data);
            }

            fw.close();
            System.out.println("Successfully generated dataset.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {

        DataGenerator d = new DataGenerator();
        Scanner sc = new Scanner(System.in);
        int option = 0;
        while (option != 4) {
            System.out.println("Select one option -");
            System.out.println("1. Generate Users\n" +
                            "2. Generate Attendance\n" +
                            "3. Generate Dataset\n" +
                            "4. Exit");
            System.out.print(": ");

            option = sc.nextByte();
            switch (option) {
                case 1 -> d.user_generator();
                case 2 -> d.attendance_generator();
                case 3 -> d.dataset_generator();
            }
        }
    }
}
