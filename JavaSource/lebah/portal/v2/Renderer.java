package lebah.portal.v2;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.ClassLoadManager;
import lebah.portal.velocity.VServlet;
import lebah.portal.velocity.VTemplate;

public class Renderer extends VServlet {
	
	private String stackTraceError = "";
	private boolean hasStackTraceError = false;
	
	public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		stackTraceError = "";
		hasStackTraceError = false;
		doPost(req, res);
	}

	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		HttpSession session = req.getSession();
		PrintWriter out = res.getWriter();
		context = (org.apache.velocity.VelocityContext) session.getAttribute("VELOCITY_CONTEXT");
		engine = (org.apache.velocity.app.VelocityEngine) session.getAttribute("VELOCITY_ENGINE");
		if ( context == null ) {
			initVelocity(getServletConfig());
			session.setAttribute("VELOCITY_CONTEXT", context);
			session.setAttribute("VELOCITY_ENGINE", engine);
		}
		
  
		context.remove("stackTraceError");
		context.remove("hasStackTraceError");
		
		String module = req.getPathInfo();
		if ( module != null ) module = module.substring(1);
		
		context.put("module_name", module);
		String cacheId = session.getId();
		String className = module;
		try {

    		Object content = ClassLoadManager.load(className, module, cacheId);
    		((VTemplate) content).setId(module);
			((VTemplate) content).setEnvironment(engine, context, req, res);	
			((VTemplate) content).setServletContext(getServletConfig().getServletContext());	
			((VTemplate) content).setServletConfig(getServletConfig());
			((VTemplate) content).setDiv(true);
			((VTemplate) content).print(session);
		} catch ( Exception e ) {
			System.out.println("Renderer got error: ***");
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			stackTraceError = sw.toString();
			hasStackTraceError = true;
			
			context.put("stackTraceError", stackTraceError);
			context.put("hasStackTraceError", hasStackTraceError);

			try {
				Object content = ClassLoadManager.load("lebah.portal.v2.DisplayStackTraceErrorModule", "lebah.portal.v2.DisplayStackTraceErrorModule", "stackTraceError");
				((VTemplate) content).setId(module);
				((VTemplate) content).setEnvironment(engine, context, req, res);	
				((VTemplate) content).setServletContext(getServletConfig().getServletContext());	
				((VTemplate) content).setServletConfig(getServletConfig());
				((VTemplate) content).setDiv(true);
				((VTemplate) content).print(session);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		
	}


}
