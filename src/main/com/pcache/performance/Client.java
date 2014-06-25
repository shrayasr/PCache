package main.com.pcache.performance;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Client implements Runnable{

	private String _command;
	private Logger _logger;
	private CountDownLatch _latch;
	
	private void _initializeLogger() {
		PropertyConfigurator.configure("properties/log4j.properties");
		_logger = Logger.getLogger(Client.class.getName());
	}

	public Client() {
		_command = null;
		_initializeLogger();
	}

	public Client(String command, CountDownLatch latch) {
		this._command = command;
		this._latch = latch;
		_initializeLogger();
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
			
			

		} catch (Exception ex) {
			
      _logger.error("CLIENT ERROR");
			_logger.error(ex.getMessage());
		}  
		finally {
			_latch.countDown();
		}

	}

}
