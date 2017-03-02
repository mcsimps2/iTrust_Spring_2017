package edu.ncsu.csc.itrust.cucumber.util;

import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.util.NameValuePair;

/**
 * Driver object to help with page caching
 * @author Nicholas Anthony
 *
 */
public class MyDriver extends HtmlUnitDriver {

	/**
	 * Constructor
	 */
	public MyDriver() {
		super();
	}
	
	/**
	 * Saves the current page
	 * @return the current cached page
	 */
	public Page savePage() {
		return super.getWebClient().getCurrentWindow().getEnclosedPage();
	}
	
	/**
	 * Returns the page
	 * @param page the cached page
	 */
	public void getSavedPage(Page page) {
		super.getWebClient().getCurrentWindow().setEnclosedPage(page);
	}
	
	/**
	 * Returns whether or not caching is enabled in the response headers
	 * @return boolean indicating whether or not caching is enabled
	 */
	public boolean cachingEnabled() {
		for (NameValuePair nvp : super.getWebClient().getCurrentWindow().getEnclosedPage().getWebResponse().getResponseHeaders()) {
			if (nvp.getName().equals("Cache-Control") && nvp.getValue().equals("no-cache, max-age=0, must-revalidate, no-store"))
				return false;
		}
		return true;
	}
	
}
