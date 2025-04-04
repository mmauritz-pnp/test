package übungen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class druckplattenverbrauch {
	private static final String dburl = "jdbc:sqlserver://npv43.npv.de:1433;databaseName=ads_log";
	private static final String dbuser = "adsuiteservices";
	private static final String dbpassword = "palt*2016";
	private static final String csvfile = "//npv.de/fs/Fremdbelichtung/Archiv/ZurArchivierung/2025/"
	
	public static void main(String[] args) {
		
	}

	public static void exportToCSV() {
		
		String gestern = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-mm-dd"));
		String liste = "SELECT datum, objekt, ausgabe, plattenart, COUNT(objekt) AS plattenverbrauch\r\n"
				+ "FROM test3 \r\n"
				+ "WHERE datum = DATEADD(DAY, -8, CAST(GETDATE() AS DATE)) \r\n"
				+ "GROUP BY datum, objekt, ausgabe, plattenart;";
		
		Connection con = DriverManager.getConnection(dburl, dbuser, dbpassword);
				
		}
	}