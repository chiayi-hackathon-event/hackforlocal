package forms;

/**
 * Created by Saber on 2016/3/7.
 */
public class formResetPassword {
    protected String password;
    protected String email;
    protected String token;

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
