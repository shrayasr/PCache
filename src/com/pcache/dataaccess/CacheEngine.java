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
public class CacheEngine {

	// The main tree that stores the entire cache
	private static Node _cacheTree = new Node("cache");

	public static void reInitializeCache() {
		_cacheTree = new Node("cache");
	}
	
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
	public static Timeseries<?> getTimeseries(String namespace, 
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

		// Sanity checks
		exceptIfNamespaceInvalid(namespace);
		exceptIfNamespaceExists(namespace);

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
		
		// Sanity checks
		exceptIfNamespaceInvalid(oldNamespace);
		exceptIfNoNamespaceExists(oldNamespace);

		exceptIfNamespaceInvalid(newNamespace);
		exceptIfNamespaceExists(newNamespace);

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
		
		// Sanity checks
		exceptIfNamespaceInvalid(namespace);
		exceptIfNoNamespaceExists(namespace);

		exceptIfStructureIdInvalid(structureId);
		exceptIfStructureIdExists(namespace, structureId);

		exceptIfStructureDefinitionInvalid(structureDefinition);

		// Pick up the Node associated with the namespace
		Node namespaceNode = _cacheTree.getChild(namespace);
		
		// Create the structure object

		/*
		 * THIS IS IMPORTANT. 
		 * 
		 * Using String as the type of the object always. When self adaptive
		 * store comes into play, this part will change
		 * 
		 * Going with strings as of now so that things will be easy from a 
		 * pushing shit out perspective. 
		 * 
		 * TODO self adaptive store
		 */

		Structure structure = new Structure(structureId, 
				String.class, structureDefinition);

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

		// Sanity checks
		exceptIfNamespaceInvalid(namespace);
		exceptIfNoNamespaceExists(namespace);

		exceptIfStructureIdInvalid(oldStructureId);
		exceptIfNoStructureIdExists(namespace, oldStructureId);

		exceptIfStructureIdExists(namespace, newStructureId);
		exceptIfStructureIdInvalid(newStructureId);


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
	 * @param <T>
	 * @param namespace the namespace under which the structure is associated
	 * @param structureId the ID of the structure to whom the instance is to
	 * 			be associated with
	 * @param structureInstanceId the ID of the structure instance 
	 * @param timeseries the timeseries object to associate with that instance
	 * @throws PCacheException 
	 */
	public static <T> void addNewStructureInstance(String namespace, 
			String structureId, String structureInstanceId, 
			Timeseries<T> timeseries) throws PCacheException {

		// Sanity checks
		exceptIfNamespaceInvalid(namespace);
		exceptIfNoNamespaceExists(namespace);

		exceptIfStructureIdInvalid(structureId);
		exceptIfNoStructureIdExists(namespace, structureId);

		exceptIfStructureInstanceIdInvalid(structureInstanceId);
		exceptIfStructureInstanceIdExists(namespace, structureId, 
				structureInstanceId);

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

	/**
	 * Check if the namespace is invalid. Namespaces can only contain "a-z" 
	 * "A-Z" "0-9" and "_"
	 * @param namespace the namespace to check for validity
	 * @throws PCacheException thrown if the namespace isn't of the valid
	 * 			format
	 */
	private static void exceptIfNamespaceInvalid(String namespace) 
			throws PCacheException {

		// If the namespace doesn't match set pattern, except
		if (!namespace.matches("[a-zA-Z0-9_]+")) {
			throw new PCacheException("Namespace can only contain alpabets"
					+ ", underscores and numbers");
		}
	}

	/**
	 * Check if the namespace already exists in the cache.
	 * @param namespace the namespace to check for existance
	 * @throws PCacheException thrown if the namespace already exists in the
	 * 			cache
	 */
	private static void exceptIfNamespaceExists(String namespace) 
			throws PCacheException {

		// If the namespace already exists in the cache, except
		if (_cacheTree.containsChild(namespace)) {
			throw new PCacheException("Namespace already exists");
		}
	}

	/**
	 * Check if the namespace doesn't exist in the cache.
	 * @param namespace the namespace to check for existance
	 * @throws PCacheException thrown if the namespace doesn't exist in the 
	 * 			cache
	 */
	private static void exceptIfNoNamespaceExists(String namespace) 
			throws PCacheException {

		// If the namespace already exists in the cache, except
		if (!_cacheTree.containsChild(namespace)) {
			throw new PCacheException("Namespace doesn't exist");
		}
	}

	/**
	 * Check if the structure definition is invalid. Structures can only contain
	 * "a-z" "A-Z" "0-9" "," and "_"
	 * @param structureDefinition the structure definition to check for 
	 * 			validity
	 * @throws PCacheException thrown if the structure definition is invalid
	 */
	private static void exceptIfStructureDefinitionInvalid(
			String structureDefinition) throws PCacheException {

		// If the structureID doesn't match required pattern, except
		if (!structureDefinition.matches("[a-zA-Z0-9_,]+")) {
			throw new PCacheException("Structure definition can only contain "
					+ "alpabets, underscores and numbers");
		}
		
	}

	/**
	 * Check if the structure ID is invalid. Structure IDs can only contain
	 * "a-z" "A-Z" "0-9" and "_"
	 * @param structureId the structure ID to check for validity
	 * @throws PCacheException thrown if the structure ID is invalid
	 */
	private static void exceptIfStructureIdInvalid(String structureId) 
			throws PCacheException {

		// If the structureID doesn't match required pattern, except
		if (!structureId.matches("[a-zA-Z0-9_]+")) {
			throw new PCacheException("Structure ID can only contain alpabets"
					+ ", underscores and numbers");
		}
		
	}

	/**
	 * Check if the structure ID already exists under the namespace
	 * @param namespace the namespace to check under
	 * @param structureId the structure ID to look for under the namespace
	 * @throws PCacheException thrown if the structure ID exists under that 
	 * 			namespace
	 */
	private static void exceptIfStructureIdExists(String namespace,
			String structureId) throws PCacheException {

		// If the structure ID is already associated with the namespace, except
		if (_cacheTree.getChild(namespace).containsChild(structureId)) {
			throw new PCacheException("Structure ID already exists under "
					+ "given namespace");
		}
	}

	/**
	 * Check if the structure ID doesn't exist under the namespace
	 * @param namespace the namespace to check under
	 * @param structureId the structure ID to look for under the namespace
	 * @throws PCacheException thrown if the structure ID doesn't exist under
	 * 			that namespace
	 */
	private static void exceptIfNoStructureIdExists(String namespace,
			String structureId) throws PCacheException {

		// If the structure ID is already associated with the namespace, except
		if (!_cacheTree.getChild(namespace).containsChild(structureId)) {
			throw new PCacheException("Structure ID already exists under "
					+ "given namespace");
		}
	}

	/**
	 * Check if the structure instance ID is valid. Structure instance IDs can
	 * contain only "a-z" "A-Z" "0-9" "_" "=" and ","
	 * @param structureInstanceId the structure instance ID to check for 
	 * @throws PCacheException thrown if the structure instance ID is invalid
	 */
	private static void exceptIfStructureInstanceIdInvalid(
			String structureInstanceId) throws PCacheException {

		// Except if the structure instance ID doesn't match pattern
		if (!structureInstanceId.matches("[a-zA-Z0-9_,=]+")) {
			throw new PCacheException("Structure instance ID can only contain"
					+ " alpabets, underscores and numbers");
		}
		
	}

	/**
	 * Check if the structure instance ID exists associated to a structure under
	 * the given namespace
	 * @param namespace the namespace to look under
	 * @param structureId the structure ID to which the instance is associated
	 * @param structureInstanceId the structure instance ID to check for
	 * @throws PCacheException thrown if the structure instance ID already 
	 * 			associated to that structure under the namespace
	 */
	private static void exceptIfStructureInstanceIdExists(String namespace,
			String structureId, String structureInstanceId)
					throws PCacheException {
		
		// If the structure instance ID already exists for the given structure
		// under that namespace, except
		if (_cacheTree.getChild(namespace).getChild(structureId)
				.containsChild(structureInstanceId)) {

			throw new PCacheException("Structure instance ID already exists "
					+ "under given namespace");
		}
	}

}
