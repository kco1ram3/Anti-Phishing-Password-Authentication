package entity;

import java.util.Calendar;
import java.util.Date;
import utility.SecurityHelper;
/**
 *
 * @author Add By Net
 * @since 14/12/14
 */
public class ClientInfo {
    private static final String SEPERATOR = "^";
    private String Username;
    private String Password;
    private String ServerAddressFromClient;
    private String N1;
    private String N2;
    private String KeyClient;
    private String AuthenticatorClient;
    private Calendar StartTime;
    private Calendar EndTime;
    
    public ClientInfo(String uname, String pwd) {
        this.Username = uname;
        this.Password = pwd;
        StartTime = Calendar.getInstance();
        EndTime = StartTime;
        EndTime.add(Calendar.MINUTE, 5);
    }
    
    public String getIDc() {
        return Username + Password + ServerAddressFromClient;
    }
   
     public String getUserName() {
        return Username;
    }
      public String getPassword() {
        return Password;
    }
    
    public String getN1() {
        return N1;
    }
    
    public void setN1(String n1) {
         N1 = n1;
    }
    
    public String getN2() {
        return N2;
    }
    
    public void setN2(String n2) {
         N2 = n2;
    }
    
    public String getKcByServer() {
        String serverName = ServerInfo.getServerName();
        KeyClient = SecurityHelper.hash256(
                    Username + SEPERATOR +
                    Password + SEPERATOR +
                    serverName);
        return KeyClient;
    }
    
    public String getAuthencatorClient() {
        return AuthenticatorClient;
    }
    
    public String getTimes() {
        return StartTime.getTime() + SEPERATOR + EndTime.getTime();
    }
    
    public Calendar getEndTimes() {
        return EndTime;
    }
    
    public String getServerAddressFromClient() {
        return ServerAddressFromClient;
    }
}
