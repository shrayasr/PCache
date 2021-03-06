package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import main.com.pcache.DO.timeseries.VariableTimeseries;
import main.com.pcache.exceptions.PCacheException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class VariableTimeseriesTest
{

	@Test
	public void testTimeseries() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			
		}};
		
		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
	}

	@Test
	public void testTimeseriesRangeBetween() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		String timestampFrom = "2010-01-05T12:00:00.000+05:30";
		String timestampTo = "2010-01-20T12:00:00.000+05:30";
		

		VariableTimeseries tsSub = ts.getRangeBetween(timestampFrom, timestampTo);

		assertEquals(3, tsSub.size());

	}

	@Test
	public void testTimeseriesRangeFrom() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		String timestampFrom = "2010-01-05T12:00:00.000+05:30";

		VariableTimeseries tsSub = ts.getRangeFrom(timestampFrom);

		assertEquals(5, tsSub.size());

	}

	@Test
	public void testTimeseriesRangeTo() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		String timestampTo = "2010-01-20T12:00:00.000+05:30";
		
		VariableTimeseries tsSub = ts.getRangeTo(timestampTo);

		assertEquals(6, tsSub.size());

	}

	@Test
	public void testTimeseriesAddPoints() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		int timestampsBeforeAdd = ts.size();

		ArrayList<String> moreTimestamps = new ArrayList<String>() {{
			
			add("2010-02-01T12:00:00.000+05:30");
			add("2010-02-02T12:00:00.000+05:30");
			add("2010-02-03T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> moreDataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			
		}};

		ts.addPoints(moreTimestamps, moreDataPoints);

		int timestampsAfterAdd = ts.size();

		assertEquals(timestampsBeforeAdd+3, timestampsAfterAdd);
	}

	public void testTimeseriesRemovePoints() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-11T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			
		}};
		
		ArrayList<String> dataPoints = new ArrayList<String>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		int timestampsBeforeRemove = ts.size();

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-10T12:00:00.000+05:30");
			add("2010-01-23T12:00:00.000+05:30");
			add("2010-01-27T12:00:00.000+05:30");
			add("2010-02-01T12:00:00.000+05:30"); // this point doesn't exist
			
		}};

		ts.removePoints(timestampsToRemove);

		int timestampsAfterRemove = ts.size();

		assertEquals(timestampsBeforeRemove-3, timestampsAfterRemove);
		
	}

}
