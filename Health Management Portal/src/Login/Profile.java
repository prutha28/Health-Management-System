package Login;

import java.sql.*;
import java.sql.Connection;

public class Profile {
	public static void showProfile() {
		try {
			Connection conn = ConnectionClass.getConnection();
			PreparedStatement st = conn.prepareStatement("SELECT * FROM JSHARDA.PATIENT WHERE PID=?");
			st.setString(1, User.getLoggedInUser());
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				System.out.println("Unique ID: " + rs.getString(1));
				System.out.println("Name: " + rs.getString(3));
				System.out.println("Date Of Birth: " + rs.getString(2));
				System.out.println("Address: " + rs.getString(4));
				System.out.println("Gender: " + rs.getString(5));
				System.out.println("Contact: " + rs.getString(8));
				if( User.getLoggedInUser().equals(User.getPatientId())){
					System.out.println("Password: " + rs.getString(7));
				}
			}
			if (rs != null) {
				rs.close();
			}

			if (st != null) {
				st.close();
			}

			if (User.getLoggedInUser() == User.getPatientId())
				PatientMainPage.showPatientMenu();
			else
				HSMainPage.showHSMenu();

		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}
	}

	public static void editProfile() {
		try {
			System.out.println("1 to update name");
			System.out.println("2 to update address");
			System.out.println("3 to update contact information");
			System.out.println("4 to update password");
			System.out.println("5 to update date of birth");
			System.out.println("6 to update Gender");
			System.out.println("7 to go back to main menu");

			int choice = Utility.getScanner().nextInt();
			Connection conn = ConnectionClass.getConnection();
			switch (choice) {
			case 1: {
				System.out.println("Enter your updated name");
				String name = Utility.getScanner().nextLine();
				PreparedStatement st = conn
						.prepareStatement("UPDATE JSHARDA.PATIENT SET JSHARDA.PATIENT.NAME = ?" + " WHERE PID=?");
				st.setString(1, name);
				st.setString(2, User.getLoggedInUser());
				st.executeQuery();
				System.out.println("Succesfully updated name");
				if (st != null)
					st.close();
				Profile.editProfile();
			}

			case 2: {
				System.out.println("Enter your updated Address");
				String address = Utility.getScanner().nextLine();
				PreparedStatement st = conn
						.prepareStatement("UPDATE JSHARDA.PATIENT SET JSHARDA.PATIENT.ADDRESS = ?" + " WHERE PID=?");
				st.setString(1, address);
				st.setString(2, User.getLoggedInUser());
				st.executeQuery();
				System.out.println("Succesfully updated address");
				if (st != null)
					st.close();
				Profile.editProfile();
			}

			case 3: {
				System.out.println("Enter your updated contact number");
				String number = Utility.getScanner().nextLine();
				PreparedStatement st = conn
						.prepareStatement("UPDATE JSHARDA.PATIENT SET JSHARDA.PATIENT.CONTACT_NUMBER = ?" + " WHERE PID=?");
				st.setString(1, number);
				st.setString(2, User.getLoggedInUser());
				st.executeQuery();
				System.out.println("Succesfully updated contact number");
				if (st != null)
					st.close();
				Profile.editProfile();
			}

			case 4: {
				System.out.println("Enter your password");
				String password = Utility.getScanner().nextLine();
				PreparedStatement st = conn
						.prepareStatement("UPDATE JSHARDA.PATIENT SET JSHARDA.PATIENT.PASSWORD = ?" + " WHERE PID=?");
				st.setString(1, password);
				st.setString(2, User.getLoggedInUser());
				st.executeQuery();
				System.out.println("Succesfully updated password");
				if (st != null)
					st.close();
				Profile.editProfile();
			}

			case 5: {
				System.out.println("Enter your date of birth");
				String dateString = Utility.getScanner().nextLine();
				Date dob = DateUtils.getSQLDateFromString(dateString);
				PreparedStatement st = conn
						.prepareStatement("UPDATE JSHARDA.PATIENT SET JSHARDA.PATIENT.DOB = ?" + " WHERE PID=?");
				st.setDate(1, dob);
				st.setString(2, User.getLoggedInUser());
				st.executeQuery();
				System.out.println("Succesfully updated date of birth.");
				if (st != null)
					st.close();
				Profile.editProfile();
			}
			
			case 6: {
				System.out.println("Enter your gender");
				String gender = Utility.getScanner().nextLine();
				PreparedStatement st = conn
						.prepareStatement("UPDATE JSHARDA.PATIENT SET JSHARDA.PATIENT.GENDER = ?" + " WHERE PID=?");
				st.setString(1, gender);
				st.setString(2, User.getLoggedInUser());
				st.executeQuery();
				System.out.println("Succesfully updated gender.");
				if (st != null)
					st.close();
				Profile.editProfile();
			}
			case 7: {
				if (User.getLoggedInUser() == User.getPatientId())
					PatientMainPage.showPatientMenu();
				else
					HSMainPage.showHSMenu();
			}

			default: {
				System.out.print("Invaild choice. Please try again!");
				Profile.editProfile();
			}
			}

		} catch (Throwable e) {
			e.printStackTrace();
			ConnectionClass.close();
		}
	}
}
