package edu.ncsu.csc.itrust.cucumber.util;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;


//Based heavily on the SharedDriver example found at https://github.com/cucumber/cucumber-jvm/blob/master/examples/java-webbit-websockets-selenium/src/test/java/cucumber/examples/java/websockets/SharedDriver.java

public class iTrustDriver extends EventFiringWebDriver {
	private static WebDriver REAL_DRIVER = new HtmlUnitDriver();
	private static final String BASE_URL = "http://localhost:8080";	
	private static final Thread CLOSE_THREAD = new Thread() {
		@Override
		public void run() {
			REAL_DRIVER.close();
		}
	};
	
	static {
		Runtime.getRuntime().addShutdownHook(CLOSE_THREAD);
		//((HtmlUnitDriver) REAL_DRIVER).setJavascriptEnabled(true);
	}

	public iTrustDriver() {
		super(REAL_DRIVER);
	}

	@Override
	public void close() {
		if (Thread.currentThread() != CLOSE_THREAD) {
			throw new UnsupportedOperationException("You shouldn't close this WebDriver. It's shared and will close when the JVM exits.");
		}
		super.close();
	}

	public void loadPage(String url){
		REAL_DRIVER.get(BASE_URL+url);
	}
	

	public boolean verifyLocation(String url){
		if(REAL_DRIVER.getCurrentUrl().equalsIgnoreCase(BASE_URL+url)){
			return true;
		}
		else{
			return false;
		}
	}

}
