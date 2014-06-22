package main.com.pcache.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import main.com.pcache.engines.VariableTimeseriesEngine;
import main.com.pcache.exceptions.PCacheException;

import org.joda.time.DateTime;


public class RequestHandler implements Runnable {

	private Socket _socket;

	public RequestHandler(Socket socket) {
		this._socket = socket;
	}

	public void run() {

		try ( PrintWriter out = new PrintWriter(_socket.getOutputStream(), 
				true);

				BufferedReader in = new BufferedReader(new InputStreamReader(
						_socket.getInputStream())); ) {

			String line = in.readLine();

			if (line == null) {
				out.println("ERR: No command to run");
				_socket.close();
				return;
			}

			handleRequest(line, out);

		} catch (IOException e) {
			e.printStackTrace();
		} 

	}

	private void handleRequest(String line, PrintWriter out)
	{

		String[] tokens = line.split(" ");
		long operationStartTime = 0L;
		long operationEndTime = 0L;

		try {

			String command = tokens[0].trim().toUpperCase();

			operationStartTime = System.currentTimeMillis();

			switch(command) {
			
			case "PING": {
				out.println("PONG");
				
				break;
			}

			case "ALLOC": {

				if (tokens.length != 3) {
					throw new PCacheException("ALLOCATE takes 2 arguments. " +
							"Usage: ALLOCATE <TIMESTAMPS> <DATAPOINTS>");
				}

				String timestampsList = tokens[1];
				String dataPointsList = tokens[2];

				List<String> timestamps  = Arrays.asList(timestampsList
						.split(","));
				List<String> dataPoints = Arrays.asList(dataPointsList
						.split(","));

				long ID = VariableTimeseriesEngine.allocate(timestamps, 
						dataPoints);
				out.println(ID);

				break;
			}

			case "SIZE": {
				
				if (tokens.length != 2) {
					throw new PCacheException("SIZE takes 1 argument. " +
							"Usage: SIZE <ID>");
				}

				long ID = Long.parseLong(tokens[1]);

				int size = VariableTimeseriesEngine.size(ID);
				out.println(size);

				break;
			}
			
			case "GETALL": {
				
				if (tokens.length != 2) {
					throw new PCacheException("GETALL takes 1 argument. " +
							"Usage: GETALL <ID>");
				}
				
				long ID = Long.parseLong(tokens[1]);
				
				out.println(VariableTimeseriesEngine.getAll(ID).toJson());
				
				break;
			}
			
			case "GETFROM": {
				
				if (tokens.length != 3) {
					throw new PCacheException("GETFROM takes 2 arguments. " +
							"Usage: GETFROM <ID> <FROM TIMESTAMP>");
				}
				
				long ID = Long.parseLong(tokens[1]);
				String timestampFrom = tokens[2].trim();
				
				out.println(VariableTimeseriesEngine.getFrom(ID, timestampFrom).toJson());
				
				break;
			}
			
			case "GETTO": {
				
				if (tokens.length != 3) {
					throw new PCacheException("GETTO takes 2 arguments. " +
							"Usage: GETTO <ID> <TO TIMESTAMP>");
				}
				
				long ID = Long.parseLong(tokens[1]);
				String timestampFrom = tokens[2].trim();
				
				out.println(VariableTimeseriesEngine.getTo(ID, timestampFrom).toJson());
				
				break;
			}
			
			case "ADD": {
				
				if (tokens.length != 4) {
					throw new PCacheException("ADD takes 3 arguments. " +
							"Usage: ADD <ID> <TIMESTAMPS> <DATAPOINTS>");
				}
				
				long ID = Long.parseLong(tokens[1]);
				
				List<String> timestamps = Arrays.asList(tokens[2].split(","));
				List<String> dataPoints = Arrays.asList(tokens[3].split(","));
				
				VariableTimeseriesEngine.addPoints(ID, timestamps, dataPoints);
				
				out.println("DONE. "+timestamps.size()+" points added");
				
				break;
			}
			
			case "MOD": {
				
				if (tokens.length != 4) {
					throw new PCacheException("MOD takes 3 arguments. " +
							"Usage: MOD <ID> <TIMESTAMPS> <DATAPOINTS>");
				}
				
				long ID = Long.parseLong(tokens[1]);
				
				List<String> timestamps = Arrays.asList(tokens[2].split(","));
				List<String> dataPoints = Arrays.asList(tokens[3].split(","));
				
				VariableTimeseriesEngine.modifyPoints(ID, timestamps, dataPoints);
				
				out.println("DONE. "+timestamps.size()+" points modified");
				
				break;
			}
			
			case "DEL": {
				
				if (tokens.length != 3) {
					throw new PCacheException("DEL takes 2 arguments. " +
							"Usage: DEL <ID> <TIMESTAMPS>");
				}
				
				long ID = Long.parseLong(tokens[1]);
				
				List<String> timestamps = Arrays.asList(tokens[2].split(","));
				
				VariableTimeseriesEngine.removePoints(ID, timestamps);
				
				out.println("DONE. "+timestamps.size()+" points deleted");
				
				break;
			}
			
			default: {
				out.println("ERR: Command not supported");
				break;
			}
			
			}

			operationEndTime = System.currentTimeMillis();

			System.out.println(_socket.getInetAddress() +
					" ["+DateTime.now().toString("d/m/Y:H:M:S z")+"] " +
					command + " ["+(operationEndTime - operationStartTime)/1000.0+"s]");
		}

		catch (PCacheException ex) {
			out.println("ERR: " + ex.getMessage());
		}

		finally {
			try
			{
				_socket.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}

	}


}
