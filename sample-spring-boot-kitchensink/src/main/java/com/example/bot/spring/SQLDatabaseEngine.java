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
	   
		String result = null;
		Connection connection = null;
		PreparedStatement stmt = null;
		String keyword;
		String response;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
		}
		catch (URISyntaxException e1) {
			log.info("URISyntaxException while closing file: {}", e1.toString());
		}
		catch (SQLException e2) {
			log.info("URISyntaxException while closing file: {}", e2.toString());
		}
		
		try {
			stmt = connection.prepareStatement("SELECT keyword, response FROM lab3 WHERE keyword like concat ('%', ?, '%')");
			//"SELECT keyword,response from lab3" ; "SELECT keyword, response FROM lab3 WHERE keyword like concat ('%', ?, '%')"

			stmt.setString(1, text);
			rs = stmt.executeQuery();

			while (result == null && rs.next()) {
				keyword  = rs.getString(1);
				response = rs.getString(2); 
				if (text.toLowerCase().equals(keyword.toLowerCase())) {
					result = response;
				}
			}
		}
		catch (SQLException e3) {
			log.info("SQLException while closing file: {}", e3.toString());
		}
		
		finally {
			try {
				if(rs!=null)
					rs.close();
				if(stmt!=null)
					stmt.close();
				if(connection!=null)
					connection.close();
			}
			catch (SQLException e4) {
				log.info("SQLException while closing file: {}", e4.toString());
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