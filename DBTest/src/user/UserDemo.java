package user;

import java.util.List;

import DB.DBUser;

public class UserDemo {
	
	public static void main(String[] args) {
		// Let's first look at our database
		displayUsers();
		
		// Now let's add some users
		
		// Method #1: Create a user object
		User joshL = new User("Joshua", "LaFever", "jlafever", false);
		
		// Method #2: Create an empty user object, and then set it later
		User joshO = new User();
		joshO.setUser("Joshua", "Ortner", "jortner", false);
		
		// Method #3: Create an object out of a database record
		User admin = DBUser.getUser("admin");
		
		// Admin obviously exists so let's add the Josh's to the database
		
		// Method #1: Use DBUser's static method
		DBUser.insertUser(joshL, "password");
		
		// Method #2: Use User's object method
		joshO.insertIntoDatabase("password2");
		
		// Let's look at the database again
		displayUsers();
		
		// Login to joshL
		joshL.login("password");
		
		// Unsucessfully login to joshO
		joshO.login("password");
		
		// Get an admin to change joshO's password
		/*
		 *  I should make this so that a user can change their password if they know the old one, but I just
		 *  made it where the admin has to change it for now.
		 */
		admin.login("admin");
		DBUser.changePassword(admin, "jortner", "password3");
		
		// Login with new password
		joshO.login("password3");
		
		
	}
	
	public static void displayUsers() {
		// Display all users
				List<User> users = DBUser.getAllUsers();
				System.out.println("DISPLAYING USERS IN DATABASE");
				for(int i = 0; i < users.size(); i++) {
					System.out.println("User #" + i);
					users.get(i).display();
					System.out.println();
				}
	}

}
