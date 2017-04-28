package Code.Languages;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import Code.Core.GlobalVariables;

public class CustomCommandSerializer {

	public static File ccFile = new File("custom_commands.xml");
	public static Hashtable<String, CustomCommandEntry> ccMap;
	
	public static final String ROOT = "languages";
	public static final String LANGUAGE = "language";
	public static final String BUILD_CODE = "build-code";
	public static final String RUN_CODE = "run-code";

	private CustomCommandSerializer() {}

	
	public static void addEntry(CustomCommandEntry entry){
		if(ccMap == null){
			ccMap = new Hashtable<String, CustomCommandEntry>();
		}
		
		ccMap.put(entry.language.name, entry);
	}
	
	
	public static CustomCommandEntry getCCE(String language) throws NullPointerException{
		CustomCommandEntry cceTemp = ccMap.get(language);
		if (cceTemp != null)
			return cceTemp;
		else
			throw new NullPointerException();
	}
	
	public static void init() throws Exception{
		if (!ccFile.exists()){
			ccMap = new Hashtable<String, CustomCommandEntry>();			
			writeCC();
		}

		readCC();
	}
	
	public static void readCC() throws Exception {
		
			File inputFile = ccFile;
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(inputFile);
			doc.getDocumentElement().normalize();

			NodeList nList = doc.getElementsByTagName(LANGUAGE);
			for (int temp = 0; temp < nList.getLength(); temp++) {
				CustomCommandEntry cce = new CustomCommandEntry(null);

				Node nNode = nList.item(temp);

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
					Element eElement = (Element) nNode;
					cce.language = LanguageFactory.getLanguageByString(eElement.getAttribute("name"));

					try {
						cce.customBuildCommand = eElement.getElementsByTagName(BUILD_CODE).item(0).getTextContent();
						cce.isCustomBuildCommand = true;
					} catch (Exception e) {
						cce.isCustomBuildCommand = false;
					}

					try {
						cce.customRunCommand = eElement.getElementsByTagName(RUN_CODE).item(0).getTextContent();
						cce.isCustomRunCommand = true;
					} catch (Exception e) {
						cce.isCustomRunCommand = false;
					}
					
					addEntry(cce);
				}
			}
	}


	public static void writeCC() throws FileNotFoundException {
		
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			// root element
			Element rootElement = doc.createElement(ROOT);
			doc.appendChild(rootElement);

			for (CustomCommandEntry value : ccMap.values()) {
				if(value.customBuildCommand == "" && value.customRunCommand == ""){
					break;
				}
				
				// supercars element
				Element language = doc.createElement(LANGUAGE);
				rootElement.appendChild(language);

				// setting attribute to element
				Attr attr = doc.createAttribute("name");
				attr.setValue(value.language.name);
				language.setAttributeNode(attr);

				// carname element
				if (value.isCustomBuildCommand) {
					Element buildcode = doc.createElement(BUILD_CODE);
					buildcode.appendChild(doc.createTextNode(value.customBuildCommand));
					language.appendChild(buildcode);
				}

				if (value.isCustomRunCommand) {
					Element runcode = doc.createElement(RUN_CODE);
					runcode.appendChild(doc.createTextNode(value.customRunCommand));
					language.appendChild(runcode);
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(ccFile);
			transformer.transform(source, result);
			// Output to console for testing
			if(GlobalVariables.debug){
				StreamResult consoleResult = new StreamResult(System.out);
				transformer.transform(source, consoleResult);
			}
		} catch (Exception e) {
			if(GlobalVariables.debug) e.printStackTrace();
		}
	}
}