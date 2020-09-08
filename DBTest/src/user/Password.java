package user;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class Password {
	private static final Random RANDOM = new SecureRandom();
	private static final int ITERATIONS = 10000;
	private static final int KEY_LENGTH = 256;
	
	// Sets random byte array for salting the password
	public static byte[] getNextSalt() {
		byte[] salt = new byte[16];
		RANDOM.nextBytes(salt);
		return salt;
	}
	
	/*
	 *  Hashes password using a given salt. 
	 *  This will be used to generate a password to store into the database, as well as regenerating the same 
	 *  password to check against the database for password validation
	 */
	
	public static byte[] hash(char[] password, byte[] salt) {
		PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
		Arrays.fill(password,  Character.MIN_VALUE);
		try {
			SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			return skf.generateSecret(spec).getEncoded();
		} catch(NoSuchAlgorithmException | InvalidKeySpecException e) {
			throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
		} finally {
			spec.clearPassword();
		}
	}
	
	/**
	 * 
	 * @param password
	 * @param salt
	 * @param expectedHash
	 * @return
	 * 
	 * This might not be used. I've been having problems pulling the password out of the database
	 * and storing it back into a byte array with the correct encoding. It was easier to just do a SQL query
	 * searching for a pair of username and password
	 */
	public static boolean isExpectedPassword(char[] password, byte[] salt, byte[] expectedHash) {
	    byte[] pwdHash = hash(password, salt);
	    Arrays.fill(password, Character.MIN_VALUE);
	    
	    if (pwdHash.length != expectedHash.length) return false;
	    for (int i = 0; i < pwdHash.length; i++) {
	      if (pwdHash[i] != expectedHash[i]) return false;
	    }
	    return true;
	  }
}
