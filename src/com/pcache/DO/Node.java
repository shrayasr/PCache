package com.pcache.DO;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Node {

	private String _name;
	private Map<String, Node> _children;
	private Timeseries _timeseries;

	public Node(String name, Timeseries timeseries) {
		this._name = name;
		this._timeseries = timeseries;
		this._children = new HashMap<String, Node>();
	}

	public Timeseries find(String path) {
		
		String[] pathParts = path.split("\\.");

		Node lastNode = this;
		for (String pathPart : pathParts) {
			lastNode = lastNode.getChild(pathPart);
		}

		return lastNode.getTimeseries();
	}

	public String getName() {
		return this._name;
	}

	public void setName(String name) {
		this._name = name;
	}

	public Timeseries getTimeseries() {
		return this._timeseries;
	}

	public void addChild(Node child) {
		this._children.put(child.getName(), child);
	}

	public void removeChild(String name) {
		this._children.remove(name);
	}

	public void replaceChild(String name, Node node) {
		this._children.put(name, node);
	}

	public boolean containsChild(String name) {
		return this._children.containsKey(name);
	}

	public Node getChild(String name) { 
		return this._children.get(name);
	}

	public Set<Node> getChildren() {
		return Collections.unmodifiableSet(new HashSet<Node>(this._children.values()));
	}

}
