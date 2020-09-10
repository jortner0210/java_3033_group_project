package DB;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import user.Password;
import user.User;

// Child class of DBManager. Used only for access the users table in the database
public class DBUser extends DBManager {
	
	public static final String DATABASE = "db_users_2";
	public static final String TABLE = "users";
	
	/*
	// Find user in db if username exists
	public User getUser(String username) {
		
		String sql = "SELECT * FROM " + TABLE + " WHERE username = '" + username + "'";
		
		try (Connection conn = super.connect(DATABASE);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {
				if(rs != null) {
					String firstName = rs.getString("first_name");
					String lastName = rs.getString("last_name");
					String userName = rs.getString("username");
					boolean admin = rs.getBoolean("admin");
					User user = new User(firstName, lastName, userName, admin);
					return user;
				}
			
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return null;
	}*/
	
	public static boolean userExists(String username) {
		
		String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
		boolean userFound = false;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// Connect to database
			conn = connect(DATABASE);
			ps = conn.prepareStatement(sql);
			
			// Set parameter for query
			ps.setString(1, username);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println(e.getMessage() + "inside userExists");
		} finally {
			// Check if query came back with a user; then close connections
			try {
				if(rs.next()) {
					userFound = true;
					try { rs.close();}  catch (Exception e) { /* Ignored */ }
				}
			} catch (SQLException e) { /* Ignored */ }
			try { ps.close(); } catch (Exception e) { /* Ignored */ }
			try { conn.close(); } catch (Exception e ) { /*Ignored */ }
		}
		
