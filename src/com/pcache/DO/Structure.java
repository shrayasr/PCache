package com.pcache.DO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Data Object for storing the list of structures in the cache
 */
public class Structure {

	private String _name;
	//TODO add datatype field as well
	private String _structure;

	/**
	 * Constructor. Initialize a new structure
	 * @param name of the structure (Unique)
	 * @param structureDefinition, the structure that is going to be stored. It should be a comma separated list of keys
	 */
	public Structure (String name, String structureDefinition) {
		this._name = name;
		this._structure = cleanStructure(structureDefinition);
	}

	/**
	 * Get a unique ID for the combination
	 * @param namespace the namespace that needs to be created. This is what uniquely identifies the namespace in the cache
	 * @param structId an identifier for the structure. This is what uniquely identifies the structure in the namespace
	 * @param structInstance an instance of that structure. This is a comma separated list of key=value pairs.
	 * Eg: sensor_type=heat,sensor_name=S451_heat
	 * @return the Unique ID
	 */
	public static String generateUUID(String namespace, String structId, String structInstance) {
		return namespace + "." + structId + "." + structInstance;
	}

	/**
	 * Check if the given structure instance can be a part of the structure
	 * @param structInstance an instance of that structure. This is a comma separated list of key=value pairs.
	 * Eg: sensor_type=heat,sensor_name=S451_heat
	 * @return true or false based on the validity
	 */
	public boolean containsInstance(String structInstance) {
		String structureFromInstance = extractStructureFromInstance(structInstance);
		return (this._structure.equals(structureFromInstance));
	}

	/**
	 * Get the base structure from the given structure instance
	 * @param dirtyStructure the dirty structure instance ID to extract the base structure from
	 * @return the base structure
	 */
	private String extractStructureFromInstance(String dirtyStructure) {

		// Clean the structure first (with the K=V pairs)
		String cleanInstanceStructure = cleanStructure(dirtyStructure);

		// Extract the individual parts
		ArrayList<String> cleanInstanceStructureParts = new ArrayList<String>(Arrays.asList(cleanInstanceStructure.split(",")));

		String structureFromInstance = "";
		
		// Go through the list of parts, only extracting the Keys from the K=V pairs
		for (String cleanInstanceStructurePart : cleanInstanceStructureParts) {

			if (cleanInstanceStructurePart.contains("=")) {

				// Append the extracted parts to a string
				structureFromInstance += cleanInstanceStructurePart.substring(0,cleanInstanceStructurePart.indexOf("=")) + ",";
			}
		}

		// Remove the trailing comma and return
		return structureFromInstance.substring(0,structureFromInstance.length()-1);

	}

	/**
	 * Clean the structure by organizing all its parts in alphabetical order
	 * @param dirtyStructure the dirty structure to clean
	 * @return the cleaned structure
	 */
	private String cleanStructure(String dirtyStructure) {

		String cleanStructure = "";

		// Split by comma and throw into an arraylist for easier sorting
		ArrayList<String> unSortedList = new ArrayList<String>(Arrays.asList(dirtyStructure.split(",")));
		
		// Sort the dirty array
		Collections.sort(unSortedList);

		// Append the sorted parts to a string
		for (String cleanStructurePart : unSortedList) {
			cleanStructure += cleanStructurePart + ",";
		}

		// Remove the trailing comma and return
		return cleanStructure.substring(0, cleanStructure.length()-1);
	}


	// GETTERS
	public String get_structure() {
		return this._structure;
	}

	public String get_name() {
		return this._name;
	}


	// OVERRIDES

	// Overriding equals and hashcode allows us to call .equals on an object of type "structure"
	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Structure s = (Structure) obj;

		return ((_name == s._name) && (_structure == s._structure));

	}

	@Override
	public int hashCode() {

		final int prime = 27;
		
		int result = 1;
		
		result = prime * result + ((_name == null ? 0 : _name.hashCode()));
		result = prime * result + ((_structure == null ? 0 : _structure.hashCode()));

		return result;

	}

}
