import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

public class TableComparison {
	
public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		//only input is path of folder
	    InputProcessing  input = new InputProcessing();  
	    //dest,path
	    String folderZip = input.unZip("C:\\Users\\reenaya\\Desktop","C:\\Users\\reenaya\\Desktop/xmp-im-routing-module-3.162.36.zip");
	    System.out.println(folderZip);
	    input.ExtractJAR("C:\\Users\\reenaya\\Desktop", folderZip, "hibernate");
	    input.ExtractJAR("C:\\Users\\reenaya\\Desktop", folderZip, "ddlschema");
	    
	    
//	    ----------------------------Comparing two versions-------------------
	    File dir1 = new File("C:\\Users\\reenaya\\Desktop\\hibernate\\xmp-im-routing-module");
		File dir2 = new File("C:\\Users\\reenaya\\Desktop\\hibernate-old\\xmp-im-routing-module");
		CompareVersions compare = new CompareVersions();
		try
		{
			compare.getDiff(dir1,dir2);
		}
		catch(IOException ie)
		{
			ie.printStackTrace();
		}
	}
	 
}

