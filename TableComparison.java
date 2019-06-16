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
			compare.getDiff(dir1,dir2);
//			compare.getDiff(new File("ExtractedEPNM0"), new File("ExtractedEPNM0"));
		} catch (IOException ie) {
			ie.printStackTrace();
		}

//		for (Entry<String, ArrayList<String>> m : newBKs.entrySet()){
//			System.out.println(m.getKey()+":"+m.getValue());
//		}
//		for (Entry<String, ArrayList<String>> m : oldBKs.entrySet()){
//			System.out.println(m.getKey()+":"+m.getValue());
//		}
//		to check 
		
		Collections.sort(TablesAdded);
		Collections.sort(TablesDeleted);
		bw.write("<h4>Comparision between EPNM 3.0 and EPNM 3.1</h4>");
		if (!TablesAdded.isEmpty()) {
			// System.out.println("ADDED TABLES:" + TablesAdded);
			bw.write("<strong>Added Tables</strong><ol>");
			for (int i = 0; i < TablesAdded.size(); i++) {
				String add = TablesAdded.get(i).substring(0, TablesAdded.get(i).indexOf("."));
				bw.write("<li>" + add + "</li>");
				// for listing BKs
				if (newBKs.containsKey(add)) {
					System.out.println("table found at:"+i+add);
					ArrayList<String> cols = new ArrayList<>(newBKs.get(add));
					System.out.println("addedBKs:"+cols);
					if (!cols.isEmpty()) {
						bw.write("<strong>BKs</strong><ol>");
						for (int in = 0; in < cols.size(); in++)
							bw.write("<li>" + cols.get(in) + "</li>");
						bw.write("</ol>");
					}

				}
			}
			bw.write("</ol>");
		}

		if (!TablesDeleted.isEmpty()) {
			// System.out.println("DELETED TABLES:" + TablesDeleted);
			bw.write("<strong>DeletedTables</strong><ol>");
			for (int i = 0; i < TablesDeleted.size(); i++) {
				String del = TablesDeleted.get(i).substring(0, TablesDeleted.get(i).indexOf("."));
				bw.write("<li>" + del + "</li>");
			}
			bw.write("</ol>");
		}

		if (!allUpdatedTables.isEmpty()) {
			bw.write("<strong>UpdatedTables</strong><ol>");
			String tableName = "";
			for (Entry<String, UpdatedTableDetails> m : allUpdatedTables.entrySet()) {
				tableName = m.getKey().substring(0, m.getKey().indexOf("."));
				UpdatedTableDetails obj = m.getValue();
				if (!obj.getAddedColumns().isEmpty() || !obj.getRemovedColumns().isEmpty()
						|| !obj.getUpdatedCol().isEmpty()) {
					// System.out.println("UpdatedTable:" + tableName);
					bw.write("<li>" + tableName + "</li><ul>");
					ArrayList<String> dummy1 = null, dummy2 = null;
					if (newBKs.containsKey(tableName)) {
						dummy1 = newBKs.get(tableName);
					}
					if (oldBKs.containsKey(tableName)) {
						dummy2 = oldBKs.get(tableName);
					}
					System.out.println(dummy1);
					System.out.println(dummy2);
					if(!dummy1.isEmpty()){
					boolean addedBK = dummy1.removeAll(dummy2);
					if (addedBK) {
						bw.write("<strong>added BKs</strong><ol>");
						for (int a = 0; a < dummy1.size(); a++)
							bw.write("<li>" + dummy1.get(a) + "</li>");
						bw.write("</ol>");
					}
					}
					// dummy1 = new ArrayList<>(added);
					// dummy2 = new ArrayList<>(deleted);
					// boolean removedBK = dummy2.removeAll(dummy1);
					// if (removedBK) {
					// bw.write("" + tableName + " <strong>removed
					// BKs</strong><ol>");
					// for (int a = 0; a < dummy2.size(); a++)
					// bw.write("<li>" + dummy2.get(a) + "</li>");
					// bw.write("</ol>");
					// }

					if (!obj.getAddedColumns().isEmpty()) {
						// System.out.println("addedCol:");
						bw.write("<li>ColumnsAdded</li><ol>");
						for (int i = 0; i < obj.getAddedColumns().size(); i++) {
							// System.out.println(obj.getAddedColumns().get(i));
							bw.write("<li>" + obj.getAddedColumns().get(i) + "</li>");
						}
						bw.write("</ol>");

					}
					if (!obj.getRemovedColumns().isEmpty()) {
						// System.out.println("removedCol:");
						bw.write("<li>ColumnsRemoved</li><ol>");
						for (int i = 0; i < obj.getRemovedColumns().size(); i++) {
							// System.out.println(obj.getRemovedColumns().get(i));
							bw.write("<li>" + obj.getRemovedColumns().get(i) + "</li>");
						}
						bw.write("</ol>");
					}

					if (!obj.getUpdatedCol().isEmpty()) {
						// System.out.println("updatedCol:");
						bw.write("<li>ColumnsUpdated</li><ol>");
						for (int i = 0; i < obj.getUpdatedCol().size(); i++) { // total
																				// updated
																				// col
							HashMap<String, String> ok = obj.getUpdatedCol().get(i);
							// System.out.println(ok);
							for (Entry<String, String> map : ok.entrySet()) {
								// System.out.println(map.getKey() + " " +
								// map.getValue());
								bw.write("<li>" + map.getKey() + "&nbsp" + map.getValue() + "</li>");
							}
						}
						bw.write("</ol>");

					}
					bw.write("</ul>");
					// System.out.println();
				}
			}

		}

		bw.close();
	}

}
