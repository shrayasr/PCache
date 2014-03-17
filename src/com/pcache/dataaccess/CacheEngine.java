package com.pcache.dataaccess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.pcache.DO.Structure;
import com.pcache.exceptions.PCacheException;

public class CacheEngine {

	private static HashMap<String, ArrayList<Structure>> _namespaceStructureMap;

	public static int sizeOfNamespace(String id) throws PCacheException {
		
		if (_namespaceStructureMap.containsKey(id)) {
			
			return _namespaceStructureMap.get(id).size();
			
		} else {
			
			throw new PCacheException("Namespace not added, add namespace first");
		}
		
	}

	public static void addNamespace(String id) {

		if (!_namespaceStructureMap.containsKey(id)) {
			_namespaceStructureMap.put(id, new ArrayList<Structure>());
		}

	}

	public static ArrayList<String> getAllNamespaces() {
		
		ArrayList<String> result = new ArrayList<String>(_namespaceStructureMap.keySet());
		return result;

	}

	public static void addStructure(String namespaceId, Structure structure) throws PCacheException {

		if (_namespaceStructureMap.containsKey(namespaceId)) {
			
			ArrayList<Structure> structs = _namespaceStructureMap.get(namespaceId);
			structs.add(structure);
			_namespaceStructureMap.put(namespaceId, structs);

		} else {
			
			throw new PCacheException("Namespace not added, add namespace first");
		}
		
	}

	public static ArrayList<Structure> getAllStructuresForNamespace(String id) throws PCacheException {
		
		if (_namespaceStructureMap.containsKey(id)) {

			return _namespaceStructureMap.get(id);
			
		} else {
			
			throw new PCacheException("Namespace not added, add namespace first");
		}

	}

}
