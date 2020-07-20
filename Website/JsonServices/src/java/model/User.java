/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import entity.m_member;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.DatabaseHelper;
import utility.SecurityHelper;

/**
 *
 * @author ZigLost
 */
public class User {
    private DatabaseHelper dbHelper;
    private static final String TABLE_NAME = "user";
    
    private String Username;
    private String Salt;
    private String MaskedSecret;
    private String Token;
    
    public User() {}
    
    public User(String username, String password, 
            String serverName, String keyServer) {
        this.Username = username;
        this.Salt = SecurityHelper.getSalt();
        this.MaskedSecret = genMaskedSecret(serverName, keyServer, password);
    }
    
    public String getUsername() {
        return Username;
    }

    public void setUsername(String Username) {
        this.Username = Username;
    }

    public String getSalt() {
        return Salt;
    }

    public void setSalt(String Salt) {
        this.Salt = Salt;
    }

    public String getMaskedSecret() {
        return MaskedSecret;
    }

    public void setMaskedSecret(String MaskedSecret) {
        this.MaskedSecret = MaskedSecret;
    }
    
    public String getToken() {
        if (Token == null) {
            return "";
        }
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }
    
    public String genMaskedSecret(String serverName, String keyServer, String password) {
        return genMaskedSecret(serverName, keyServer, password, Salt);
    }
    
    public String genMaskedSecret(String serverName, String keyServer, String password, String salt) {
        String mask = "",
                keyC = SecurityHelper.hash256(Username + password + serverName),
                temp = SecurityHelper.hash256(keyServer + salt);
        
        mask = SecurityHelper.xorHex(keyC, temp);
        
        return mask;
    }
    
    public  boolean isUserExist(String username) throws SQLException {
        boolean isExist = false;
        dbHelper = new DatabaseHelper();
        javaxt.sql.Connection conn = new javaxt.sql.Connection();
        try {
            conn.open(dbHelper.getDatabase());
            javaxt.sql.Recordset rs = new javaxt.sql.Recordset();
            rs.open(String.format("SELECT * FROM " + TABLE_NAME + " "
                    + "WHERE " + ColumnName.USERNAME + " = '%s'", username), conn);

            if (rs.hasNext()) {
                isExist = true;
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(m_member_model.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        conn.close();

        return isExist;
    }
    
    public long insert(User user) throws SQLException {
        long rowID = 0;
        dbHelper = new DatabaseHelper();
        javaxt.sql.Connection conn = new javaxt.sql.Connection();
        try {
            conn.open(dbHelper.getDatabase());
            javaxt.sql.Recordset rs = new javaxt.sql.Recordset();
            rs.open("SELECT * FROM " + TABLE_NAME + " WHERE 1 = 0", conn, false);

            rs.addNew();
            rs.setValue("UserName", user.getUsername());
            rs.setValue("Salt", user.getSalt());
            rs.setValue("MaskedSecret", user.getMaskedSecret());
            rs.update();
            rs.close();

            rowID = rs.getGeneratedKey().toLong();
        } catch (SQLException ex) {
            Logger.getLogger(m_member_model.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        conn.close();

        return rowID;
    }
    
    public User getUserByName(String username) throws SQLException {
        dbHelper = new DatabaseHelper();
        javaxt.sql.Connection conn = new javaxt.sql.Connection();
        User user = new User();

        try {
            conn.open(dbHelper.getDatabase());
            javaxt.sql.Recordset rs = new javaxt.sql.Recordset();
            rs.open(String.format("SELECT * FROM " + TABLE_NAME + " "
                    + "WHERE " + ColumnName.USERNAME + " = '%s'", username), conn);

            while (rs.hasNext()) {
                user.setUsername(rs.getValue(ColumnName.USERNAME).toString());
                user.setSalt(rs.getValue(ColumnName.SALT).toString());
                user.setMaskedSecret(rs.getValue(ColumnName.MASKEDSECRET).toString());
                rs.moveNext();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(m_member_model.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        conn.close();

        return user;
    }
    
    public void updateToken(String username, String token) throws SQLException {
        dbHelper = new DatabaseHelper();
        javaxt.sql.Connection conn = new javaxt.sql.Connection();
        try {
            conn.open(dbHelper.getDatabase());
            javaxt.sql.Recordset rs = new javaxt.sql.Recordset();

            rs.open(String.format("SELECT * FROM " + TABLE_NAME + " WHERE " + ColumnName.USERNAME + " = '%s'", username), conn, false);

            rs.setValue(ColumnName.TOKEN, token);
            rs.update();
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        conn.close();
    }
    
    public boolean checkExistToken(String token) throws SQLException {
        boolean isExist = false;
        dbHelper = new DatabaseHelper();
        javaxt.sql.Connection conn = new javaxt.sql.Connection();
        try {
            conn.open(dbHelper.getDatabase());
            javaxt.sql.Recordset rs = new javaxt.sql.Recordset();
            rs.open(String.format("SELECT * FROM " + TABLE_NAME + " "
                    + "WHERE " + ColumnName.TOKEN + " = '%s'", token), conn, false);

            if (rs.hasNext()) {
                isExist = true;
                rs.setValue(ColumnName.TOKEN, null);
                rs.update();
            }
            rs.close();
        } catch (SQLException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
            throw ex;
        }
        conn.close();

        return isExist;
    }
    
    public class ColumnName {
        public static final String USERNAME = "UserName";
        public static final String SALT = "Salt";
        public static final String MASKEDSECRET = "MaskedSecret";
        public static final String TOKEN = "Token";
    }
}
