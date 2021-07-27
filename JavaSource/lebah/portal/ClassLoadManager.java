/* ************************************************************************
LEBAH PORTAL FRAMEWORK
Copyright (C) 2007  Shamsul Bahrin

* ************************************************************************ */


package lebah.portal;

import java.util.Enumeration;
import java.util.Hashtable;

public class ClassLoadManager {
	
	static Hashtable<String, Object> loadedList = new Hashtable<String, Object>();

	public static Object load(String className, String module, String cacheId) throws Exception {
		if ( className == null ) return null;
		Object object = null;
		
		
		String id = className + module + cacheId;
		//find if this name exists in the cache
		try {
			object = loadedList.get(id);
		} catch ( Exception e ) {
			System.out.println("CLASSLOADMANAGER CHECK 1...");
			e.printStackTrace();
		}
		//if not load it   
		if ( object == null ) {
			//Class klazz = Class.forName(className);
			Thread t = Thread.currentThread();
			ClassLoader cl = t.getContextClassLoader();
			Class klazz = cl.loadClass(className);			
			object = klazz.newInstance();	
			//put into cache  
			try {
				loadedList.put(id, object);
			} catch ( Exception e ) {
				System.out.println("CLASSLOADMANAGER CHECK 2...");
				e.printStackTrace();
			}
		} else {
			//System.out.println("Loaded lebah module from cache: " + id);
		}

		
		return object; 
	}	
	
	public static Object load(String className) throws Exception {
		if ( className == null ) return null;
		Object object = null;
		//if not load it   
		if ( object == null ) {
			//Class klazz = Class.forName(className);
			Thread t = Thread.currentThread();
			ClassLoader cl = t.getContextClassLoader();
			Class klazz = cl.loadClass(className);			
			object = klazz.newInstance();	
		}
		return object; 
	}	
	
	public static Hashtable<String, Object> getCaches() {
		return loadedList;
	}
	
	public static void clearCache() {
		Hashtable<String, Object> copies = new Hashtable<String, Object>();
		copies.putAll(loadedList);
		loadedList.clear();
		for ( Enumeration e = copies.keys(); e.hasMoreElements(); ) {
			String key = (String) e.nextElement();
			Object obj = copies.get(key);
			obj = null;
		}
	}
	
	public static void clearCache(String cacheId) {
	     for (Enumeration e = loadedList.keys() ; e.hasMoreElements() ;) {
	         String key = (String) e.nextElement();
	         if ( key.lastIndexOf(cacheId) > 0 ) {
	        	 Object obj = loadedList.get(key);
	        	 obj = null;
	        	 loadedList.remove(key);
	         }
	     }
		
	}
	
}
