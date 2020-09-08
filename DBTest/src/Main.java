import user.User;
import user.Password;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import DB.DBManager;
import DB.DBUser;

public class Main {

	public static void main(String[] args) throws SQLException  {
		
		// Here is a quick test to show you a few of the functionalities we have so far
		
		// Let's first create a user
		String firstName = "Joshua";
		String lastName = "LaFever";
		String userName = "jlafever";
		String password = "jlafever";
		boolean hasAdminPrivileges = false;
		
		User josh = new User(firstName, lastName, userName, hasAdminPrivileges);
		System.out.println("Manually created user; not in database yet.");
		// Let's see that our object has been created
		josh.display();
		System.out.println();
		
		// Now let's add me to the database
		josh.insertUserIntoDatabase(password);
		
		// You can check in the database to see that I am there now.
		
		
		// Let's get of our current object to show the next part. 
		josh = new User();
		System.out.println("Should be empty now.");
		josh.display();
		System.out.println();
		
		// Now let's populate it again from the database
		DBUser db = new DBUser();
		josh = db.getUser("jlafever");
		System.out.println("Showing the user populated from the database.");
		josh.display();
		System.out.println();
		
		/* 
		 * Last thing; now let's log in. 
		 * There isn't really any logic to state what login means at this point, as far as what privileges 
		 * that gives a user, but we can check that we have the correct credentials
		 */
		// One more time let's null out this object to demonstrate
		josh = new User();
		System.out.println("Empty again.");
		josh.display();
		System.out.println();
		
		// Incorrect password
		if(josh.login(userName, "password")) {
			System.out.println("Successful login.");
		}
		else {
			System.out.println("Unsuccessful login.");
		}
		
		// Try again with correct password 
		if(josh.login(userName, password)) {
			System.out.println("Successful login.");
			josh.display();
		}
		else {
			System.out.println("Unsuccessful login.");
		}
		
		// NOTE - When I logged in, using an empty object, it automatically populated the current object
	}

}
