package utility;

import java.util.Properties;
import javaxt.sql.Database;

public class DatabaseHelper {

    private Database db;

    public DatabaseHelper() {
        Properties prop = new Properties();
        try {
            prop.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
            
            db = new Database();
            db.setDriver(javaxt.sql.Driver.MySQL);
            db.setHost(prop.getProperty("hostname"));
            db.setUserName(prop.getProperty("username"));
            db.setPassword(prop.getProperty("password"));
            db.setName(prop.getProperty("database"));
        } catch (Exception ex) {
        }
    }

    public Database getDatabase() {
        return db;
    }
}
