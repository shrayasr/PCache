package main.com.pcache.performance;

import main.com.pcache.utils.Commons;

import org.joda.time.DateTime;

public class Utils {

	public String generateTimeseries(int seedYear,
			int lowerBoundPoints, int upperBoundPoints) {
		
		String returnString = "";
		String[] valueOptions = {"UP","DOWN"};

		int noOfDaysToAdd = Commons.randInt(lowerBoundPoints, upperBoundPoints);

		DateTime date = DateTime.parse(seedYear + "-01-01");
		for (int i=0;i<noOfDaysToAdd;i++) {

			returnString += date.toString("YYYY-MM-dd'T'HH:mm:ss.sssZZ") + ",";
			date = date.plusDays(1);
		}

		returnString = returnString.substring(0, returnString.length()-1);

		returnString += " ";

		for (int i=0;i<noOfDaysToAdd; i++) {
			String option = valueOptions[Commons.randInt(0, 1)];
			returnString += option + ",";
		}

		returnString = returnString.substring(0, returnString.length()-1);

		return returnString;
	}


}
