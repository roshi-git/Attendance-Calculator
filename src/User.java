public class User {

    private String name, uname;
    private String email, pass;
    private int user_type;

    // Getters and Setters for USERNAME
    public String GetUname() {
        return uname;
    }
    public void SetUName(String uname) {
        this.uname = uname;
    }

    // Getters and Setters for NAME
    public String GetName() {
        return name;
    }
    public void SetName(String name) {
        this.name = name;
    }

    // Getters and Setters for E-MAIL
    public String GetMail() {
        return email;
    }
    public void SetMail(String email) {
        this.email = email;
    }

    // Getters and Setters for PASSWORD
    public String GetPass() {
        return pass;
    }
    public void SetPass(String pass) {
        this.pass = pass;
    }

    // Getters and Setters for USERTYPE
    public int GetUType () { return user_type; }
    public void SetUType (int user_type) {this.user_type = user_type; }
}