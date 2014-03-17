package com.pcache.dataaccess;

import java.util.ArrayList;

import javax.management.ImmutableDescriptor;

import com.pcache.DO.Namespace;
import com.pcache.exceptions.PCacheException;

/**
 * Represents the set of all namespaces in the cache
 * @author shrayas
 *
 */
public class NamespaceEngine {

	// Storage point for all namespaces
	private static ArrayList<Namespace> _namespaces = new ArrayList<Namespace>();



	/**
	 * No. of namespaces in the cache
	 * @return number of namespace in the cache
	 */
	public static int size() {
		return _namespaces.size();
	}

	/**
	 * Add a new namespace
	 * @param id, the unique identifier for the namespace
	 * @return the new namespace created
	 * @throws PCacheException, Exception is thrown if the namespace already exists
	 */
	public static Namespace addNamespace(String id) throws PCacheException {

		if (!exists(id)) {

			// If namespace doesn't exist, create it and return it
			Namespace n = new Namespace(id);
			_namespaces.add(n);
			return n;

		} else {

			// If it does, then throw an exception
			throw new PCacheException("Namespace already exists");

		}
	}

	/**
	 * Fetch a namespace 
	 * @param id, the unique identifier for the namespace
	 * @return the namespace fetched, if any
	 * @throws PCacheException, Exception is thrown if the namespace doesn't exist
	 */
	public static Namespace getNamespace(String id) throws PCacheException {

		if (exists(id)) {

			// If the namespace exists, return it
			Namespace n = new Namespace(id);
			return _namespaces.get(_namespaces.indexOf(n));
			
		} else {
			
			// If it doesn't throw an exception
			throw new PCacheException("Namespace doesn't exist");
		}
		
	}

	/**
	 * Change the ID of a namespace
	 * @param oldId, the unique identifier for the current namespace
	 * @param newId, what the unique identifier should be
	 * @throws PCacheException, Exception is thrown if the namespace doesn't exist
	 */
	public static void editNamespace(String oldId, String newId) throws PCacheException {
		
		if (!exists(newId)) {

			// If the new id doesn't already exist, Get the current namespace
			Namespace ns = getNamespace(oldId);
	
			// change its ID to a new one
			ns.set_id(newId);
	
			// Replace it back in its original position
			_namespaces.set(_namespaces.indexOf(ns), ns);
		
		} else {
			
			// If new id already exists, throw an exception
			throw new PCacheException("New namespace already exist");
		}
		
	}

	/**
	 * Delete a namespace and its associated structures and timeseries' from the cache
	 * @param id, the unique identifier of the namespace to delete
	 * @throws PCacheException, Exception thrown if the namespace doesn't exist
	 */
	public static void deleteNamespace(String id) throws PCacheException {

		if (exists(id)) {

			// If the namespace exists, delete it
			Namespace n = new Namespace(id);
			_namespaces.remove(_namespaces.indexOf(n));

			//TODO remove the associated structures as well
			
		} else {
			
			// Else, throw an exception
			throw new PCacheException("Namespace doesn't exist");
		}
		
	}

	/**
	 * Does the namespace exist?
	 * @param id, the unique identifier tied to the namespace to look for
	 * @return true or false depending on the existence
	 */
	public static boolean exists(String id) {
		Namespace n = new Namespace(id);
		return _namespaces.contains(n);
	}

}
