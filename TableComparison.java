import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.List;
import javax.lang.model.element.Element;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TableComparison {
	
public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

	  	File file1 = new File("input1.xml");
	  	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document doc;
        doc = dbf.newDocumentBuilder().parse("input1.xml");
        doc.getDocumentElement().normalize();
        
		NodeList pageNode = doc.getElementsByTagName("property");
		//to store one file contents
		ArrayList<ArrayList<HashMap<String, String>>>Whole = new ArrayList<ArrayList<HashMap<String, String>>>();
		ArrayList<String>colheads1 = new ArrayList<>();
		int len =pageNode.getLength();
		ColumnDetails col1[] = new ColumnDetails[len+1];
		XMLparsing parse = new XMLparsing();
	    for(int i=0;i<len;i++)
	    {
	      //to store one column contents
	      ArrayList<HashMap<String, String>>result = new ArrayList<HashMap<String, String>>();
	  	  Node node = pageNode.item(i);               //each property or each column heading
	  	  NamedNodeMap attributes = node.getAttributes();
	  	  parse.storeAttributes(attributes,result);
	  	  parse.getAllChildren(node,result);
	  	  Whole.add(result);
	  	  col1[i] = new ColumnDetails(result);
	  	  colheads1.add(col1[i].getName());
	  	  col1[i].printColumn();            //to print column details
	    }
//	    System.out.println(Whole);
	    //for another file
	    
	    File file2 = new File("input2.xml");
	  	DocumentBuilderFactory dbffinal = DocumentBuilderFactory.newInstance();
        Document docfinal;
        docfinal = dbffinal.newDocumentBuilder().parse("input2.xml");
        doc.getDocumentElement().normalize();
        
		NodeList pageNodefinal = docfinal.getElementsByTagName("property");
		//to store one file contents
		ArrayList<ArrayList<HashMap<String, String>>>Wholefinal = new ArrayList<ArrayList<HashMap<String, String>>>();
		ArrayList<String>colheads2 = new ArrayList<>();
		int len2 = pageNodefinal.getLength();
		ColumnDetails col2[] = new ColumnDetails[len2+1];
	    for(int i=0;i<pageNodefinal.getLength();i++)
	    {
	      //to store one column contents
	      ArrayList<HashMap<String, String>>result = new ArrayList<HashMap<String, String>>();
	  	  Node node = pageNodefinal.item(i);               //each property or each column heading
	  	  NamedNodeMap attributes = node.getAttributes();
	  	  parse.storeAttributes(attributes,result);
	  	  parse.getAllChildren(node,result);
	  	  Wholefinal.add(result);
	  	  col2[i] = new ColumnDetails(result);
	  	  colheads2.add(col2[i].getName());
//	  	  col2[i].printColumn();
	    }
//	    System.out.println(Wholefinal);
	    
	    Comparator comparator = new Comparator();
	    comparator.Test(Whole, Wholefinal,colheads1 , colheads2);
	}
	 
	 
}

