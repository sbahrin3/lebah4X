/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin


* ************************************************************************ */
package lebah.servlets;

import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import lebah.db.entity.Persistence;
import lebah.db.entity.User;
import lebah.util.PasswordService;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public class AuthenticateUser {
	private String role;
	private String userId;
	private String userLogin;
	private String userName;
	private boolean allowed;
	HttpServletRequest req;	 
	
	public AuthenticateUser() {
		
	}
	
	public AuthenticateUser(HttpServletRequest req) {
		this.req = req;
	}	
	
		
	public boolean lookup(String username, String password) throws  Exception {
		System.out.println("Lookup user: " + username);
		password = PasswordService.encrypt(password);
		
		Hashtable<String, Object> h = new Hashtable<String, Object>();
		h.put("username", username);
		h.put("password", password);
		String hql = "select u from User u where u.userName = :username and u.userPassword = :password";
		List<User> users = Persistence.db(req).list(hql, h);
		User user = null;
		if ( users.size() > 0 ) {
			user = users.get(0);
			this.allowed = true;
			this.userId = user.getId();
			this.userLogin = user.getUserName();
			this.userName = user.getFirstName() + " " + user.getLastName();
		}
		else {
			this.allowed = false;
			this.userId = null;
			this.userLogin = null;
			this.userName = null;
		}
			
		return allowed;
	}
	
	public String getRole() {
		return role;
	}
	
	public String getUserName() {
		return userName;
	}
	
	public String getUserLogin() {
		return userLogin;
	}	
	
	public boolean getAllowed() {
		return allowed;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
