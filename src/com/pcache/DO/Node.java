package com.pcache.DO;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.pcache.DO.timeseries.VariableTimeseries;

/**
 * The cache is modeled as a tree. It is a basic set of Node objects linked with
 * each other. Heres what it looks like pictorially: 
 * 
 * ROOT 
 * |
 * +--NAMESPACE 
 * |  |
 * |  +--STRUCTURE 
 * |  |
 * |  +--STRUCTURE 
 * |     |
 * |     +--STRUCTURE_INSTANCE 
 * |     |
 * |     +--STRUCTURE_INSTANCE 
 * |
 * +--NAMESPACE 
 * 
 * Each node has 4 properties:
 * 		1. Name
 * 		2. Structure object
 * 		3. Timeseries object
 * 		4. Set of children
 * 
 * The ROOT node is the main cache node. It has no data associated to it, no
 * name even
 * 
 * The NAMESPACE node is what contains the highest level of classification in
 * the cache, the namespace. The "Name" property of this node is set to the 
 * namespace identifier
 * 
 * The STRUCTURE node is what defines the structure under a namespace. The
 * "Name" is set to the structure identifier and the structure object has the
 * structure definition for this particular structure 
 * 
 * The STRUCTURE_INSTANCE node is a particular instance of the STRUCTURE. It 
 * should follow the definition set in the STRUCTURE node that it is associated
 * to. This is the last leaf in the tree and to it would be associated the 
 * Timeseries dataset
 * 
 */
public class Node {

	// Properties of the Node
	private String _name;
	private Map<String, Node> _children;
	private Structure _structure;
	private VariableTimeseries _timeseries;

	/**
	 * Constructor. Initialize a barebones node
	 * @param name of the node
	 */
	public Node(String name) {
		this._name = name;
		this._structure = null;
		this._timeseries = null;
		this._children = new HashMap<String, Node>();
	}

	/**
	 * Constructor. Initialize a node with a Timeseries associated to it
	 * @param <T>
	 * @param name of the node
	 * @param timeseries the data to associate with the node
	 */
	public Node(String name, VariableTimeseries timeseries) {
        this._name = name;
        this._structure = null;
		this._timeseries = timeseries;
		this._children = new HashMap<String, Node>();
	}

	/**
	 * Constructor. Initialize a node with a Structure associated to it
	 * @param name of the node
	 * @param structure definition to associate with the node
	 */
	public Node(String name, Structure structure) {
        this._name = name;
        this._structure = structure;
		this._timeseries = null;
		this._children = new HashMap<String, Node>();
	}

	/* GETTERS ********************/

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public VariableTimeseries getTimeseries() {
		return this._timeseries;
	}

	public void setTimeseries(VariableTimeseries ts) {
		this._timeseries = ts;
	}

	public Structure getStructure() {
		return this._structure;
	}

	public Node getChild(String name) { 
		return this._children.get(name);
	}

	public Set<Node> getChildren() {
		return Collections.unmodifiableSet(new HashSet<Node>(this._children
				.values()));
	}

	/******************************/

	/**
	 * Add a child to the given node
	 * @param child Node to be added
	 */
	public void addChild(Node child) {
		this._children.put(child.getName(), child);
	}

	/**
	 * Remove a child from the given node
	 * @param name of the child to remove
	 */
	public void removeChild(String name) {
		this._children.remove(name);
	}

	/**
	 * Replace the current Node of the child with a new one 
	 * @param name of the child
	 * @param node new Node for the child
	 */
	public void replaceChild(String name, Node node) {
		this._children.put(name, node);
	}

	/**
	 * Check if the Node has the child in question
	 * @param name of the Child
	 * @return true/false depending on if the child in question exists
	 */
	public boolean containsChild(String name) {
		return this._children.containsKey(name);
	}

}
