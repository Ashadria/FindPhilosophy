package com.dasha.philosophy.service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.JDBCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dasha.philosophy.dao.ClickDataDao;
import com.dasha.philosophy.model.ClickData;

@Service("clickDataService")
public class ClickDataService {
	@Autowired
	private ClickDataDao clickDataDao;
	
	private static Map<String, Integer> clickDataForPreviouslyEncounteredStartingUrls = new HashMap<String, Integer>();

	/**
	 * Initialize the click cache that is going to keep track of all the URL to click information
	 */
	public void initClickDataCache() {
		try {
			List<ClickData> clickData = clickDataDao.listClickData();
			for (ClickData individualClickData : clickData) {
				clickDataForPreviouslyEncounteredStartingUrls.put(individualClickData.getStartUrl(), individualClickData.getNumberOfClicksToPhilosophy());
			}
		} catch (JDBCException e) {
			SQLException cause = (SQLException) e.getCause();
		    //Alert the user about the error during DB interaction
		    System.err.println("Error occured when trying to retreive click data from database: " + cause.getMessage());		
		}	
	}
	
	/**
	 * Save the individual click entry for a particular URL in DB.
	 * Also, update the cached Map with the new entry
	 * @param clickData
	 */
	public void saveClickDataInDatabase(ClickData clickData) {
		try {
			clickDataDao.saveClickDataInDatabase(clickData);
			clickDataForPreviouslyEncounteredStartingUrls.put(clickData.getStartUrl(), clickData.getNumberOfClicksToPhilosophy());
		} catch (JDBCException e) {
			SQLException cause = (SQLException) e.getCause();
		    //Alert the user about the error during DB interaction
		    System.err.println("Error occured when trying to save click data to database: " + cause.getMessage());		
		}
	}
	
	/**
	 * Check if the URL already has click data associated with it
	 * @param urlToCheck
	 * @return
	 */
    public boolean checkIfAlreadyHaveClickDataForUrl(String urlToCheck) {
    	return clickDataForPreviouslyEncounteredStartingUrls.containsKey(urlToCheck);
    }
    
    /**
     * Retrieve the clicks for the specified url
     * @param inputUrl
     * @return
     */
    public Integer fetchClickDataForUrl(String inputUrl) {
    	return clickDataForPreviouslyEncounteredStartingUrls.get(inputUrl);
    }

    /**
     * Clear the cached Map
     */
	public void clearClickDataCache() {
		clickDataForPreviouslyEncounteredStartingUrls = new HashMap<String, Integer>();
	}
}
