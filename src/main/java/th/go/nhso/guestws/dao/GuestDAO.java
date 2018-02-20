/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.go.nhso.guestws.dao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 *
 * @author jakchai.t
 */
public class GuestDAO {   
    
    
    final static Logger logger = Logger.getLogger(GuestDAO.class);
    
    public GuestData findGuestByPID(String username, String fname, String lname, String expires_date, String visitor_key,String callerIpAddress) throws Exception {
        
        //PropertyConfigurator.configure("D:\\logs\\log4j.properties");
        GuestData guestData = new GuestData();
        Properties prop = new Properties();
        prop.load(new FileInputStream("c://tomcat9//conf//guestws.properties"));
        if (visitor_key == null) {
            visitor_key = "";
        }
        
        if (visitor_key.equals("" + prop.getProperty("visitor_key"))) {
            String str_ip_allow = prop.getProperty("gws_ip_allow");
            
            //logger.info("This is info : " + prop.getProperty("gws_ip_allow"));
            String[] arr = str_ip_allow.split(",");
            ArrayList<String> arrList = new ArrayList<String>();

            for(String a:arr){
                arrList.add(a); 
            } 
            /*
            for (int i = 0; i < arrList.size(); i++) {
                logger.info("This is gws_ip_allow : " + arrList.get(i));
            }
            */            
            if (arrList.contains(callerIpAddress)){
                logger.info("This is info : " + callerIpAddress);
            
                /*
		if(logger.isDebugEnabled()){
			logger.debug("This is debug : " + callerIpAddress);
		}

		if(logger.isInfoEnabled()){
			logger.info("This is info : " + callerIpAddress);
		}

		logger.warn("This is warn : " + callerIpAddress);
		logger.error("This is error : " + callerIpAddress);
		logger.fatal("This is fatal : " + callerIpAddress);            
                     
                */


            //String pid="",fname="",lname="";
            //pid = guest.getPid();
            if (username == null) {
                username = "";
            }
            //fname = guest.getFname();
            if (fname == null) {
                fname = "";
            }
            //lname = guest.getLname();
            if (lname == null) {
                lname = "";
            }

            //if(expires_date==null) expires_date="";
            String fullname = fname + " " + lname;
            String ret = null, hostname = "", str_pass = "";
            hostname = InetAddress.getLocalHost().getHostName();
            //if(ret==null) ret="";
            String chkadduser = "";
            String start_date = "";
            //DateTimeFormatter dtf = DateTimeFormatter.ofPattern(prop.getProperty("gws_date_format"));
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            if(expires_date == null) expires_date = "";
            //LocalDate localDate = LocalDate.now();
            Date localDate = new Date();
            //start_date = "" + dtf.format(localDate);  
            start_date = sdf.format(localDate);
            if (expires_date.equals("")) {                
                //expires_date = "" + dtf.format(localDate.plusDays(1));
                sdf.applyPattern(prop.getProperty("gws_date_format"));
                expires_date = "" + sdf.format(localDate);
            }else{    
                //logger.debug("debug expires_date : " + expires_date);
                expires_date = "" + expires_date.trim();                
                Date d_exp = sdf.parse(expires_date);
                sdf.applyPattern(prop.getProperty("gws_date_format"));
                //Date d_exp = sdf.parse("21/11/2017");
                expires_date = ""+sdf.format(d_exp);          
                
            }
            logger.debug("debug expires_date : " + expires_date);
            

            String SALTCHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 8) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            str_pass = salt.toString();
            int int_start = 0, int_end = 0;
            String str_username = "", str_acc_exp = "",str_all_data="";
            try {
                //Process p = Runtime.getRuntime().exec("net user " + username + " /del");
                //Runtime.getRuntime().exec ("net user 3110400064292 password /add /times:M-SU,8am-6pm /expires:9/11/2017 /fullname:\"Wisit Tundon\"");
                Process p = Runtime.getRuntime().exec("net user " + username + "");
                p.waitFor();
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                ret = input.readLine(); 
                if(ret == null) ret="";
                if (ret.indexOf("User name") != -1) {
                    int_start = ("User name".length() + 1);
                    int_end = ret.length();
                    str_username = ret.substring(int_start, int_end);
                    str_username = str_username.trim();
                    logger.debug("debug str_username : " + str_username +" and username "+ username);
                }    
                if(!str_username.equals(username)){
                  p = Runtime.getRuntime().exec("net user " + username + " " + str_pass + " /add /times:M-SU,8am-6pm /expires:" + expires_date + " /fullname:\"" + fullname + "\"");                 
                  p.waitFor();
                  p = Runtime.getRuntime().exec("net localgroup WebRegister " + hostname + "\\" + username + " /add");
                  p.waitFor();   

                  logger.info("net user " + username + " " + str_pass + " /add /times:M-SU,8am-6pm /expires:" + expires_date + " /fullname:\"" + fullname + "\"");
                  logger.debug("debug user add : " + username);                  
                }else{
                  p = Runtime.getRuntime().exec("net user " + username + " " + str_pass + " /times:M-SU,8am-6pm /expires:" + expires_date + " /fullname:\"" + fullname + "\"");                 
                  p.waitFor();   
                  
                  logger.info("net user " + username + " " + str_pass + " /times:M-SU,8am-6pm /expires:" + expires_date + " /fullname:\"" + fullname + "\"");
                  logger.debug("debug user update : " + username);
                }
                    
                
                p = Runtime.getRuntime().exec("net user " + username + "");
                p.waitFor();

                input = new BufferedReader(new InputStreamReader(p.getInputStream()));


                //SimpleDateFormat sdf = new SimpleDateFormat(prop.getProperty("gws_date_format"));

                while ((ret = input.readLine()) != null) {
                    if (ret.indexOf("User name") != -1) {
                        int_start = ("User name".length() + 1);
                        int_end = ret.length();
                        str_username = ret.substring(int_start, int_end);

                        guestData.setUsername(str_username);
                    } else if (ret.indexOf("Account expires") != -1) {
                        int_start = ("Account expires".length() + 1);
                        int_end = ret.length();
                        str_acc_exp = ret.substring(int_start, int_end);
                        str_acc_exp = str_acc_exp.trim();
                        //out.println(str_acc_exp);
                        
                        Date d = sdf.parse(str_acc_exp);
                        sdf.applyPattern(prop.getProperty("gws_date_newformat"));
                        str_acc_exp = sdf.format(d);
                        

                        guestData.setExpiresdate(str_acc_exp);

                        //out.println(str_pass);               
                    }
                    str_all_data = str_all_data + ret + "\n";
                    //out.println(ret+"<br>");
                    
                }
                guestData.setFname(fname);
                guestData.setLname(lname);
                guestData.setFullname(fullname);
                guestData.setPassword(str_pass);

                //SimpleDateFormat sdfs = new SimpleDateFormat(prop.getProperty("gws_date_format"));
                Date ds = sdf.parse(start_date);
                sdf.applyPattern(prop.getProperty("gws_date_newformat"));
                start_date = sdf.format(ds);

                guestData.setStartdate(start_date);
                guestData.setAlldata(str_all_data);

                input.close();

            } catch (Exception e) {
                e.printStackTrace();                
                logger.error("This is error : " + e);
                guestData.setUsername("");
                guestData.setFname("");
                guestData.setLname("");
                guestData.setFullname("");
                guestData.setPassword("");
                guestData.setStartdate("");
                guestData.setExpiresdate("");
                guestData.setAlldata("");                  
                guestData.setError_msg("" + e);
            } finally {
                
                try {

                } catch (Exception e) {
                }
            }
           }else{
                guestData.setUsername("");
                guestData.setFname("");
                guestData.setLname("");
                guestData.setFullname("");
                guestData.setPassword("");
                guestData.setStartdate("");
                guestData.setExpiresdate("");
                guestData.setAlldata("");                
                guestData.setError_msg("This ip is not allowed.");
            }
        } else {
            guestData.setUsername("");
            guestData.setFname("");
            guestData.setLname("");
            guestData.setFullname("");
            guestData.setPassword("");
            guestData.setStartdate("");
            guestData.setExpiresdate("");
            guestData.setAlldata("");
            guestData.setError_msg("This visitor_key is not allowed.");
            
        }
        

        return guestData;
    }
}
