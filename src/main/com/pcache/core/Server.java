package main.com.pcache.core;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Server {

	private static int _PORT_NUMBER=6369;
	private static int _POOL_SIZE = 10;

	public static void main (String[] args) {

		Options options = new Options();

		Option pool_size = new Option("pool_size", true, "No. of threads to use to handle connections");
		pool_size.isRequired();

		options.addOption(pool_size);

		CommandLineParser parser = new GnuParser();
		CommandLine cmd;
		try
		{
			cmd = parser.parse(options, args);
			if (cmd.hasOption("pool_size")) {
				_POOL_SIZE = Integer.parseInt(cmd.getOptionValue("pool_size"));
			}
		} catch (ParseException e1)
		{
			e1.printStackTrace();
		}

		System.out.println("Starting PCache Server");
		System.out.println("PORT: " + _PORT_NUMBER);
		System.out.println("THREAD POOL SIZE: " + _POOL_SIZE);

		ExecutorService executorService = Executors.newFixedThreadPool(_POOL_SIZE);

		boolean listening = true;
		try (ServerSocket serverSocket = new ServerSocket(_PORT_NUMBER,400)) {

			while (listening) {
				executorService.execute(new RequestHandler(serverSocket.accept()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
