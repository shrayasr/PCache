package com.pcache.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pcache.DO.Namespace;
import com.pcache.dataaccess.NamespaceEngine;
import com.pcache.exceptions.PCacheException;

public class NamespaceEngineTest {

	@Test
	public void testAddNamespace() {

		try {

			NamespaceEngine.addNamespace("hello");
			assertEquals(NamespaceEngine.exists("hello"), true);

			// Cleanup
			NamespaceEngine.deleteNamespace("hello");

		} catch (PCacheException ex) {

			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void testGetNamespace() {
		
		try {

			// What is expected
			Namespace expected = new Namespace("hello");

			NamespaceEngine.addNamespace("hello");
			Namespace actual = NamespaceEngine.getNamespace("hello");
			assertEquals(expected, actual);

			// Cleanup
			NamespaceEngine.deleteNamespace("hello");
			
		} catch (PCacheException ex) {

			ex.printStackTrace();
			fail(ex.getMessage());
		}
	}

	@Test
	public void testEditNamespace() {

		try {

			// What is expected
			Namespace expected = new Namespace("olleh");
			
			NamespaceEngine.addNamespace("hello");
			NamespaceEngine.editNamespace("hello", "olleh");
			Namespace actual = NamespaceEngine.getNamespace("olleh");

			assertEquals(expected, actual);

			// Cleanup
			NamespaceEngine.deleteNamespace("olleh");

		} catch (PCacheException ex) {

			ex.printStackTrace();
			fail(ex.getMessage());
			
		}

	}

	@Test
	public void testDeleteNamespace() {

		try {

			NamespaceEngine.addNamespace("hello");
			NamespaceEngine.deleteNamespace("hello");
			
			assertEquals(NamespaceEngine.size(), 0);

		} catch (PCacheException ex) {

			ex.printStackTrace();
			fail(ex.getMessage());
		}

	}

}
