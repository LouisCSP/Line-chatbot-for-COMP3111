package com.example.bot.spring;
import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.util.logging.Logger;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;


@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	
	@Override
	String search(String text) throws Exception {
		//return null;
	   
		String result = null;
		BufferedReader br = null;
		InputStreamReader isr = null;
		Connection connection = getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT keyword,response from lab3");
		ResultSet rs = stmt.executeQuery();
		//String sCurrentLine;
		String sinput;
		String soutput;
		try {
			while (result == null && rs.next()) {
				sinput  = rs.getString(1);
				soutput = rs.getString(2); 
				//String[] parts = sCurrentLine.split(":");
				if (text.toLowerCase().equals(sinput.toLowerCase())) {
					result = soutput;
				}
			}

		} finally {
			try {
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if(connection!=null)
					connection.close();
				if (br != null)
					br.close();
				if (isr != null)
					isr.close();
			} catch (IOException ex) {
				log.info("IOException while closing file: {}", ex.toString());
			}
		}
		if (result != null) {
			return result;
		}
		throw new Exception("NOT FOUND");
    
    }
	
	
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);
		
		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}

}

//package com.example.bot.spring;
//
//import lombok.extern.slf4j.Slf4j;
//import javax.annotation.PostConstruct;
//import javax.sql.DataSource;
//import java.sql.*;
//import java.net.URISyntaxException;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.URI;

//@Slf4j
//public class SQLDatabaseEngine extends DatabaseEngine {
//	@Override
//	String search(String text) throws Exception {
//		//Write your code here
//
//		String result = null;
//		BufferedReader br = null;
//		InputStreamReader isr = null;
//		String keyword = null;
//		String response = null;
//
//		Connection connection = getConnection();
//		PreparedStatement stmt = connection.prepareStatement("SELECT keyword, response FROM lab3");
//
//		ResultSet rs = stmt.executeQuery();
//
//		try {
//			while (result == null && rs.next()) {
//				keyword = rs.getString(1);
//				response = rs.getString(2); 
//
//				if (text.toLowerCase().equals(keyword.toLowerCase())) {
//					result = response;
//				}
//			}
//		} finally {
//			try {
//				if(rs!=null)
//					rs.close();
//				if(stmt!=null)
//					stmt.close();
//				if(connection!=null)
//					connection.close();
//				if (br != null)
//					br.close();
//				if (isr != null)
//					isr.close();
//			} catch (IOException ex) {
//				log.info("IOException while closing file: {}", ex.toString());
//			}
//		}
//		if (result != null) {
//			return result;
//		}
//		throw new Exception("NOT FOUND");
//	}
//
///*		
//		SQLDatabaseEngine sqlDbEngine = new SQLDatabaseEngine();
//		Connection connection = null;
//		PreparedStatement stmt = null;
//		ResultSet rs = null;
////		String keyword = null;
//		String result = null;
//		try {
//			connection = sqlDbEngine.getConnection();
//		
//			stmt = connection.prepareStatement(
//					"SELECT keyword, response FROM lab3 WHERE keyword like concat ('%', ?, '%')");
//			stmt.setString(1, text);	//1 --> first parameter, that is related to the order of ? of the above statement
//			
//			rs = stmt.executeQuery();
//			result = rs.getString(2);
//			
////			while ((result == null)&&(rs.next()))
////			{
////				result = rs.getString(2);
////				keyword = rs.getString(1);
////				if (text.equals(keyword))
////				{
////					result = rs.getString(2);
//////				}
////				//System.out.println("Keyword: " + rs.getString(1) + "\tResponse: " + rs.getString(2));
////			}
//		} catch (URISyntaxException e) {
//			log.info("URISyntaxException while reading file: {}", e.toString());
//		} finally {
//			try {
//				if (rs != null)
//					rs.close();
//				if (stmt != null)
//					stmt.close();
//				if (connection != null)
//					connection.close();
//			} catch (SQLException ex) {
//				log.info("SQLException while reading file: {}", ex.toString());
//			}
//		}
//		if (result != null)
//			return result;
//		
//		throw new Exception("NOT FOUND");
//		}
//    
//
//	
//
//	
////	PreparedStatement[] stmt = new PerparedStatement[5]();
////	for (int i = 0; i<5; i++)
////	{
////		stmt[i] = connection.prepareStatement(
////				"SELECT keyword, response FROM lab3 WHERE keyword like concat ('%', ?, '%')");
////	}
//	
//	
//		
//
////	private final String FILENAME = "/static/database.txt";
//*/	
//	
//	private Connection getConnection() throws URISyntaxException, SQLException {
//		Connection connection;
//		URI dbUri = new URI(System.getenv("DATABASE_URL"));
//
//		String username = dbUri.getUserInfo().split(":")[0];
//		String password = dbUri.getUserInfo().split(":")[1];
//		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";
//
//		log.info("Username: {} Password: {}", username, password);
//		log.info ("dbUrl: {}", dbUrl);
//		
//		connection = DriverManager.getConnection(dbUrl, username, password);
//
//		return connection;
//	}
//
//}
