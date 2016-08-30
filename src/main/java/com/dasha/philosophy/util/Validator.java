package com.dasha.philosophy.util;

public class Validator {
	private static String wikiPrefixHttp = "http://en.wikipedia.org/wiki/";

	/**
	 * Prepends the standard Wiki URL piece to the string provided in attemps to build a valid Wiki URL.
	 * @param inputString
	 * @return String
	 */
	public static String buildWikiUrl(String inputString) {
        return wikiPrefixHttp + inputString;
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
