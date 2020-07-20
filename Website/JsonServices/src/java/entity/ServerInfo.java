package entity;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import utility.SecurityHelper;
import utility.UsbHelper;
/**
 *
 * @author Add By Net
 * @since 14/12/14
 */
public class ServerInfo {
    private static String ServerName;
    private static String ServerAddress;
    private static final String SEPERATOR = "^";
    private static String MessageToClient;
    private String N2;
    private String AuthenticatorServer;
    private String AuthenticatorCServer;
    private String SAC;
    private String KeyServer;
    private ClientInfo Client;
    
    public ServerInfo(ClientInfo ci) {
        this.Client = ci;
    }
    
    public static String getServerAddress() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        ServerAddress = ip.getHostAddress();
        return ServerAddress;
    }
    
    /**
     * Add By Net
     * @since 3/1/2015
     */
    public static String getServerName() {
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            Logger.getLogger(ServerInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
        ServerName = ip.getHostName();
        return ServerName;
    }
    
    public String getN2() {
        N2 = SecurityHelper.getRandomString(32);
        return N2;
    }
    
    public String getKs() {
        KeyServer = UsbHelper.getKeyFromUsb();
        return KeyServer;
    }
    
    public String getAuthenticatorServer() {
        AuthenticatorServer = SecurityHelper.HMAC(Client.getKcByServer(),
                                Client.getIDc() + SEPERATOR +
                                getServerAddress() + SEPERATOR +
                                Client.getN1() + SEPERATOR +
                                getN2() + SEPERATOR + 
                                Client.getTimes());
        return AuthenticatorServer;
    }
    
    public String getAuthenticatorCServer(String userName ,String password ,String serverName, String N2) {
        AuthenticatorCServer = SecurityHelper.HMAC(Client.getKcByServer(),
                                userName + password + serverName + SEPERATOR +
                                serverName + SEPERATOR + //getServerAddress()
                                N2 + SEPERATOR + 
                                Client.getTimes());
        return AuthenticatorCServer;
    }
    
    
    public String getSAC() {
        SAC = SecurityHelper.HMAC(getKs(), Client.getIDc() + SEPERATOR +
                                    getN2() + SEPERATOR +
                                    Client.getTimes());
        return SAC;
    }
    
    public String getMessageToBeSendToClient(String user ,String password) {
        MessageToClient = Client.getIDc() + SEPERATOR +
                            getServerAddress() + SEPERATOR +
                            Client.getN1() + SEPERATOR +
                            getN2() + SEPERATOR +
                            Client.getTimes() + SEPERATOR +
                            getAuthenticatorServer() + SEPERATOR +
                            getSAC();
        return MessageToClient;
    }
}
