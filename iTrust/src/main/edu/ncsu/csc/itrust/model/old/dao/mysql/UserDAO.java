package edu.ncsu.csc.itrust.model.old.dao.mysql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.ITrustException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;

/**
 * Used for managing all static information related to a patient. For other
 * information related to all aspects of patient care, see the other DAOs.
 * 
 * DAO stands for Database Access Object. All DAOs are intended to be
 * reflections of the database, that is, one DAO per table in the database (most
 * of the time). For more complex sets of queries, extra DAOs are added. DAOs
 * can assume that all data has been validated and is correct.
 * 
 * DAOs should never have setters or any other parameter to the constructor than
 * a factory. All DAOs should be accessed by DAOFactory (@see
 * {@link DAOFactory}) and every DAO should have a factory - for obtaining JDBC
 * connections and/or accessing other DAOs.
 */
public class UserDAO {
	private DAOFactory factory;

	/**
	 * The typical constructor.
	 * 
	 * @param factory
	 *            The {@link DAOFactory} associated with this DAO, which is used
	 *            for obtaining SQL connections, etc.
	 */
	public UserDAO(DAOFactory factory) {
		this.factory = factory;
	}

	/**
	 * Returns the name from the specified database table for the given MID
	 * 
	 * @param mid
	 *            The MID of the patient in question.
	 * @param table
	 *            The name of the database table to look in
	 * @return A String representing the patient's first name and last name.
	 * @throws ITrustException
	 * @throws DBException
	 */
	public String getName(long mid, String table) throws ITrustException, DBException {
		try (Connection conn = factory.getConnection();
				PreparedStatement ps = conn.prepareStatement(String.format("SELECT firstName, lastName FROM %s WHERE MID=?", table))) {
			ps.setLong(1, mid);
			ResultSet rs;
			rs = ps.executeQuery();
			if (rs.next()) {
				String result = rs.getString("firstName") + " " + rs.getString("lastName");
				rs.close();
				return result;
			} else {
				rs.close();
				throw new ITrustException("User does not exist");
			}
		} catch (SQLException e) {
			throw new DBException(e);
		}
	}
}
