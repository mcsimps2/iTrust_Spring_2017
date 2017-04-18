package edu.ncsu.csc.itrust.controller.settings;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.old.dao.DAOFactory;
import edu.ncsu.csc.itrust.model.old.dao.mysql.AuthDAO;
import edu.ncsu.csc.itrust.model.user.ColorSchemeType;

@ManagedBean(name = "settings_controller")
@SessionScoped
public class SettingsController extends iTrustController {
	private AuthDAO authDAO;
	
	private String colorScheme;
	private long mid;
	
	public SettingsController() {
		super();
		
		this.authDAO = new AuthDAO(DAOFactory.getProductionInstance());
		this.mid = this.sessionUtils.getSessionLoggedInMIDLong();
		
		setColorScheme(authDAO.getColorScheme(mid).toString());
	}
	
	public static List<ColorSchemeType> getColorSchemes() {
		return Arrays.asList(ColorSchemeType.values());
	}
	
	public void save() {
		try {
			authDAO.setColorScheme(mid, ColorSchemeType.matchString(getColorScheme()));
			// TODO show a success message
		} catch (DBException e) {
			e.printStackTrace();
			// TODO show an error
		}
	}

	public String getColorScheme() {
		return colorScheme;
	}

	public void setColorScheme(String colorScheme) {
		this.colorScheme = colorScheme;
	}
}