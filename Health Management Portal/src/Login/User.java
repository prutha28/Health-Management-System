package Login;

public class User {
private static String logged_in = null;
private static String patient_id = null;

public static String getLoggedInUser(){
	return logged_in;
}

public static String getPatientId(){
	return patient_id;
}

public static void setLoggedInUser(String s){
	if(s == "")
		s = null;
	else
		logged_in = s;
}

public static void setPatientID(String s){
	if(s == "")
		s = null;
	else
		patient_id = s;
}

}
