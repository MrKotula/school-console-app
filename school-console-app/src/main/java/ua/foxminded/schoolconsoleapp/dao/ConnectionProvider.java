package ua.foxminded.schoolconsoleapp.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import ua.foxminded.schoolconsoleapp.reader.Reader;

public class ConnectionProvider {
    private static final String FILENAME_DB_PROPERTIES = "db.properties";
    private static final String DB_DRIVER = "db.driver";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_LOGIN = "db.login";
    private static final String DB_URL = "db.url";
    private static final String MESSAGE_DRIVER_NOT_FOUND = "Driver not found";
    private static final String MESSAGE_CONNECTION_ERROR = "Connection failed";

    private String url;
    private String login;
    private String password;
    private Reader reader = new Reader();
    
    public ConnectionProvider() {
	Properties properties = reader.readProperties(FILENAME_DB_PROPERTIES);
	url = properties.getProperty(DB_URL);
	login = properties.getProperty(DB_LOGIN);
	password = properties.getProperty(DB_PASSWORD);
	try {
	    Class.forName(properties.getProperty(DB_DRIVER));
	} catch (ClassNotFoundException e) {
	    System.out.println(MESSAGE_DRIVER_NOT_FOUND);
	    System.out.println(MESSAGE_CONNECTION_ERROR);
	    e.printStackTrace();
	}
    }

    public Connection getConnection() throws SQLException {
	return DriverManager.getConnection(url, login, password);
    }
}
