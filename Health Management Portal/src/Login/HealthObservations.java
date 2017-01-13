package Login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Scanner;

import oracle.jdbc.internal.OracleTypes;

public class HealthObservations {

	public static void showOptions() {
		
		System.out.println("\n");
		System.out.println("1 to add a new health observation.");	
		System.out.println("2 to show all the health observations.");
		System.out.println("3 go back to the menu.");

		Scanner sc = Utility.getScanner();
		int choice = sc.nextInt();
		
		switch(choice){
		case 1 :
			addHealthObservation();
			break;
		case 2: 
			showAllHealthObservations();
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

	public static void addHealthObservation() {
		
		Scanner sc = Utility.getScanner();
		System.out.println("Please enter the metric name for the observation:");
		String metricName = sc.nextLine();
		
		System.out.println("Please enter the observed value:" );
		int observedValue = sc.nextInt();
		
		System.out.println("Please enter the date of observation in MM/dd/yyyy format");
		String observationDateString = sc.next();
		java.sql.Date dateOfObservation = DateUtils.getSQLDateFromString(observationDateString);

		Connection con = ConnectionClass.getConnection();
		String addHealthObs = "{call ADD_HEALTH_OBSERVATION( ?,?,?,?,?,?)}";
		int response = -1;
		try {
			CallableStatement addHealthObsProc = con.prepareCall(addHealthObs) ;
			addHealthObsProc.setString( 1, User.getLoggedInUser() );
			addHealthObsProc.setString( 2, metricName );
			addHealthObsProc.setString( 3, User.getPatientId());
			addHealthObsProc.setDate( 4, dateOfObservation);
			addHealthObsProc.setInt(5, observedValue);
			addHealthObsProc.registerOutParameter(6, OracleTypes.INTEGER );
			addHealthObsProc.executeUpdate();
			response = addHealthObsProc.getInt(6);
			
			if( response == 1){
				System.out.println("The observation value is violating the recommended values. Alert has been generated.");
			}else{ //if( response == 2){
				System.out.println("The observation value is within the recommended values and has been recorded successfully.");
			}
			showOptions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void showAllHealthObservations() {
		Connection con = ConnectionClass.getConnection();
		String showAllhealthObs = "{call SHOW_ALL_OBS_FOR_PATIENT(?,?,?,?)}";
		try {
			CallableStatement showAllHealthObsProc = con.prepareCall(showAllhealthObs);
			showAllHealthObsProc.setString(1, User.getLoggedInUser());
			showAllHealthObsProc.setString(2, User.getPatientId());
			showAllHealthObsProc.registerOutParameter(3, OracleTypes.CURSOR);
			showAllHealthObsProc.registerOutParameter(4, OracleTypes.INTEGER);
			showAllHealthObsProc.executeUpdate();

			ResultSet rs = (ResultSet) showAllHealthObsProc.getObject(3);
			System.out.println("\n");
			System.out.println("Showing Health Observations for patient :" + User.getPatientId());
//			System.out.println("\nDisease Name\tMetric Name\tObserved On\tRecorded On\tAdded By");
			
			int len = 0;
			while (rs.next()) {
				String diseaseName = rs.getString(1);
				String metricName = rs.getString(2);
				Date observedOn = rs.getDate(3);
				Date recordedOn = rs.getDate(4);
				String addedBy = rs.getString(6);
				System.out.println("\n");
				System.out.println("\nDisease Name : " + diseaseName);
				System.out.println("\nMetric Name : " + metricName);
				System.out.println("\nObserved On : " + observedOn);
				System.out.println("\nRecorded On : " + recordedOn);
				System.out.println("\nAdded By : " + addedBy);
				len++;
			}
			if (len == 0) {
				System.out.println("No health observations found!");
			}
			if (rs != null) {
				rs.close();
			}
			showOptions();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
