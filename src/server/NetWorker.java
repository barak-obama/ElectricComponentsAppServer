package server;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class NetWorker {
	public final XMLConector xml;
	ServerSocket serversocket;
	Thread searcher;
	List<User> users = new ArrayList<>();
	
	public NetWorker() throws IOException {
		xml = new XMLConector();
		serversocket = new ServerSocket(42424);
		searcher = new Thread(new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Socket s = serversocket.accept();
						User u = new User(s, NetWorker.this);
						users.add(u);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		});
	}
}
