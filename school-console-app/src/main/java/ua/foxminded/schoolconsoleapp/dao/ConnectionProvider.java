package ua.foxminded.schoolconsoleapp.dao;

import java.sql.Connection;
import java.sql.SQLException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class ConnectionProvider {
    private final HikariDataSource hikariDataSource;

    public ConnectionProvider(String fileConfig) {
	HikariConfig hikariConfig = new HikariConfig(fileConfig);
	this.hikariDataSource = new HikariDataSource(hikariConfig);
    }

    public Connection getConnection() throws SQLException {
	return hikariDataSource.getConnection();
    }
}
