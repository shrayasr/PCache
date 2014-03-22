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

	// Namespace - Structure ID map. This gives the list of structures that belong to a namespace
	private static HashMap<String, ArrayList<String>> _namespaceToStructsMap = new HashMap<String, ArrayList<String>>();

	// Structure ID - Structure map. This gives the structure object associated to a structure ID
	private static HashMap<String, Structure> _structIdToStructMap = new HashMap<String, Structure>();

	// Structure ID - Structure instances map. This gives the list of structure Instances that belong to a given structure ID
	private static HashMap<String, ArrayList<String>> _structIdToStructInstancesIdMap = new HashMap<String, ArrayList<String>>();

	// Structure Instance - Timeseries map. Gives the timeseries that is associated to the structure instance
	private static HashMap<String, Timeseries> _structInstanceIdToTimeSeriesMap = new HashMap<String, Timeseries>();

	/**
	 * Create a namespace.
	 * @param namespace the namespace that needs to be created. This is what uniquely identifies the namespace in the cache
	 * @throws PCacheException thrown if the namespace already exists
	 */
	public static void createNamespace(String namespace) throws PCacheException {

		long start = System.currentTimeMillis();

		// If the namespace already exists, throw an exception
		if (_namespaceToStructsMap.containsKey(namespace)) {
			throw new PCacheException("Namespace already exists");
		}

		// Put the new namespace into the map, along with a blank arraylist to store the structures under this for the future
		_namespaceToStructsMap.put(namespace, new ArrayList<String>());

		System.out.println("Added namespace " + namespace + " in " + (System.currentTimeMillis() - start) + "ms");
		
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

		long start = System.currentTimeMillis();

		// If the namespace doesn't exist, throw an exception
		if (!_namespaceToStructsMap.containsKey(namespace)) {
			throw new PCacheException("Namespace doesnt exist");
		}

		// Get the list of structure keys in the namespace
		ArrayList<String> structuresInNamespace = _namespaceToStructsMap.get(namespace);

		// Qualify the struct ID with the namespace to make it unique
		String namespaceQualifiedStructId = getNamespaceQualifiedStructId(namespace, structId);

		// Create a structure out of the given properties
		Structure struct = new Structure(namespaceQualifiedStructId, structDefinition);
		
		// If the structure already exists, throw an exception
		if (structuresInNamespace.contains(struct.get_name())) {
			throw new PCacheException("Structure already exists");
		}

		// Add in the new key
		structuresInNamespace.add(struct.get_name());
		
		// Update the list of structure keys for the namespace
		_namespaceToStructsMap.put(namespace, structuresInNamespace);

		// Add a key - structure mapping
		_structIdToStructMap.put(struct.get_name(), struct);

		// Along with the key, add a blank arraylist to store list of structure instances
		_structIdToStructInstancesIdMap.put(struct.get_name(), new ArrayList<String>());
		
		System.out.println("Added struct " + structId + " to namespace " + namespace + " in " + (System.currentTimeMillis() - start) + "ms");

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

		long start = System.currentTimeMillis();

		// If the namespace doesn't exist, throw an exception
		if (!_namespaceToStructsMap.containsKey(namespace)) {
			throw new PCacheException("Namespace doesnt exist");
		}

		// Qualify the structure with the namespace to make it unique
		String namespaceQualifiedStructId = getNamespaceQualifiedStructId(namespace, structId);

		// Get the list of structure keys in the namespace
		ArrayList<String> structuresInNamespace = _namespaceToStructsMap.get(namespace);
		
		// If the structure doesn't exist, throw an exception
		if (!structuresInNamespace.contains(namespaceQualifiedStructId)) {
			throw new PCacheException("Structure doesn't exist");
		}

		// Pick up the structure whose instance is being created
		Structure structInQuestion = _structIdToStructMap.get(namespaceQualifiedStructId);

		// If the instance being created isn't the type of the structure being referred to, throw an exception
		if (!structInQuestion.containsInstance(structInstance)) {
			throw new PCacheException("Instance types don't match.");
		}

		// Create an object key from the namespace, structure ID and the structure instance details
		String structInstanceId = Structure.generateUUID(namespace, structId, structInstance);

		// Pick up the list of structure instances for that structure
		ArrayList<String> structureInstances = _structIdToStructInstancesIdMap.get(namespaceQualifiedStructId);

		// If the object already exists in the list, throw an exception
		if (structureInstances.contains(structInstanceId)) {
			throw new PCacheException("Instance already exists");
		}

		// Add the object key to the list of structure instances
		structureInstances.add(structInstanceId);

		// Update the structure - structure instances map with the new set of structure instances
		_structIdToStructInstancesIdMap.put(namespaceQualifiedStructId, structureInstances);

		// Add the new timeseries to that object key
		_structInstanceIdToTimeSeriesMap.put(structInstanceId, timeseries);

		System.out.println("Created an instance of structure " + structId + " ["+structInstance+"] under namespace " + namespace + " in " + (System.currentTimeMillis() - start) +"ms");

	}

	/**
	 * Qualify the structure ID with the namespace to guarentee uniqueness
	 * @param namespace the namespace that needs to be created. This is what uniquely identifies the namespace in the cache
	 * @param structId an identifier for the structure. This is what uniquely identifies the structure in the namespace
	 * @return
	 */
	private static String getNamespaceQualifiedStructId(String namespace, String structId) {
		return namespace + "." + structId;
	}

}
