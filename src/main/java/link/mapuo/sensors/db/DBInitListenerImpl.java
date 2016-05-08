package link.mapuo.sensors.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class DBInitListenerImpl implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		DataSource ds = getDataSource();
		try (Connection conn = ds.getConnection()) {
			dropTables(conn);
			createTemperatureTable(conn);
			createHumidityTable(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		DataSource ds = getDataSource();
		try (Connection conn = ds.getConnection()) {
			dropTables(conn);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private DataSource getDataSource() {
		try {
			return (DataSource) new InitialContext().lookup("java:comp/env/jdbc/SensorsDB");
		} catch (NamingException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void createHumidityTable(Connection conn) {
		String createString = "CREATE TABLE HUMIDITY_TABLE (ID UUID PRIMARY KEY, NAME VARCHAR(255), LOCATION VARCHAR(255), TEMPERATURE TINYINT, HUMIDITY TINYINT);";

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createString);
		} catch (SQLException e) {
		}
	}

	private void createTemperatureTable(Connection conn) {
		String createString = "CREATE TABLE TEMPERATURE_TABLE (ID UUID PRIMARY KEY, NAME VARCHAR(255), LOCATION VARCHAR(255), TEMPERATURE TINYINT);";

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(createString);
		} catch (SQLException e) {
		}
	}

	private void dropTables(Connection conn) {
		String dropString = "DROP TABLE IF EXISTS TEMPERATURE; DROP TABLE IF EXISTS HUMIDITY;";

		try (Statement stmt = conn.createStatement()) {
			stmt.execute(dropString);
		} catch (SQLException e) {
		}
	}

}
