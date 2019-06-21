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
		if (dirA.getName().endsWith("hbm.xml")) {
			pageNode = doc.getElementsByTagName("class");
			if (pageNode.getLength() == 0)
				pageNode = doc.getElementsByTagName("subclass");
		} else if (dirA.getName().endsWith(".xml"))
			pageNode = doc.getElementsByTagName("constraint");
		return pageNode;
	}

	public void ProcessBK(ArrayList<String> bk, File dir) {
		if (dir.getParent().contains("ExtractedEPNM1")) {
			String table = bk.get(0);
			bk.remove(0);
			TableComparison.newBKs.put(table, bk);
		} else if (dir.getParent().contains("ExtractedEPNM0")) {
			String table = bk.get(0);
			bk.remove(0);
			TableComparison.oldBKs.put(table, bk);
		}
	}

	public void CompareFileContent(File dirA, File dirB)
			throws ParserConfigurationException, SAXException, IOException {
		// --------------------ddlschema code---------------------

		if (!dirA.getName().contains("hbm.xml") && !(dirB.getName().contains("hbm.xml"))) {
			NodeList nodelist = getNodes(dirA);
			XMLparsing parse = new XMLparsing();
			int l1 = nodelist.getLength();

			for (int i = 0; i < l1; i++) {
				ArrayList<String> ddl1 = new ArrayList<String>();
				Node node = nodelist.item(i);

				Node className = node.getParentNode();
				String key = className.getAttributes().getNamedItem("classname").getNodeValue();
				int ind = key.lastIndexOf('.');
				if (ind >= 0)
					key = key.substring(ind + 1);       //table name

				NamedNodeMap attributes = node.getAttributes();
				parse.storeDDLattributes(attributes, ddl1);
				parse.getAllDDLchildren(node, ddl1);
				ddl1.set(0, key);
				ProcessBK(ddl1, dirA);
			}

			NodeList nodelist2 = getNodes(dirB);
			int l2 = nodelist2.getLength();

			for (int i = 0; i < l2; i++) {
				ArrayList<String> ddl2 = new ArrayList<String>();
				Node node = nodelist2.item(i);

				Node className = node.getParentNode();
				String key = className.getAttributes().getNamedItem("classname").getNodeValue();
				int ind = key.lastIndexOf('.');
				if (ind >= 0)
					key = key.substring(ind + 1);

				NamedNodeMap attributes = node.getAttributes();
				parse.storeDDLattributes(attributes, ddl2);
				parse.getAllDDLchildren(node, ddl2);
				ddl2.set(0, key);
				ProcessBK(ddl2, dirB);
			}
			Comparator compare = new Comparator();
		}
		// ----------------------------ddlschema ended-------------
		// --------------------property code---------------------
		else {
			XMLparsing parse = new XMLparsing();
			NodeList nodelist = getNodes(dirA); // all 2nd level children
			Node parentNode = nodelist.item(0); // since only one class node

			NodeList nodelist1 = parentNode.getChildNodes();
			int len = nodelist1.getLength();
			ArrayList<ColumnDetails> col1 = new ArrayList<ColumnDetails>();
			for (int i = 0; i < len; i++) {
				Node node = nodelist1.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().equals("property") || node.getNodeName().equals("component")) {
						ColumnDetails obj = new ColumnDetails();
						if (node.hasAttributes()) {
							NamedNodeMap pattributes = node.getAttributes();

							for (int prop = 0; prop < pattributes.getLength(); prop++) {
								if (node.getNodeName().equals("property")
										&& pattributes.item(prop).getNodeName().equals("name")
										&& !pattributes.item(prop).getNodeValue().equals("instanceUuid")
										&& !pattributes.item(prop).getNodeValue().equals("displayName")
										&& !pattributes.item(prop).getNodeValue().equals("description")
										&& !pattributes.item(prop).getNodeValue().equals("owningEntityId")
										&& !pattributes.item(prop).getNodeValue().equals("deployPending")
										&& !pattributes.item(prop).getNodeValue().equals("name"))
									obj.setPropertyName(pattributes.item(prop).getNodeValue());
								else if (node.getNodeName().equals("component")
										&& pattributes.item(prop).getNodeName().equals("name")) {
									obj.setComponentName(pattributes.item(prop).getNodeValue());
									obj.setColumnName(pattributes.item(prop).getNodeValue());
								} else if (pattributes.item(prop).getNodeName().equals("type"))
									obj.setType(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("class"))
									obj.setType(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("column"))
									obj.setColumnName(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("unique"))
									obj.setUnique(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("default"))
									obj.setDefaultValue(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("length"))
									obj.setLength(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("not-null"))
									obj.setNotNull(pattributes.item(prop).getNodeValue());
							}
						}
						if (node.hasChildNodes() && node.getNodeName().equals("property")) {
							NodeList totalChild = node.getChildNodes();
							for (int no = 0; no < totalChild.getLength(); no++) {
								Node pChild = totalChild.item(no);
								if (pChild.hasAttributes() && pChild.getNodeType() == Node.ELEMENT_NODE
										&& pChild.getNodeName().equals("column")) {
									NamedNodeMap childAttri = pChild.getAttributes();
									for (int child = 0; child < childAttri.getLength()
											&& !obj.getPropertyName().equals(" "); child++) {
										if (childAttri.item(child).getNodeName().equals("name"))
											obj.setColumnName(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("unique"))
											obj.setUnique(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("default"))
											obj.setDefaultValue(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("length"))
											obj.setLength(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("not-null"))
											obj.setNotNull(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("type")
												&& obj.getType() == null)
											obj.setType(childAttri.item(child).getNodeValue());

									}

								}
							}
						}
						col1.add(obj);
					}

				}
			}
			// for file 2
			NodeList nodelistB = getNodes(dirB); // all 2nd level children
			Node parentNode2 = nodelistB.item(0);

			NodeList nodelist2 = parentNode2.getChildNodes();
			int len2 = nodelist2.getLength();
			ArrayList<ColumnDetails> col2 = new ArrayList<ColumnDetails>();
			for (int i = 0; i < len2; i++) {
				Node node = nodelist2.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					if (node.getNodeName().equals("property") || node.getNodeName().equals("component")) {
						ColumnDetails obj2 = new ColumnDetails();
						if (node.hasAttributes()) {
							NamedNodeMap pattributes = node.getAttributes();

							for (int prop = 0; prop < pattributes.getLength(); prop++) {
								if (node.getNodeName().equals("property")
										&& pattributes.item(prop).getNodeName().equals("name")
										&& !pattributes.item(prop).getNodeValue().equals("instanceUuid")
										&& !pattributes.item(prop).getNodeValue().equals("displayName")
										&& !pattributes.item(prop).getNodeValue().equals("description")
										&& !pattributes.item(prop).getNodeValue().equals("owningEntityId")
										&& !pattributes.item(prop).getNodeValue().equals("deployPending")
										&& !pattributes.item(prop).getNodeValue().equals("name"))
									obj2.setPropertyName(pattributes.item(prop).getNodeValue());
								else if (node.getNodeName().equals("component")
										&& pattributes.item(prop).getNodeName().equals("name")) {
									obj2.setComponentName(pattributes.item(prop).getNodeValue());
									obj2.setColumnName(pattributes.item(prop).getNodeValue());
								} else if (pattributes.item(prop).getNodeName().equals("type"))
									obj2.setType(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("class"))
									obj2.setType(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("column"))
									obj2.setColumnName(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("unique"))
									obj2.setUnique(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("default"))
									obj2.setDefaultValue(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("length"))
									obj2.setLength(pattributes.item(prop).getNodeValue());
								else if (pattributes.item(prop).getNodeName().equals("not-null"))
									obj2.setNotNull(pattributes.item(prop).getNodeValue());
							}
						}
						if (node.hasChildNodes() && node.getNodeName().equals("property")) {
							NodeList totalChild = node.getChildNodes();
							for (int no = 0; no < totalChild.getLength(); no++) {
								Node pChild = totalChild.item(no);
								if (pChild.hasAttributes() && pChild.getNodeType() == Node.ELEMENT_NODE
										&& pChild.getNodeName().equals("column")) {
									NamedNodeMap childAttri = pChild.getAttributes();
									for (int child = 0; child < childAttri.getLength()
											&& !obj2.getPropertyName().equals(" "); child++) {
										if (childAttri.item(child).getNodeName().equals("name"))
											obj2.setColumnName(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("unique"))
											obj2.setUnique(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("default"))
											obj2.setDefaultValue(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("length"))
											obj2.setLength(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("not-null"))
											obj2.setNotNull(childAttri.item(child).getNodeValue());
										else if (childAttri.item(child).getNodeName().equals("type")
												&& obj2.getType() == null)
											obj2.setType(childAttri.item(child).getNodeValue());
									}
								}
							}
						}
						col2.add(obj2);
					}
				}
			}
			Comparator comparator = new Comparator();
			comparator.CompareFileS(col1, col2, dirA, dirB);
		}
	}
}
