import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
import java.io.File;

public class CompareFiles {

	public static NodeList getNodes(File dirA) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = dbf.newDocumentBuilder().parse(dirA);
		doc.getDocumentElement().normalize();
		NodeList pageNode = doc.getElementsByTagName("property");
		return pageNode;
	}

	public void CompareFileContents(File dirA, File dirB)
			throws ParserConfigurationException, SAXException, IOException {
		// to store one file contents
		NodeList nodelist1 = getNodes(dirA);
		ArrayList<ArrayList<HashMap<String, String>>> Whole = new ArrayList<ArrayList<HashMap<String, String>>>();
		ArrayList<String> colheads1 = new ArrayList<>();
		int len = nodelist1.getLength();
		ColumnDetails col1[] = new ColumnDetails[len + 1];
		XMLparsing parse = new XMLparsing();
		for (int i = 0; i < len; i++) {
			// to store one column contents
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			Node node = nodelist1.item(i); // each property or each column
											// heading
			NamedNodeMap attributes = node.getAttributes();
			parse.storeAttributes(attributes, result);
			parse.getAllChildren(node, result);
			Whole.add(result);
			col1[i] = new ColumnDetails(result);
			colheads1.add(col1[i].getName());
			// col1[i].printColumn(); //to print column details
		}
		// System.out.println(Whole);
		// for another file
		NodeList nodelist2 = getNodes(dirB);
		// to store one file contents
		ArrayList<ArrayList<HashMap<String, String>>> Wholefinal = new ArrayList<ArrayList<HashMap<String, String>>>();
		ArrayList<String> colheads2 = new ArrayList<>();
		int len2 = nodelist2.getLength();
		ColumnDetails col2[] = new ColumnDetails[len2 + 1];
		for (int i = 0; i < len2; i++) {
			// to store one column contents
			ArrayList<HashMap<String, String>> result = new ArrayList<HashMap<String, String>>();
			Node node = nodelist2.item(i); // each property or each column
											// heading
			NamedNodeMap attributes = node.getAttributes();
			parse.storeAttributes(attributes, result);
			parse.getAllChildren(node, result);
			Wholefinal.add(result);
			col2[i] = new ColumnDetails(result);
			colheads2.add(col2[i].getName());
			// col2[i].printColumn();
		}
		// System.out.println(Wholefinal);

		Comparator comparator = new Comparator();
		comparator.Test(Whole, Wholefinal, colheads1, colheads2, dirA, dirB);

	}
}
