import java.util.*;

public class Login {
	static Scanner input = new Scanner(System.in);
	
	private static String getUserName() {
		System.out.println("Please enter username:");
		String username = input.nextLine();
		return username;
	}
	
	private static String getPassword() {
		System.out.println("Please enter password:");
		String password = input.nextLine();
		return password;
	}
	
	public static void logIn() {
		String username = getUserName();
		String password = getPassword();
		
		
	}
}
