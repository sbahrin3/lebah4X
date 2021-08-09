package lebah.db.test;

import org.hibernate.exception.ConstraintViolationException;

import lebah.db.entity.Persistence;
import lebah.db.entity.User;

public class InitDefaultUsers {
	
	public static void main(String[] args) {
		
		Persistence db = Persistence.db();
		
		int n = 0;
		try {
			n = db.execute("delete from User u");
			
			System.out.println(n);
			
			User[] users = 
				{
					new User("admin", "123", "Admin", ""),
					new User("anon", "123", "Anon", ""),
					new User("zulaikha", "123", "Siti Zulaikha", "Nawi"), 
					new User("salihin", "123", "Mohd. Salihin", "Nawi"), 
					new User("ivan", "123", "Ivan", "Vincent"), 
					new User("bond", "123", "James", "Bond"), 
					new User("ali", "123", "Mohd Ali", "Nawi"), 
				};
			
			db.save(users);
			
		} catch (ConstraintViolationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		db.close();
	}

}
