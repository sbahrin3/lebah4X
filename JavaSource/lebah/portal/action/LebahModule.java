package lebah.portal.action;

import java.lang.reflect.Method;

import lebah.db.entity.Persistence;

/**
 * 
 * @author Shamsul Bahrin bin Abd Mutalib
 *
 */

public abstract class LebahModule extends AjaxModule {
	
		
	protected String command = "";
	
	protected Persistence db = null;
	
	public void initDb() {
		try {
			db = Persistence.db(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String doAction() throws Exception {
		
		initDb();
		
		String vm = "";
		command = request.getParameter("command");
		context.put("command", command);
		
		if ( "__loadModule__".equals(command)) command = "";
		preProcess();
		
		//*** Application Current Token
		String token = (String) context.get("request_uid");
		context.put("current_token", token != null ? token : "");
		//---
		
		context.put("util", lebah.util.Util.getInstance());
		if ( command == null || "".equals(command)) vm = start();
		else {
			vm = doCommand();
			if ( "".equals(vm)) {
				boolean found = false;
				Method[] methods = getClass().getDeclaredMethods();
				for ( Method m : methods ) {
					if ( m.isAnnotationPresent(Command.class)) {
						Command c = m.getAnnotation(Command.class);
						if ( command.equals(c.value())) {
							vm = (String) m.invoke(this, null);
							found = true;
							break;
						}
					}
				}
				
				Class cls = null;
				if ( !found ) {
					cls = getClass().getSuperclass();
					
					if ( cls != null ) {
						vm = executeCommand(cls);
						found = !"".equals(vm);
					}
				}
				
				while ( !found ) {
					if ( cls != null ) {
						cls = cls.getSuperclass();

						if ( cls != null ) {
							
							if ( 
									"lebah.portal.action.AjaxModule".equals(cls.getName())
							) {
								found = true;
								break;
							}
							
							vm = executeCommand(cls);
							found = !"".equals(vm);
							if ( found ) break;
						}
						else{
							found = false;
						}
						
					}
					else 
						found = false;
				}
			
			}
			
		}
		postProcess();
		return vm;
	}
	
	private String executeCommand(Class clss) throws Exception {
		String view = "";
		Method[] methods2 = clss.getDeclaredMethods();
		for ( Method m : methods2 ) {
			if ( m.isAnnotationPresent(Command.class)) {
				Command c = m.getAnnotation(Command.class);
				if ( command.equals(c.value())) {
					view = (String) m.invoke(this, null);
					break;
				}
			}
		}
		return view;
	}
	
	public abstract String start();
	public void preProcess() throws Exception {};
	public String doCommand() throws Exception { return ""; };
	public void postProcess() throws Exception {};
	
	
	public void var(String varName, Object varValue) {
		if ( varValue != null ) context.put(varName, varValue);
		else context.remove(varName);
	}
	


}
