package Login;

import java.util.Scanner;

public class Utility {
	static Scanner sc = null;
	public static Scanner getScanner(){
		if(sc!=null)
			return sc;
		else{
			Scanner sc = new Scanner(System.in);
			return sc;
		}

	}
}
