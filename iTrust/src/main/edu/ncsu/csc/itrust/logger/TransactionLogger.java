package edu.ncsu.csc.itrust.logger;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.TransactionDAO;
import edu.ncsu.csc.itrust.model.old.enums.TransactionType;
import edu.ncsu.csc.itrust.unit.testutils.TestDAOFactory;

/**
 * Singleton provider of the transaction logging mechanism.
 * 
 * @author mwreesjo
 *
 */
public class TransactionLogger {
	/** Whether or not the transaction logger is being used in testing or production */
	private static boolean testInstance = false;
	/** The singleton instance of this class. */
	private static TransactionLogger singleton = null;

	/** The DAO which exposes logging functionality to the singleton */
	TransactionDAO dao;

	private TransactionLogger() {
		dao = DAOFactory.getProductionInstance().getTransactionDAO();
		testInstance = false;
	}
	
	private void TestTransactionLogger() {
		dao = TestDAOFactory.getTestInstance().getTransactionDAO();
		testInstance = true;
	}
	
	private void ProductionTransactionLogger() {
		dao = TestDAOFactory.getTestInstance().getTransactionDAO();
		testInstance = false;
	}

	/**
	 * @return Singleton instance of this transaction logging mechanism.
	 */
	public static synchronized TransactionLogger getInstance() {
		if (singleton == null)
			singleton = new TransactionLogger();
		return singleton;
	}

	/**
	 * Logs a transaction. @see {@link TransactionDAO#logTransaction}
	 */
	public void logTransaction(TransactionType type, Long loggedInMID, Long secondaryMID, String addedInfo) {
		try {
			dao.logTransaction(type, loggedInMID, secondaryMID, addedInfo);
		} catch (DBException e1) {
			//Try the other version
			try
			{
				if (testInstance)
				{
					TestTransactionLogger();
					dao.logTransaction(type, loggedInMID, secondaryMID, addedInfo);
				}
				else
				{
					ProductionTransactionLogger();
					dao.logTransaction(type, loggedInMID, secondaryMID, addedInfo);
				}
			}
			catch (DBException e2)
			{
				e2.printStackTrace();
			}
		}
	}
}