package com.pcache.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pcache.DO.Timeseries;
import com.pcache.exceptions.PCacheException;

public class TimeseriesTest
{

	@Test
	public void testTimeseries() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			
		}};
		
		System.out.println(timestamps.size());
		System.out.println(timestamps.get(0));
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			
		}};
		
		Timeseries<String> ts = new Timeseries<String>(timestamps, dataPoints);
		
	}

}
