package controller;

import entity.ClientInfo;
import entity.ServerInfo;
import entity.m_member;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import utility.SecurityHelper;

public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String Token = request.getParameter("Token");
        String isLogin = "";
        HttpSession session1 = request.getSession();
        try {
            if (Token == null) {
                isLogin = (String) session1.getAttribute("isLogin");
            } else {
                if (new User().checkExistToken(Token)) {
                    isLogin = "true";
                } else {
                    isLogin = "false";
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (isLogin == null) {
            isLogin = "";
        }

        if (isLogin.equals("true")) {
            HttpSession session2 = request.getSession(true);
            session2.setAttribute("isLogin", isLogin);

            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("window.location.href='" + request.getContextPath() + "/Home';");
            out.println("</script>");
            out.close();
        } else if (isLogin.equals("false")) {
            PrintWriter out = response.getWriter();
            out.println("<script type=\"text/javascript\">");
            out.println("window.location.href='" + request.getContextPath() + "/Login';");
            out.println("</script>");
            out.close();
        }

        RequestDispatcher view = request.getRequestDispatcher("login.jsp");
        view.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        String status = request.getParameter("status");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String ip = request.getParameter("ip");
        String userID = request.getParameter("userID");
        String N1 = request.getParameter("N1");
        String N2 = request.getParameter("N2");
        String times = request.getParameter("times");
        String authenicatorS = "";
        String authenicatorC = request.getParameter("authenicatorC");
        String sac = request.getParameter("sac");
        boolean invalidIP = false;
        JSONArray list = new JSONArray();
        JSONObject data;

        try {
            data = new JSONObject();
            if (status.equals("1")) {

                ClientInfo clientInfo = new ClientInfo(username, password);
                User existsUser = new User().getUserByName(clientInfo.getUserName());
                if (existsUser.getUsername() != null) {
                    ServerInfo serverInfo = new ServerInfo(clientInfo);
                    String ipServier = ServerInfo.getServerAddress();

                    if (ipServier == null ? ip == null : ipServier.equals(ip)) {
                        invalidIP = true;
                    }

                    ip = ipServier;
                    N2 = serverInfo.getN2();
                    clientInfo.setN2(N2);

                    HttpSession sess = request.getSession(true);
                    sess.setAttribute("USER", clientInfo);

                    times = clientInfo.getTimes(); //new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date()) + "|" + new SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new Date(System.currentTimeMillis() + 30 * 60 * 1000));

                    authenicatorS = serverInfo.getAuthenticatorServer();
                    sac = serverInfo.getSAC();
                    data.put("username", username);
                    data.put("password", password);
                    data.put("ip", ip);
                    data.put("userID", userID);
                    data.put("N1", N1);
                    data.put("N2", N2);
                    data.put("Times", times);
                    data.put("AuthenticatorS", authenicatorS);
                    data.put("SAC", sac);
                    data.put("Kc", clientInfo.getKcByServer());
                    data.put("invalidIP", invalidIP);
                    data.put("IsExistsUser", true);
                } else {
                    data.put("IsExistsUser", false);
                }
            } else if (status.equals("2")) {

                HttpSession sess = request.getSession(true);
                ClientInfo client = (ClientInfo) sess.getAttribute("USER");
                ServerInfo serverInfo = new ServerInfo(client);
                Calendar StartTime = Calendar.getInstance();
                boolean isTimeOut = false;
                boolean authentication = false;
                long dateNow = StartTime.getTime().getTime();

                if (client.getEndTimes().getTime().getTime() <= dateNow) {
                    isTimeOut = true;
                }

                //User checkUser = new User(client.getUserName(), client.getPassword(), serverInfo.getServerName(), serverInfo.getKs());
                User existsUser = new User().getUserByName(client.getUserName());
                String strMasked = existsUser.genMaskedSecret(ServerInfo.getServerName(), serverInfo.getKs(), client.getPassword(), existsUser.getSalt());
                if (existsUser != null) {
                    if (existsUser.getMaskedSecret().equals(strMasked)) {
                        if (serverInfo.getAuthenticatorCServer(client.getUserName(), client.getPassword(), serverInfo.getServerName(), client.getN2()).equals(authenicatorC)) {
                            authentication = true;
                            existsUser.setToken(SecurityHelper.hash256(SecurityHelper.getRandomString(32)));
                            existsUser.updateToken(client.getUserName(), existsUser.getToken());
                        }
                    }
                }

                data.put("ip", N2);
                data.put("userID", userID);
                data.put("N2", N2);
                data.put("Times", times);
                data.put("AuthenicatorC", authenicatorC);
                data.put("SAC", sac);
                data.put("isTimeOut", isTimeOut);
                data.put("authentication", authentication);
                data.put("token", existsUser.getToken());
            }
            list.add(data);
            //Thread.sleep(1000);
            out.print(list);
        } catch (Exception ex) {
            out.println("<br /><font color='#FF0000'>" + ex.getMessage() + "</font>");
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            out.flush();
            out.close();
        }
    }

    public static String generateSessionKey(int length) {
        String alphabet = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        int n = alphabet.length();
        String result = new String();
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            result = result + alphabet.charAt(r.nextInt(n));
        }
        return result;
    }

    public static String hash256(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        return bytesToHex(md.digest());
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte byt : bytes) {
            result.append(Integer.toString((byt & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }

    public static String encrypt(String message) {
        try {
            return hash256(message);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(LoginServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
