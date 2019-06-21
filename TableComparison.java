import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;

import java.util.*;
import java.util.Map.Entry;
import java.lang.*;
import java.io.*;
import javax.script.*;

//traversed info
//https://paste.ubuntu.com/p/Mcy5NHJrhb/
//prints good html report as in finalHTMLreport.pdf
public class TableComparison {

	// -------------------to store--------------
	public static ArrayList<String> TablesAdded = new ArrayList<String>();
	public static ArrayList<String> TablesDeleted = new ArrayList<String>();
	public static SortedMap<String, UpdatedTableDetails> allUpdatedTables = new TreeMap<>();

	public static HashMap<String, ArrayList<String>> newBKs = new HashMap<String, ArrayList<String>>();
	public static HashMap<String, ArrayList<String>> oldBKs = new HashMap<String, ArrayList<String>>();

	// ------------------------ended------------

	static String OldVersion = "ExtractedEPNM0"; // give location of folders to
													// be created
	static String NewVersion = "ExtractedEPNM1";

	File dir1 = new File("ExtractedEPNM0");
	File dir2 = new File("ExtractedEPNM1");

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {

		File f = new File("myhtml.html");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		InputProcessing input = new InputProcessing();
		// 2 folders as input
		String oldFolderInput = br.readLine();
		String newFolderInput = br.readLine();
		
		File Version1 = new File(oldFolderInput); // folder 1 in file format
		File Version2 = new File(newFolderInput); // folder 2 in file format

		// making old and new folders to store all new and old jar extracts
		new File(OldVersion).mkdirs();
		new File(NewVersion).mkdirs();

		// extract jar into above folders
		input.ExtractFolder(Version1, OldVersion); // src,dest
		input.ExtractFolder(Version2, NewVersion); // src,dest

		// ----------------------------Comparing two versions-------------------
		File dir1 = new File(OldVersion);
		File dir2 = new File(NewVersion);

		CompareVersion compare = new CompareVersion();
		try {
			compare.getDiff(dir1, dir2);
		} catch (IOException ie) {
			ie.printStackTrace();
		}

		Collections.sort(TablesAdded);
		Collections.sort(TablesDeleted);
		bw.write("<h4>Comparision between EPNM 3.0 and EPNM 3.1</h4>");
		if (!TablesAdded.isEmpty()) {
			bw.write("<strong>Added Tables</strong><ol>");
			for (int i = 0; i < TablesAdded.size(); i++) {
				String add = TablesAdded.get(i);
				bw.write("<li>" + add + "</li>");
				// for listing BKs
				// if (newBKs.containsKey(add)) {
				// // System.out.println("table found at:" + i + add);
				// ArrayList<String> cols = new ArrayList<>(newBKs.get(add));
				// // System.out.println("addedBKs:" + cols);
				// if (!cols.isEmpty() && cols.size() > 0) {
				// bw.write("<strong>BKs</strong><ol>");
				// for (int in = 0; in < cols.size(); in++)
				// bw.write("<li>" + cols.get(in) + "</li>");
				// bw.write("</ol>");
				// }
				//
				// }
			}
			bw.write("</ol>");
		}

		if (!TablesDeleted.isEmpty()) {
			bw.write("<strong>DeletedTables</strong><ol>");
			for (int i = 0; i < TablesDeleted.size(); i++) {
				String del = TablesDeleted.get(i);
				bw.write("<li>" + del + "</li>");
			}
			bw.write("</ol>");
		}
		// SortedMap<String, UpdatedTableDetails>
		if (!allUpdatedTables.isEmpty()) {
			bw.write("<strong>UpdatedTables</strong><ol>");
			String tableName = "";
			for (Entry<String, UpdatedTableDetails> m : allUpdatedTables.entrySet()) {

				tableName = m.getKey();
				UpdatedTableDetails obj = m.getValue();
				if (!obj.getAddedColumns().isEmpty() || !obj.getRemovedColumns().isEmpty()
						|| !obj.getUpdatedCol().isEmpty()) {
					bw.write("<li>" + tableName + "</li><ul>");
					ArrayList<String> dummy1 = new ArrayList<String>();
					ArrayList<String> dummy2 = new ArrayList<String>();
					ArrayList<String> pro1 = new ArrayList<String>();
					ArrayList<String> pro2 = new ArrayList<String>();

					if (newBKs.containsKey(tableName)) {
						dummy1 = newBKs.get(tableName);
						pro1 = new ArrayList<>(dummy1);
					}
					if (oldBKs.containsKey(tableName)) {
						dummy2 = oldBKs.get(tableName);
						pro2 = new ArrayList<>(dummy2);
					}
					if (!dummy1.isEmpty()) {
						boolean addedBK = dummy1.removeAll(dummy2);
						if ((addedBK && dummy1.size() > 0)) {
							bw.write("<li><strong>added BKs</strong></li><ol>");
							for (int a = 0; a < dummy1.size(); a++)
								bw.write("<li>" + dummy1.get(a) + "</li>");
							bw.write("</ol>");
						}
					}

					if (!pro2.isEmpty()) {
						boolean removedBK = pro2.removeAll(pro1);
						if ((removedBK && pro2.size() > 0)) {
							bw.write("<li><strong>removed BKs</strong></li><ol>");
							for (int a = 0; a < pro2.size(); a++)
								bw.write("<li>" + pro2.get(a) + "</li>");
							bw.write("</ol>");
						}
					}

					if (!obj.getAddedColumns().isEmpty()) {
						bw.write("<li>ColumnsAdded</li><ol>");
						for (int i = 0; i < obj.getAddedColumns().size(); i++) {
							bw.write("<li>" + obj.getAddedColumns().get(i) + "</li>");
						}
						bw.write("</ol>");

					}
					if (!obj.getRemovedColumns().isEmpty()) {
						bw.write("<li>ColumnsRemoved</li><ol>");
						for (int i = 0; i < obj.getRemovedColumns().size(); i++) {
							bw.write("<li>" + obj.getRemovedColumns().get(i) + "</li>");
						}
						bw.write("</ol>");
					}

					if (!obj.getUpdatedCol().isEmpty()) { // araylist of
															// hashmaps
						bw.write("<li>ColumnsUpdated</li><ol>");
						for (int i = 0; i < obj.getUpdatedCol().size(); i++) {
							HashMap<String, String> ok = obj.getUpdatedCol().get(i);
							for (Entry<String, String> map : ok.entrySet()) {
								bw.write("<li>" + map.getKey() + "</li>");
								bw.write("" + map.getValue() + "");
							}
						}
						bw.write("</ol>");
					}
					bw.write("</ul>");
				}
			}
			bw.write("</ol>");
		}
		bw.close();
	}

}
