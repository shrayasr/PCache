package com.pcache.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import com.pcache.DO.Node;
import com.pcache.DO.Timeseries;
import com.pcache.dataaccess.CacheEngine;
import com.pcache.exceptions.PCacheException;

public class CacheEngineTest
{

	@Before
	public void setUp() throws Exception
	{
		CacheEngine.reInitializeCache();
	}

	@Test (expected=PCacheException.class)
	public void testAddNewNamespace_nsInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("asdf!@#$");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewNamespace_nsExists() throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewNamespace("foo");
	}
	
	@Test 
	public void testAddNewNamespace_ok() throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
	}

	@Test (expected=PCacheException.class)
	public void testRenameNamespace_oldNsInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.renameNamespace("foo@", "bar");
	}
	
	@Test (expected=PCacheException.class)
	public void testRenameNamespace_oldNsNoExist() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.renameNamespace("bar", "baz");
	}
	
	@Test (expected=PCacheException.class)
	public void testRenameNamespace_newNsInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.renameNamespace("foo", "bar@");
	}
	
	@Test (expected=PCacheException.class)
	public void testRenameNamespace_newNsExist() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewNamespace("bar");
		CacheEngine.renameNamespace("foo", "bar");
	}
	
	@Test 
	public void testRenameNamespace_ok() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.renameNamespace("foo", "bar");
	}

	@Test
	public void testGetNamespaces_ok() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewNamespace("bar");
		CacheEngine.addNewNamespace("baz");

		List<Node> namespaces = new ArrayList<Node>(CacheEngine.getNamespaces());

		assertEquals(3, namespaces.size());
	}

	@Test (expected=PCacheException.class)
	public void testRemoveNamespace_nsInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewNamespace("bar");
		CacheEngine.addNewNamespace("baz");

		CacheEngine.removeNamespace("foo!");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveNamespace_nsNoExist() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewNamespace("bar");
		CacheEngine.addNewNamespace("baz");

		CacheEngine.removeNamespace("fooz");
	}

	@Test 
	public void testRemoveNamespace_ok() throws PCacheException
	{
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewNamespace("bar");
		CacheEngine.addNewNamespace("baz");

		int noOfNamespaceBeforeDelete = CacheEngine.getNamespaces().size();

		CacheEngine.removeNamespace("foo");

		int noOfNamespaceAfterDelete = CacheEngine.getNamespaces().size();

		assertEquals(noOfNamespaceBeforeDelete-1, noOfNamespaceAfterDelete);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructure_nsInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo!", "bar", "baz,boo");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_nsNoExists() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo_nope", "bar", "baz,boo");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_structInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar!", "baz,boo");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_structExists() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructure("foo", "bar", "qwer,asdf");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_structDefnInvalid() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "!!baz,boo");
	}
	
	@Test 
	public void testAddNewStructure_ok() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_nsInvalid() throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.renameStructure("foo!", "bar", "barz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_nsNoExists() throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.renameStructure("fooz", "bar", "barz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_oldStructInvalid() 
			throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.renameStructure("foo", "bar!", "barz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_oldStructNoExists() 
			throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.renameStructure("foo", "barz", "barzz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_newStructInvalid() 
			throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.renameStructure("foo", "bar", "bar!");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_newStructExists() 
			throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructure("foo", "baz", "foo,bar");
		CacheEngine.renameStructure("foo", "bar", "baz");
	}

	@Test(expected=PCacheException.class)
	public void testGetStructuresUnderNamespace_nsInvalid() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.getStructures("foo!");
	}

	@Test(expected=PCacheException.class)
	public void testGetStructuresUnderNamespace_nsNoExist() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.getStructures("bar");
	}

	@Test
	public void testGetStructuresUnderNamespace_ok() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "foo1", "a,b");
		CacheEngine.addNewStructure("foo", "foo2", "c,d");
		CacheEngine.addNewStructure("foo", "foo3", "e,f");

		List<Node> structures = new ArrayList<Node>(CacheEngine
				.getStructures("foo"));

		assertEquals(3, structures.size());
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructure_nsInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		CacheEngine.removeStructure("foo!", "bar");
	}
	
	@Test (expected=PCacheException.class)
	public void testRemoveStructure_nsNoExists() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		CacheEngine.removeStructure("fooz", "bar");
	}
	
	@Test (expected=PCacheException.class)
	public void testRemoveStructure_structInvalid() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		CacheEngine.removeStructure("foo", "bar!");
	}
	
	@Test (expected=PCacheException.class)
	public void testRemoveStructure_structNoExists() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		CacheEngine.removeStructure("foo", "barz");
	}

	@Test 
	public void testRemoveStructure_ok() throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		int noStructsBeforeDelete = CacheEngine.getStructures("foo").size();

		CacheEngine.removeStructure("foo", "bar");

		int noStructsAfterDelete = CacheEngine.getStructures("foo").size();

		assertEquals(noStructsBeforeDelete-1, noStructsAfterDelete);
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_nsInvalid() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo!", "bar", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_nsNoExist() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("fooz", "bar", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structIdInvalid() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar!", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structIdNoExist() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "barr", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structInstIdInvalid() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo!=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structInstIdExist() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structInstanceIdNoMatch() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,bo=2",null);
	}

	@Test 
	public void testAddNewStructureInstance_ok() 
			throws PCacheException
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
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",ts);
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_nsInvalid() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
		
		CacheEngine.getStructureInstances("foo!", "bar");
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_nsNoExist() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("fooz", "bar", "baz=1,boo=2",null);

		CacheEngine.getStructureInstances("fooz", "bar");
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_structIdInvalid() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar!", "baz=1,boo=2",null);

		CacheEngine.getStructureInstances("foo", "bar!");
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_structIdNoExist() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "barr", "baz=1,boo=2",null);

		CacheEngine.getStructureInstances("foo", "bara");
	}

	@Test 
	public void testGetInstancesUnderStructure_ok() 
			throws PCacheException
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2",null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=3,boo=2",null);

		List<Node> structInstances = new ArrayList<Node>(CacheEngine
				.getStructureInstances("foo", "bar"));

		assertEquals(3, structInstances.size());
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_nsInvalid() 
			throws PCacheException 
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);

		CacheEngine.removeStructureInstance("foo!", "bar", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_nsNoExist() throws PCacheException  
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);

		CacheEngine.removeStructureInstance("fooz", "bar", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structInvalid() 
			throws PCacheException  
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		CacheEngine.removeStructureInstance("foo", "bar!", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structNoExist() 
			throws PCacheException  
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		CacheEngine.removeStructureInstance("foo", "barz", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structInstanceInvalid() 
			throws PCacheException  
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		CacheEngine.removeStructureInstance("foo", "bar", "!baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structInstanceNoExist() 
			throws PCacheException  
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		CacheEngine.removeStructureInstance("foo", "bar", "bazz=1,boo=2");
	}

	@Test 
	public void testRemoveStructureInstance_ok() throws PCacheException  
	{
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		int noOfInstancesBeforeDel = CacheEngine
				.getStructureInstances("foo", "bar").size();

		CacheEngine.removeStructureInstance("foo", "bar", "baz=1,boo=2");

		int noOfInstancesAfterDel = CacheEngine
				.getStructureInstances("foo", "bar").size();

		assertEquals(noOfInstancesBeforeDel-1, noOfInstancesAfterDel);
	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_nsInvalid() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("foo!", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_nsNoExist() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("fooz", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_structIdInvalid() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("foo", "bar!", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_structIdNoExist() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("foo", "barz", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_structInstanceIdInvalid() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("foo", "bar", "!baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_structInstanceIdNoExist() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("foo", "bar", "baz=2,boo=2", 
				timestampsToAdd, dataPointsToAdd);
	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_tsNull() throws PCacheException
	{
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test 
	public void testAddPointsToTimeseries_ok() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		

		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		int countBeforeAdd = CacheEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		CacheEngine.addPointsToTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

		int countAfterAdd = CacheEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();

		assertEquals(countBeforeAdd+2, countAfterAdd);
	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_nsInvalid() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("foo!", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_nsNoExist() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("fooz", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_structIdInvalid() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("foo", "bar!", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_structIdNoExist() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("foo", "barz", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_structInstanceIdInvalid() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("foo", "bar", "!baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_structInstanceIdNoExist() 
			throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("foo", "bar", "baz=2,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_nullTs() 
			throws PCacheException
	{
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test
	public void testUpdatePointsInTimeseries_ok() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		int downCountBeforeUpdate = 0;

		Map<Long, Object> points = CacheEngine.getTimeseriesFrom("foo", "bar", 
				"baz=1,boo=2", "2010-01-07T12:00:00.000+05:30");

		for (Entry<Long, Object> entry : points.entrySet()) {
			downCountBeforeUpdate++ ;
		}
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		CacheEngine.updatePointsInTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

		points = CacheEngine.getTimeseriesFrom("foo", "bar", "baz=1,boo=2", 
				"2010-01-07T12:00:00.000+05:30");

		int downCountAfterUpdate = 0;

		for (Entry<Long, Object> entry : points.entrySet()) {
			if (entry.getValue() == "DOWN")
				downCountAfterUpdate++;
		}

		assertEquals(downCountBeforeUpdate, downCountAfterUpdate);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_nsInvalid() throws PCacheException
	{
			
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("foo!", "bar", "baz=1,boo=2", ts2);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_nsNoExist() throws PCacheException
	{
			
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("fooz", "bar", "baz=1,boo=2", ts2);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_structIdInvalid() 
			throws PCacheException
	{
			
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("foo", "bar!", "baz=1,boo=2", ts2);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_structIdNoExist() 
			throws PCacheException
	{
			
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("foo", "barz", "baz=1,boo=2", ts2);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_structInstanceIdInvalid() 
			throws PCacheException
	{
			
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("foo", "bar", "!baz=1,boo=2", ts2);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_structInstanceIdNoExist() 
			throws PCacheException
	{
			
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("foo", "bar", "baz=2,boo=2", ts2);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_tsNull() throws PCacheException
	{
			
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("foo", "bar", "baz=1,boo=2", ts2);

	}


	@Test
	public void testUpdateTimeseries_ok() throws PCacheException
	{
			
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		Timeseries ts2 = new Timeseries(newTimestamps, newDataPoints);

		CacheEngine.updateTimeseries("foo", "bar", "baz=1,boo=2", ts2);

		int countAfterReplace = CacheEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();

		assertEquals(2, countAfterReplace);
		
	}

	@Test
	public void testRemovePointsFromTimeseries_ok() throws PCacheException
	{
		
		ArrayList<String> timestamps = new ArrayList<String>() {{
			
			add("2010-01-01T12:00:00.000+05:30");
			add("2010-01-02T12:00:00.000+05:30");
			add("2010-01-03T12:00:00.000+05:30");
			add("2010-01-04T12:00:00.000+05:30");
			add("2010-01-05T12:00:00.000+05:30");
			add("2010-01-06T12:00:00.000+05:30");
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPoints = new ArrayList<Object>() {{
			
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("DOWN");
			add("UP");
			add("UP");
			add("UP");
			
		}};

		Timeseries ts = new Timeseries(timestamps, dataPoints);
		
		CacheEngine.addNewNamespace("foo");
		CacheEngine.addNewStructure("foo", "bar", "baz,boo");
		CacheEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		int countBeforeRemove = CacheEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		CacheEngine.removePointsFromTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToRemove);

		int countAfterRemove = CacheEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();

		assertEquals(countBeforeRemove-2, countAfterRemove);

	}
}
