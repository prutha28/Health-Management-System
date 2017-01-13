package Login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

public class DateUtils {

	public static java.sql.Date getSQLDateFromString( String dateString){
		
		SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");

		java.util.Date javaDate = null;
		try {
			javaDate = format.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		java.sql.Date sqlDate = new java.sql.Date(javaDate.getTime());
		return sqlDate;
	}
}
