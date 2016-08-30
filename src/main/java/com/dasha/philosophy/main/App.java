package com.dasha.philosophy.main;

import java.io.IOException;
import java.util.Scanner;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.dasha.philosophy.service.ClickDataService;
import com.dasha.philosophy.service.PhilosophyFindingService;
import com.dasha.philosophy.util.Validator;

public class App {
	public static void main(String[] args) throws IOException {
		
		AnnotationConfigApplicationContext  context = new AnnotationConfigApplicationContext();
		context.scan("com.dasha.philosophy");
		context.refresh();
        
		PhilosophyFindingService philosphyFindingService = (PhilosophyFindingService) context.getBean("philosophyFindingService");
		ClickDataService clickDataService = (ClickDataService) context.getBean("clickDataService");


        @SuppressWarnings("resource")
		Scanner argsScanner = new Scanner(System.in);
        
        //Populate the click cache from the DB
        clickDataService.initClickDataCache();

        while(true) {
            System.out.println("Enter a string you'd like to use in a Wiki URL or \"done\" to finish the program run:");
            String inputString = argsScanner.next();
            if (inputString.equalsIgnoreCase("done")) {
            	clickDataService.clearClickDataCache();
            	context.close();
                System.exit(0);
            }
            //Attempt to build a valid Wiki URL
            String startingUrl = Validator.buildWikiUrl(inputString);
            try {
                //Proceed to try and find Philosophy page based on supplied starting URL
            	philosphyFindingService.determineClicksToPhilosophyWikiPage(startingUrl);
            } catch (IOException e){
            	System.err.println("Error occured when trying to determine the number of clicks to the Philosophy Wiki Page.");
    			e.printStackTrace();
    			context.close();
    			System.exit(1);
            }
        }
    }
}
