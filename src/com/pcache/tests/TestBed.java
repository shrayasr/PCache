package com.pcache.tests;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.pcache.DO.Timeseries;
import com.pcache.dataaccess.CacheEngine;
import com.pcache.exceptions.PCacheException;

public class TestBed {

	public static Timeseries makeTs(int noOfPoints) throws PCacheException {

        String[] outputOptions = {"UP","DOWN"};
		
		ArrayList<String> timestamps = new ArrayList<>();
		ArrayList<Object> dataPoints = new ArrayList<>();

		DateTime date = new DateTime();

		for (int i=0; i<1000; i++) {
			
			String dateMilis = date.toString(DateTimeFormat.forPattern("YYYY-MM-dd'T'HH:mm:ss.sssZZ"));
			timestamps.add(dateMilis);

			int dataOption = randInt(0,1);
			dataPoints.add(outputOptions[dataOption]);

			date = date.plusDays(1);
			
		}

		Timeseries ts = new Timeseries(timestamps, dataPoints);

		return ts;

	}

	public static int randInt(int min, int max) {
		
		Random rand = new Random();

		int randomNum = rand.nextInt((max-min) + 1) + min;

		return randomNum;
		
	}

	public static void main(String args[]) throws PCacheException {
		
		String namespacePrefix = "ns_";
		String structurePrefix = "_s_";
		
		ArrayList<String> structDefs = new ArrayList<>();

		int totalPointsInCache=0;
        long start = System.currentTimeMillis();

        int startId = 1;
        int endId = 100;

        structDefs.add("!!");
        for (int i=startId; i<=endId; i++) {
        	structDefs.add(UUID.randomUUID().toString().replace("-", "_"));
        }

		for (int i=startId;i<=endId;i++) {
			
			String namespace = namespacePrefix + i;

			CacheEngine.addNewNamespace(namespace);

//			System.out.println(namespace);

			for (int j=startId;j<=endId;j++) {

				String structureId = namespace + structurePrefix + j;
				String structureDefinition = structDefs.get(j);

				CacheEngine.addNewStructure(namespace, structureId, structureDefinition);

//				System.out.println("  "+structureId + ":" + structureDefinition);

				for (int k=startId;k<=endId;k++) {
					
					String structureInstanceId = structureDefinition + "=" + k;

					int noOfPointsInTs = randInt(10000, 20000);
					totalPointsInCache += noOfPointsInTs;

					Timeseries ts = makeTs(noOfPointsInTs);

					CacheEngine.addNewStructureInstance(namespace, structureId, structureInstanceId, ts);

//					System.out.println("    " + structureInstanceId + ":" + noOfPointsInTs + "["+(System.currentTimeMillis() - start)+"ms]");
				}
			}
		}

		System.out.println("Done. No of points in cache: "+totalPointsInCache);
		System.out.println("Took " + (System.currentTimeMillis() - start) + "ms");

		int nsId = randInt(startId,endId);
		int sId = randInt(startId,endId);
		int siId = randInt(startId,endId);

		String nsToGet = "ns_"+nsId;
		String sToGet = nsToGet + "_s_" + sId;
		String siToGet = structDefs.get(sId) + "=" + siId;

		System.out.println("Fetching... ["+nsToGet + " " + sToGet + " " + siToGet+"]");
		start = System.currentTimeMillis();
		Timeseries tsGot = CacheEngine.getTimeseries(nsToGet, sToGet, siToGet);
		System.out.println("Done. Took " + (System.currentTimeMillis() - start) + "ms");
	}

}
