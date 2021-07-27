package lebah.portal.v2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;

import lebah.db.entity.User;

public class AppSession {
	
	HttpServletRequest req;
	HttpServletResponse res;
	VelocityContext context;
	User user;
	String id;
	
	public AppSession(HttpServletRequest req, HttpServletResponse res, VelocityContext context, User user) {
		this.req = req;
		this.res = res;
		this.context = context;
		this.user = user;
		this.id = (String) context.get("request_uid"); 
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public void setUser(User user) {
		this.user = user;
	}
	
	public User getUser() {
		return user;
	}

}
