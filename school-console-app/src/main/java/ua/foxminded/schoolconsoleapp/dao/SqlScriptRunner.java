package ua.foxminded.schoolconsoleapp.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import org.apache.ibatis.jdbc.ScriptRunner;
import ua.foxminded.schoolconsoleapp.exception.DaosException;

public class SqlScriptRunner {
    private Connection connection;

    public SqlScriptRunner(Connection connection) {
	this.connection = connection;
    }

    public void runSqlScript(String scriptFilename) throws DaosException {
	ScriptRunner scriptRunner = new ScriptRunner(connection);
	scriptRunner.setLogWriter(null);
	try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(scriptFilename);
		InputStreamReader reader = new InputStreamReader(inputStream);) {
	    scriptRunner.runScript(reader);
	} catch (IOException | NullPointerException e) {
	    throw new DaosException(String.format("Script %s not found!", scriptFilename), e);
	}
    }
}
