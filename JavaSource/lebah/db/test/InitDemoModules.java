package lebah.db.test;

import lebah.db.entity.Module;
import lebah.db.entity.Persistence;

public class InitDemoModules {
	
	public static void main(String[] args) throws Exception {
		
		Persistence db = Persistence.db();
		
		Module[] modules = 
			{ 
				new Module("lebah.portal.v2.HelloWorldModule", "Hello World", "demo"),
				new Module("lebah.portal.v2.HelloCalculatorModule", "Calculator Demo", "demo")
			};
		
		db.save(modules);
		db.close();
	}

}
