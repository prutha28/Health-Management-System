package Login;

import java.sql.CallableStatement;
import java.sql.Connection;
import oracle.jdbc.OracleTypes;
import Login.*;
public class LoginPage {

	public static void Login(){
		try{
			Connection conn = ConnectionClass.getConnection();
			String login = "{call Login(?,?,?,?)}";
			CallableStatement loginProc = conn.prepareCall(login);
			System.out.println("Enter your unique id\n");
			String id = Utility.getScanner().nextLine();
			loginProc.setString(1, id);
			System.out.println("Enter your password\n");
			String password = Utility.getScanner().nextLine();
			loginProc.setString(2, password);
			System.out.println("Enter your role. 1 for patient,  2 for health supporter\n");
			int role = Utility.getScanner().nextInt();
			loginProc.setInt(3, role);
			loginProc.registerOutParameter(4, OracleTypes.INTEGER);
			
			loginProc.executeUpdate();
			
			int status = loginProc.getInt(4);
			
			if(status == 1){
				System.out.println("successfully logged in as patient!");
				User.setLoggedInUser(id);
				User.setPatientID(id);
				PatientMainPage.showPatientMenu();
			} else if (status ==2){
				User.setLoggedInUser(id);
				HSMainPage.showHSList(2);
			}else if (status ==3){
				User.setLoggedInUser(id);
				HSMainPage.showHSList(3);
			} else if(status ==4){
				System.out.println("You are not a health supporter. Please login as a patient");
				Login();
			} else{
				System.out.println("Invalid credentials.Please login again!");
				Login();
			}
		}catch(Throwable oops) {
			oops.printStackTrace();
			System.exit(1);
		} finally{
			ConnectionClass.close();
		}
	}	
}
