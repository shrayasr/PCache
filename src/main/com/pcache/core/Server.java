package main.com.pcache.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.sql.PooledConnection;

public class Server {

	public static void main (String[] args) {
		
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		
		int PORT_NUMBER=6369;
		
		System.out.println("Listening on " + PORT_NUMBER);

		boolean listening = true;
		try (ServerSocket serverSocket = new ServerSocket(PORT_NUMBER)) {
			
			while (listening) {
				executorService.execute(new RequestHandler(serverSocket.accept()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
