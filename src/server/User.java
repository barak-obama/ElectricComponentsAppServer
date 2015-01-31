package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class User implements Runnable {
	Thread conector;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	public static final String SPACER = "ÃÄ";
	
	
	enum Option {
		GET_COMPONENT, ADD_COMPONENTS, EXIT,
	}

	public User(Socket s) throws IOException {
		socket = s;
		reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));

	}
	private boolean addComponnents;
	@Override
	public void run() {
		String s = null;
		while (isRunning()) {
			if (s != null) {
				String[] info = s.split(SPACER);
				Option p = Option.valueOf(info[0]);
				switch (p) {
				case GET_COMPONENT:
					sendComponent(Integer.parseInt(info[1]));
					break;
				case ADD_COMPONENTS:
					addComponnents = true;
					break;
				case EXIT:
					end();
					break;
				}
			}
		}

	}
	private void sendComponent(int id) {
		ElectricComponent ec = xml.
		
	}
	private boolean running = true;
	private boolean isRunning() {
		// TODO Auto-generated method stub
		return running;
	}
	
	private void end(){
		running = false;
	}

	private void sendComponents(File file) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		for(String line : (String [])reader.lines().toArray()) {
			writer.write(line);
			writer.flush();
		}
	}

}
