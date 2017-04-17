package edu.ncsu.csc.itrust.server;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.exception.DBException;

@MultipartConfig
public class UploadServlet extends HttpServlet{

	private static final long serialVersionUID = 1L;

	private static final String SQL_FIND = "SELECT content FROM image WHERE name = ?";
	private static final String SQL_ADD = "INSERT INTO image(content, name) VALUES(?, ?)";
	private static final String SQL_UPDATE = "UPDATE image SET content = ? WHERE name = ?";
	
	private DataSource ds;
	
	public UploadServlet() throws DBException {
		super();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	public UploadServlet(DataSource ds) {
		super();
		this.ds = ds;
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {		
	    Part filePart = request.getPart("file");
	    String imageName = "patient" + request.getParameter("pid");
	    InputStream imageContent = filePart.getInputStream();

	    try (Connection connection = ds.getConnection(); PreparedStatement statement = connection.prepareStatement(SQL_FIND)) {
            statement.setString(1, imageName);
            
            try (ResultSet resultSet = statement.executeQuery()) {
            	String query;
                if (resultSet.next()) {
                	query = SQL_UPDATE;
                } else {
                	query = SQL_ADD;
                }
                
                try (PreparedStatement statement2 = connection.prepareStatement(query)) {
                	statement2.setBinaryStream(1, imageContent);
                	statement2.setString(2, imageName);
                	statement2.executeQuery();
                }
                
            }
            
        } catch (SQLException e) {
            throw new ServletException("Something failed at SQL/DB level.", e);
        }
	}

}
