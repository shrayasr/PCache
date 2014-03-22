package com.pcache.DO;

import java.util.ArrayList;

/**
 * Data Object to store the list of timeseries in the cache
 * @author shrayas
 *
 */
public class Timeseries {

	private ArrayList<String> _timestamps;
	private ArrayList<String> _dataPoints;

	/**
	 * Constructor. Initialize a time series 
	 * @param timestamps the list of timestamps
	 * @param dataPoints the associated list of data points 
	 */
	public Timeseries (ArrayList<String> timestamps, ArrayList<String> dataPoints) {
		
		this._timestamps = timestamps;
		this._dataPoints = dataPoints;
		
	}

}
