package com.pcache.dataaccess;

import com.pcache.DO.Node;
import com.pcache.DO.Structure;
import com.pcache.DO.Timeseries;
import com.pcache.exceptions.PCacheException;

/**
 * The main access point for the Cache. All Cache operations (namespace add, 
 * modify a structure, associate a timeseries, etc) are to be done via here.
 * 
 * Actions supported on the different nodes are detailed in the table below.
 * +--------------------+---+---+---+---+
 * | NODE TYPE          | C | R | U | D |
 * +--------------------+---+---+---+---+
 * | Namespace          | X | X | X | X |
 * +--------------------+---+---+---+---+
 * | Structure          | X | X |   | X |
 * +--------------------+---+---+---+---+
 * | Structure Instance | X | X | X | X |
 * +--------------------+---+---+---+---+
 * 
 * Modification of the structure definition isn't allowed because that would
 * reflect on the state of its instances and bring about an instability. 
 * However, structure names can be modified.
 * 
 */
public class CacheEngineV2 {

	// The main tree that stores the entire cache
	private static Node _cacheTree = new Node("cache");

	/**
	 * Get the Timeseries associated to a Structure Instance Node
	 * @param namespace the namespace being referred to in the cache
	 * @param structureId the ID of the structure under the namespace
	 * @param structureInstanceId the ID of the specific instance under the 
	 * 			structure 
	 * @return the Timeseries for the given 
	 * 			namespace.structure.structureInstance combination
	 * @throws PCacheException when either namespaceId, structureId or
	 * 			structureInstanceId don't exist
	 */
	public static Timeseries getTimeseries(String namespace, 
			String structureId, String structureInstanceId) 
					throws PCacheException {

		try {

			// Try to return the timeseries requested
			return _cacheTree.getChild(namespace).getChild(structureId)
				.getChild(structureInstanceId).getTimeseries();

		} catch (NullPointerException ex) {
			// If some part of it isn't returned, it will return null and hence
			// following parts of the chain will throw a null pointer exception
			// which is caught here and a PCacheException is thrown
			throw new PCacheException("Namespace/Structure/Structure Instance"
					+ " doesn't exist", ex);
		}
	}

	/**
	 * Create a namespace.
	 * @param namespace the namespace that needs to be created. This is what 
	 * 			uniquely identifies the namespace in the cache
	 * @throws PCacheException thrown if the namespace already exists
	 */
	public static void addNewNamespace(String namespace) 
			throws PCacheException {
		
		// If the namespace already exists in the cache, except
		if (_cacheTree.containsChild(namespace)) {
			throw new PCacheException("Namespace already exists");
		}

		// Create a new node to represent the namespace
		Node newNamespace = new Node(namespace);

		// Add it as a child to the ROOT cache node
		_cacheTree.addChild(newNamespace);
		
	}

	/**
	 * Renames an existing namespace
	 * @param oldNamespace the name of the current namespace
	 * @param newNamespace the new name for the namespace 
	 * @throws PCacheException thrown if the namespace to rename doesn't exist
	 */
	public static void renameNamespace(String oldNamespace, 
			String newNamespace) throws PCacheException {
		
		// If the cache doesn't contain the namespace, except
		if (!_cacheTree.containsChild(oldNamespace)) {
			throw new PCacheException("Namespace doesn't exist");
		}

		// Pick up the Node associated to the old namespace
		Node namespaceNode = _cacheTree.getChild(oldNamespace);

		// Set its new name
		namespaceNode.setName(newNamespace);

		// Remove the old namespace 
		_cacheTree.removeChild(oldNamespace);

		// Add it back with the name name
		_cacheTree.addChild(namespaceNode);
	}

