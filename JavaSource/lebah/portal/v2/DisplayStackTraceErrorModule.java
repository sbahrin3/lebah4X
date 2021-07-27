package lebah.portal.v2;

import lebah.portal.action.LebahModule;

public class DisplayStackTraceErrorModule extends LebahModule {
	
	private String path = "/vtl/main";
	

	@Override
	public String start() {
		return path + "/stackTraceError.vm";
	}
	


}
