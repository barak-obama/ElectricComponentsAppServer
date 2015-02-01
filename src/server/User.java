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
	NetWorker netWorker;
	public static final String SPACER = "Ã", PROP_SPACER = "Â",
			BETWEAN_TITLE_AND_INFO_SPACER = "Ç";

	enum Option {
		GET_COMPONENT, ADD_COMPONENTS, EXIT,GET_TITLES;
	}

	public User(Socket s, NetWorker netWorker) throws IOException {
		socket = s;
		reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		this.netWorker = netWorker;
	}

	@Override
	public void run() {
		String s = null;
		while (isRunning()) {
			if (s != null) {
				String[] info = s.split(SPACER);
				Option p = Option.valueOf(info[0]);
				switch (p) {
				case GET_COMPONENT:
					try {
						sendComponent(Integer.parseInt(info[1]));
					} catch (NumberFormatException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case ADD_COMPONENTS:
					addComponnent(info[2]);
					break;
				case EXIT:
					end();
					break;
				case GET_TITLES:
					sendTitles();
					break;
				}
			}
		}

	}

	private void sendTitles() {
		// TODO Auto-generated method stub
		
	}

	private void addComponnent(String string) {
		// TODO Auto-generated method stub
		
	}

	private void sendComponent(int id) throws IOException {
		ElectricComponent ec = netWorker.xml.getElectricComponentById(id);
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		Info f = ec.info.get(0);
		sb.append(f.getTitle() + BETWEAN_TITLE_AND_INFO_SPACER + f.getInfo());
		for (int i = 1; i < ec.info.size(); i++) {
			f = ec.info.get(0);
			sb.append(PROP_SPACER + f.getTitle()
					+ BETWEAN_TITLE_AND_INFO_SPACER + f.getInfo());
		}
		sb.append("}");
		writer.write(sb.toString());

	}

	private boolean running = true;

	private boolean isRunning() {

		return running;
	}

	private void end() {
		netWorker.users.remove(this);

		try {
			writer.close();
			reader.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		running = false;
	}

}
