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
		GET_COMPONENT, ADD_COMPONENTS, EXIT,GET_TITLES,TITLES
	}

	public User(Socket s, NetWorker netWorker) throws IOException {
		socket = s;
		reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
		writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		conector = new Thread(this);
		this.netWorker = netWorker;
		conector.start();
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
					try {
						sendTitles();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}

	}

	private void sendTitles() throws IOException {
		Info[] kk = netWorker.xml.getTitles();
		String[] ss = new String[kk.length];
		for (int i = 0; i < ss.length; i++) {
			ss[i] = kk[i].toString();
		}
		String massege = Option.TITLES +SPACER +"{" + String.join(PROP_SPACER,ss) + "}";
		writer.write(massege);
		writer.flush();
		
	}

	private void addComponnent(String string) {
		try {
			netWorker.xml.saveElectricComponent(parse(string));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
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
	
	public ElectricComponent parse(String s){
		s = s.substring(1, s.length() - 1);
		String[] infos = s.split(PROP_SPACER);
		Info[] inf = new Info[infos.length];
		for(int i = 0; i < inf.length; i++ ){
			String[] p = infos[i].split(BETWEAN_TITLE_AND_INFO_SPACER);
			inf[i] = new Info(p[0], p[1]);
		}
		return new ElectricComponent(inf);
		
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
