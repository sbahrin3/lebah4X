package lebah.portal.v2;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import lebah.portal.ClassLoadManager;
import lebah.portal.action.Command;
import lebah.portal.action.LebahModule;

public class CacheManagerModule extends LebahModule {
	
	String path = "vtl/modules/cacheManager";

	@Override
	public String start() {
		try {
			list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path + "/start.vm";
	}

	
	@Command("list")
	public String list() throws Exception {
		
		List<Map<String, Object>> cacheList = new ArrayList<Map<String, Object>>();
		context.put("cacheList", cacheList);
		Hashtable<String, Object> caches = ClassLoadManager.getCaches();
		for ( Enumeration e = caches.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object obj = caches.get(key);
			LebahModule module = (LebahModule) obj;
			
			Map<String, Object> cache = new HashMap<String, Object>();
			cache.put("key", key);
			cache.put("sessionId", key.substring(key.length() - 32));
			cache.put("module", module);
			
			cacheList.add(cache);
			
		}
		
		return path + "/list.vm";
	}
	
	
}
