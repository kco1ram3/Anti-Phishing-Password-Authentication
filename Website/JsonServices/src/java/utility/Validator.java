/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;

import entity.m_member;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Add by Net
 * @since 14/12/2014
 */
public class Validator {
    private static DatabaseHelper dbHelper;
    
    public static boolean isServerAddress(String ip) {
        // Verifies if the request server’s address belongs to the server
        boolean isTrue = false;
        InetAddress serverAddress = null;
        
        try {
            serverAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(Validator.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if(ip.equals(serverAddress.getHostAddress())) {
            isTrue = true;
        }
        return isTrue;
    }
    
    public static boolean isUserExist(String username) {
        boolean isExist = false;
        // checks its database to see if the user exists
        dbHelper = new DatabaseHelper();
        javaxt.sql.Connection conn = new javaxt.sql.Connection();
        try {
            conn.open(dbHelper.getDatabase());
            javaxt.sql.Recordset rs = new javaxt.sql.Recordset();
            rs.open("SELECT * FROM " + m_member.TABLE_NAME + " WHERE " + 
                        m_member.ColumnName.USERNAME + " = " + username, conn);

            isExist = rs.hasNext();
            rs.close();
        } catch (SQLException ex) {
            
        }
        conn.close();

        return isExist;
    }
    
    public static boolean isAuthenPass() {
        boolean isPass = false;
        // Need to compare the messages between client and server
        return isPass;
    }
}
