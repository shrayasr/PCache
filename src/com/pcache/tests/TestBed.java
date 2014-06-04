package com.pcache.tests;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.pcache.DO.timeseries.VariableTimeseries;
import com.pcache.engines.VariableTimeseriesEngineOld;
import com.pcache.exceptions.PCacheException;

public class TestBed {

	public static VariableTimeseries makeTs(int noOfPoints) throws PCacheException {

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

		VariableTimeseries ts = new VariableTimeseries(timestamps, dataPoints);

		return ts;

	}

	public static int randInt(int min, int max) {
		
		Random rand = new Random();

		int randomNum = rand.nextInt((max-min) + 1) + min;

		return randomNum;
		
	}

	public static void main(String args[]) throws PCacheException {
		dfsTest(10);
		//bfsTest(10);
	}

	private static void bfsTest(int noOfNsSSi) throws PCacheException {
				
		String namespacePrefix = "ns_";
		String structurePrefix = "_s_";
		
		ArrayList<String> structDefs = new ArrayList<>();

		int totalPointsInCache=0;

        int startId = 1;
        int endId = noOfNsSSi;

        structDefs.add("!!");
        for (int i=startId; i<=endId; i++) {
        	structDefs.add(UUID.randomUUID().toString().replace("-", "_"));
        }

        long start,end;
        start = System.currentTimeMillis();
        for (int i=startId;i<endId;i++) {
        	
			String namespace = namespacePrefix + i;
			VariableTimeseriesEngineOld.addNewNamespace(namespace);
        }
        end = System.currentTimeMillis();
        System.out.println(String.format("%s Ns' created in %sms", noOfNsSSi, (end-start)));

        int nsId = 1;

        start = System.currentTimeMillis();
        for (int i=startId;i<endId;i++) {
        	
        	String namespace = "ns_" + nsId;
        	String structureId = String.format("ns_%s_s_%s", nsId, i);
        	String structureDefinition = structDefs.get(i);

            VariableTimeseriesEngineOld.addNewStructure(namespace, structureId, structureDefinition);
        }
        end = System.currentTimeMillis();
        System.out.println(String.format("%s S' created under 1 namespace in in %sms", noOfNsSSi, (end-start)));

        start = System.currentTimeMillis();
        long totalMakeTsTime = 0;
        for (int i=startId;i<endId;i++) {
        	
        	String namespace = "ns_" + nsId;
        	String structureId = String.format("ns_%s_s_%s", nsId, i);
        	String structureDefinition = structDefs.get(i);
        	String structureInstanceId = structureDefinition + "=" + i;

            int noOfPointsInTs = randInt(100000000, 200000000);
            totalPointsInCache += noOfPointsInTs;

            long makeTsStart = System.currentTimeMillis();
            VariableTimeseries ts = makeTs(noOfPointsInTs);
            long makeTsEnd = System.currentTimeMillis();

            totalMakeTsTime += (makeTsEnd - makeTsStart);

            VariableTimeseriesEngineOld.addNewStructureInstance(namespace, structureId, structureInstanceId, ts);
        }
        end = System.currentTimeMillis();
        end = end - totalMakeTsTime;
        System.out.println(String.format("%s Si' created under 1 structure in "
        		+ "%sms. Total no. of points: %s", noOfNsSSi, (end-start), totalPointsInCache));

	}

	private static void dfsTest(int noOfNsSSi) throws PCacheException {
			
		String namespacePrefix = "ns_";
		String structurePrefix = "_s_";
		
		ArrayList<String> structDefs = new ArrayList<>();

		int totalPointsInCache=0;
        long start = System.currentTimeMillis();

        int startId = 1;
        int endId = noOfNsSSi;

        structDefs.add("!!");
        for (int i=startId; i<=endId; i++) {
        	structDefs.add(UUID.randomUUID().toString().replace("-", "_"));
        }

        long totalMakeTsTime = 0;
		for (int i=startId;i<=endId;i++) {
			
			String namespace = namespacePrefix + i;

			VariableTimeseriesEngineOld.addNewNamespace(namespace);

//			System.out.println(namespace);

			for (int j=startId;j<=endId;j++) {

				String structureId = namespace + structurePrefix + j;
				String structureDefinition = structDefs.get(j);

				VariableTimeseriesEngineOld.addNewStructure(namespace, structureId, structureDefinition);

//				System.out.println("  "+structureId + ":" + structureDefinition);

				for (int k=startId;k<=endId;k++) {
					
					String structureInstanceId = structureDefinition + "=" + k;

					long startMakeTs = System.currentTimeMillis();
					int noOfPointsInTs = randInt(10000000, 20000000);
					totalPointsInCache += noOfPointsInTs;
					VariableTimeseries ts = makeTs(noOfPointsInTs);
					long endMakeTs = System.currentTimeMillis();

					totalMakeTsTime += (endMakeTs - startMakeTs);

					VariableTimeseriesEngineOld.addNewStructureInstance(namespace, structureId, structureInstanceId, ts);

//					System.out.println("    " + structureInstanceId + ":" + noOfPointsInTs + "["+(System.currentTimeMillis() - start)+"ms]");
				}
			}
		}

		System.out.println("Done. No of points in cache: "+totalPointsInCache);
		System.out.println("Took " + (System.currentTimeMillis() - start - totalMakeTsTime) + "ms");

		int nsId = randInt(startId,endId);
		int sId = randInt(startId,endId);
		int siId = randInt(startId,endId);

		String nsToGet = "ns_"+nsId;
		String sToGet = nsToGet + "_s_" + sId;
		String siToGet = structDefs.get(sId) + "=" + siId;

		System.out.println("Fetching... ["+nsToGet + " " + sToGet + " " + siToGet+"]");
		start = System.currentTimeMillis();
		VariableTimeseries tsGot = VariableTimeseriesEngineOld.getTimeseries(nsToGet, sToGet, siToGet);
		System.out.println("Done. Took " + (System.currentTimeMillis() - start) + "ms");	
	}

}
