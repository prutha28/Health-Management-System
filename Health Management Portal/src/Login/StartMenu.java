package Login;

import java.sql.*;
import java.util.Scanner;

//import oracle.jdbc.OracleTypes;
import Login.ConnectionClass;
import Login.Utility;
import Login.LoginPage;

public class StartMenu {

	public static void main(String[] args) {
		StartPage();

	}

	public static void StartPage(){
		System.out.println("1 for login");
		System.out.println("2 for sign up as patient");
		System.out.println("3 for sign up as health supporter");
		System.out.println("4 to go back to the main menu\n");

		Scanner sc = Utility.getScanner();
		while(!sc.hasNextInt())
			sc.next();
		int choice = sc.nextInt();
		switch(choice){
		case 1:
			LoginPage.Login();
		case 2:
			signup(2);
		case 3:
			signup(3);
		default:
			System.out.println("Enter a valid choice\n");
			StartPage();
		}
	}

	public static void signup(int role){
		try {
			Connection conn = ConnectionClass.getConnection();
			String signUp = "{call SignUp(?,?,?,?,?,?,?,?)}";
			CallableStatement signupProc = conn.prepareCall(signUp);
			
			System.out.println("Enter your unique id\n");
			String id = Utility.getScanner().nextLine();
			signupProc.setString(1, id);
//			signupProc.setDate(2, getCurrentDate());
			
			System.out.println("Enter your name\n");
			String name = Utility.getScanner().nextLine();
			signupProc.setString(3, name);
			
			System.out.println("Enter your address\n");
			String address = Utility.getScanner().nextLine();
			signupProc.setString(4, address);
			
			System.out.println("Enter your gender (M/F)\n");
			String gender = Utility.getScanner().nextLine();
			signupProc.setString(5, gender);
			
			System.out.println("Enter your date of birth (MM/dd/yyyy)\n");
			String dateString = Utility.getScanner().nextLine();
			java.sql.Date dob = DateUtils.getSQLDateFromString(dateString);
			signupProc.setDate(2, dob);
			
			System.out.println("Enter your contact number\n");
			String number = Utility.getScanner().nextLine();
			signupProc.setString(6, number);
			
			System.out.println("Enter your password\n");
			String password = Utility.getScanner().nextLine();
			signupProc.setString(7, password);
			
			if(role == 2)
				signupProc.setString(8,  String.valueOf('W'));
			else 
				signupProc.setString(8, String.valueOf('N'));

			signupProc.executeUpdate();
			System.out.println("Successfully Signed Up!\n");
			StartPage();
		} 
		catch (Throwable oops) {
			oops.printStackTrace();
			//ConnectionClass.close(ConnectionClass.getConnection());
			System.exit(1);
		} finally{
			ConnectionClass.close();
		}
	}

	private static java.sql.Date getCurrentDate() {
		java.util.Date today = new java.util.Date();
		return new java.sql.Date(today.getTime());
	}

	static void close(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (Throwable whatever) {
			}
		}
	}

}
