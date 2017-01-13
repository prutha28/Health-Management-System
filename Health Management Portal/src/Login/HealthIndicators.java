package Login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import oracle.jdbc.internal.OracleTypes;

public class HealthIndicators {

	public static void showOptions() {
		
		System.out.println("1 to add a new health indicator.");
		System.out.println("2 to show a specific health indicator.");
		System.out.println("3 to show all health indicators.");
		System.out.println("4 to update a specific health indicator.");
		System.out.println("5 to go back to main menu");
		Scanner sc = Utility.getScanner();
		int choice = sc.nextInt();
		
		switch(choice){
		case 1 :
			addHealthIndicator();
			break;
		case 2: 
			showHealthIndicator();
			break;
		case 3: 
			showAllHealthIndicators();
			break;
		case 4: 
			updateHealthIndicators();
			break;
		case 5: 
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
	
	private static void updateHealthIndicators() {
		Connection con = ConnectionClass.getConnection();
		String updateHealthIndicator = "{call UPDATE_HEALTH_INDICATOR(?,?,?,?,?,?,?,?)}";
		Scanner sc = Utility.getScanner();
		try {
			System.out.println("\nUpdating the Health Indicator for " + User.getPatientId());
			
			System.out.println("\nPlease Enter the Disease Name: ");
			String diseaseName = sc.nextLine();
		
			System.out.println("\nPlease Enter the Metric Name: ");
			String metricName = sc.nextLine();
			
			System.out.println("\nPlease Enter the Recommended Frequency: ");
			int frequency = sc.nextInt();

			System.out.println("\nPlease Enter the Recommended Minimum Limit: ");
			int min = sc.nextInt();

			System.out.println("\nPlease Enter the Recommended Maximum Limit: ");
			int max = sc.nextInt();
			
			CallableStatement addHealthIndicatorProc = con.prepareCall(updateHealthIndicator) ;
			addHealthIndicatorProc.setString(1, User.getPatientId());
			addHealthIndicatorProc.setString(2, diseaseName);
			addHealthIndicatorProc.setString(3, metricName);
			addHealthIndicatorProc.setInt(4, max);
			addHealthIndicatorProc.setInt(5, min);
			addHealthIndicatorProc.setInt(6, frequency);
			addHealthIndicatorProc.setString(7, User.getLoggedInUser());
			addHealthIndicatorProc.registerOutParameter(8, OracleTypes.INTEGER );	
			addHealthIndicatorProc.executeUpdate();
			int response = addHealthIndicatorProc.getInt(8);
			
			// Handle response codes.
			if( response == -2){
				System.out.println("\nThe recommended value for upper limit is less than the value for lower limit.\nPlease enter valid values.");
			}else if( response == -1){
				System.out.println("\nLogged in User is not a valid user.");
			}else{
				System.out.println("\nThe health indicator has been updated successfully.");
			}
			showOptions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private static void addHealthIndicator() {
		Connection con = ConnectionClass.getConnection();
		String addHealthIndicator = "{call ADD_HEALTH_INDICATOR(?,?,?,?,?,?,?,?,?)}";
		Scanner sc = Utility.getScanner();
		try {
			System.out.println("\nAdding the Health Indicator for " + User.getPatientId());
			
			System.out.println("\nPlease Enter the Disease Name: ");
			String diseaseName = sc.nextLine();
		
			System.out.println("\nPlease Enter the Metric Name: ");
			String metricName = sc.nextLine();
			
			System.out.println("\nPlease Enter the Recommended Frequency: ");
			int frequency = sc.nextInt();

			System.out.println("\nPlease Enter the Recommended Minimum Limit: ");
			int min = sc.nextInt();

			System.out.println("\nPlease Enter the Recommended Maximum Limit: ");
			int max = sc.nextInt();
			
			System.out.println("\nPlease Enter the Date of observation (MM/dd/yyyy) : ");
			String dateOfObservationString = sc.next();
			java.sql.Date dateOfObservation = DateUtils.getSQLDateFromString(dateOfObservationString) ;
			
			CallableStatement addHealthIndicatorProc = con.prepareCall(addHealthIndicator) ;
			addHealthIndicatorProc.setString(1, User.getPatientId());
			addHealthIndicatorProc.setString(2, diseaseName);
			addHealthIndicatorProc.setString(3, metricName);
			addHealthIndicatorProc.setInt(4, frequency);
			addHealthIndicatorProc.setInt(5, max);
			addHealthIndicatorProc.setInt(6, min);
			addHealthIndicatorProc.setDate(7, dateOfObservation);
			addHealthIndicatorProc.setString(8, User.getLoggedInUser());
			addHealthIndicatorProc.registerOutParameter(9, OracleTypes.INTEGER );	
			addHealthIndicatorProc.executeUpdate();
			int response = addHealthIndicatorProc.getInt(9);
			
			// Handle response codes.
			if( response == -2){
				System.out.println("\nThe recommended value for upper limit is less than the value for lower limit.\nPlease enter valid values.");
			}else if( response == -1){
				System.out.println("\nLogged in User is not a valid user.");
			}else if( response == -3){
				System.out.println("\nThere is already a health indicator for this disease.");
			}else{
				System.out.println("\nThe health indicator has been added successfully.");
			}
			showOptions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void showAllHealthIndicators() {
		System.out.println("\nShowing all the Health Indicators for " + User.getPatientId());
		Connection con = ConnectionClass.getConnection();
		String addHealthIndicator = "{call SHOW_ALL_HEALTH_IND_FR_PATIENT(?,?,?,?)}";
		try {
			CallableStatement addHealthIndicatorProc = con.prepareCall(addHealthIndicator) ;
			addHealthIndicatorProc.setString(2, User.getPatientId());
			addHealthIndicatorProc.setString(1, User.getLoggedInUser());
			addHealthIndicatorProc.registerOutParameter(3, OracleTypes.CURSOR );
			addHealthIndicatorProc.registerOutParameter(4, OracleTypes.INTEGER );
			addHealthIndicatorProc.executeUpdate();
		
			ResultSet rs = (ResultSet) addHealthIndicatorProc.getObject(3);
			int response = addHealthIndicatorProc.getInt(4);
			int len = 0;
			System.out.println("\n");
//			System.out.println("Disease Name\tMetric Name\tLower Limit\tUpper Limit\tFrequncy\tObserved On\tRecorded On");

			while (rs != null && rs.next()) {
				String diseaseName = rs.getString(2);
				String metricName = rs.getString(3);
				int max = rs.getInt(4);
				int min = rs.getInt(5);
				int frequency = rs.getInt(6);
				Date observedOn = rs.getDate(7);
				Date recordedOn = rs.getDate(8);
				System.out.print("\nDisease Name : " + diseaseName );
				System.out.print("\nMetric Name : "+ metricName );
				System.out.print("\nMinimum : " + min );
				System.out.print("\nMaximum : " + max );
				System.out.println("\nFrequency : " + frequency );
				System.out.println("Observed On : " + observedOn );
				System.out.println("Recorded On : " + recordedOn );
				System.out.println("\n--------------------------------");
				len++;
			}
			if (len == 0) {
				System.out.println("No health indicators found!");
			}
			if (rs != null) {
				rs.close();
			}
			//TODO Handle response codes.
			showOptions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	private static void showHealthIndicator() {
		Connection con = ConnectionClass.getConnection();
		String addHealthIndicator = "{call SHOW_HEALTH_INDICATOR(?,?,?,?,?,?,?,?,?,?)}";
		Scanner sc = Utility.getScanner();
		try {			
			System.out.println("\nPlease Enter the Disease Name: ");
			String diseaseName = sc.nextLine();
		
			System.out.println("\nPlease Enter the Metric Name: ");
			String metricName = sc.nextLine();
			
			CallableStatement addHealthIndicatorProc = con.prepareCall(addHealthIndicator) ;
			addHealthIndicatorProc.setString(1, User.getPatientId());
			addHealthIndicatorProc.setString(2, diseaseName);
			addHealthIndicatorProc.setString(3, metricName);
			addHealthIndicatorProc.setString(4, User.getLoggedInUser());
			addHealthIndicatorProc.registerOutParameter(5, OracleTypes.VARCHAR );
			addHealthIndicatorProc.registerOutParameter(6, OracleTypes.VARCHAR );
			addHealthIndicatorProc.registerOutParameter(7, OracleTypes.INTEGER );
			addHealthIndicatorProc.registerOutParameter(8, OracleTypes.INTEGER );
			addHealthIndicatorProc.registerOutParameter(9, OracleTypes.INTEGER );
			addHealthIndicatorProc.registerOutParameter(10, OracleTypes.INTEGER );
			addHealthIndicatorProc.executeUpdate();
			
			String diseaseId = addHealthIndicatorProc.getString(6);
			int max = addHealthIndicatorProc.getInt(7);
			int min = addHealthIndicatorProc.getInt(8);
			int frequency = addHealthIndicatorProc.getInt(9);
			int response = addHealthIndicatorProc.getInt(10);
//			System.out.println("\nDisease Name\tMetric Name\tLower Limit\tUpper Limit\tFrequency");
			System.out.println("\nShowing the Health Indicator for " + User.getPatientId());
			System.out.print("\nDisease Name : " + diseaseName );
			System.out.print("\nMetric Name : "+ metricName );
			System.out.print("\nLower Limit : " + min );
			System.out.print("\nUpper Limit : " + max );
			System.out.println("\nFrequency : " + frequency );
			System.out.println("\n--------------------------------");
			//TODO Handle response codes.
			showOptions();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	

}
