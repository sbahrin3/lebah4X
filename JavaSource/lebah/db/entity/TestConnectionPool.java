package lebah.db.entity;

import java.util.List;

public class TestConnectionPool {
	
	
	
	public static void main(String[] args) throws Exception {
		
		int n = 10000; //number of threads
		for ( int i=0; i < n; i++ ) {
			new Thread(new Tester("" + i)).start();
		}
	}
	
	
	static class Tester implements Runnable {
		
		Persistence db = Persistence.db();
		String threadId;
		
		public Tester(String id) {
			threadId = id;
		}

		@Override
		public void run() {	
			listUsers();
			/*
			logMessage(id);
			*/
		}
		
		void listUsers() {
			try {
				
				List<User> users = db.list("select u from User u");
				for ( User u : users) {
					System.out.println("Thread no: " + threadId + ", Data: " + u.getUserName());
					Thread.sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		void insertData(String id) {
			for ( int i=0; i < 10; i++ ) {
				log(i + ") Message " + id);
			}
		}
		
		void log(String message) {
			
			Log log = new Log();
			System.out.println(message);
			log.setId(lebah.util.UIDGenerator.getUID() + "-" + threadId);
			log.setMessage(message);
			db.save(log);
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	

	
	
}
