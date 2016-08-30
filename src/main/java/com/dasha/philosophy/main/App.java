package com.dasha.philosophy.main;

import java.io.IOException;
import java.util.Scanner;

import com.dasha.philosophy.service.PhilosophyFindingService;
import com.dasha.philosophy.util.Validator;

public class App {
	static PhilosophyFindingService service = new PhilosophyFindingService();

	public static void main(String[] args) throws IOException {

        @SuppressWarnings("resource")
		Scanner argsScanner = new Scanner(System.in);

        while(true) {
            System.out.println("Enter a string you'd like to use in a Wiki URL or \"done\" to finish the program run:");
            String inputString = argsScanner.next();
            if (inputString.equalsIgnoreCase("done")) {
                System.exit(0);
            }
            //Attempt to build a valid Wiki URL
            String startingUrl = Validator.buildWikiUrl(inputString);
            try {
                //Proceed to the Philosophy page
            	service.determineClicksToPhilosophyWikiPage(startingUrl);
            } catch (IOException e){
            	System.err.println("Error occured when trying to determine the number of clicks to the Philosophy Wiki Page.");
    			e.printStackTrace();
    			System.exit(1);
            }
        }
    }
}
