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
import com.pcache.DO.timeseries.VariableTimeseries;
import com.pcache.engines.VariableTimeseriesEngine;
import com.pcache.exceptions.PCacheException;

public class CacheEngineTest
{

	@Before
	public void setUp() throws Exception
	{
		VariableTimeseriesEngine.reInitializeCache();
	}

	@Test (expected=PCacheException.class)
	public void testAddNewNamespace_nsInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("asdf!@#$");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewNamespace_nsExists() throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewNamespace("foo");
	}
	
	@Test 
	public void testAddNewNamespace_ok() throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
	}

	@Test (expected=PCacheException.class)
	public void testRenameNamespace_oldNsInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.renameNamespace("foo@", "bar");
	}
	
	@Test (expected=PCacheException.class)
	public void testRenameNamespace_oldNsNoExist() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.renameNamespace("bar", "baz");
	}
	
	@Test (expected=PCacheException.class)
	public void testRenameNamespace_newNsInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.renameNamespace("foo", "bar@");
	}
	
	@Test (expected=PCacheException.class)
	public void testRenameNamespace_newNsExist() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewNamespace("bar");
		VariableTimeseriesEngine.renameNamespace("foo", "bar");
	}
	
	@Test 
	public void testRenameNamespace_ok() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.renameNamespace("foo", "bar");
	}

	@Test
	public void testGetNamespaces_ok() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewNamespace("bar");
		VariableTimeseriesEngine.addNewNamespace("baz");

		List<Node> namespaces = new ArrayList<Node>(VariableTimeseriesEngine.getNamespaces());

		assertEquals(3, namespaces.size());
	}

	@Test (expected=PCacheException.class)
	public void testRemoveNamespace_nsInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewNamespace("bar");
		VariableTimeseriesEngine.addNewNamespace("baz");

		VariableTimeseriesEngine.removeNamespace("foo!");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveNamespace_nsNoExist() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewNamespace("bar");
		VariableTimeseriesEngine.addNewNamespace("baz");

		VariableTimeseriesEngine.removeNamespace("fooz");
	}

	@Test 
	public void testRemoveNamespace_ok() throws PCacheException
	{
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewNamespace("bar");
		VariableTimeseriesEngine.addNewNamespace("baz");

		int noOfNamespaceBeforeDelete = VariableTimeseriesEngine.getNamespaces().size();

		VariableTimeseriesEngine.removeNamespace("foo");

		int noOfNamespaceAfterDelete = VariableTimeseriesEngine.getNamespaces().size();

		assertEquals(noOfNamespaceBeforeDelete-1, noOfNamespaceAfterDelete);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructure_nsInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo!", "bar", "baz,boo");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_nsNoExists() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo_nope", "bar", "baz,boo");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_structInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar!", "baz,boo");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_structExists() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "qwer,asdf");
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructure_structDefnInvalid() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "!!baz,boo");
	}
	
	@Test 
	public void testAddNewStructure_ok() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_nsInvalid() throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.renameStructure("foo!", "bar", "barz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_nsNoExists() throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.renameStructure("fooz", "bar", "barz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_oldStructInvalid() 
			throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.renameStructure("foo", "bar!", "barz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_oldStructNoExists() 
			throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.renameStructure("foo", "barz", "barzz");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_newStructInvalid() 
			throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.renameStructure("foo", "bar", "bar!");
	}

	@Test (expected=PCacheException.class)
	public void testRenameStructure_newStructExists() 
			throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructure("foo", "baz", "foo,bar");
		VariableTimeseriesEngine.renameStructure("foo", "bar", "baz");
	}

	@Test(expected=PCacheException.class)
	public void testGetStructuresUnderNamespace_nsInvalid() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.getStructures("foo!");
	}

	@Test(expected=PCacheException.class)
	public void testGetStructuresUnderNamespace_nsNoExist() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.getStructures("bar");
	}

	@Test
	public void testGetStructuresUnderNamespace_ok() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "foo1", "a,b");
		VariableTimeseriesEngine.addNewStructure("foo", "foo2", "c,d");
		VariableTimeseriesEngine.addNewStructure("foo", "foo3", "e,f");

		List<Node> structures = new ArrayList<Node>(VariableTimeseriesEngine
				.getStructures("foo"));

		assertEquals(3, structures.size());
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructure_nsInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		VariableTimeseriesEngine.removeStructure("foo!", "bar");
	}
	
	@Test (expected=PCacheException.class)
	public void testRemoveStructure_nsNoExists() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		VariableTimeseriesEngine.removeStructure("fooz", "bar");
	}
	
	@Test (expected=PCacheException.class)
	public void testRemoveStructure_structInvalid() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		VariableTimeseriesEngine.removeStructure("foo", "bar!");
	}
	
	@Test (expected=PCacheException.class)
	public void testRemoveStructure_structNoExists() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		VariableTimeseriesEngine.removeStructure("foo", "barz");
	}

	@Test 
	public void testRemoveStructure_ok() throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		int noStructsBeforeDelete = VariableTimeseriesEngine.getStructures("foo").size();

		VariableTimeseriesEngine.removeStructure("foo", "bar");

		int noStructsAfterDelete = VariableTimeseriesEngine.getStructures("foo").size();

		assertEquals(noStructsBeforeDelete-1, noStructsAfterDelete);
	}
	
	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_nsInvalid() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo!", "bar", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_nsNoExist() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("fooz", "bar", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structIdInvalid() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar!", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structIdNoExist() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "barr", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structInstIdInvalid() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo!=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structInstIdExist() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
	}

	@Test (expected=PCacheException.class)
	public void testAddNewStructureInstance_structInstanceIdNoMatch() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,bo=2",null);
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",ts);
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_nsInvalid() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
		
		VariableTimeseriesEngine.getStructureInstances("foo!", "bar");
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_nsNoExist() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("fooz", "bar", "baz=1,boo=2",null);

		VariableTimeseriesEngine.getStructureInstances("fooz", "bar");
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_structIdInvalid() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar!", "baz=1,boo=2",null);

		VariableTimeseriesEngine.getStructureInstances("foo", "bar!");
	}

	@Test (expected=PCacheException.class)
	public void testGetInstancesUnderStructure_structIdNoExist() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "barr", "baz=1,boo=2",null);

		VariableTimeseriesEngine.getStructureInstances("foo", "bara");
	}

	@Test 
	public void testGetInstancesUnderStructure_ok() 
			throws PCacheException
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2",null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2",null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=3,boo=2",null);

		List<Node> structInstances = new ArrayList<Node>(VariableTimeseriesEngine
				.getStructureInstances("foo", "bar"));

		assertEquals(3, structInstances.size());
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_nsInvalid() 
			throws PCacheException 
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);

		VariableTimeseriesEngine.removeStructureInstance("foo!", "bar", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_nsNoExist() throws PCacheException  
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);

		VariableTimeseriesEngine.removeStructureInstance("fooz", "bar", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structInvalid() 
			throws PCacheException  
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		VariableTimeseriesEngine.removeStructureInstance("foo", "bar!", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structNoExist() 
			throws PCacheException  
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		VariableTimeseriesEngine.removeStructureInstance("foo", "barz", "baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structInstanceInvalid() 
			throws PCacheException  
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		VariableTimeseriesEngine.removeStructureInstance("foo", "bar", "!baz=1,boo=2");
	}

	@Test (expected=PCacheException.class)
	public void testRemoveStructureInstance_structInstanceNoExist() 
			throws PCacheException  
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		VariableTimeseriesEngine.removeStructureInstance("foo", "bar", "bazz=1,boo=2");
	}

	@Test 
	public void testRemoveStructureInstance_ok() throws PCacheException  
	{
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=2,boo=2", null);
		
		int noOfInstancesBeforeDel = VariableTimeseriesEngine
				.getStructureInstances("foo", "bar").size();

		VariableTimeseriesEngine.removeStructureInstance("foo", "bar", "baz=1,boo=2");

		int noOfInstancesAfterDel = VariableTimeseriesEngine
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("foo!", "bar", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("fooz", "bar", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("foo", "bar!", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("foo", "barz", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("foo", "bar", "!baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("foo", "bar", "baz=2,boo=2", 
				timestampsToAdd, dataPointsToAdd);
	}

	@Test (expected=PCacheException.class)
	public void testAddPointsToTimeseries_tsNull() throws PCacheException
	{
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("foo", "bar", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		

		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		int countBeforeAdd = VariableTimeseriesEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-09T12:00:00.000+05:30");
			add("2010-01-10T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("UP");
			
		}};

		VariableTimeseriesEngine.addPointsToTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

		int countAfterAdd = VariableTimeseriesEngine.getTimeseries("foo", "bar", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseriesEngine.updatePointsInTimeseries("foo!", "bar", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseriesEngine.updatePointsInTimeseries("fooz", "bar", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseriesEngine.updatePointsInTimeseries("foo", "bar!", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseriesEngine.updatePointsInTimeseries("foo", "barz", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseriesEngine.updatePointsInTimeseries("foo", "bar", "!baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseriesEngine.updatePointsInTimeseries("foo", "bar", "baz=2,boo=2", 
				timestampsToAdd, dataPointsToAdd);

	}

	@Test (expected=PCacheException.class)
	public void testUpdatePointsInTimeseries_nullTs() 
			throws PCacheException
	{
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);
		
		ArrayList<String> timestampsToAdd = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> dataPointsToAdd = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseriesEngine.updatePointsInTimeseries("foo", "bar", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		int downCountBeforeUpdate = 0;

		Map<Long, Object> points = VariableTimeseriesEngine.getTimeseriesFrom("foo", "bar", 
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

		VariableTimeseriesEngine.updatePointsInTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToAdd, dataPointsToAdd);

		points = VariableTimeseriesEngine.getTimeseriesFrom("foo", "bar", "baz=1,boo=2", 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("foo!", "bar", "baz=1,boo=2", ts2);

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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("fooz", "bar", "baz=1,boo=2", ts2);

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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("foo", "bar!", "baz=1,boo=2", ts2);

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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("foo", "barz", "baz=1,boo=2", ts2);

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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("foo", "bar", "!baz=1,boo=2", ts2);

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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("foo", "bar", "baz=2,boo=2", ts2);

	}

	@Test (expected=PCacheException.class)
	public void testUpdateTimeseries_tsNull() throws PCacheException
	{
			
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", null);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("foo", "bar", "baz=1,boo=2", ts2);

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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> newTimestamps = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};
		
		ArrayList<Object> newDataPoints = new ArrayList<Object>() {{
			
			add("DOWN");
			add("DOWN");
			
		}};

		VariableTimeseries ts2 = new VariableTimeseries(newTimestamps, newDataPoints);

		VariableTimeseriesEngine.updateTimeseries("foo", "bar", "baz=1,boo=2", ts2);

		int countAfterReplace = VariableTimeseriesEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();

		assertEquals(2, countAfterReplace);
		
	}

	@Test (expected=PCacheException.class)
	public void testRemovePointsFromTimeseries_nsInvalid() 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		VariableTimeseriesEngine.removePointsFromTimeseries("foo!", "bar", "baz=1,boo=2", 
				timestampsToRemove);
	}

	@Test (expected=PCacheException.class)
	public void testRemovePointsFromTimeseries_nsNoExist() 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		VariableTimeseriesEngine.removePointsFromTimeseries("fooz", "bar", "baz=1,boo=2", 
				timestampsToRemove);
	}

	@Test (expected=PCacheException.class)
	public void testRemovePointsFromTimeseries_structIdInvalid() 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		VariableTimeseriesEngine.removePointsFromTimeseries("foo", "bar!", "baz=1,boo=2", 
				timestampsToRemove);
	}

	@Test (expected=PCacheException.class)
	public void testRemovePointsFromTimeseries_structIdNoExist() 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		VariableTimeseriesEngine.removePointsFromTimeseries("foo", "barz", "baz=1,boo=2", 
				timestampsToRemove);
	}

	@Test (expected=PCacheException.class)
	public void testRemovePointsFromTimeseries_structInstanceIdInvalid() 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		VariableTimeseriesEngine.removePointsFromTimeseries("foo", "bar", "!baz=1,boo=2", 
				timestampsToRemove);
	}

	@Test (expected=PCacheException.class)
	public void testRemovePointsFromTimeseries_structInstanceIdNoExist() 
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		VariableTimeseriesEngine.removePointsFromTimeseries("foo", "bar", "baz=2,boo=2", 
				timestampsToRemove);
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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);
		
		VariableTimeseriesEngine.addNewNamespace("foo");
		VariableTimeseriesEngine.addNewStructure("foo", "bar", "baz,boo");
		VariableTimeseriesEngine.addNewStructureInstance("foo", "bar", "baz=1,boo=2", ts);

		int countBeforeRemove = VariableTimeseriesEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();

		ArrayList<String> timestampsToRemove = new ArrayList<String>() {{
			
			add("2010-01-07T12:00:00.000+05:30");
			add("2010-01-08T12:00:00.000+05:30");
			
		}};

		VariableTimeseriesEngine.removePointsFromTimeseries("foo", "bar", "baz=1,boo=2", 
				timestampsToRemove);

		int countAfterRemove = VariableTimeseriesEngine.getTimeseries("foo", "bar", 
				"baz=1,boo=2").size();

		assertEquals(countBeforeRemove-2, countAfterRemove);

	}
}
