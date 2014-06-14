package com.pcache.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

import org.joda.time.DateTime;

import com.pcache.engines.VariableTimeseriesEngine;
import com.pcache.exceptions.PCacheException;

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

		try {

			String command = tokens[0].trim().toUpperCase();

			switch(command) {

			case "ALLOCATE": {

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
			}

			}

			System.out.println(_socket.getInetAddress() +
					" ["+DateTime.now().toString("d/m/Y:H:M:S z")+"] " +
					command);
		}

		catch (PCacheException ex) {
			out.println(ex.getMessage());
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
