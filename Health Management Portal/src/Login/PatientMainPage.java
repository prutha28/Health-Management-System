package Login;

import Login.*;

public class PatientMainPage {

	public static void showPatientMenu() {
		System.out.println("1 for Show Profile");
		System.out.println("2 for Edit Profile");
		System.out.println("3 to Manager health supporters");
		System.out.println("4 to Manage Diagnosis");
		System.out.println("5 to Manage Health Indicators");
		System.out.println("6 to Manage Health Observations");
		System.out.println("7 to Manage Alerts");
		System.out.println("8 to logout");

		int choice = Utility.getScanner().nextInt();

		switch (choice) {
		case 1:
			Profile.showProfile();
		case 2:
			Profile.editProfile();
		case 3:
			HS_Manage.showOptions();
		case 4:
			Diagnosis.showOptions();
		case 5:
			HealthIndicators.showOptions();
		case 6:
			HealthObservations.showOptions();
		case 7:
			Alerts.showOptions();
		case 8: {
			System.out.println("Bye Bye");
			ConnectionClass.close();
			System.exit(0);
		}
		default: {
			System.out.println("Invalid choice.Enter again.");
			showPatientMenu();
		}
		}
	}
}
