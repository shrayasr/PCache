package main.com.pcache.performance;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client implements Runnable{

	private String _command;

	public Client() {
		_command = null;
	}

	public Client(String command) {
		this._command = command;
	}
	
	public void run() {

		String HOST = "localhost";
		int PORT_NUMBER = 6369;

		try (
				Socket socket = new Socket(HOST, PORT_NUMBER);
				PrintStream out = new PrintStream(socket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				) {
			
			if (_command != null) {
				out.println(_command);
			}
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
