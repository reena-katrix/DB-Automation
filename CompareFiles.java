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

import java.io.BufferedWriter;
import java.io.File;

public class CompareFiles {

	
	public static NodeList getNodes(File dirA) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document doc = dbf.newDocumentBuilder().parse(dirA);
		doc.getDocumentElement().normalize();
		NodeList pageNode = null;
		if (dirA.getName().endsWith("hbm.xml"))
			pageNode = doc.getElementsByTagName("property");
		else if (dirA.getName().endsWith(".xml"))
			pageNode = doc.getElementsByTagName("constraint");
		return pageNode;
	}
	public void ProcessBK(ArrayList<String>bk,File dir )
	{
		if(dir.getParent().contains("ExtractedEPNM1")){
			if(bk.get(0).indexOf("_")>=0){
			String table = bk.get(0).substring(0,bk.get(0).indexOf("_"));
			bk.remove(0);
			TableComparison.newBKs.put(table, bk);
			}
		}
		else if(dir.getParent().contains("ExtractedEPNM0")){
			if(bk.get(0).indexOf("_")>=0){
			String table = bk.get(0).substring(0, bk.get(0).indexOf("_"));
			bk.remove(0);
			TableComparison.oldBKs.put(table, bk);
			}
		}
	}
	public void CompareFileContent(File dirA, File dirB)
			throws ParserConfigurationException, SAXException, IOException {
		// --------------------ddlschema code---------------------
		
		if (!dirA.getName().contains("hbm.xml") && !(dirB.getName().contains("hbm.xml"))) {
			NodeList nodelist = getNodes(dirA);
			XMLparsing parse = new XMLparsing();
			int l1 = nodelist.getLength();
			BK_Class bk[] = new BK_Class[nodelist.getLength()+ 1];
			ArrayList<ArrayList<String>> Wholeddl1 = new ArrayList<ArrayList<String>>();

			for (int i = 0; i < l1; i++) {
				ArrayList<String> ddl1 = new ArrayList<String>();
				Node node = nodelist.item(i);
				NamedNodeMap attributes = node.getAttributes();
				parse.storeDDLattributes(attributes, ddl1);
				parse.getAllDDLchildren(node, ddl1);
				ProcessBK(ddl1, dirA );
//				bk[i] = new BK_Class(ddl1,dirA);
//				Wholeddl1.add(ddl1);
			}
//			 System.out.println("ddl1" + Wholeddl1);

			NodeList nodelist2 = getNodes(dirB);
			int l2 = nodelist2.getLength();
			BK_Class bk2[] = new BK_Class[nodelist2.getLength()+ 1];
			ArrayList<ArrayList<String>> Wholeddl2 = new ArrayList<ArrayList<String>>();

			for (int i = 0; i < l2; i++) {
				ArrayList<String> ddl2 = new ArrayList<String>();
				Node node = nodelist2.item(i);
				NamedNodeMap attributes = node.getAttributes();
				parse.storeDDLattributes(attributes, ddl2);
				parse.getAllDDLchildren(node, ddl2);
//				bk2[i] = new BK_Class(ddl2,dirB);
//				Wholeddl2.add(ddl2);
				ProcessBK(ddl2, dirB );
			}
//			 System.out.println("ddl2" + Wholeddl2);
			Comparator compare = new Comparator();
//			compare.TestDDL(Wholeddl1, Wholeddl2, dirA, dirB);
//			compare.CompareBK(bk,l1 ,bk2, l2, dirA, dirB);
		}
		// ----------------------------ddlschema
		// ended---------------------------
		// --------------------property code---------------------
		else {
			System.out.println("TABLE ONE");
			NodeList nodelist1 = getNodes(dirA);
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
				col1[i] = new ColumnDetails(result);
				
//				 col1[i].printColumn(); //to print column details
			}
			// for another file
			System.out.println("TABLE TWO");
			NodeList nodelist2 = getNodes(dirB);
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
				col2[i] = new ColumnDetails(result);
//				 col2[i].printColumn();
			}
			Comparator comparator = new Comparator();
			comparator.CompareFileData(col1, len, col2, len2, dirA, dirB);
		}
	}
}
