package com.pcache.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;

import com.pcache.DO.Structure;
import com.pcache.DO.Timeseries;
import com.pcache.exceptions.PCacheException;

/**
 * The central point of the cache. The data store. 
 */
public class CacheEngine {

	private static HashMap<String, ArrayList<String>> _namespaceKeyStructMap = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, Structure> _structKeyStructMap = new HashMap<String, Structure>();
	private static HashMap<String, ArrayList<String>> _structKeyStructInstancesMap = new HashMap<String, ArrayList<String>>();
	private static HashMap<String, Timeseries> _structInstanceKeyTimeSeriesMap = new HashMap<String, Timeseries>();

	/**
	 * Create a namespace.
	 * @param namespace the namespace that needs to be created. This is what uniquely identifies the namespace in the cache
	 * @throws PCacheException thrown if the namespace already exists
	 */
	public static void createNamespace(String namespace) throws PCacheException {

		// If the namespace already exists, throw an exception
		if (_namespaceKeyStructMap.containsKey(namespace)) {
			throw new PCacheException("Namespace already exists");
		}

		// Put the new namespace into the map, along with a blank arraylist to store the structures under this for the future
		_namespaceKeyStructMap.put(namespace, new ArrayList<String>());
		
	}

	/**
	 * Add a structure to a given namespace. 
	 * @param namespace the namespace that needs to be created. This is what uniquely identifies the namespace in the cache
	 * @param structId an identifier for the structure. This is what uniquely identifies the structure in the namespace
	 * @param structDefinition The definition for the structure. This is essentially a comma seperated list of the identifiers.
	 * Eg: "sensor_type,sensor_name"
	 * @throws PCacheException thrown if the namespace doesn't exist or if the structure already exists
	 */
	public static void addStructureToNamespace(String namespace, String structId, String structDefinition) throws PCacheException {

		// If the namespace doesn't exist, throw an exception
		if (!_namespaceKeyStructMap.containsKey(namespace)) {
			throw new PCacheException("Namespace doesnt exist");
		}

		// Get the list of structure keys in the namespace
		ArrayList<String> structuresInNamespace = _namespaceKeyStructMap.get(namespace);

		// Create a structure out of the given properties
		Structure struct = new Structure(structId, structDefinition);
		
		// If the structure already exists, throw an exception
		if (structuresInNamespace.contains(struct.get_name())) {
			throw new PCacheException("Structure already exists");
		}

		// Add in the new key
		structuresInNamespace.add(struct.get_name());
		
		// Update the list of structure keys for the namespace
		_namespaceKeyStructMap.put(namespace, structuresInNamespace);

		// Add a key - structure mapping
		_structKeyStructMap.put(struct.get_name(), struct);

		// Along with the key, add a blank arraylist to store list of structure instances
		_structKeyStructInstancesMap.put(struct.get_name(), new ArrayList<String>());
		
	}

	/**
	 * Create an instance of an already existing structure in a namespace and associate a Timeseries to it
	 * @param namespace the namespace that needs to be created. This is what uniquely identifies the namespace in the cache
	 * @param structId an identifier for the structure. This is what uniquely identifies the structure in the namespace
	 * @param structInstance an instance of that structure. This is a comma separated list of key=value pairs.
	 * Eg: sensor_type=heat,sensor_name=S451_heat
	 * @param timeseries the timeseries to bind to the instance
	 * @throws PCacheException thrown if the namespace doesn't exist or the structure doesn't exist or the instance types don't match or if the instance already exists
	 */
	public static void createStructureInstance(String namespace, String structId, String structInstance, Timeseries timeseries) throws PCacheException {

		// If the namespace doesn't exist, throw an exception
		if (!_namespaceKeyStructMap.containsKey(namespace)) {
			throw new PCacheException("Namespace doesnt exist");
		}

		// Get the list of structure keys in the namespace
		ArrayList<String> structuresInNamespace = _namespaceKeyStructMap.get(namespace);
		
		// If the structure doesn't exist, throw an exception
		if (!structuresInNamespace.contains(structId)) {
			throw new PCacheException("Structure doesn't exist");
		}

		// Pick up the structure whose instance is being created
		Structure structInQuestion = _structKeyStructMap.get(structId);

		// If the instance being created isn't the type of the structure being referred to, throw an exception
		if (!structInQuestion.containsInstance(structInstance)) {
			throw new PCacheException("Instance types don't match.");
		}

		// Create an object key from the namespace, structure ID and the structure instance details
		String objectKey = Structure.generateUUID(namespace, structId, structInstance);

		// Pick up the list of structure instance for that structure
		ArrayList<String> structureInstances = _structKeyStructInstancesMap.get(structId);

		// If the object already exists in the list, throw an exception
		if (structureInstances.contains(objectKey)) {
			throw new PCacheException("Instance already exists");
		}

		// Add the object key to the list of structure instances
		structureInstances.add(objectKey);

		// Update the structure - structure instances map with the new set of structure instances
		_structKeyStructInstancesMap.put(structId, structureInstances);

		// Add the new timeseries to that object key
		_structInstanceKeyTimeSeriesMap.put(objectKey, timeseries);

	}

}
