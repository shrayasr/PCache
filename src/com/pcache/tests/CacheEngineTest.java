package com.pcache.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

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

		Timeseries<String> ts = new Timeseries<String>(timestamps, dataPoints);

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
	public void testRemoveStructureInstance_nsInvalid() throws PCacheException 
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
}
