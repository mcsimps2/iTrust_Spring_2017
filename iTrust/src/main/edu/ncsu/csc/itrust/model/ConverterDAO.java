package edu.ncsu.csc.itrust.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

/**
 * This class pulls the JDBC driver information from Tomcat's context.xml file
 * in WebRoot/META-INF/context.xml. This is done only for convenience - so that
 * you only have to pull your JDBC info from one place (context.xml)<br />
 * <br />
 * The tangled mess you see here is SAX, the XML-parser and XPath, an XML
 */
public class ConverterDAO {
	public static BasicDataSource ds = null;

	private static String getAttribute(Document document, String attribute) throws XPathExpressionException {
		return (String) XPathFactory.newInstance().newXPath().compile("/Context/Resource/" + attribute)
				.evaluate(document.getDocumentElement(), XPathConstants.STRING);
	}

	private static Document parseXML(BufferedReader reader) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(false);
		DocumentBuilder builder = factory.newDocumentBuilder();
		return builder.parse(new InputSource(reader));
	}

	/**
	 * Releases all idle connections of the pre-existing ConverterDAO datasource and returns a new one
	 * @return a new datasource
	 */
	public static synchronized DataSource getDataSource() {
		if (ds != null)
		{
			try
			{
				ds.close();
			}
			catch (SQLException e)
			{
				//Do nothing
			}
		}
		FileReader f = null;
		BufferedReader r = null;
		try {
			f = new FileReader("WebRoot/META-INF/context.xml");
			r = new BufferedReader(f);
			Document document = parseXML(r);
			ds = new BasicDataSource();
			ds.setDriverClassName(getAttribute(document, "@driverClassName"));
			ds.setUsername(getAttribute(document, "@username"));
			ds.setPassword(getAttribute(document, "@password"));
			ds.setUrl(getAttribute(document, "@url"));
			ds.setMaxTotal(15);
			ds.setMaxIdle(4);
			ds.setTestOnBorrow(true);
			ds.setRemoveAbandonedOnBorrow(true);
			ds.setRemoveAbandonedOnMaintenance(true);
			ds.setRemoveAbandonedTimeout(60);
			ds.setPoolPreparedStatements(true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			try{
				r.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					f.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return ds;
	}
	
}
