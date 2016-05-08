package link.mapuo.sensors.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

import com.google.common.collect.Lists;

import link.mapuo.sensors.model.Temperature;

public class TemperatureDAO extends GenericDAO<Temperature> {
	private static final String TEMPERATURE_TABLE = "TEMPERATURE_TABLE";

	public TemperatureDAO(Connection con) {
		super(con, TEMPERATURE_TABLE);
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
	public Temperature create(Temperature t) throws SQLException {
		String sql = "INSERT INTO " + tableName + " VALUES(?, ?, ?, ?)";
		PreparedStatement insertStmt = con.prepareStatement(sql);
		insertStmt.setObject(1, t.getUuid());
		insertStmt.setString(2, t.getName());
		insertStmt.setString(3, t.getLocation());
		insertStmt.setInt(4, t.getTemperature());
		insertStmt.execute();
		return find_by_id(t.getUuid());
	}

	@Override
	public List<Temperature> findAll() throws SQLException {
		String sql = "select * from " + tableName;
		PreparedStatement whereStmt = con.prepareStatement(sql);
		ResultSet rs = whereStmt.executeQuery();
		List<Temperature> list = Lists.newArrayList();
		while (rs.next()) {
			Temperature t = extractTemp(rs);
			list.add(t);
		}
		return list;
	}

	@Override
	public Temperature find_by_id(UUID uuid) throws SQLException {
		String sql = "select * from " + tableName + " where ID = ?";
		PreparedStatement whereStmt = con.prepareStatement(sql);
		whereStmt.setObject(1, uuid);
		ResultSet rs = whereStmt.executeQuery();
		if (rs.next()) {
			Temperature t = extractTemp(rs);
			return t;
		}
		return null;
	}

	@Override
	public Temperature update(Temperature t) throws SQLException {
		String sql = "update " + tableName + " set NAME = ?, LOCATION = ?, TEMPERATURE = ? where ID = ?";
		PreparedStatement updateStmt = con.prepareStatement(sql);
		updateStmt.setString(1, t.getName());
		updateStmt.setString(2, t.getLocation());
		updateStmt.setInt(3, t.getTemperature());
		updateStmt.setObject(4, t.getUuid());
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

	private Temperature extractTemp(ResultSet rs) throws SQLException {
		Temperature t = new Temperature((UUID) rs.getObject(1), rs.getInt(4));
		t.setName(rs.getString(2));
		t.setLocation(rs.getString(3));
		return t;
	}
}
