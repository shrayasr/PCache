package com.pcache.DO;

import java.util.ArrayList;

/**
 * Data Object for storing the list of namespaces in the cache
 * @author shrayas
 *
 */
public class Namespace {

	private String _id;
	private ArrayList<Structure> _structures;

	/**
	 * Constructur. Initialize a barebones namespace
	 * @param id The ID of the namespace (a unique string is expected)
	 */
	public Namespace (String id) {
		this._id = id;
		this._structures = null;
	}

	/**
	 * Constructor. Initialize a new namespace with some structures
	 * @param id The ID of the namespace (a unique string is expected)
	 * @param structures, the list of structures associated with the namespace
	 */
	public Namespace (String id, ArrayList<Structure> structures) {
		this._id = id;
		this._structures = structures;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	@Override
	public boolean equals(Object obj) {

		if (obj == this) {
			return true;
		}

		if (obj == null || obj.getClass() != this.getClass()) {
			return false;
		}

		Namespace n = (Namespace) obj;

		return _id == n._id;

	}

	@Override
	public int hashCode() {

		final int prime = 27;
		
		int result = 1;
		
		result = prime * result + ((_id == null ? 0 : _id.hashCode()));

		return result;

	}

}
