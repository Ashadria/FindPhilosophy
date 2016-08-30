package com.dasha.philosophy.util;

public class Validator {
	private static String wikiPrefixHttp = "http://en.wikipedia.org/wiki/";
	private static String wikiPrefixHttps = "https://en.wikipedia.org/wiki";

	/**
	 * Determines if the incoming URL is of proper Wiki format.
	 * @param inputUrl
	 * @return
	 */
	public static boolean isValidWikiUrl(String inputUrl) {
        return inputUrl.startsWith(wikiPrefixHttp) || inputUrl.startsWith(wikiPrefixHttps);
    }

	/**
	 * Filters out undesirable links related to languages, pronunciation and non-wiki ones 
	 * (looked this up)
	 * @param link
	 * @return
	 */
    public static boolean isFirstValidLink(String link) {
        return (link.contains("wiki") && (!link.contains("Greek") || !link.contains("Latin") || !link.contains("wiktionary")));
    }
}
