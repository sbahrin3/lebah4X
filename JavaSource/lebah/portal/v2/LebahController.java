package lebah.portal.v2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.velocity.VelocityContext;

import lebah.db.entity.DbProperties;
import lebah.db.entity.Menu;
import lebah.db.entity.Persistence;
import lebah.db.entity.User;
import lebah.portal.velocity.VServlet;
import lebah.util.Util;

/**
 * 
 * @author Shamsul Bahrin
 *
 */

public class LebahController extends VServlet {

	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		PrintWriter out = res.getWriter();
		res.setContentType("text/html");
		HttpSession session = req.getSession();
		//String className = this.getClass().getName();
		context = (org.apache.velocity.VelocityContext) session.getAttribute("VELOCITY_CONTEXT");
		engine = (org.apache.velocity.app.VelocityEngine) session.getAttribute("VELOCITY_ENGINE");
		if (context == null) {
			initVelocity(getServletConfig());
			session.setAttribute("VELOCITY_CONTEXT", context);
			session.setAttribute("VELOCITY_ENGINE", engine);
			
			String token = (String) context.get("request_uid");
			context.put("main_token", token != null ? token : "");
			session.setAttribute("MAIN_TOKEN", token != null ? token : "");
		}
	
		// usefull utilities
		context.put("util", new Util());

		// *** BEGIN
		String serverName = req.getServerName();
		int serverPort = req.getServerPort();
		session.setAttribute("_portal_server", serverPort != 80 ? serverName + ":" + serverPort : serverName);
		context.put("server", serverPort != 80 ? serverName + ":" + serverPort : serverName);
		String uri = req.getRequestURI();
		String s1 = uri.substring(1);
		context.put("appname", s1.substring(0, s1.indexOf("/")));
		session.setAttribute("_portal_appname", s1.substring(0, s1.indexOf("/")));
		
        String server = serverPort != 80 ? serverName + ":" + serverPort : serverName;
        String http = req.getRequestURL().toString().substring(0, req.getRequestURL().toString().indexOf("://") + 3);
        String serverUrl = http + server;
        context.put("serverUrl", serverUrl);
        String appname = uri.substring(1);
        appname = appname.substring(0, appname.indexOf("/"));
        context.put("appUrl", serverUrl.concat("/").concat(appname)); 
        
		
		String queryString = req.getQueryString();
		context.put("queryString", queryString);
		String pathInfo = req.getPathInfo();
		if ( pathInfo != null )
			pathInfo = pathInfo.substring(1);
		
        String app_path = getServletContext().getRealPath("/"); //getServletContext().getRealPath("/");
        app_path = app_path.replace('\\', '/');     
        session.setAttribute("_portal_app_path", app_path);
                
        boolean portalIsLogin =  context.get("_logged_in") != null ? (Boolean) context.get("_logged_in") : false;
        
        String refKey = session.getAttribute("_ref_key") != null ? (String) session.getAttribute("_ref_key") : "";
        String prevRefKey = refKey;
        if ( pathInfo.length() > 1 && !portalIsLogin && !refKey.equals(pathInfo) ) {
        	refKey = pathInfo;
        	session.setAttribute("_ref_key", refKey);
        	context.put("_ref_key", refKey);
        	
        }       
        
        //check if refKey return exception
        try {
        	DbProperties.valueOf("url." + refKey);
        } catch ( Exception e ) {
        	System.out.println("========================    WARNING     ================================ ");
        	System.out.println("PROBABLY WRONG USE OF \"/c/refKey\" SOMEWHERE... PLEASE CHECK ALL href://");
        	System.out.println("=========================================================================");
        	e.printStackTrace();
        	
        	if ( portalIsLogin ) { //already login, do not change refKey!! should be a mistake in href somewhere
        		refKey = prevRefKey;
        	}
        	else {
	        	if ( !"".equals(prevRefKey)) {
	        		refKey = prevRefKey;
	        	} else {
	        		refKey = "default";
	        	}
        	}
        	session.setAttribute("_ref_key", refKey);
        	context.put("_ref_key", refKey);
        }
        
        System.out.println("Using refKey : " + refKey);
                
        try {
	        if ( !"".equals(refKey) ) {
	        	context.put("header", DbProperties.valueOf("title." + refKey));
	        } else {
	        	context.put("header", DbProperties.valueOf("title.default"));
	        }
        } catch ( Exception e ) {
        	context.put("header", "HEADER NOT FOUND");
			//e.printStackTrace();
			
        }
        
               
        String roleId = "anon";
        String _portal_userid = (String) session.getAttribute("_portal_userid");
        System.out.println("portal userid: " + _portal_userid);
        context.put("pageStyle", "default.css");
        if ( _portal_userid == null || "".equals(_portal_userid) ) {
            session.setAttribute("_portal_login", "none");
            context.put("_logged_in", false);
            context.remove("user");
            
        } else {
        	context.put("_logged_in", true);
        	//get current user
        	User user;
			try {
				user = Persistence.db(req).find(User.class, _portal_userid);
				if ( user != null ) {
	        		context.put("user", user);
	        		roleId = user.getRole() != null ? user.getRole().getId() : "anon";
	        		
	        		String pageStyle = user.getPageStyle();
	        		if ( pageStyle != null && !"".equals(pageStyle)) {
	        			context.put("pageStyle", pageStyle);
	        		}
	        	}
				
				String ddir = "../";
		        context.put("relativeDir", ddir);
		        
		        res.setHeader("Expires", "Tue, 25 Dec 1993 23:59:59 GMT"); //this date needs to be in the past.
				res.setHeader("Pragma", "no-cache");
				res.setHeader("Cache-control", "no-cache");
				res.setHeader("Last-Modified", "FRI, JUN 26 2020 23:59:59 GMT"); // this date needs to be in the future
				
				
				List<Menu> menus = Persistence.db(req).list("select m from Menu m where m.parent is null order by m.orderNo");
				context.put("userMenus", menus);
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
        	
        }
		
   		BodyWrapper view = new BodyWrapper(engine, context, req, res);
		try {
			view.print();
		} catch ( Exception ex ) {
			ex.printStackTrace();
		}
		
		
	}
	
	
	private AppSession getAppSession(HttpServletRequest req, HttpServletResponse res, VelocityContext context, User user) {
		AppSession appSession = (AppSession) context.get("AppSession");
		if ( appSession == null ) {
			System.out.println("**** AppSession is NULL, so create new!!!");
			appSession = new AppSession(req, res, context, null);
			context.put("AppSession", appSession);
		}
		return appSession;
	}

	

}
