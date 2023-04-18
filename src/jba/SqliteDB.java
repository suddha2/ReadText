package jba;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import me.tongfei.progressbar.ProgressBar;
import me.tongfei.progressbar.ProgressBarBuilder;
import me.tongfei.progressbar.ProgressBarStyle;

//SQLite DB 
public class SqliteDB {
	
	private String url = "";
	
	public SqliteDB(String dbName) {
		this.url = "jdbc:sqlite:"+dbName;
	}
	
	private Connection connect() {
		// SQLite connection string
		
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(this.url);
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return conn;
	}

	// Insert method
	public void addData(ArrayList<DataRow> lst) throws SQLException {
		String dml = "insert into percipitation(xref,yref,date,value) values(?,?,?,?)";
		DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		Connection conn = this.connect();
		PreparedStatement pstmt = conn.prepareStatement(dml);

		// CLI Progress bar for displaying activity
		ProgressBarBuilder pbb = new ProgressBarBuilder();
		pbb.continuousUpdate();
		pbb.showSpeed();
		pbb.setTaskName("Updating database");
		pbb.setStyle(ProgressBarStyle.UNICODE_BLOCK);
		pbb.build();

		// Using batch execute for faster operations
		conn.setAutoCommit(false);
		int batchCounter = 1;

		for (DataRow dr : ProgressBar.wrap(lst, pbb)) {
			pstmt.setString(1, dr.getxRef());
			pstmt.setString(2, dr.getyRef());
			pstmt.setString(3, dr.getDate().format(dateTimeFormatter));
			pstmt.setInt(4, dr.getValue());
			pstmt.addBatch();
			batchCounter++;
			if (batchCounter % 1000 == 0)
				pstmt.executeBatch();
		}
		// Commit transaction and close connections
		conn.commit();
		pstmt.close();
		conn.close();
	}
	
	// Simple method to display all records in table
	public void getData() {
		String dml = "select * from percipitation";
		try {
			Connection conn = this.connect();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(dml);

			// loop through the result set
			System.out.println("\rXref" + "\t" + "Yref" + "\t" + "date" + "\t" + "value");
			while (rs.next()) {
				System.out.println(rs.getInt("Xref") + "\t" + rs.getInt("Yref") + "\t" + rs.getString("date") + "\t"
						+ rs.getInt("value"));
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
