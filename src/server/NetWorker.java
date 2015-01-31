package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class NetWorker {
	XMLConector xml;
	ServerSocket serversocket;
	Thread searcher;
	
	public NetWorker() throws IOException {
		xml = new XMLConector();
		serversocket = new ServerSocket(42424);
		searcher = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					
				}
			}
		});
	}
}
