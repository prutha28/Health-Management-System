package Login;

import java.sql.*;

public final class ConnectionClass {
public static Connection conn= null;
static final String jdbcURL = "jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01";
static final String dbDriver = "oracle.jdbc.driver.OracleDriver";

public static Connection getConnection()
	{
		if(conn != null)
			return conn;
			try {

				Class.forName(dbDriver);

			} catch (ClassNotFoundException e) {

				System.out.println(e.getMessage());
			}
			try {

				conn = DriverManager.getConnection(jdbcURL, "jsharda","200109115");
				return conn;

			} catch (SQLException e) {

				System.out.println("didnt get the connection");
				System.exit(0);
			}
			return conn;
		}
	

public static void close() {
	Connection conn = getConnection();
	if (conn != null) {
		try {
			conn.close();
			conn = null;
		} catch (Throwable whatever) {
			System.out.println("could not close connection");
		} finally{
			System.exit(1);
		}
		
	}
}

}
