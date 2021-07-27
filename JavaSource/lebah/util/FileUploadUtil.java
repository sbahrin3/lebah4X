package lebah.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadUtil {
	
	public static String upload(HttpServletRequest request, String elementName, String uploadDir) throws Exception {
		String savedFileName = "";
		File dir = new File(uploadDir);
		if ( !dir.exists() ) dir.mkdir();
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload upload = new ServletFileUpload(factory);
		List items = upload.parseRequest(request);
		Iterator itr = items.iterator();
		List<FileItem> files = new ArrayList<FileItem>();
		while (itr.hasNext()) {
			FileItem item = (FileItem)itr.next();
			if ((!(item.isFormField())) && (item.getName() != null) && (!("".equals(item.getName())))) {
				if ( elementName.equals(item.getFieldName())) {
					files.add(item);
				}
			}
		}
		for ( FileItem item : files ) {
			String fileName = item.getName();
			if ( fileName != null && !"".equals(fileName)) {
				String ext = fileName.lastIndexOf(".") > 0 ? fileName.substring(fileName.lastIndexOf(".")) : "";
				String uid = lebah.util.UIDGenerator.getUUID();
				savedFileName = uid + ext;
				String savedName = uploadDir + "/" + savedFileName;
				
				item.write(new File(savedName));
			}
		}
		return savedFileName;
	}
	
	
	
}
