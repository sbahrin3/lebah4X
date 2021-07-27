package lebah.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.servlets.IServlet;

public class LoginServlet implements IServlet {
	
	private boolean isLoginSuccess;
	//private List<Message> messages = new ArrayList<Message>();
	private boolean isAccessDenied;
	

    public void doService(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	response.setContentType("text/javascript");
    	HttpSession session = request.getSession();
    	PrintWriter out = response.getWriter();
    	
    	String username = request.getParameter("username");
    	String password = request.getParameter("password");
    	    	
    	try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	
    	JSON json = new JSON();
		try {
			AuthenticateUser auth = new AuthenticateUser(request);

			if ( auth.lookup(username, password) ) {

				session.setAttribute("_portal_userid", auth.getUserId());
				session.setAttribute("_portal_role", auth.getRole());
				session.setAttribute("_portal_username", auth.getUserName());
				session.setAttribute("_portal_login", auth.getUserLogin());
				session.setAttribute("_portal_islogin", "true");

				isLoginSuccess = true;
				
				json.add("result", "true");

			} else {
				session.setAttribute("_portal_userid", "");
				session.setAttribute("_portal_role", "anon");
				session.setAttribute("_portal_username", "Anonymous");
				session.setAttribute("_portal_login", "");
				session.setAttribute("_portal_islogin", "false");
				isAccessDenied = true;
				json.add("result", "false");
			}

		} catch ( Exception e ) {
			e.printStackTrace();
		}
    	
    	out.println(json.getJSONElements());
    	 
    }

}
