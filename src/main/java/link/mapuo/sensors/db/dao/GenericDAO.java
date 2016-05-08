package link.mapuo.sensors.db.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public abstract class GenericDAO<T> {

	public abstract int count() throws SQLException;
	public abstract T create(T t) throws SQLException;
	public abstract List<T> findAll() throws SQLException;
	public abstract T find_by_id(UUID uuid) throws SQLException;
	public abstract T update(T t) throws SQLException;
	public abstract boolean delete(UUID uuid) throws SQLException;

	// Protected
	protected final String tableName;

	protected Connection con;

	protected GenericDAO(Connection con, String tableName) {
		this.tableName = tableName;
		this.con = con;
	}

}
