/* ************************************************************************
LEBAH PORTAL FRAMEWORK, http://lebah.sf.net
Copyright (C) 2007  Shamsul Bahrin
* ************************************************************************ */

package lebah.portal.velocity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeConstants;

/**
 * @author Shamsul Bahrin Abd Mutalib
 * @version 1.01
 */
public abstract class VServlet extends HttpServlet {
	protected VelocityEngine engine;
	protected VelocityContext context;
	
    public void init( ServletConfig config ) throws ServletException {
        super.init(config);
        
    }
    
    /*
     * As of 2009-08-13, this method will be called from the DesktopController
     * to initialize the Velocity Engine and Velocity Context.
     */
    public void initVelocity(ServletConfig config) throws ServletException {
        try { 
        	engine = VelocityEngineHolder.getInstance(config, getServletContext()).getVelocityEngine();
        	context = new VelocityContext();
        	context.put("request_uid", lebah.util.UIDGenerator.getUID());
        } catch ( Exception e ) {
        	System.out.println(">> ERROR IN VELOCITYSERVLET INITVELOCITY");
	        e.printStackTrace();
        }
    }

    
}
