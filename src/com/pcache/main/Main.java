package com.pcache.main;

import com.pcache.dataaccess.NamespaceEngine;
import com.pcache.exceptions.PCacheException;

public class Main {

	public static void main(String[] args) throws PCacheException {

		NamespaceEngine.addNamespace("sentinel");
		
	}

}
