package com.pcache.core;

import java.io.IOException;
import java.net.ServerSocket;

public class MultiServer {

	public static void main (String[] args) throws IOException {
		
		int PORT_NUMBER=6369;

		System.out.println("Listening on " + PORT_NUMBER);

		boolean listening = true;
		try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
			
			while (listening) {
				new RequestHandler(serverSocket.accept()).start();
			}

		}


	}

}
