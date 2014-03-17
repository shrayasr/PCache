package com.pcache.DO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Data Object for storing the list of structures in the cache
 * @author shrayas
 *
 */
public class Structure {

	private String _name;
	private String _namespaceId;
	//TODO add datatype field as well
	private ArrayList<String> _objectIds;
	private String _structure;

	/**
	 * Constructor. Initialize a new structure
	 * @param name of the structure (Unique)
	 * @param namespaceId, the namespace the structure is associated to
	 * @param objectIds the list of objects associated to this structure (null at first)
	 * @param structureDefinition, the structure that is going to be stored. It should be a comma separated list of keys
	 */
	public Structure (String name, String namespaceId, ArrayList<String> objectIds, String structureDefinition) {
		this._name = name;
		this._namespaceId = namespaceId;
		this._objectIds = objectIds;
		this._structure = cleanStructure(structureDefinition);
	}

	private String cleanStructure(String dirtyStructure) {

		String cleanStructure = "";

		// Split by comma and throw into an arraylist for easier sorting
		ArrayList<String> unSortedList = new ArrayList<String>(Arrays.asList(dirtyStructure.split(",")));
		
		// Sort the dirty array
		Collections.sort(unSortedList);

		for (String cleanStructurePart : unSortedList) {
			cleanStructure += cleanStructurePart + ",";
		}

		// Remove the trailing comma and return
		return cleanStructure.substring(0, cleanStructure.length()-1);
	}

	public String get_structure() {
		return this._structure;
	}

}