	/**
	 * Add a new structure to an existing namespace
	 * @param namespace the namespace to assign the structure to
	 * @param structureId the ID of the structure that is being created
	 * @param structureDefinition The definition for the structure. This is 
	 * 			essentially a comma seperated list of the identifiers. 
	 * 			Eg: "sensor_type,sensor_name"
	 * @throws PCacheException thrown if the namespace doesn't exist or if the
	 * 			Structure ID that is being created already exists
	 */
	public static void addNewStructure(String namespace, String structureId, 
			String structureDefinition) throws PCacheException {
		
		// If the namespace doesn't exist in the cache, except
		if (!_cacheTree.containsChild(namespace)) {
			throw new PCacheException("Namespace doesn't exist");
		}

		// If the structure ID is already associated with the namespace, except
		if (_cacheTree.getChild(namespace).containsChild(structureId)) {
			throw new PCacheException("Structure ID already exists under "
					+ "given namespace");
		}

		// Pick up the Node associated with the namespace
		Node namespaceNode = _cacheTree.getChild(namespace);
		
		// Create the structure object
		Structure structure = new Structure(structureId, structureDefinition);

		// Create a new node for the structure giving the definition
		Node newStructure = new Node(structureId, structure);

		// Add the structure node as a child to the namespace node
		namespaceNode.addChild(newStructure);

		// Update the namespace with the new node containing the new structure
		_cacheTree.replaceChild(namespace, namespaceNode);
	}

	/**
	 * Rename the ID of an existing structure under an existing namespace
	 * @param namespace the namespace the structure is under
	 * @param oldStructureId ID of the existing structure
	 * @param newStructureId ID of the new structure
	 * @throws PCacheException
	 */
	public static void renameStructure(String namespace, 
			String oldStructureId, String newStructureId) 
					throws PCacheException {

		// If the namespace doesn't exist, except
		if (!_cacheTree.containsChild(namespace)) {
			throw new PCacheException("Namespace doesn't exist");
		}

		// If the structure doesn't exist, except
		if (!_cacheTree.getChild(namespace).containsChild(oldStructureId)) {
			throw new PCacheException("Structure doesn't exist");
		}

		// Pick up the Node associated with that namespace and structure
		Node structureNode = _cacheTree.getChild(namespace)
				.getChild(oldStructureId);

		// Change its name
		structureNode.setName(newStructureId);

		// Remove the structure the list of children of the namespace
		_cacheTree.getChild(namespace).removeChild(oldStructureId);

		// And add it in
		_cacheTree.getChild(namespace).addChild(structureNode);

	}

	/**
	 * Add a new Instance of an existing structure 
	 * @param namespace the namespace under which the structure is associated
	 * @param structureId the ID of the structure to whom the instance is to
	 * 			be associated with
	 * @param structureInstanceId the ID of the structure instance 
	 * @param timeseries the timeseries object to associate with that instance
	 * @throws PCacheException 
	 */
	public static void addNewStructureInstance(String namespace, 
			String structureId, String structureInstanceId, 
			Timeseries timeseries) throws PCacheException {

		// If the namespace doesn't exist, except
		if (!_cacheTree.containsChild(namespace)) {
			throw new PCacheException("Namespace doesn't exist");
		}

		// If the structure doesn't exist, except
		if (!_cacheTree.getChild(namespace).containsChild(structureId)) {
			throw new PCacheException("Structure doesn't exist");
		}

		// Pick up the node associated with the structure
		Node structureNode = _cacheTree.getChild(namespace).
				getChild(structureId);

		// Pick up the structure 
		Structure structure = structureNode.getStructure();

		// If the instance being created isn't of the type of the base structure
		// then, except
		if (!structure.containsInstance(structureInstanceId)) {
			throw new PCacheException("Instance isn't of the same structure");
		}

		// Create the required node with the timeseries
		Node newStructureInstance = new Node(structureInstanceId, timeseries);

		// Add it as a child to the structure
		structureNode.addChild(newStructureInstance);

		// Update the current structure with the new one
		_cacheTree.getChild(namespace).replaceChild(structureId, structureNode);
		
	}

}
