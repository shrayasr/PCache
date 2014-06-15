package main.com.pcache.core;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{

	//public static void main (String[] args) {
	
	public void run() {

		String HOST = "localhost";
		int PORT_NUMBER = 6369;

		try (
				Socket socket = new Socket(HOST, PORT_NUMBER);
				PrintStream out = new PrintStream(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				) {
			
			out.println("ALLOC 2010-01-01T12:00:00.000+05:30,2010-01-02T12:00:00.000+05:30,2010-01-03T12:00:00.000+05:30,2010-01-04T12:00:00.000+05:30,2010-01-05T12:00:00.000+05:30 1,2,3,4,5");
			//out.println("SIZE 1");

			String line = "";
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
