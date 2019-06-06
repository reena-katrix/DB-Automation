import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

public class TableComparison {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		// only input is path of folder
		InputProcessing input = new InputProcessing();
		// dest,path
		// input.Extractzip("C:\\Users\\reenaya\\Desktop","C:\\Users\\reenaya\\Desktop/artifacts.zip");
		// System.out.println("unzip done");
		input.ExtractJAR("C:\\Users\\reenaya\\Videos", "C:\\Users\\reenaya\\Documents\\xmp-im-mpls-module-3.161.49.jar","hibernate");
		input.ExtractJAR("C:\\Users\\reenaya\\Music","C:\\Users\\reenaya\\Documents\\xmp-im-mpls-module-3.162.39-SNAPSHOT.jar", "hibernate");

		// ----------------------------Comparing two versions-------------------
		File dir1 = new File("C:\\Users\\reenaya\\Music\\hibernate\\xmp-im-mpls-module");
		File dir2 = new File("C:\\Users\\reenaya\\Videos\\hibernate\\xmp-im-mpls-module");
		CompareVersions compare = new CompareVersions();
		try {
			compare.getDiff(dir1, dir2);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}

}
