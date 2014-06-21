package test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import main.com.pcache.DO.PCacheTimestamp;
import main.com.pcache.exceptions.PCacheException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PCacheTimestampTest
{

	@Test
	public void test_directTest() throws PCacheException
	{
		PCacheTimestamp test = new PCacheTimestamp("2010-01-01T12:00:00.000+05:30");
		
		assertEquals(new PCacheTimestamp("2010-01-01T12:00:00.000+05:30"), test);
	}
	
	@Test
	public void test_arrayList() throws PCacheException
	{
		PCacheTimestamp test = new PCacheTimestamp("2010-01-01T12:00:00.000+05:30");
		ArrayList<PCacheTimestamp> x = new ArrayList<>();
		
		x.add(test);
		
		assertEquals(true, x.contains(new PCacheTimestamp("2010-01-01T12:00:00.000+05:30")));
	}
	
	@Test
	public void test_map() throws PCacheException
	{
		
		PCacheTimestamp test = new PCacheTimestamp("2010-01-01T12:00:00.000+05:30");
		Map<PCacheTimestamp, String> y = new TreeMap<PCacheTimestamp, String>();
		
		y.put(test, "hello");
		
		assertEquals(true,y.containsKey(new PCacheTimestamp("2010-01-01T12:00:00.000+05:30")));
		
	}
	

}
