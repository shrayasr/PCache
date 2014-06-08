package com.pcache.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main (String[] args) {
		
		String HOST = "localhost";
		int PORT_NUMBER = 6369;

		try (
				Socket socket = new Socket(HOST, PORT_NUMBER);
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		) {
			
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
