package user;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import DB.DBUser;

// Stores a user's information
public class User {
	private int id = 0;
	private String firstName = null;
	private String lastName = null;
	private String userName = null;
	private boolean admin = false;
	
	/* 
	 * Can start out as empty; this will be used when we want to populate an object from information 
	 * stored in the database	 * 
	 */
	public User() {};
	
	// Regular constructor to be used
	public User(
			String _firstName,
			String _lastName,
			String _userName,
			boolean _admin) {
		firstName = _firstName;
		lastName = _lastName;
		userName = _userName;
		admin = _admin;
	}
	
	// Set user information, usually will be called when populating from database
	public void setUser(
			String _firstName,
			String _lastName,
			String _userName,
			boolean _admin) {
		firstName = _firstName;
		lastName = _lastName;
		userName = _userName;
		admin = _admin;
	}
	
	// Login user
	public boolean login(String username, String password) throws SQLException {
		DBUser db = new DBUser();
		Connection conn = db.connect(db.DATABASE);
		
		// Find username in database and save salt
		String query = "SELECT * FROM USERS WHERE USERNAME = '" + username + "'";
		PreparedStatement statement;
		statement = conn.prepareStatement(query);
		ResultSet rs = statement.executeQuery();
		
		// Get the salt associated with the username
		if(!rs.next()) {
			System.out.println("TESTING -- username not found");
			return false;
		}
		byte[] salt = rs.getBytes("salt");
		this.setUser(rs.getString("first_name"), rs.getString("last_name"), rs.getString("username"), rs.getBoolean("admin"));
		Password pw = new Password();
		
		// New query that searches for both username and password
		query = "SELECT * FROM USERS WHERE USERNAME = + '" + username + "' AND PASSWORD = ?";
		statement = conn.prepareStatement(query);
		// Make new hashed password to compare
		byte[] hashed = pw.hash(password.toCharArray(), salt);
		InputStream pass = new ByteArrayInputStream(hashed);
		statement.setBinaryStream(1, pass, hashed.length);
		rs = statement.executeQuery();
		
		if(rs.next()) {
			return true;
		} 
		
		return false;
		
	}

	// Insert a user into the database from an existing object; requires password
	public void insertUserIntoDatabase(String password) {
		if(this.firstName == null || this.lastName == null || this.userName == null) {
			System.out.println("User is missing information.");
			return;
		}
		DBUser db = new DBUser();
		Password pw = new Password();
		byte[] salt = pw.getNextSalt();
		byte[] hashed = pw.hash(password.toCharArray(), salt);
		db.insertUser(firstName, lastName, userName, hashed, salt, admin ? 1 : 0);
		
	}
	
	
	// Testing method for displaying info about a user
	public void display() {
		System.out.println("First name: "+ firstName);
		System.out.println("Last name: " + lastName);
		System.out.println("Username: " + userName);
		System.out.println("Admin: " + admin);
	}
	
	// Getters and setters
	public void setFirstName(String _firstName) {
		this.firstName = _firstName;
	}
	
	public String getFirstName() {
		return this.firstName;
	}
	
	public void setLastName(String _lastName) {
		this.lastName = _lastName;
	}
	
	public String getLastName() {
		return this.lastName;
	}
	
	public void setFullName(String _firstName, String _lastName) {
		this.firstName = _firstName;
		this.lastName = _lastName;
	}
	
	public String getFullName() {
		return this.firstName + this.lastName;
	}
	
	public void setUserName(String _userName) {
		this.userName = _userName;
	}
	
	public String getUserName() {
		return this.userName;
	}
	
	public void setAdminPrivileges(boolean _admin) {
		this.admin = _admin;
	}
	
	public boolean isAdmin() {
		return this.admin;
	}
	
	
	
	

}
