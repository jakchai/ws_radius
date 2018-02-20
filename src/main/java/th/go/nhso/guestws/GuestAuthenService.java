/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package th.go.nhso.guestws;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import th.go.nhso.guestws.dao.GuestDAO;
import th.go.nhso.guestws.dao.GuestData;

/**
 *
 * @author jakchai.t
 */
@WebService(serviceName = "GuestAuthenService")
@Stateless()
public class GuestAuthenService {

    @Resource
    WebServiceContext webServiceContext;  
    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "guestAuthen")
    /*
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }
    */
    public GuestData findGuestByPID(@WebParam(name = "username") 
    String username, @WebParam(name = "fname") 
    String fname, @WebParam(name = "lname") 
    String lname, @WebParam(name = "expires_date") 
    String expires_date, @WebParam(name = "visitor_key") 
    String visitor_key){
        
      MessageContext messageContext = webServiceContext.getMessageContext();
      HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST); 
      String callerIpAddress = request.getRemoteAddr();

      //System.out.println("Caller IP = " + callerIpAddress);       
        GuestData guestData=null;
        GuestDAO dao = new GuestDAO();
        try{
            guestData = dao.findGuestByPID(username,fname,lname,expires_date,visitor_key,callerIpAddress);
        }catch(Exception e){
            e.printStackTrace();
        }             
        return guestData;
    }
}
