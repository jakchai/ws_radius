/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.go.nhso.guestws.dao;

/**
 *
 * @author jakchai.t
 */
public class GuestData implements java.io.Serializable{

    private String fullname="";
    private String fname="";
    private String lname="";
    private String username="";
    private String password="";
    private String startdate="";
    private String expiresdate="";
    private String alldata="";
    private String error_msg="";
    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String pid) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getExpiresdate() {
        return expiresdate;
    }

    public void setExpiresdate(String expiresdate) {
        this.expiresdate = expiresdate;
    }
    public String getAlldata() {
        return alldata;
    }

    public void setAlldata(String alldata) {
        this.alldata = alldata;
    } 
    public String getError_msg() {
        return error_msg;
    }
    public void setError_msg(String error_msg) {
        this.error_msg = error_msg;
    }    
}
