package übungen;

import java.util.Vector;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import java.sql.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class sftpabfrage {

    public static void main(String[] args) {

        String url = "pnpd11.npv.de";
        String user = "ppi";
        String password = "ppi";
        int port = 22;
        
        String dburl = "jdbc:sqlserver://npv43.npv.de:1433;databaseName=ads_log";
        String dbuser = "adsuiteservices";
        String dbpassword = "palt*2016";

        String[] singlePlateDirs = {
        	    "//PPI/print/news1200_1/ppi_single_plate/WorkDir/",
        	    "//PPI/print/news1200_2/ppi_single_plate/WorkDir/",
        	    "//PPI/print/news1200_3/ppi_single_plate/done/"
        	};

        	String[] panoPlateDirs = {
        	    "//PPI/print/news1200_1/ppi_pano_plate/WorkDir/",
        	    "//PPI/print/news1200_2/ppi_pano_plate/WorkDir/",
        	    "//PPI/print/news1200_3/ppi_pano_plate/done/"
        	};

        for (int i = 0; i < singlePlateDirs.length; i++) {
        	
        	 Session session = null;
             Channel channel = null;
             ChannelSftp sftpChannel = null;
             Connection conn = null;
        	
			try {
				JSch jsch = new JSch();
				session = jsch.getSession(user, url, port);
				session.setPassword(password);
				session.setConfig("StrictHostKeyChecking", "no");
				session.connect();

				channel = session.openChannel("sftp");
				channel.connect();
				sftpChannel = (ChannelSftp) channel;

				for (String remoteDir : new String[] { singlePlateDirs[i], panoPlateDirs[i] }) {
					sftpChannel.cd(remoteDir);

					Vector<LsEntry> fileList = sftpChannel.ls(".");

					conn = DriverManager.getConnection(dburl, dbuser, dbpassword);
					System.out.println("Erfolgreiche DB-Verbindung!");

					String insertSQL = "INSERT INTO test3 (dateiname, objekt, ausgabe, datum, plattenart) VALUES (?, ?, ?, ?, ?)";
					PreparedStatement ps = conn.prepareStatement(insertSQL);

					String plattenartTyp = remoteDir.contains("ppi_pano_plate") ? "Pano" : "Single";

					for (ChannelSftp.LsEntry entry : fileList) {
						if (!entry.getAttrs().isDir()) {
							String dateiname = entry.getFilename();
							String objekt = dateiname.length() > 3 ? dateiname.substring(0, 3) : "";
							String ausgabe = dateiname.length() > 9 ? dateiname.substring(5, 8) : "";

							int aenderungsdatumTimestamp = entry.getAttrs().getMTime();
							LocalDateTime aenderungsdatum = Instant.ofEpochSecond(aenderungsdatumTimestamp)
									.atZone(ZoneId.systemDefault()).toLocalDateTime();

							String plattenart = plattenartTyp;

							ps.setString(1, dateiname);
							ps.setString(2, objekt);
							ps.setString(3, ausgabe);
							ps.setTimestamp(4, Timestamp.valueOf(aenderungsdatum));
							ps.setString(5, plattenart);

							ps.executeUpdate();

							System.out.println("Nach DB: " + dateiname + " | Objekt: " + objekt + " | Ausgabe: "
									+ ausgabe + " | Datum: " + aenderungsdatum + " | Plattenart: " + plattenart);
						}
					}

					ps.close();
					conn.close();
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (sftpChannel != null)
					sftpChannel.exit();
				if (channel != null)
					channel.disconnect();
				if (session != null)
					session.disconnect();
				try {
					if (conn != null)
						conn.close();
					System.out.println("Close");
				} catch (Exception e) {
				}
			} 
		}
    }
}
