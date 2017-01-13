package Login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

public class Alerts {

	public static void showOptions() {
	
		System.out.println("1 to show all the alerts.");
		System.out.println("2 to clear alerts.");
		System.out.println("3 to go back to the menu.");
		
		Scanner sc = Utility.getScanner();
		int choice = sc.nextInt();
		
		switch(choice){
		case 1 :
			showAllAlerts();
			break;
		case 2 :
			if( User.getPatientId().equals(User.getLoggedInUser())){
				// Patient has logged in.
				System.out.println("\nPlease Enter observations before clearing alerts.");
				HealthObservations.addHealthObservation();
				clearAlert();
				
			}else{
				// For a health supporter.
				clearAlert();
			}
			break;
		case 3: 
			{
				if (User.getLoggedInUser() == User.getPatientId())
					PatientMainPage.showPatientMenu();
				else
					HSMainPage.showHSMenu();
			}
			break;
		default: 
		{
			System.out.println("Invalid choice. Please enter again!");
			showOptions();
		}
		}
	}

	private static void showAllAlerts() {
		try {
			Connection conn = ConnectionClass.getConnection();
			String alerts = "{call SHOW_ALERTS_FOR_PATIENT(?,?)}";
			CallableStatement alertsProc = conn.prepareCall(alerts);
			alertsProc.setString(1, User.getPatientId());
			alertsProc.registerOutParameter(2, OracleTypes.CURSOR);
			alertsProc.executeQuery();
			int len = 0;
			ResultSet rs = (ResultSet) alertsProc.getObject(2);
//			System.out.println("Alert Id\tAlert Type\tDsisease Name\tMetric Name\tGenerated at");
			System.out.println("\n");
			while (rs.next()) {
				String alertId = rs.getString(1);
				String alertType = rs.getString(2);
				String diseaseName = rs.getString(3);
				String metricName = rs.getString(4);
				Date generatedAt = rs.getDate(5);

				String alertMessage ;
				
				if( alertType.equals("L")){
					alertMessage = "Low Activity Alert." ;
				}else{
					alertMessage = "Outside the recommended limits Alert." ;
				}
				System.out.println("\nAlert id : " + alertId);
				System.out.print("Alert Type : " + alertMessage);
				System.out.print("\nDisease name : " + diseaseName);
				System.out.print("\nMetric Type : " + metricName);
				System.out.print("\nGenerated At : " + generatedAt);
				System.out.println("\n");
				len++;
			}
			if (len == 0) {
				System.out.println("No Active Alerts found for the patient!");
			}
			if (rs != null) {
				rs.close();
			}
			showOptions();

		} catch (Throwable e) {
			e.printStackTrace();
			showOptions();
		}
	}	
	
	private static void clearAlert(){
		try{
			System.out.println("Please enter the Alert Id to clear:");
			String alertId = Utility.getScanner().nextLine();
			Connection conn = ConnectionClass.getConnection();
			String clearAlert = "{call CLEAR_ALERT_FOR_PATIENT(?,?,?, ?)}";
			CallableStatement clearAlertProc = conn.prepareCall(clearAlert);
			clearAlertProc.setString(1, User.getPatientId());
			clearAlertProc.setString(2, alertId);
			clearAlertProc.setString(3, User.getLoggedInUser());
			clearAlertProc.registerOutParameter(4, OracleTypes.INTEGER);
			clearAlertProc.executeUpdate();
			
			int response = clearAlertProc.getInt(4);
			if(response == 1){
				System.out.println("\nAlert has been cleared!");
				showOptions();
			} else if( response == -1){
				System.out.println("You are not authorized to clear this alert.");
			}else{
				// TODO
			}

		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}
	}

}
