import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Scanner;
import java.util.*;
import java.lang.*;
import java.io.*;

public class TableComparison {

	static String OldVersion = "OldVersion";
	static String NewVersion = "NewVersion";
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		InputProcessing input = new InputProcessing();
		//2 folders as input
		String oldFolder = br.readLine();
		String newFolder = br.readLine();

		File Version1 = new File(oldFolder); // folder 1
		new File(OldVersion).mkdirs();
		input.ExtractFolder(Version1, OldVersion);
		File Version2 = new File(newFolder); // folder 2
		new File(NewVersion).mkdirs();
		input.ExtractFolder(Version2, NewVersion);

		// ----------------------------Comparing two versions-------------------
		File dir1 = new File("OldVersion");
		File dir2 = new File("NewVersion");
		CompareVersions compare = new CompareVersions();
		try {
			compare.getDiff(dir1, dir2);
		} catch (IOException ie) {
			ie.printStackTrace();
		}
	}
}
