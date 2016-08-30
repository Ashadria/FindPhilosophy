package com.dasha.philosophy.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import java.io.IOException;
import org.junit.Test;
import com.dasha.philosophy.util.Validator;

/**
 * Unit tests for the Philosophy Wiki page finding service
 */
public class PhilosophyFindingServiceTest {
	private static String testString1 = "Ethology";
	private static Integer numberOfClicks1 = 9;
	private static String testString2 = "Scientific_method";
	private static Integer numberOfClicks2 = 8;
	
	@Test
	public void testSingleNewUrl() {
		System.out.println("Running testSingleSuccessLookup");
		try {
			PhilosophyFindingService service = new PhilosophyFindingService();	
			
			String testUrl1 = Validator.buildWikiUrl(testString1);
			Integer clicksToPhilosophy = service.determineClicksToPhilosophyWikiPage(testUrl1);
			assertEquals(numberOfClicks1, clicksToPhilosophy);
		} catch (IOException e) {
			//Fail the test since no exception should be thrown
			fail("Test failed with the following exception: " + e.getMessage());
		}
    }
	
	@Test
	public void testAlreadyExistingUrl() {
		System.out.println("Running testAlreadyExistingUrl");
		try {
			PhilosophyFindingService service = new PhilosophyFindingService();
			
			String testUrl1 = Validator.buildWikiUrl(testString1);
			Integer clicksToPhilosophy = service.determineClicksToPhilosophyWikiPage(testUrl1);
			assertEquals(numberOfClicks1, clicksToPhilosophy);
			
			Integer clicksToPhilosophyTry2 = service.determineClicksToPhilosophyWikiPage(testUrl1);
			assertEquals(numberOfClicks1, clicksToPhilosophyTry2);
		} catch (IOException e) {
			//Fail the test since no exception should be thrown
			fail("Test failed with the following exception: " + e.getMessage());
		}
    }
	
	@Test
	public void testMultipleNewUrls() {
		System.out.println("Running testMultipleNewUrls");
		try {
			PhilosophyFindingService service = new PhilosophyFindingService();	
			
			String testUrl1 = Validator.buildWikiUrl(testString1);
			Integer clicksToPhilosophy = service.determineClicksToPhilosophyWikiPage(testUrl1);
			assertEquals(numberOfClicks1, clicksToPhilosophy);
			
			String testUrl2 = Validator.buildWikiUrl(testString2);
			Integer clicksToPhilosophyTry2 = service.determineClicksToPhilosophyWikiPage(testUrl2);
			assertEquals(numberOfClicks2, clicksToPhilosophyTry2);
		} catch (IOException e) {
			//Fail the test since no exception should be thrown
			fail("Test failed with the following exception: " + e.getMessage());
		}
    }
}
