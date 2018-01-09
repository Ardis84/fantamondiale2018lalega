package b.controllers;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.swing.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class FXMLControllerCreator {
	
	public static void main(String[] argList) {
		System.out.println("Starting...");
		Path file = askForFile("Pick FXML file", Arrays.asList("fxml","FXML"));
		if(file != null) {
			try {
				generateControllerFromFXML(file, new PrintWriter(System.out));
			} catch(IOException e) {
				e.printStackTrace(System.err);
			}
		}
		System.out.println("...Done");
		System.exit(0);
	}
	
	private static JFileChooser jfc = null;
	
	public static Path askForFile(String title, List<String>
			allowedFileExtensions) {
		if(jfc == null) {
			jfc = new JFileChooser();
		}
		jfc.setDialogTitle(title);
		javax.swing.filechooser.FileNameExtensionFilter ff = new javax.swing
				.filechooser.FileNameExtensionFilter(title, allowedFileExtensions
				.toArray(new String[0]));
		jfc.setFileFilter(ff);
		final int action = jfc.showOpenDialog(null);
		if(action != JFileChooser.CANCEL_OPTION) {
			return jfc.getSelectedFile().toPath();
		} else {
			return null;
		}
	}
	
	public static void generateControllerFromFXML(Path fxml, Writer out)
			throws IOException {
		try(InputStream in = Files.newInputStream(fxml)) {
			final Document doc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(in);
			Map<String, String> fxids = new HashMap<>(); // map of <ID, TagName>
			Map<String, String> fxmethods =
					new HashMap<>(); // map of <ID, calling TagName>
			//
			recursiveFXScan(doc.getChildNodes(), fxids, fxmethods);
			//
			String fxmlFilename = fxml.getFileName().toString();
			int dotIndex = fxmlFilename.lastIndexOf('.');
			String controllerName = fxmlFilename.substring(0,dotIndex) +
					"Controller";
			out.write(String.format("\n"
					+ "import javafx.event.ActionEvent;\n"
					+ "import javafx.fxml.FXML;\n"
					+ "import javafx.fxml.Initializable;\n"
					+ "import javafx.scene.*;\n"
					+ "import javafx.scene.control.*;\n"
					+ "import javafx.scene.layout.*;\n"
					+ "\n"
					+ "public class %s implements Initializable {\n\n",
					controllerName));
			for(Map.Entry<String,String> entry : fxids.entrySet()){
				out.write(
						String.format("\t@FXML private %s %s;\n",
								entry.getValue(), entry.getKey()
						)
				);
			}
			out.write(
					"\n\t@Override public void initialize(URL url, ResourceBundle "
					+ "rb) {\n"
					+ "\t\t// initialize\n"
					+ "\t}\n");
			for(Map.Entry<String,String> entry : fxmethods.entrySet()){
				out.write(
						String.format(
								"\n\t@FXML private void %s(ActionEvent ae){\n"
								+ "\t\t// TODO: handle %s event\n"
								+ "\t}\n",
								entry.getKey(), entry.getValue()
						)
				);
			}
			out.write("}\n");
			out.flush();
		} catch(ParserConfigurationException | SAXException ex) {
			throw new IOException("FAILURE!", ex);
		}
	}
	
	private static final String ATTRIBUTE_FXID = "fx:id";
	private static final String ATTRIBUTE_FXMETHOD = "onAction";
	
	private static void recursiveFXScan(
			final NodeList childNodes, final Map<String, String> fxids,
			final Map<String, String> fxmethods
	) {
		for(int i = 0; i < childNodes.getLength(); i++) {
			final Node item = childNodes.item(i);
			if(item instanceof Element) {
				//
				Element e = (Element) item;
				String tagName = e.getTagName();
				if(e.hasAttribute(ATTRIBUTE_FXID)) {
					String value = e.getAttribute(ATTRIBUTE_FXID);
				//	System.out.printf("%s has ID %s\n", tagName, value);
					fxids.put(value, tagName);
				}
				if(e.hasAttribute(ATTRIBUTE_FXMETHOD)) {
					String value = e.getAttribute(ATTRIBUTE_FXMETHOD).replace
							("#", "");
				//	System.out.printf("%s calls method %s\n", tagName, value);
					fxmethods.put(value, tagName);
				}
			}
			final NodeList children = item.getChildNodes();
			recursiveFXScan(children, fxids, fxmethods);
		}
	}
}
