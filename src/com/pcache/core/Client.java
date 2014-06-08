package com.pcache.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main (String[] args) throws UnknownHostException, IOException {
		
		String HOST = "localhost";
		int PORT_NUMBER = 6369;

		Socket pcacheSocket = new Socket(HOST, PORT_NUMBER);

		PrintWriter out = new PrintWriter(pcacheSocket.getOutputStream(), true);
		BufferedReader in  = new BufferedReader(new InputStreamReader(
				pcacheSocket.getInputStream()));

		String payload = "HELLO_WORLD";

		out.println(payload);
		System.out.println("client: " + payload);

		String response = "";
		while((response = in.readLine()) != null) {
			System.out.println("REPLY: " + response);
			System.out.println("BOOM");
		}

		System.out.println("here");

		pcacheSocket.close();


	}

}
