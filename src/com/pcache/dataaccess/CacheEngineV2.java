package com.pcache.dataaccess;

import com.pcache.DO.Node;
import com.pcache.DO.Timeseries;
import com.pcache.exceptions.PCacheException;

public class CacheEngineV2 {

	private static Node _cacheTree = new Node("cache", null);

	public static Timeseries get(String path) {
		return _cacheTree.find(path);
	}

	public static void addNewNamespace(String namespace) throws PCacheException {
		
		if (_cacheTree.containsChild(namespace)) {
			throw new PCacheException("Namespace already exists");
		}

		Node newNamespace = new Node(namespace, null);
		_cacheTree.addChild(newNamespace);
		
	}

	public static void renameNamespace(String oldNamespace, String newNamespace) throws PCacheException {
		
		if (!_cacheTree.containsChild(oldNamespace)) {
			throw new PCacheException("Namespace doesn't exist");
		}

		Node namespaceNode = _cacheTree.getChild(oldNamespace);
		namespaceNode.setName(newNamespace);

		_cacheTree.removeChild(oldNamespace);
		_cacheTree.addChild(namespaceNode);
	}

	public static void addNewStructure(String namespace, String structureId) {
		
		Node namespaceNode = _cacheTree.getChild(namespace);
		Node newStructure = new Node(structureId, null);
		namespaceNode.addChild(newStructure);
		_cacheTree.replaceChild(namespace, namespaceNode);
	}

	public static void renameStructure(String namespace, String oldStructureId, String newStructureId) throws PCacheException {

		if (!_cacheTree.getChild(namespace).containsChild(oldStructureId)) {
			throw new PCacheException("Structure doesn't exist");
		}

		Node structureNode = _cacheTree.getChild(namespace).getChild(oldStructureId);
		structureNode.setName(newStructureId);

		_cacheTree.getChild(namespace).removeChild(oldStructureId);
		_cacheTree.getChild(namespace).addChild(structureNode);

	}

	public static void addNewStructureInstance(String namespace, String structureId, String structureInstanceId, Timeseries timeseries) {

		Node structureNode = _cacheTree.getChild(namespace).getChild(structureId);
		Node newStructureInstance = new Node(structureInstanceId, timeseries);
		structureNode.addChild(newStructureInstance);
		_cacheTree.getChild(namespace).replaceChild(structureId, structureNode);
		
	}

}


// sentinel.filmstrip(tid,sid).tid=1,sid=2:TS