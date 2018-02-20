<%-- 
    Document   : testNU
    Created on : Nov 9, 2017, 10:06:24 AM
    Author     : jakchai.t
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.io.BufferedReader,java.io.InputStreamReader,java.net.InetAddress,th.go.nhso.guestws.dao.GuestData,java.util.Date,java.text.SimpleDateFormat" %>
<%@ page import="java.util.Random,java.time.LocalDate,java.time.format.DateTimeFormatter,java.util.Locale,java.util.Properties,java.io.FileInputStream" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <%
            String pid = "3110400064292";
            //String fullname = "Wisit Tundon";
            String fullname = "วิสิทธิ์ ทันดอน";
            String password = "password";
            String expdate = "9/11/2017";
            String ret=null,hostname="";
            hostname = InetAddress.getLocalHost().getHostName();
            //out.println("hostname : " + hostname);
            String chkadduser="";
            
            Properties prop = new Properties();
            prop.load(new FileInputStream("c://tomcat9//conf//guestws.properties"));
            //out.println(prop.getProperty("gws_date_format"));
            
            String expires_date = "",start_date="";            
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(prop.getProperty("gws_date_format"));
            LocalDate localDate = LocalDate.now();
            start_date = "" + dtf.format(localDate); 
            expires_date = "" + dtf.format(localDate.plusDays(1));   
            out.println("exp :" + expires_date);
            String str_username="",str_acc_exp="",str_pass="";
            String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
            StringBuilder salt = new StringBuilder();
            Random rnd = new Random();
            while (salt.length() < 8) { // length of the random string.
                int index = (int) (rnd.nextFloat() * SALTCHARS.length());
                salt.append(SALTCHARS.charAt(index));
            }
            str_pass = salt.toString();                         
            out.println(str_pass);             
            
            GuestData guestData = new GuestData();
            
            Process p = Runtime.getRuntime().exec ("net user "+pid+" /del");
            //Runtime.getRuntime().exec ("net user 3110400064292 password /add /times:M-SU,8am-6pm /expires:9/11/2017 /fullname:\"Wisit Tundon\"");
            p.waitFor();
            p = Runtime.getRuntime().exec ("net user "+pid+" "+str_pass+" /add /times:M-SU,8am-6pm /expires:"+expires_date+" /fullname:\""+fullname+"\"");
            //Process p = Runtime.getRuntime().exec ("net localgroup WebRegister ITJAKCHAI_T\\3110400064292 /add");
            //out.print("net user "+pid+" "+str_pass+" /add /times:M-SU,8am-6pm /expires:"+expires_date+" /fullname:\""+fullname+"\"");
            p.waitFor();
            p = Runtime.getRuntime().exec ("net localgroup WebRegister "+hostname+"\\"+pid+" /add");
            p.waitFor();
            

            p = Runtime.getRuntime().exec ("net user "+pid+"");
            p.waitFor();

            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
 
                /*
                for(int i = 0; i < 32; i++){
                        ret = input.readLine() ;
                        out.println(ret + "<br>");                
                }
                out.println("================================<br>");
                */
                int int_start=0,int_end=0;                
                
                while ((ret = input.readLine()) != null) {
                     if(ret.indexOf("User name")!= -1){
                         int_start = ("User name".length()+1);
                         int_end = ret.length();
                         str_username = ret.substring(int_start, int_end);
                         out.println(str_username);
                         guestData.setUsername(str_username);
                     }else if(ret.indexOf("Account expires")!= -1){
                         int_start = ("Account expires".length()+1);
                         int_end = ret.length();
                         str_acc_exp = ret.substring(int_start, int_end);
                         //out.println(str_acc_exp);
                        SimpleDateFormat sdf = new SimpleDateFormat(prop.getProperty("gws_date_format"));
                        Date d = sdf.parse(str_acc_exp);
                        sdf.applyPattern(prop.getProperty("gws_date_newformat"));
                        str_acc_exp = sdf.format(d);  
                        
                        out.print("<br>str_acc_exp : " + str_acc_exp + "<br>");
  
                        
                     }
                     out.println(ret+"<br>");
                 }
              
                input.close();
                

             

        %>
    </body>
</html>
