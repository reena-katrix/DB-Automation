import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XMLparsing {
public static void storeAttributes(NamedNodeMap rootAtt,ArrayList<HashMap<String, String>>result) {
    	
		HashMap<String, String>res = new HashMap<String, String>();
		for(int i=0;i<rootAtt.getLength();i++)
		{

                if(rootAtt.item(i).getNodeName().equals("name") ||rootAtt.item(i).getNodeName().equals("type") 
			  ||rootAtt.item(i).getNodeName().equals("unique") ||rootAtt.item(i).getNodeName().equals("length") ||rootAtt.item(i).getNodeName().equals("default"))
			 res.put(rootAtt.item(i).getNodeName(),rootAtt.item(i).getNodeValue());
		}
		result.add(res);
    }
public static void getAllChildren(Node parentNode,ArrayList<HashMap<String, String>>result)
{
    NodeList childList = parentNode.getChildNodes();
    for (int i = 0;i < childList.getLength(); i++) {
        Node node = childList.item(i);           //ith child
        if (node.getNodeType() == Node.ELEMENT_NODE) {
	     	NamedNodeMap attributes = node.getAttributes();
	     	storeAttributes(attributes,result);
	     	
        }
    }
}
}
