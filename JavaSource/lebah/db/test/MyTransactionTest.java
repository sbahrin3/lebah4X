package lebah.db.test;

import java.util.List;

import lebah.db.entity.Transaction;
import lebah.db.entity.User;

public class MyTransactionTest {
	
	public static void main(String[] args) {
		
		Transaction tx = new Transaction();
		tx.begin();
		try {
			User u1 = tx.db().find(User.class, "user1");
			u1.setFirstName("User No 1 After Update!");
			tx.update(u1);
			
			User u2 = tx.db().find(User.class, "user2");
			u2.setFirstName("User No 2 After Update!");
			tx.update(u2);
			
			User u3 = tx.db().find(User.class, "user1");
			u3.setId("user2");  //sini akan hasilkan error
			tx.update(u3);
			
			tx.commit();
			
		} catch ( Exception e ) {
			System.out.println("Error: doing Rollback! " + e.getMessage());
			tx.rollback();
		}
		
		List<User> users = tx.db().list("select u from User u");
		users.forEach(u -> {
			System.out.println(u.getId() + ", " + u.getFirstName());
		});
		
	}

}
