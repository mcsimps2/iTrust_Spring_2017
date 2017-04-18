package edu.ncsu.csc.itrust.controller.settings;

import java.util.Arrays;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import edu.ncsu.csc.itrust.controller.iTrustController;
import edu.ncsu.csc.itrust.model.user.ColorSchemeType;

@ManagedBean(name = "settings_controller")
@SessionScoped
public class SettingsController extends iTrustController {
	public static List<ColorSchemeType> getColorSchemes() {
		return Arrays.asList(ColorSchemeType.values());
	}
	
	public void save() {
		// TODO save settings
	}
}