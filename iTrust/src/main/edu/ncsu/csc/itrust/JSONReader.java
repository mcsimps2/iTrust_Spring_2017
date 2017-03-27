package edu.ncsu.csc.itrust;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONObject;

/**
 * Taken (slightly modified) from the Maven artifact org.json:json
 * as well as the stackoverflow question
 * http://stackoverflow.com/questions/4308554/simplest-way-to-read-json-from-a-url-in-java
 * Author: Roland Illig
 */
public class JSONReader
{
	/**
	 * Reads JSON from a URL and converts it to a JSONObject
	 * @param url the url to read from
	 * @return a JSONObject representing what was read on the URL
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static JSONObject readFromURL(String url) throws MalformedURLException, IOException
	{
		InputStream is = new URL(url).openStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
		StringBuilder sb = new StringBuilder();
		int c;
		while ((c = br.read()) != -1)
		{
			sb.append((char) c);
		}
		is.close();
		return new JSONObject(sb.toString());
	}
}
