package com.pcache.core;

public class MultipleAsyncClients
{

	public static void main (String[] args) {
	
		for (int i=0; i<10; i++) {
			new Thread(new Client()).start();
		}
		
	}
	
}
