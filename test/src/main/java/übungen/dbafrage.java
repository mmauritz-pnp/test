package übungen;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class dbafrage {
    public static void main(String[] args) {
        
        String url = "jdbc:postgresql://172.25.34.162:5432/alfa";
        String user = "adsuite";
        String password = "6a0j4PUHZq5K"; 

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            if (conn != null) {
                System.out.println("erfolgreich");
            }
        } catch (SQLException e) {
            System.out.println("fehler");
            e.printStackTrace();
        }
    }
}



//
//public class dbafrage {
//    public static void main(String[] args) {
//        // Datenbankverbindung
//        String url = "jdbc:sqlserver://npv33.npv.de;databaseName=ads;encrypt=true;trustServerCertificate=true";
//        String user = "adsuiteservices";
//        String password = "palt*2016"; 
//
//        try (Connection conn = DriverManager.getConnection(url, user, password);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery("SELECT * FROM DeineTabelle")) {
//
//            System.out.println("Verbindung erfolgreich!");
//
//            // Daten ausgeben
//            while (rs.next()) {
//                System.out.println("ID: " + rs.getInt("ID") + ", Name: " + rs.getString("Name"));
//            }
//
//        } catch (SQLException e) {
//            System.out.println("Verbindung fehlgeschlagen!");
//            e.printStackTrace();
//        }
//    }
//}

