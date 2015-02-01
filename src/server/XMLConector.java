package server;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

public class XMLConector {

	public File xmlFile;
	public Document xmlDoc;

	public XMLConector() {
		try {
			initXML();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean initXML() throws SAXException, IOException,
			ParserConfigurationException {
		xmlFile = new File("src/server/electricComponents.xml");
		System.out.println(xmlFile.exists());
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		xmlDoc = db.parse(xmlFile);
		return true;
	}

	public ElectricComponent[] getElectricComponents() {
		Element root = (Element) xmlDoc.getFirstChild();
		NodeList list = root.getElementsByTagName("component");
		ArrayList<ElectricComponent> components = new ArrayList<>();
		for (int i = 0; i < list.getLength(); i++) {
			components.add(parseElement((Element) list.item(i)));
		}
		return components.toArray(new ElectricComponent[components.size()]);
	}

	public ElectricComponent getElectricComponentById(int id) {
		Element root = (Element) xmlDoc.getFirstChild();
		NodeList list = root.getElementsByTagName("component");
		ElectricComponent component = null;
		for (int i = 0; i < list.getLength(); i++) {
			Element e = (Element) list.item(i);
			if (e.getAttribute("id").equals("" + id)) {
				component = parseElement(e);
				return component;
			}
		}
		return null;
	}

	public ElectricComponent parseElement(Element componentElement) {
		NodeList info = componentElement.getChildNodes();
		ArrayList<Info> infos = new ArrayList<>();
		for (int i = 0; i < info.getLength(); i++) {
			Element e = (Element) info.item(i);
			infos.add(new Info(e.getTagName(), e.getTextContent()));
		}
		infos.add(new Info("id", componentElement.getAttribute("id")));
		return new ElectricComponent(infos.toArray(new Info[infos.size()]));
	}

	public void saveElectricComponent(ElectricComponent c)
			throws FileNotFoundException, IOException {
		Element root = (Element) xmlDoc.getFirstChild();

		NodeList n = root.getElementsByTagName("electricComponents");

		int newId = 0;
		if (n.getLength() != 0) {
			Element e = (Element) n.item(n.getLength() - 1);
			newId = Integer.parseInt(e.getAttribute("id")) + 1;
		}

		Element com = xmlDoc.createElement("electricComponents");
		com.setAttribute("id", "" + newId);

		for (Info f : c.getAllInfo()) {
			Element inf = xmlDoc.createElement(f.getTitle());
			inf.setTextContent(f.getInfo());
			com.appendChild(inf);
		}

		root.appendChild(com);

		OutputFormat outFormat = new OutputFormat(xmlDoc);
		outFormat.setIndenting(true);
		FileOutputStream outFile = new FileOutputStream(xmlFile);
		XMLSerializer xmlserializer = new XMLSerializer(outFile, outFormat);
		xmlserializer.serialize(xmlDoc);
		try {
			initXML();
		} catch (SAXException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getTitles() {
		ElectricComponent[] com = getElectricComponents();
		String kkkk = "{";
		for (int k = 0; k < com.length; k++) {
			String g = com[k].getInfoByTitle("name").getInfo()
					+ User.BETWEAN_TITLE_AND_INFO_SPACER
					+ com[k].getInfoByTitle("image").getInfo()
					+ User.BETWEAN_TITLE_AND_INFO_SPACER
					+ com[k].getInfoByTitle("id").getInfo();
			kkkk = (k!=0?User.PROP_SPACER:"") + kkkk + g;
		}
		kkkk = kkkk + "}";
		return kkkk;
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		ArrayList<Info> fff = new ArrayList<>();
		fff.add(new Info("hahaha1", "hahahahahah"));
		fff.add(new Info("hahaha2", "hahahahahah"));
		fff.add(new Info("hahaha3", "hahahahahah"));

		fff.add(new Info("hahaha4", "hahahahahah"));
		fff.add(new Info("hahaha5", "hahahahahah"));
		fff.add(new Info("hahaha6", "hahahahahah"));
		fff.add(new Info("hahaha7", "hahahahahah"));
		fff.add(new Info("price", "[3,5,8,10.2]"));
		fff.add(new Info("use", "[kill,ugbfuvbshvio,pp,p]"));
		XMLConector xml = new XMLConector();
		xml.saveElectricComponent(new ElectricComponent(fff
				.toArray(new Info[fff.size()])));

	}

}
