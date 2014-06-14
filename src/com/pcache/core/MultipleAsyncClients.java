package com.pcache.core;

public class MultipleAsyncClients
{

	public static void main (String[] args) {
	
		
		//new Thread(new Client()).start();
		
		for (int i=0; i<100; i++) {
			new Thread(new Client(), "LOLing Thread").start();
		}
		
	}
	
}
