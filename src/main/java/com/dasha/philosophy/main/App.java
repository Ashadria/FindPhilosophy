package com.dasha.philosophy.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.dasha.philosophy.service.PhilosophyFindingService;
import com.dasha.philosophy.util.Validator;

public class App {
	static PhilosophyFindingService service = new PhilosophyFindingService();

	public static void main(String[] args) throws IOException {

        @SuppressWarnings("resource")
		Scanner argsScanner = new Scanner(System.in);

        while(true) {
            System.out.println("Enter a valid Wiki URL or \"done\" to finish the program run:");
            String inputUrl = argsScanner.next();
            if (inputUrl.equalsIgnoreCase("done")) {
                System.exit(0);
            }
            // Check the validity of the input URL
            if (!Validator.isValidWikiUrl(inputUrl)) {
                System.out.println("Please provide a valid Wiki URL, example: https://en.wikipedia.org/wiki/Ethology");
                continue;
            }
            // Proceed to the Philosophy page
            try {
            	service.determineClicksToPhilosophyWikiPage(inputUrl);
            } catch (IOException e){
            	System.err.println("Error occured when trying to determine the number of clicks to the Philosophy Wiki Page.");
    			e.printStackTrace();
    			System.exit(1);
            }
        }
    }
}
