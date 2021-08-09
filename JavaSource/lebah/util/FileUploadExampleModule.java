package lebah.util;

import lebah.module.AppProperties;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class FileUploadExampleModule extends LebahModule {
	
	String path = "/fileUploadExample";
	
	public void preProcess() {
		System.out.println("command=" + command);
	}

	@Override
	public String start() {
		return path + "/start.vm";
	}
	
	@Command("uploadFile")
	public String uploadFile() throws Exception {
		System.out.println("uploadFile");
		String elementName = getParam("elementName");
		String uploadDir = AppProperties.uploadDir();
		String savedFileName = FileUploadUtil.upload(request, elementName, uploadDir);
		context.put("savedFileName", savedFileName);
		return path + "/uploaded.vm";
	}
	

}
