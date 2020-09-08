package DB;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import user.User;

// Child class of DBManager. Used only for access the users table in the database
public class DBUser extends DBManager {
	
	public final String DATABASE = "db_users_2";
	public final String TABLE = "users";
	

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
	}
	
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
			statement.setBinaryStream(1, pw, password.length);
			statement.setBinaryStream(2, slt, salt.length);;
			statement.executeUpdate();		
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}
}
