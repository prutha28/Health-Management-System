package Login;

import java.sql.*;

import oracle.jdbc.OracleTypes;

public class HS_Manage {
	public static void showOptions() {
		System.out.println("1 for list of health supporters");
		System.out.println("2 to add a health supporter");
		System.out.println("3 to remove a health supporter");
		System.out.println("4 to toggle health supporter roles");
		System.out.println("5 to go back to main menu");
		
		int choice = Utility.getScanner().nextInt();

		switch (choice) {
		case 1:
			listHealthSupporters();
		case 2:
			addHealthSupporter();
		case 3:
			removeHealthSupporter();
		case 4:
			toggleHealthSupporter();
		case 5: {
			if (User.getLoggedInUser() == User.getPatientId())
				PatientMainPage.showPatientMenu();
			else
				HSMainPage.showHSMenu();
		}
		default: {
			System.out.println("Invalid choice. Please enter again!");
			showOptions();
		}
		}
	}
	private static void removeHealthSupporter(){
		try{
			System.out.println("Enter Health Supporter's id to remove:");
			String hsid = Utility.getScanner().nextLine();
			Connection conn = ConnectionClass.getConnection();
			String removeHS = "{call REMOVE_HS(?,?,?)}";
			CallableStatement removeHSProc = conn.prepareCall(removeHS);
			removeHSProc.setString(2, hsid);
			removeHSProc.setString(1, User.getPatientId());
			removeHSProc.registerOutParameter(3, OracleTypes.INTEGER);
			
			removeHSProc.executeUpdate();
			
			int response = removeHSProc.getInt(3);
			if(response == -1){
				System.out.println("You dont have a health supporter with given id. Please try with correct HS Id!");
			} else if(response == -2){
				System.out.println("Since you are sick, you can not remove your only health supporter!");
			}else {
				System.out.println("Successfully removed "+hsid+" from health supporters");
			}
			showOptions();

		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}

	}
	private static void addHealthSupporter() {
		try {
			System.out.println("Enter Health Supporter's id:");
			String hsid = Utility.getScanner().nextLine();
			System.out.println("\nEnter date of authorization ( MM/dd/yyyy):");
			String authDateString = Utility.getScanner().nextLine();
			Date authDate = DateUtils.getSQLDateFromString(authDateString);
			Connection conn = ConnectionClass.getConnection();
			String addHS = "{call ADD_HS(?,?,?, ?)}";
			CallableStatement addHSProc = conn.prepareCall(addHS);
			addHSProc.setString(1, hsid);
			addHSProc.setString(2, User.getPatientId());
			addHSProc.setDate(3, authDate);
			addHSProc.registerOutParameter(4, OracleTypes.INTEGER);
			
			addHSProc.executeUpdate();
			
			int response = addHSProc.getInt(4);
			if(response == -1){
				System.out.println("Given Health Supporter ID is not present in system. Please create account and try!");
			}else if(response == -3){
				System.out.println("This health supporter has already been added as the Primary Health Supporter.");
			} 
			else if(response == -2){
				System.out.println("Already have 2 health supporter. You can not add more");
			}else if(response == 0){
				System.out.println("You can not add yourself as health supporter!");
			}else {
				System.out.println("Successfully added "+hsid+" as a health supporter");
			}
			showOptions();
		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}

	}

	private static void listHealthSupporters() {
		try {
			Connection conn = ConnectionClass.getConnection();
			String listHS = "{call SHOW_ALL_HS_FOR_PATIENT(?,?)}";
			CallableStatement listHSProc = conn.prepareCall(listHS);
			listHSProc.setString(1, User.getPatientId());
			listHSProc.registerOutParameter(2, OracleTypes.CURSOR);

			listHSProc.executeUpdate();

			ResultSet rs = (ResultSet) listHSProc.getObject(2);
			int len = 0;
			System.out.println("\nID\tName\tType\tDate of Authorization\n");
			while (rs.next()) {
				String hsid = rs.getString(1);
				String name = rs.getString(2);
				String type = rs.getString(3);
				Date authDate = rs.getDate(4);

				System.out.print( hsid + "\t");
				System.out.print( name + "\t");
				System.out.print( type + "\t");
				System.out.print( authDate + "\t");
				System.out.println("\n");
				len++;
			}
			if (len == 0) {
				System.out.println("\nNo health supporters found!");
			}
			if (rs != null) {
				rs.close();
			}
			showOptions();

		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}

	}
	
	private static void toggleHealthSupporter(){
		try {
			Connection conn = ConnectionClass.getConnection();
			String toggleHS = "{call TOGGLE_HS_ROLE(?,?)}";
			CallableStatement toggleHSProc = conn.prepareCall(toggleHS);
			toggleHSProc.setString(1, User.getPatientId());
			toggleHSProc.registerOutParameter(2, OracleTypes.INTEGER);

			toggleHSProc.executeUpdate();

			int val = toggleHSProc.getInt(2);
			if(val == -1){
				System.out.println("\nYou dont have two HS to toggle their role");
			} else {
				System.out.println("\nSuccessfully updated the roles");
			}
			showOptions();

		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}
	}
}
