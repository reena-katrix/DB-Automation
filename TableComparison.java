import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Scanner;
import java.util.*;
import java.lang.*;
import java.io.*;

public class TableComparison {

	static String OldVersion = "OldVersion";          //give location of folders to be created
	static String NewVersion = "NewVersion";
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		InputProcessing input = new InputProcessing();
		//2 folders as input
		String oldFolderInput = br.readLine();
		String newFolderInput = br.readLine();

		File Version1 = new File(oldFolderInput); // folder 1 in file format
		File Version2 = new File(newFolderInput); // folder 2 in file format
		
		//making old and new folders to store all new and old jar extracts
		new File(OldVersion).mkdirs();
		new File(NewVersion).mkdirs();

		//extract jar into above folders
		input.ExtractFolder(Version1, OldVersion);           //src,dest
		input.ExtractFolder(Version2, NewVersion);           //src,dest

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
