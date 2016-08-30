package com.dasha.philosophy.service;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.dasha.philosophy.util.UrlProcessor;
import com.dasha.philosophy.util.Validator;

public class PhilosophyFindingService {
	
	private Map<String, Integer> previouslyEncounteredStartingUrls = new HashMap<String, Integer>();
	private Set<String> linksEncounteredOnThisTraversal = new HashSet<String>();
	private Integer clickCounter = 0;
	private static String wikiPrefixHttp = "http://en.wikipedia.org";
	private static String wikiPrefixHttps = "https://en.wikipedia.org";

	private static String philosophyUrlHttp = wikiPrefixHttp + "/wiki/philosophy";
	private static String philosophyUrlHttps = wikiPrefixHttps + "/wiki/philosophy";

	/**
	 * Main function call that works on determining the number of clicks required to get to
	 * the Philosophy page from any random page on the Wiki
	 * @param inputUrl (String) used a starting URL for lookup
	 * @return clicksUsed (Integer) to get to the Philosophy page
	 * @throws IOException
	 */
	public Integer determineClicksToPhilosophyWikiPage (String inputUrl) throws IOException {
		System.out.println("Looking for a path to Philosophy Wiki page from " + inputUrl + " ... ");
    	Integer clicksUsed = null;
    	//Check the cached collection of starting URLs to see if we've already gone down this path
    	if (checkIfAlreadyEncounteredUrl(inputUrl)) {
    		clicksUsed = previouslyEncounteredStartingUrls.get(inputUrl);
			System.out.println("Encountered a previously processed URL. "
					+ "Returning the number of clicks to Philosophy Wiki: " + clicksUsed);
		} else {
			clicksUsed = countClicksToPhilosophyPage(inputUrl);
			if (clicksUsed != null) {
                System.out.println("Reached Philosophy Wiki page after " + clicksUsed + " clicks.");
                previouslyEncounteredStartingUrls.put(inputUrl, clicksUsed);
            }
		} 
        return clicksUsed;
    }
    
    private boolean checkIfAlreadyEncounteredUrl(String urlToCheck) {
    	return previouslyEncounteredStartingUrls.containsKey(urlToCheck);
    }

	private Integer countClicksToPhilosophyPage(String incomingUrl) throws IOException {
        //Reset the click counter and exit the loop once the Philosophy page is found
        if (incomingUrl.equalsIgnoreCase(philosophyUrlHttp) || incomingUrl.equalsIgnoreCase(philosophyUrlHttps)) {
        	Integer clicksUsed = clickCounter;
            clickCounter = 0;
            linksEncounteredOnThisTraversal = new HashSet<String>();
            return clicksUsed;
        //Check if the link being processed has already been encountered in this traversal
        } else if (checkIfAlreadyEncounteredLinkOnThisTraversal(incomingUrl)) {
        	clickCounter = 0;
        	System.err.println("Attempting to click through the page already encountered. Suspecting inifnite loop. Exiting.");
        	return null;
        //If more than 100 links have been tried then we're probably stuck at this point
        } else if (clickCounter > 100) {
            clickCounter = 0;
            System.err.println("Too many clicks attempted, unable to reach Philosophy Wiki page.");
            return null;
        //Continue to the next URL in search of the Philosophy page
        } else {
            return handleNewLink(incomingUrl);
        }
    }

	private boolean checkIfAlreadyEncounteredLinkOnThisTraversal(String linkToCheck) {
    	return linksEncounteredOnThisTraversal.contains(linkToCheck);
    }
	
	private Integer handleNewLink(String incomingUrl) throws IOException {
		//Do some cache-keeping, update counter and try the next link in the chain
		linksEncounteredOnThisTraversal.add(incomingUrl);
		String nextLink = gotoNextLink(incomingUrl);
		printLinkTitle(nextLink);
		clickCounter++;
		return countClicksToPhilosophyPage(nextLink);
	}

    private String gotoNextLink(String urlToProcess) throws IOException {
        String nextLink = "";

        URL url = UrlProcessor.stringToURL(urlToProcess); 
        //Attempt to parse the content of Wiki page
        Document doc = Jsoup.parse(url, 100000);
        
        //Find links with "<a href" that are direct children of <p> tags
        Elements links = doc.select("p > a[href]");

        //Use the first valid link found
        for (int i = 0; i < links.size(); i++) {
        	String linkProcessing = links.get(i).toString();
            if (Validator.isFirstValidLink(linkProcessing)) {
                nextLink = linkProcessing;
                break;
            }
        }
        //Get the portion of the link of containing "/wiki/<some_sort_of_string>" 
        //and prepend the traditional Wiki starting URL pattern
        return wikiPrefixHttp + nextLink.substring(9, nextLink.indexOf("\"", 10));
    }
    
    private void printLinkTitle(String linkToProcess) {
        //Output the URL title in the console based on the standard Wiki URL pattern
        System.out.println(" --> "+ linkToProcess);
    }
}
