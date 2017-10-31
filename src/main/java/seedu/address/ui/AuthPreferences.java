package seedu.address.ui;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;

/**
 * Auth preferences object to record username and password
 */
public class AuthPreferences {

    public static final String CONFIG_FILE = "passwordConfig.txt";

    private String username;
    private String password;

    public AuthPreferences() {
        username = "admin";
        password = "admin";
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Initial configuration for password settings
     */
    public static void initConfig() {
        Writer writer = null;
        try {
            AuthPreferences preference = new AuthPreferences();
            Gson gson = new Gson();
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preference, writer);
        } catch (IOException ex) {
            Logger.getLogger(AuthPreferences.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                Logger.getLogger(AuthPreferences.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


}
