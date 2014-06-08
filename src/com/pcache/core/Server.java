package com.pcache.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

	public static void main (String[] args) throws IOException {
		
		int PORT_NUMBER=6369;

		ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);

		System.out.println("Server listening on " + PORT_NUMBER);

		Socket clientSocket = serverSocket.accept();

		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				clientSocket.getInputStream()));

		String inputLine;
		while ((inputLine = in.readLine()) != null) {
			out.println(inputLine);
			System.out.println("SERVER: " + inputLine);
		}

		serverSocket.close();

	}

}
