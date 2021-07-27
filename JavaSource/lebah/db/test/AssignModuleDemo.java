package lebah.db.test;

import lebah.db.entity.Module;
import lebah.db.entity.Persistence;
import lebah.db.entity.Role;
import lebah.db.entity.RoleModule;

public class AssignModuleDemo {
	
	public static void main(String[] args) {
		
		Role role = Persistence.db().find(Role.class, "anon");
		System.out.println(role.getName());
		
		Module module1 = Persistence.db().find(Module.class, "lebah.portal.v2.HelloWorldModule");
		Module module2 = Persistence.db().find(Module.class, "lebah.portal.v2.HelloCalculatorModule");
		
		System.out.println(module1.getTitle());
		System.out.println(module2.getTitle());
		
		RoleModule[] roleModules = {
					new RoleModule(role, module1),
					new RoleModule(role, module2)
				};
		
		Persistence.db().save(roleModules);
		Persistence.db().close();
	
	}

}
