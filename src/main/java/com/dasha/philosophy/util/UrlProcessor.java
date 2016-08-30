package com.dasha.philosophy.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.IOUtils;

public class UrlProcessor {
	
	/**
	 * Convert incoming string to a URL.
     * Print an error message if the string turns out to be an invalid URL and return null.
	 * @param incomingString
	 * @return String
	 */
    public static URL stringToURL(String incomingString) {
        try {
        	return new URL(incomingString);
        } catch (MalformedURLException e) {
            System.err.println("Produced invalid URL based on string provided: " + incomingString + ": " + e);
            return null;
        }
    }

    /**
     * Connect to the specified URL and process the response body.
     * Print an error message if cannot establish connection and return null.
     * Also responsible for closing the stream being processed.
     * @param incomingUrl
     * @return
     */
    public static String getUrlBody(URL incomingUrl) {
        try {
            final URLConnection connection = incomingUrl.openConnection();
            final InputStreamReader inputStream = new InputStreamReader(connection.getInputStream());
            try {
                return processInputStream(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            System.err.println("Cannot open provided URL: " + incomingUrl + ": " + e);
            return null;
        }
    }

	/**
	 * Return the content of the incoming stream that is being read as a String.
	 * @param incomingStream
	 * @return
	 * @throws IOException
	 */
    private static String processInputStream(InputStreamReader incomingStream) throws IOException {
    	StringWriter writer = new StringWriter();
    	IOUtils.copy(incomingStream, writer);
    	return writer.toString();
    }
}
