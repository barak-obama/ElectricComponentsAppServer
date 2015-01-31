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

	public ElectricComponent parseElement(Element componentElement) {
		NodeList info = componentElement.getChildNodes();
		ArrayList<Info> infos = new ArrayList<>();
		ArrayList<Double> prices = new ArrayList<>();
		ArrayList<String> uses = new ArrayList<>();
		for (int i = 0; i < info.getLength(); i++) {
			Element e = (Element) info.item(i);
			if (e.getTagName().equals("price")) {
				prices.add(Double.parseDouble(e.getTextContent()));
			} else if (e.getTagName().equals("use")) {
				uses.add(e.getTextContent());
			} else {
				infos.add(new Info(e.getTagName(), e.getTextContent()));
			}
		}
		infos.add(new Info("price", prices.toString()));
		infos.add(new Info("use", uses.toString()));
		return new ElectricComponent(infos.toArray(new Info[infos.size()]));
	}

	public void saveElectricComponent(ElectricComponent c)
			throws FileNotFoundException, IOException {
		Element root = (Element) xmlDoc.getFirstChild();

		Element com = xmlDoc.createElement("electricComponents");

		for (Info f : c.getAllInfo()) {
			if (f.getTitle().equals("use") || f.getTitle().equals("price")) {
				String s = f.getInfo().substring(1, f.getInfo().length() - 1);
				String[] uses = s.split(",");
				for (String k : uses) {
					Element inf = xmlDoc.createElement(f.getTitle());
					inf.setTextContent(k);
					com.appendChild(inf);
				}
				continue;
			}
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
