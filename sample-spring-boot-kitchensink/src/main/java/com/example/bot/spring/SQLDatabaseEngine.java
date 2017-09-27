package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		Connection connection = getConnection();
		PreparedStatement statement = connection.prepareStatement(String.format("SELECT response FROM response_table WHERE keyword LIKE '%s';", text));
		ResultSet results = statement.executeQuery();
		if (results.next()) {
			return results.getString(1);
		}
		throw new Exception("NOT FOUND");
	}
	
	
	private Connection getConnection() throws URISyntaxException, SQLException {
		Connection connection;
		URI dbUri = new URI(System.getenv("DATABASE_URL"));
		//URI dbUri = new URI("postgres://ddhjqrnwembzkf:1050d5c1f6adf0bbc5e35a47e88cbda76cd3eae7ea04b4e87c6388391ab2f12a@ec2-54-75-228-125.eu-west-1.compute.amazonaws.com:5432/d3nchrekg5aous");

		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);

		connection = DriverManager.getConnection(dbUrl, username, password);
		return connection;
	}
}
