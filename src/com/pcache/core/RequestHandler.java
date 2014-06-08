package com.pcache.core;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class RequestHandler extends Thread {

	private Socket _socket;

	public RequestHandler(Socket socket) {
		this._socket = socket;
	}

	public void run() {
		
		try (
				PrintWriter out = new PrintWriter(_socket.getOutputStream(), true);
		) {

			out.println("HELLO FROM SERVER");
			System.out.println("request handled");
			_socket.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
