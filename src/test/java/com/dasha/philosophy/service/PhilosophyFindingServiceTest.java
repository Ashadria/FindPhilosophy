package com.dasha.philosophy.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for the Philosophy Wiki page finding service
 */
public class PhilosophyFindingServiceTest {
	private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
	private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
	
	private static String testString1 = "Ethology";
	private static String testUrl1 = "http://en.wikipedia.org/wiki/Ethology";
	private static Integer numberOfClicks1 = 9;
	
	private static String testString2 = "Scientific_method";
	private static String testUrl2 = "http://en.wikipedia.org/wiki/Scientific_method";
	private static Integer numberOfClicks2 = 8;
	
	private static String loopTestString = "Matter";
	private static String loopUrl = "http://en.wikipedia.org/wiki/Matter";


	
	@InjectMocks
	private PhilosophyFindingService service;
	
	@Mock
	private ClickDataService clickDataService;
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	    System.setOut(new PrintStream(outContent));
	    System.setErr(new PrintStream(errContent));
	}
	
	@After
	public void cleanUpStreams() {
	    System.setOut(null);
	    System.setErr(null);
	}
	
	@Test
	public void testSingleNewUrl() {
		System.out.println("Running testSingleSuccessLookup");
		try {
			this.givenNewUrl(testString1, testUrl1, numberOfClicks1);
			
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
			givenAlreadyExistingUrl(testString1, testUrl1, numberOfClicks1);
			
			Integer clicksToPhilosophy = service.determineClicksToPhilosophyWikiPage(testUrl1);
			assertEquals(numberOfClicks1, clicksToPhilosophy);
		} catch (IOException e) {
			//Fail the test since no exception should be thrown
			fail("Test failed with the following exception: " + e.getMessage());
		}
    }
	
	@Test
	public void testMultipleNewUrls() {
		System.out.println("Running testMultipleNewUrls");
		try {	
			givenNewUrl(testString1, testUrl1, numberOfClicks1);
			givenNewUrl(testString2, testUrl2, numberOfClicks2);
			
			Integer clicksToPhilosophy = service.determineClicksToPhilosophyWikiPage(testUrl1);
			assertEquals(numberOfClicks1, clicksToPhilosophy);
			
			Integer clicksToPhilosophyTry2 = service.determineClicksToPhilosophyWikiPage(testUrl2);
			assertEquals(numberOfClicks2, clicksToPhilosophyTry2);
		} catch (IOException e) {
			//Fail the test since no exception should be thrown
			fail("Test failed with the following exception: " + e.getMessage());
		}
    }
	
	@Test
	public void testLoopUrl() {
		System.out.println("Running testLoopUrl");
		try {			
			givenNewUrl(loopTestString, loopUrl, null);
			
			Integer clicksToPhilosophy = service.determineClicksToPhilosophyWikiPage(loopUrl);
			assertEquals(null, clicksToPhilosophy);
		} catch (IOException e) {
			//Fail the test since no exception should be thrown
			fail("Test failed with the following exception: " + e.getMessage());
		}
    }
	
	private void givenNewUrl(String string, String url, Integer clicks) {
		when(clickDataService.checkIfAlreadyHaveClickDataForUrl(string)).thenReturn(false);
		when(clickDataService.fetchClickDataForUrl(url)).thenReturn(clicks);
	}
	
	private void givenAlreadyExistingUrl(String string, String url, Integer clicks) {
		when(clickDataService.checkIfAlreadyHaveClickDataForUrl(string)).thenReturn(true);
		when(clickDataService.fetchClickDataForUrl(url)).thenReturn(clicks);
	}
}