		System.out.println("Performed search for " + username + ". User exists: " + userFound + "\n");
		return userFound;
	}
	
	public static User getUser(String username) {
		String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
		User user = null;
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			// Connect to database
			conn = DBManager.connect(DATABASE);
			ps = conn.prepareStatement(sql);
			
			// Set parameters for query
			ps.setString(1, username);
			rs = ps.executeQuery();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			// Check if query came back with a user; then close connections
			
			try {
				if(rs.next()) {
					try {
						// Populate user object
						user = new User(
								rs.getString("first_name"),
								rs.getString("last_name"),
								rs.getString("username"),
								rs.getBoolean("admin")
								);
					} catch (SQLException e) { System.out.println(e.getMessage()); }
							
					try { rs.close();}  catch (Exception e) { /* Ignored */ }
				}
			} catch (SQLException e) { System.out.println(e.getMessage()); }
			
			try { ps.close(); } catch (Exception e) { /* Ignored */ }
			try { conn.close(); } catch (Exception e ) { /*Ignored */ }
		}
		if(user == null) {
			System.out.println("User " + username + " was not found.\n");
		} else {
			System.out.println("Found " + username + "\n");
		}
		return user;
	}
	
	/*
	// Inserts user into db
	public void insertUser(
			String first_name,
			String last_name,
			String username,
			byte[] password,
			byte[] salt,
			int admin) {
		System.out.println(first_name);
		String sql = "INSERT INTO USERS (first_name, last_name, username, password, salt, admin) ";
		sql += "VALUES ('" + first_name + "', '" + last_name + "', '" +  username + "',?, ?, " + admin + ")";
	try  {
			Connection conn = this.connect(this.DATABASE); 
			Statement stmt = conn.createStatement();
			PreparedStatement statement = conn.prepareStatement(sql); 
			
			// Convert byte arrays to binary for blob datatype in db
			InputStream pw = new ByteArrayInputStream(password);
			InputStream slt = new ByteArrayInputStream(salt);
			statement.setBinaryStrseam(1, pw, password.length);
			statement.setBinaryStream(2, slt, salt.length);;
			statement.executeUpdate();		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}*/
	
	
	public static void insertUser(User user, String password) {
		if(!userExists(user.getUserName())) {
			Connection conn = null;
			PreparedStatement ps = null;
			String sql = "INSERT INTO USERS (first_name, last_name, username, password, salt, admin)";
			sql += " VALUES(?, ?, ?, ?, ?, ?)";
			
			try {
				conn = DBManager.connect(DATABASE);
				ps = conn.prepareStatement(sql);
				byte[] salt = Password.getNextSalt();
				byte[] hashed = Password.hash(password.toCharArray(), salt);
				
				// Convert byte arrays to ByteArrayInputStreams for inserting into blob in database
				InputStream pw = new ByteArrayInputStream(hashed);
				InputStream slt = new ByteArrayInputStream(salt);
				
				// Insert values into prepared statement
				ps.setString(1, user.getFirstName());
				ps.setString(2, user.getLastName());
				ps.setString(3, user.getUserName());
				ps.setBinaryStream(4, pw, hashed.length);
				ps.setBinaryStream(5, slt, salt.length);
				ps.setBoolean(6, user.isAdmin());
				
				ps.executeUpdate();
				System.out.println("Successfully inserted user: " + user.getUserName() + "\n");
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try { ps.close(); } catch (Exception e) { /* Ignored */ }
				try { conn.close(); } catch (Exception e) { /* Ignored */ }
			}
		}
	}
	
	// This is the method that will be used if a User object is passed
	public static void insertUser(User user, byte[] password, byte[] salt) {
	
		// Only do this if this username does not exist
		if(!userExists(user.getUserName())) {
			
			Connection conn = null;
			PreparedStatement ps = null;
			String sql = "INSERT INTO USERS (first_name, last_name, username, password, salt, admin)";
			sql += " VALUES(?, ?, ?, ?, ?, ?)";
			System.out.println("expected");
			try {
				conn = DBManager.connect(DATABASE);
				ps = conn.prepareStatement(sql);
				
				// Convert byte arrays to ByteArrayInputStreams for inserting into blob in database
				InputStream pw = new ByteArrayInputStream(password);
				InputStream slt = new ByteArrayInputStream(salt);
				
				// Insert values into prepared statement
				ps.setString(1, user.getFirstName());
				ps.setString(2, user.getLastName());
				ps.setString(3, user.getUserName());
				ps.setBinaryStream(4, pw, password.length);
				ps.setBinaryStream(5, slt, salt.length);
				ps.setBoolean(6, user.isAdmin());
				
				ps.executeUpdate();
				System.out.println("Successfully inserted user: " + user.getUserName() + "\n");
			
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			} finally {
				try { ps.close(); } catch (Exception e) { /* Ignored */ }
				try { conn.close(); } catch (Exception e) { /* Ignored */ }
			}
		}
	}
	
	public static void insertUser(
			String firstName,
			String lastName, 
			String username,
			byte[] password,
			byte[] salt,
			boolean admin) {
		
			// Only do this if this username does not exist
			if(!userExists(username)) {
				Connection conn = null;
				PreparedStatement ps = null;
				String sql = "INSERT INTO USERS (first_name, last_name, username, password, salt, admin)";
				sql += " VALUES(?, ?, ?, ?, ?, ?)";
				
				try {
					conn = DBManager.connect(DATABASE);
					ps = conn.prepareStatement(sql);
					
					// Convert byte arrays to ByteArrayInputStreams for inserting into blob in database
					InputStream pw = new ByteArrayInputStream(password);
					InputStream slt = new ByteArrayInputStream(salt);
					
					// Insert values into prepared statement
					ps.setString(1, firstName);
					ps.setString(2, lastName);
					ps.setString(3, username);
					ps.setBinaryStream(4, pw, password.length);
					ps.setBinaryStream(5, slt, salt.length);
					ps.setBoolean(6, admin);
					
					ps.executeUpdate();
					System.out.println("Successfully inserted user: " + username + "\n");
				
				} catch (SQLException e) {
					System.out.println(e.getMessage());
				} finally {
					try { ps.close(); } catch (Exception e) { /* Ignored */ }
					try { conn.close(); } catch (Exception e) { /* Ignored */ }
				}
			}
	}

	public static void removeUser(String username) {
		String sql = "DELETE FROM USERS WHERE USERNAME = ?";
		
		Connection conn = null;
		PreparedStatement ps = null;
		
		try {
			conn = DBManager.connect(DATABASE);
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.executeUpdate();
			System.out.println("Removed " + username + " from database.\n");
		} catch (SQLException e) { 
			System.out.println(e.getMessage()); 
		} finally {
			try { ps.close(); } catch (Exception e) { /* Ignored */ }
			try { conn.close(); } catch (Exception e) { /* Ignored */ }
		}
	}
	
	public static void changePassword(User admin, String username, String newPassword) {
		if(!admin.isAdmin() || !admin.isLoggedIn()) { 
			System.out.println("You must be an admin to change a password without the old password.\n");
			return; 
		}
		if(!userExists(username)) {
			System.out.println("Username: " + username + " not found in database.\n");
			return;
		}
		
		System.out.println("Changing password for " + username + "\n");
		
		String sql = "UPDATE USERS SET PASSWORD = ?, SALT = ? WHERE USERNAME = ?";
		Connection conn = null;
		PreparedStatement ps = null;
		byte[] salt = Password.getNextSalt();
		byte[] hashed = Password.hash(newPassword.toCharArray(), salt);
		
		InputStream slt = new ByteArrayInputStream(salt);
		InputStream pw = new ByteArrayInputStream(hashed);
		
		try {
			conn = DBManager.connect(DATABASE);
			ps = conn.prepareStatement(sql);
			ps.setBinaryStream(1, pw, hashed.length);
			ps.setBinaryStream(2, slt, salt.length);
			ps.setString(3, username);
			ps.executeUpdate();
			System.out.println(username + " has successfully changed their password.\n");
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try { ps.close(); } catch (Exception e) { /* Ignored */ }
			try { conn.close(); } catch (Exception e) { /* Ignored */ }
		}
		
		
	}
	
	public static boolean verifyCredentials(String username, String password) {
		Connection conn = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		byte[] salt = null;
		boolean credentialsVerified = false;
		System.out.println("Checking credentials for " + username + "...\n");
		
		try {
			conn = DBManager.connect(DATABASE);
			
			// First let's get the user's salt so we can  rehash and verify them
			String sql = "SELECT * FROM USERS WHERE USERNAME = ?";
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			
			// Execute statement
			rs = ps.executeQuery();
			
			// Get salt
			try {
				if(rs.next()) {
					salt = rs.getBytes("salt");
				}
			} catch (SQLException e) { System.out.println(e.getMessage());}
			
			try { rs.close(); } catch (Exception e) { /* Ignored */ }
			try { ps.close(); } catch (Exception e) { /* Ignored */ }
			
			// If no user was found then go ahead and return false
			if(salt == null) {
				try { conn.close(); } catch (Exception e) { /* Ignored */ }
				System.out.println("Invalid username or password.\n");
				return false;
			}
			
			// New statement to actually verify now that we can rehash their password
			sql = "SELECT * FROM USERS WHERE USERNAME = ? AND PASSWORD = ?";
			byte[] hashed = Password.hash(password.toCharArray(), salt);
			
			// Turn byte array password into ByteArrayInputStreams
			InputStream pw = new ByteArrayInputStream(hashed);
			
			// Prepare statement
			ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ps.setBinaryStream(2, pw, hashed.length);
			
			
			// Execute statement
			rs = ps.executeQuery();
			try {
				if(rs.next()) {
					System.out.println(username + " has successfully logged in.\n");
					credentialsVerified = true;
				} else {
					System.out.println("Invalid username or password.\n");
				}
			} catch (SQLException e) { System.out.println(e.getMessage()); }
			
		} catch (Exception e) { 
			System.out.println("here");
			e.printStackTrace();
		}
		finally {
			try { rs.close(); } catch (Exception e) { /* Ignored */ }
			try { ps.close(); } catch (Exception e) { /* Ignored */ }
			try { conn.close(); } catch (Exception e) { /* Ignored */ }
		}
		
		return credentialsVerified;
	}
	
	
	public static List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		
		String sql = "SELECT * FROM USERS";
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement ps = null;
		try {
			conn = DBManager.connect(DATABASE);
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			try {
				while(rs.next()) {
					User user = new User();
					String fn = rs.getString("first_name");
					String ln = rs.getString("last_name");
					String username = rs.getString("username");
					boolean admin = rs.getBoolean("admin");
					user.setUser(fn, ln, username, admin);
					users.add(user);
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try { rs.close(); } catch (Exception e) { /* Ignored */ }
			try { ps.close(); } catch (Exception e) { /* Ignored */ }
			try { conn.close(); } catch (Exception e) { /* Ignored */ }
		}
		
		return users;
		
	}
}


