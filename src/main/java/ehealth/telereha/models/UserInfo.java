package ehealth.telereha.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.databind.node.ObjectNode;

@Document(collection = "UserInfo")
public class UserInfo {

    @Id
    private String id;
    private String email;
    private String pw;
    private String salt;
    private String fname;
    private String lname;
    private String role;
    private String bDate;
    private String streetAddr;
    private String houseNumAddr;
    private String cityAddr;
    private String postalCodeAddr;
    private String therapistType;
    private String userID;
    private String phoneNum;

    // Standardkonstruktor
    public UserInfo() {}

    // Angepasster Konstruktor
    public UserInfo(String email, String pw, String salt, String fname, String lname, String role,
                    String bDate, String streetAddr, String houseNumAddr, String cityAddr,
                    String postalCodeAddr, String userID, String phoneNum) {
        this.email = email;
        this.pw = pw;
        this.salt = salt;
        this.fname = fname;
        this.lname = lname;
        this.role = role;
        this.bDate = bDate;
        this.streetAddr = streetAddr;
        this.houseNumAddr = houseNumAddr;
        this.cityAddr = cityAddr;
        this.postalCodeAddr = postalCodeAddr;
        this.userID = userID;
        this.phoneNum = phoneNum;
        this.therapistType = null; 
    }

    // Getter und Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getBDate() {
        return bDate;
    }

    public void setBDate(String bDate) {
        this.bDate = bDate;
    }

    public String getStreetAddr() {
        return streetAddr;
    }

    public void setStreetAddr(String streetAddr) {
        this.streetAddr = streetAddr;
    }

    public String getHouseNumAddr() {
        return houseNumAddr;
    }

    public void setHouseNumAddr(String houseNumAddr) {
        this.houseNumAddr = houseNumAddr;
    }

    public String getCityAddr() {
        return cityAddr;
    }

    public void setCityAddr(String cityAddr) {
        this.cityAddr = cityAddr;
    }

    public String getPostalCodeAddr() {
        return postalCodeAddr;
    }

    public void setPostalCodeAddr(String postalCodeAddr) {
        this.postalCodeAddr = postalCodeAddr;
    }

    public String getTherapistType() {
        return therapistType;
    }

    public void setTherapistType(String therapistType) {
        this.therapistType = therapistType;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return email;
    }

    public void toJsonObject(ObjectNode jsonNode) {
        jsonNode.put("userId", this.userID);
        jsonNode.put("email", this.email);
        jsonNode.put("fname", this.fname);
        jsonNode.put("lname", this.lname);
        jsonNode.put("bDate", this.bDate);
        jsonNode.put("city", this.cityAddr);
        jsonNode.put("street", this.streetAddr);
        jsonNode.put("house", this.houseNumAddr);
        jsonNode.put("postalCode", this.postalCodeAddr);
        jsonNode.put("role", this.role);
        jsonNode.put("therapistType", this.therapistType);
        jsonNode.put("phoneNum", this.phoneNum);
    }
}
