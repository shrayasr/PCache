package com.pcache.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.pcache.DO.timeseries.FixedTimeseries;
import com.pcache.exceptions.PCacheException;

public class FixedTimeseriesTest
{

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}

	@Test
	public void test_constructor() throws PCacheException
	{
		
		ArrayList<String> timeseries = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-12T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Integer> dataPoints = new ArrayList<Integer>() {{
			
			add(1);
			add(2);
			add(3);
			add(1);
			add(2);
			add(3);
			add(1);
			add(2);
			add(3);
			add(3);
			
		}};
		
		FixedTimeseries<Integer> x = new FixedTimeseries<Integer>(timeseries, dataPoints, "1d", -1);
		
		assert true;
		
	}

}
