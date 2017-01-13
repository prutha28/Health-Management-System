package Login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import oracle.jdbc.OracleTypes;

public class Diagnosis {
	public static void showOptions() {
		System.out.println("1 to list all diagnosis");
		System.out.println("2 to add a diagnosis");
		System.out.println("3 to remove a diagnosis");
		System.out.println("4 to go back to main menu");

		int choice = Utility.getScanner().nextInt();

		switch (choice) {
		case 1:
			listDiagnosis();
		case 2:
			addDiagnosis();
		case 3:
			 removeDiagnosis();
		case 4: {
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

	public static void listDiagnosis() {
		try {
			Connection conn = ConnectionClass.getConnection();
			String listDiagnosis = "{call SHOW_DIAGNOSIS(?,?)}";
			CallableStatement listDiagnosisProc = conn.prepareCall(listDiagnosis);
			listDiagnosisProc.setString(1, User.getPatientId());
			listDiagnosisProc.registerOutParameter(2, OracleTypes.CURSOR);

			listDiagnosisProc.executeUpdate();

			ResultSet rs = (ResultSet) listDiagnosisProc.getObject(2);
			int len = 0;
			while (rs.next()) {
				String disease = rs.getString(1);
				if (!disease.equals("General")) {
					if (len == 0) {
						System.out.println("List of Diagnosis:");
					}
					System.out.println(rs.getString(1));
					len++;
				}
			}

			if (len == 0) {
				System.out.println("No Diagnosis!");
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

	private static void addDiagnosis() {
		try {
			Connection conn = ConnectionClass.getConnection();
			String queryString = "SELECT UNIQUE(DISEASE_NAME) "
					+ "FROM JSHARDA.DISEASE WHERE DISEASE_NAME NOT LIKE ?" ;
			PreparedStatement st = conn.prepareStatement(queryString);
			st.setString(1, "General");
			ResultSet rs = st.executeQuery();
			int index = 1;
			HashMap<Integer,String> map = new HashMap<Integer,String>();
			while (rs.next()) {
				System.out.println(index+ " to add " + rs.getString(1));
				map.put(index, rs.getString(1));
				index++;
			}
			
			int choice  =  Utility.getScanner().nextInt();
			
			System.out.println("Please enter the date of observation in MM/DD/YYYY format");
			String observationDateString = Utility.getScanner().next();
			java.sql.Date dateOfObservation = DateUtils.getSQLDateFromString(observationDateString);

			
			String addDiagnosis = "{call ADD_DIAGNOSIS(?,?,?,?,?)}";
			CallableStatement addDiagnosisProc = conn.prepareCall(addDiagnosis);
			addDiagnosisProc.setString(1, User.getPatientId());
			addDiagnosisProc.setString(2,User.getLoggedInUser());
			addDiagnosisProc.setDate(3, dateOfObservation);
			addDiagnosisProc.setString(4, map.get(choice));
			addDiagnosisProc.registerOutParameter(5, OracleTypes.INTEGER);
			
			addDiagnosisProc.executeUpdate();

			int response = addDiagnosisProc.getInt(5);
			if (response == 0) {
				System.out.println("Please add HS first before adding disease.");
			} else if (response == -1) {
				System.out.println("You are not authorized to add disease.");
			} else if (response == -2) {
				System.out.println("Disease is already added");
			} else {
				System.out.println("Successfully added " + map.get(choice));
			}
			showOptions();
		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}

	}

	public static void removeDiagnosis() {
		try {
			Connection conn = ConnectionClass.getConnection();
			String listDiagnosis = "{call SHOW_DIAGNOSIS(?,?)}";
			CallableStatement listDiagnosisProc = conn.prepareCall(listDiagnosis);
			listDiagnosisProc.setString(1, User.getPatientId());
			listDiagnosisProc.registerOutParameter(2, OracleTypes.CURSOR);

			listDiagnosisProc.executeUpdate();

			ResultSet rs = (ResultSet) listDiagnosisProc.getObject(2);
			int len = 1;
			HashMap<Integer, String> map = new HashMap<Integer, String>();
			while (rs.next()) {
				String disease = rs.getString(1);
				if (!disease.equals("General")) {
					map.put(len, disease);
					len++;
				}
			}

			if (len != 1) {
				for (int key : map.keySet()) {
					System.out.println("Enter " + key + " to remove " + map.get(key));
				}
				int choice = Utility.getScanner().nextInt();
				String diseaseToRemove = map.get(choice);
				String removeDiagnosis = "{call REMOVE_DIAGNOSIS(?,?,?,?)}";
				CallableStatement removeDiagnosisProc = conn.prepareCall(removeDiagnosis);
				removeDiagnosisProc.setString(1, User.getPatientId());
				removeDiagnosisProc.setString(2, User.getLoggedInUser());
				removeDiagnosisProc.setString(3, diseaseToRemove);
				removeDiagnosisProc.registerOutParameter(4, OracleTypes.INTEGER);
				
				removeDiagnosisProc.executeUpdate();
				
				int response = removeDiagnosisProc.getInt(4);
				if(response != -1){
					System.out.println("Successfully removed diagnosis");
				}else {
					System.out.println("Please try again!");
				}
			} else {
				System.out.println("No Diagnosis to remove");
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
}
