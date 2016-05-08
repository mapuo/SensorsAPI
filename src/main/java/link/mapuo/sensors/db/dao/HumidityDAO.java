package link.mapuo.sensors.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import link.mapuo.sensors.model.Humidity;

public class HumidityDAO extends GenericDAO<Humidity> {
	private static final String HUMIDITY_TABLE = "HUMIDITY_TABLE";

	public HumidityDAO(Connection con) {
		super(con, HUMIDITY_TABLE);
	}

	@Override
	public int count() throws SQLException {
		String query = "SELECT COUNT(*) AS count FROM " + tableName;
		PreparedStatement counter = con.prepareStatement(query);
		ResultSet res = counter.executeQuery();
		res.next();
		return res.getInt("count");
	}

	@Override
	public Humidity create(Humidity t) throws SQLException {
		String sql = "INSERT INTO " + tableName + " VALUES(?, ?, ?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(sql);
		insertStmt.setObject(1, t.getUuid());
		insertStmt.setString(2, t.getName());
		insertStmt.setString(3, t.getLocation());
		insertStmt.setInt(4, t.getTemperature());
		insertStmt.setInt(5, t.getHumidity());
		insertStmt.execute();
		return find_by_id(t.getUuid());	}

	@Override
	public List<Humidity> findAll() throws SQLException {
		String sql = "select * from " + tableName;
		PreparedStatement whereStmt = con.prepareStatement(sql);
		ResultSet rs = whereStmt.executeQuery();
		List<Humidity> list = Lists.newArrayList();
		while (rs.next()) {
			Humidity h = extractHmdt(rs);
			list.add(h);
		}
		return list;
	}

	@Override
	public Humidity find_by_id(UUID uuid) throws SQLException {
		String sql = "select * from " + tableName + " where ID = ?";
		PreparedStatement whereStmt = con.prepareStatement(sql);
		whereStmt.setObject(1, uuid);
		ResultSet rs = whereStmt.executeQuery();
		if (rs.next()) {
			Humidity h = extractHmdt(rs);
			return h;
		}
		return null;
	}

	@Override
	public Humidity update(Humidity t) throws SQLException {
		String sql = "update " + tableName + " set NAME = ?, LOCATION = ?, TEMPERATURE = ?, HUMIDITY = ? where ID = ?";
		PreparedStatement updateStmt = con.prepareStatement(sql);
		updateStmt.setString(1, t.getName());
		updateStmt.setString(2, t.getLocation());
		updateStmt.setInt(3, t.getTemperature());
		updateStmt.setInt(4, t.getHumidity());
		updateStmt.setObject(5, t.getUuid());
		updateStmt.execute();
		return find_by_id(t.getUuid());
	}

	@Override
	public boolean delete(UUID uuid) throws SQLException {
		String sql = "delete from " + tableName + " where ID = ?";
		PreparedStatement deleteStmt = con.prepareStatement(sql);
		deleteStmt.setObject(1, uuid);
		return deleteStmt.execute();
	}
	
	private Humidity extractHmdt(ResultSet rs) throws SQLException {
		Humidity humidity = new Humidity((UUID) rs.getObject(1), rs.getInt(4), rs.getInt(5));
		humidity.setName(rs.getString(2));
		humidity.setLocation(rs.getString(3));
		return humidity;
	}

}
