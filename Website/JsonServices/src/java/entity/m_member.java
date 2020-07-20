package entity;

import java.util.Date;

public class m_member {

    public static final String TABLE_NAME = "m_member";

    public class ColumnName {
        /*
        public static final String ROWID = "RowID";
        public static final String NAME = "Name";
        public static final String SURNAME = "Surname";
        public static final String EMAIL = "Email";
        public static final String MOBILEPHONENO = "MobilePhoneNo";
        public static final String USERNAME = "Username";
        public static final String PASSWORD = "Password";
        public static final String SALT = "Salt";
        public static final String OTP_REFERENCE = "OTP_Reference";
        public static final String OTP_PASSWORD = "OTP_Password";
        public static final String OTP_REQUESTTIME = "OTP_RequestTime";
        public static final String REGISTERDATE = "RegisterDate";
        public static final String UPDATEDATE = "UpdateDate";
        */
        public static final String ROWID = "A";
        public static final String NAME = "B";
        public static final String SURNAME = "C";
        public static final String EMAIL = "D";
        public static final String MOBILEPHONENO = "E";
        public static final String USERNAME = "F";
        public static final String PASSWORD = "G";
        public static final String SALT = "H";
        public static final String OTP_REFERENCE = "I";
        public static final String OTP_PASSWORD = "J";
        public static final String OTP_REQUESTTIME = "K";
        public static final String REGISTERDATE = "L";
        public static final String UPDATEDATE = "M";
    }

    private long rowID;
    private String name;
    private String surname;
    private String email;
    private String mobilePhoneNo;
    private String username;
    private String password;
    private String salt;
    private String otpReference;
    private String otpPassword;
    private Date otpRequestTime;
    private Date registerDate;
    private Date updateDate;

    public long getRowID() {
        return this.rowID;
    }

    public void setRowID(long rowID) {
        this.rowID = rowID;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobilePhoneNo() {
        return this.mobilePhoneNo;
    }

    public void setMobilePhoneNo(String mobilePhoneNo) {
        this.mobilePhoneNo = mobilePhoneNo;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return this.salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getOtpReference() {
        return this.otpReference;
    }

    public void setOtpReference(String otpReference) {
        this.otpReference = otpReference;
    }

    public String getOtpPassword() {
        return this.otpPassword;
    }

    public void setOtpPassword(String otpPassword) {
        this.otpPassword = otpPassword;
    }

    public Date getOtpRequestTime() {
        return this.otpRequestTime;
    }

    public void setOtpRequestTime(Date otpRequestTime) {
        this.otpRequestTime = otpRequestTime;
    }

    public Date getRegisterDate() {
        return this.registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getUpdateDate() {
        return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
