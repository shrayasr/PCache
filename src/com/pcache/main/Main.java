package com.pcache.main;

import java.util.ArrayList;

import com.pcache.DO.Timeseries;
import com.pcache.dataaccess.CacheEngine;
import com.pcache.exceptions.PCacheException;

public class Main {

	public static void main(String[] args) throws PCacheException {

		String namespace = "sentinel";

		CacheEngine.createNamespace(namespace);

		CacheEngine.addStructureToNamespace(namespace, "filmstrip", "tid,sid");

		ArrayList<String> timestamps = new ArrayList<String>();
		ArrayList<String> dataPoints = new ArrayList<String>();

		timestamps.add("1");
		timestamps.add("2");
		timestamps.add("3");

		dataPoints.add("x");
		dataPoints.add("y");
		dataPoints.add("z");

		Timeseries ts = new Timeseries(timestamps, dataPoints);

		CacheEngine.createStructureInstance(namespace, "filmstrip", "tid=1,sid=1", ts);


		System.out.println("Cache initialized!");
	}

}
