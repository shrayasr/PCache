package com.pcache.dataaccess;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import sun.misc.Cache;

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
	public void testAddNewStructure_structDefnInvalid() throws PCacheException
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
	
}
