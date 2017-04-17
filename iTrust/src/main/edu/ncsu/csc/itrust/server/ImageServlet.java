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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import edu.ncsu.csc.itrust.exception.DBException;

public class ImageServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private static final String SQL_FIND = "SELECT content FROM image WHERE name = ?";
	
	private DataSource ds;
	
	public ImageServlet() throws DBException {
		super();
		try {
			Context ctx = new InitialContext();
			this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: " + e.getMessage()));
		}
	}
	
	public ImageServlet(DataSource ds) {
		super();
		this.ds = ds;
	}
	
	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String imageName = "patient" + request.getParameter("pid");

        try (Connection connection = ds.getConnection(); PreparedStatement statement = connection.prepareStatement(SQL_FIND)) {
            statement.setString(1, imageName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                	InputStream i = resultSet.getBinaryStream("content");
                	byte[] content = IOUtils.toByteArray(i);
                    response.setContentType(getServletContext().getMimeType(imageName));
                    response.setContentLength(content.length);
                    response.getOutputStream().write(content);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404
                }
            }
        } catch (SQLException e) {
            throw new ServletException("Something failed at SQL/DB level.", e);
        }
    }
}
