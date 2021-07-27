package lebah.db.test;

import org.hibernate.exception.ConstraintViolationException;

import lebah.db.entity.Persistence;
import lebah.db.entity.Role;

public class InitDefaultRoles {
	
public static void main(String[] args) {
	
		Persistence db = Persistence.db();
		
		int n = 0;
		try {
			n = db.execute("delete from Role r");
			
			System.out.println("rows affected = " + n);
			
			Role[] roles = 
				{
					new Role("admin", "Admin"),
					new Role("anon", "Anon"),
					new Role("user", "User")
				};
			
			db.save(roles);
			
		} catch (ConstraintViolationException e) {
			System.out.println("SQL Integrity Constraint Violation: ");
			e.printStackTrace();
		}
	
		
		db.close();
	}

}
