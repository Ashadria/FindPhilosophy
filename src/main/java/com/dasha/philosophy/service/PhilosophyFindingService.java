package com.dasha.philosophy.service;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dasha.philosophy.model.ClickData;
import com.dasha.philosophy.util.UrlProcessor;
import com.dasha.philosophy.util.Validator;

@Service("philosophyFindingService")
public class PhilosophyFindingService {
	
	@Autowired
	private ClickDataService clickDataService;
	
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
    	if (clickDataService.checkIfAlreadyHaveClickDataForUrl(inputUrl)) {
    		clicksUsed = clickDataService.fetchClickDataForUrl(inputUrl);
			System.out.println("Encountered a previously processed URL. "
					+ "Returning the number of clicks to Philosophy Wiki: " + clicksUsed);
		} else {
			clicksUsed = countClicksToPhilosophyPage(inputUrl);
			if (clicksUsed != null) {
                System.out.println("Reached Philosophy Wiki page after " + clicksUsed + " clicks.");
                //Save off the data for this particular traversal for future use
                clickDataService.saveClickDataInDatabase(new ClickData(inputUrl, clicksUsed));
            }
		} 
        return clicksUsed;
    }

	private Integer countClicksToPhilosophyPage(String incomingUrl) throws IOException {
        //Reset the click counter and exit the loop once the Philosophy page is found
        if (incomingUrl.equalsIgnoreCase(philosophyUrlHttp) || incomingUrl.equalsIgnoreCase(philosophyUrlHttps)) {
        	Integer clicksUsed = clickCounter;
        	//Ignore the case where we start with the Philosophy page since we can still return to it
        	if (clicksUsed == 0) {
        		return handleNewLink(incomingUrl);
    		//It's not the first page we're dealt with, so we've got a valid click path
        	} else {
                clickCounter = 0;
                linksEncounteredOnThisTraversal = new HashSet<String>();
                return clicksUsed;
        	}
        //Check if the link being processed has already been encountered in this traversal
        } else if (checkIfAlreadyEncounteredLinkOnThisTraversal(incomingUrl)) {
        	clickCounter = 0;
        	System.err.println("Attempting to click through the page already encountered : " + incomingUrl
        			+ " Suspecting inifnite loop. Exiting.");
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
		if (nextLink != null) {
			printLinkTitle(nextLink);
			clickCounter++;
			return countClicksToPhilosophyPage(nextLink);
		} else {
			return null;
		}
	}

    private String gotoNextLink(String urlToProcess) throws IOException {
        String nextLink = "";

    	try {
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
        //Handling this in case we cannot find the page specified so that the entire app doesn't crash
    	} catch (HttpStatusException e) {
    		System.err.println("Error occured when trying to process the url response: " + urlToProcess
    				+ e.getMessage());
        	return null;
    	}
        
        //If we ended up with at least 1 valid link from the url we're processing,
        //we can get the portion of the link containing "/wiki/<some_sort_of_string>" 
        //and prepend the traditional Wiki starting URL pattern
        if (nextLink.length() > 0) {
            return wikiPrefixHttp + nextLink.substring(9, nextLink.indexOf("\"", 10));
        //This is the case where there were no appropriate links that matched our requirements
        } else {
        	System.err.println("No valid links found on page " + urlToProcess
        			+ " to proceed with the Search. Exiting.");
        	return null;
        }
    }
    
    private void printLinkTitle(String linkToProcess) {
        //Output the URL title in the console based on the standard Wiki URL pattern
        System.out.println(" --> "+ linkToProcess);
    }
}
