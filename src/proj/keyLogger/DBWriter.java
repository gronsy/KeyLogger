package proj.keyLogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBWriter {
	private Connection con;
	private boolean connectionSuccess=true;
	private static final Logger log=LoggerFactory.getLogger(LoggerWindow.class);
	
	private void connectDB(String dsn, String user, String passwd) {
		log.debug("DBWriter: Started connecting to database");
		try {
			log.debug("Trying to connect to database.");
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			con=DriverManager.getConnection("jdbc:sqlserver://localhost:1433;database="+dsn+";user="+user+";password="+passwd);
			log.debug("DBWriter: Successfuly connected to database.");
		}catch(Exception e) {
			log.error("Failed to connect to database.");
			connectionSuccess=false;
			JOptionPane.showMessageDialog(null, "Failed to connect to the database.", "Error!", JOptionPane.ERROR_MESSAGE);
		}
		log.debug("DBWriter: Successfully connected to database.");
	}
	
	private void addData(String tableName, JTextArea logTxt) {
		log.debug("DBWriter: Entered add data funciton.");
		PreparedStatement stmt=null;
		String query="insert into "+tableName+" values(?, ?)";
		try {
			log.debug("DBWriter: Trying to add data into database.");
			for(String line:logTxt.getText().split("\\n")) {
			String[] parts=line.split(":");	
			stmt=con.prepareStatement(query);
			stmt.setString(1, parts[0]);
			stmt.setString(2, parts[1]);
			stmt.executeUpdate();
			}
		}catch(Exception e){
			log.error("DBWriter: Failed to add into database", e);
			JOptionPane.showMessageDialog(null, "Failed to inster data into database.", "Error!", JOptionPane.ERROR_MESSAGE);
		}
		log.debug("DBWriter: Exited add data functon.");
	}
	
	public void writeToDB(String dsn, String user, String passwd, String tableName, JTextArea logTxt) {
		log.debug("DBWriter: Entered write to database function.");
		connectDB(dsn, user, passwd);
		if(connectionSuccess) {
			addData(tableName, logTxt);
		}
		log.debug("DBWriter: Exited write to database function.");
	}
}
