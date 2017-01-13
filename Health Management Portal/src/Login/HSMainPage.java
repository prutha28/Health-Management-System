package Login;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;

import oracle.jdbc.OracleTypes;

public class HSMainPage {

	public static void showHSList(int status) {
		if (status == 2) {
			System.out.println("Your patient Id for your own profile");
		}
		try {

			Connection conn = ConnectionClass.getConnection();
			String listPatients = "{call SHOW_ALL_PATIENTS_FOR_HS(?,?)}";
			CallableStatement listPatientsProc = conn.prepareCall(listPatients);
			listPatientsProc.setString(1, User.getLoggedInUser());
			listPatientsProc.registerOutParameter(2, OracleTypes.CURSOR);

			listPatientsProc.executeUpdate();

			ResultSet rs = (ResultSet) listPatientsProc.getObject(2);
//			int len = 0;
//			if (rs.last()) {
//				len = rs.getRow();
//				rs.beforeFirst();
//			}
//			if (len != 0) {
				System.out.println("Here is list of IDs of all patients for you!");
//				System.out.println("Patient ID \t Supporter Type \t Authentication date");
//			}
			while (rs.next()) {
				String patientId = rs.getString(1);
				String type = rs.getString(3);
				Date AuthDate = rs.getDate(2);
				System.out.println("\nPatient ID : " + patientId);
				System.out.println("\nSupporter Type : " + type);
				System.out.println("\nDate of Authorization : " + AuthDate);
				System.out.print(patientId + "\t" + type + "\t" + AuthDate);
			}

			System.out.println("\nSelect patient id for whom you want to access data from above list:");

			String patientId = Utility.getScanner().nextLine();

			CallableStatement checkValidity = conn.prepareCall("{CALL wrapper_check_if_valid_user(?,?,?)}");
			checkValidity.registerOutParameter(3, OracleTypes.INTEGER);
			checkValidity.setString(1, patientId);
			checkValidity.setString(2, User.getLoggedInUser());

			checkValidity.executeUpdate();
			int isValidity = checkValidity.getInt(3);
			boolean isValid = false;
			if(isValidity==1)
			 isValid = true;
			if (!isValid) {
				System.out.println("Please enter a valid patient ID");
				showHSList(status);
			} else {
				System.out.println("Granted access for given patient");
				User.setPatientID(patientId);
				showHSMenu();
			}
		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}
	}

	public static void showHSMenu() {
		System.out.println("1 for Show Profile");
		System.out.println("2 for Edit Profile");
		System.out.println("3 to Manage Diagnosis");
		System.out.println("4 to Manage Health Indicators");
		System.out.println("5 to Manage Health Observations");
		System.out.println("6 to Manage Alerts");
		System.out.println("7 to logout");

		int choice = Utility.getScanner().nextInt();

		switch (choice) {
		case 1:
			Profile.showProfile();
		case 2:
			Profile.editProfile();
		case 3:
			Diagnosis.showOptions();
		case 4:
			HealthIndicators.showOptions();
		case 5:
			HealthObservations.showOptions();
		case 6:
			Alerts.showOptions();
		case 7: {
			System.out.println("Bye Bye");
			ConnectionClass.close();
			System.exit(0);
		}
		default: {
			System.out.println("Invalid choice.Enter again.");
			showHSMenu();
		}
		}
	}
}
