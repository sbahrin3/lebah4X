package lebah.portal;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import lebah.portal.velocity.VServlet;
import lebah.portal.velocity.VTemplate;

public class DivControllerServlet  extends VServlet  {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)  throws ServletException, IOException    {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  {
		res.setContentType("text/html");
		//res.setCharacterEncoding("UTF-8");
		//res.setHeader("Content-Type", "text/html;charset=UTF-8");
		PrintWriter out = res.getWriter();
		HttpSession session = req.getSession();
		
	
		doService(req, res, out, session);
		
	
	}

	private void doService(HttpServletRequest req, HttpServletResponse res, PrintWriter out, HttpSession session)
			throws ServletException {
		
		context = (org.apache.velocity.VelocityContext) session.getAttribute("VELOCITY_CONTEXT");
		engine = (org.apache.velocity.app.VelocityEngine) session.getAttribute("VELOCITY_ENGINE");
		if ( context == null ) {
			initVelocity(getServletConfig());
			session.setAttribute("VELOCITY_CONTEXT", context);
			session.setAttribute("VELOCITY_ENGINE", engine);
		}


        String app_path = getServletContext().getRealPath("/");
        app_path = app_path.replace('\\', '/');		
		session.setAttribute("_portal_app_path", app_path);
		
		String uri = req.getRequestURI();
		String s1 = uri.substring(1);
		context.put("appname", s1.substring(0, s1.indexOf("/")));		
		session.setAttribute("_portal_appname", s1.substring(0, s1.indexOf("/")));		
		//get pathinfo
        String pathInfo = req.getPathInfo();
        pathInfo = pathInfo.substring(1); //get rid of the first '/'
	    //pathInfo only contains action
        pathInfo = pathInfo.substring(pathInfo.indexOf("/") + 1);
		String module = pathInfo != null ? pathInfo : "";	
		
		String cacheId = session.getId();
		
		//if ( !"".equals(module) ) {
			try {
				Object content = null;
				try {

					String className = module;

					/*
					if (( module.indexOf(".") > 0 )) {
						className = module;
						content = ClassLoadManager.load(className);
						((VTemplate) content).setId(className);
					}
					*/
					
					content = ClassLoadManager.load(className, module, cacheId);
						
					((VTemplate) content).setId(module);
					((VTemplate) content).setEnvironment(engine, context, req, res);	
					((VTemplate) content).setServletContext(getServletConfig().getServletContext());	
					((VTemplate) content).setServletConfig(getServletConfig());
					((VTemplate) content).setDiv(true);
		
					try {
						if ( content != null ) {
							((VTemplate) content).setShowVM(true);
							((VTemplate) content).print(session);
						}
					} catch ( Exception ex ) {
						out.println( ex.getMessage() );
					}						
					
					
				} catch ( ClassNotFoundException cnfex ) {
					content = new ErrorMsg(engine, context, req, res);
					((ErrorMsg) content).setError("ClassNotFoundException : " + cnfex.getMessage());				
				} catch ( InstantiationException iex ) {
					content = new ErrorMsg(engine, context, req, res);
					((ErrorMsg) content).setError("InstantiationException : " + iex.getMessage());			
				} catch ( IllegalAccessException illex ) {
					content = new ErrorMsg(engine, context, req, res);
					((ErrorMsg) content).setError("IllegalAccessException : " + illex.getMessage());			
				} catch ( Exception ex ) {
					content = new ErrorMsg(engine, context, req, res);
					((ErrorMsg) content).setError("Other Exception during class initiation : " + ex.getMessage());	
					ex.printStackTrace();					
				}	
				
			} catch ( Exception ex ) {
				System.out.println( ex.getMessage() );	
			} finally {
				//long totalMem = Runtime.getRuntime().totalMemory();
				//System.out.println("total memory = " + totalMem);	
			}

	}

	

}
