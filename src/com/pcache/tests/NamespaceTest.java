package com.pcache.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import com.pcache.DO.Namespace;

public class NamespaceTest {

	@Test
	public void testEqualsObject() {

		Namespace obj1 = new Namespace("hello");
		Namespace obj2 = new Namespace("hello");

		assertEquals(obj1.equals(obj1), true);
		assertEquals(obj1.equals(obj2), true);
		assertEquals(obj2.equals(obj1), true);

	}

}
