import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Comparator {

	// tablename,BKcolumns list

	public void CompareFileData(ColumnDetails file1[], int n, ColumnDetails file2[], int m, File dirA, File dirB)
			throws IOException {

		ArrayList<String> removed = new ArrayList<String>();
		ArrayList<String> added = new ArrayList<String>();
		for (int i = 0; i < n; i++) {
			int flag = 0;
			for (int j = 0; j < m; j++) {
				if (file1[i].getName().equals(file2[j].getName()))
					flag = 1;
			}
			if (flag == 0 && dirA.getParent().contains("ExtractedEPNM1"))
				added.add(file1[i].getName());
			else if (flag == 0 && dirA.getParent().contains("ExtractedEPNM0"))
				removed.add(file1[i].getName());
		}
		for (int i = 0; i < m; i++) {
			int flag = 0;
			for (int j = 0; j < n; j++) {
				if (file2[i].getName().equals(file1[j].getName()))
					flag = 1;
			}
			if (flag == 0 && dirB.getParent().contains("ExtractedEPNM1"))
				added.add(file2[i].getName());
			else if (flag == 0 && dirB.getParent().contains("ExtractedEPNM0"))
				removed.add(file2[i].getName());
		}
		ArrayList<HashMap<String, String>> updatedcol = new ArrayList<HashMap<String, String>>();
		Set<String> ColumnsList = new HashSet<String>();
		ColumnsList.add("instanceUuid");
		ColumnsList.add("displayName");
		ColumnsList.add("description");
		ColumnsList.add("owningEntityId");
		ColumnsList.add("deployPending");
		ColumnsList.add("name");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < m; j++) {
				if (file1[i].getName().equals(file2[j].getName()) && !ColumnsList.contains(file1[i].getName())
						&& !file1[i].getType().equals(file2[j].getType())) {
					HashMap<String, String> res = CompareColumn(file1[i], file2[j], dirA, dirB);
					updatedcol.add(res);
				}
			}
		}
		UpdatedTableDetails updatedColList = new UpdatedTableDetails();

		updatedColList.setAddedCol(added);
		updatedColList.setRemovedCol(removed);
		updatedColList.setUpdatedCol(updatedcol);
		TableComparison.allUpdatedTables.put(dirA.getName(), updatedColList); /// tablename,
																				/// class
																				/// of
																				/// updaes
	}

	// to compare one column from each file
	public HashMap<String, String> CompareColumn(ColumnDetails col1, ColumnDetails col2, File dirA, File dirB)
			throws IOException {
		HashMap<String, String> map = new HashMap<String, String>();
		// System.out.println(col1.getType() + "ok " + col2.getType());
		String s = " ";
		if (col1.getType() == null && col2.getType() != null) {
			if (dirA.getParent().contains("ExtractedEPNM0")) {
				s = "\ttype=" + col2.getType() + " \tadded";
				map.put(col1.getName(), s);
			} else if (dirA.getParent().contains("ExtractedEPNM1")) {
				s = "\ttype=" + col2.getType() + " \tremoved";
				map.put(col1.getName(), s);
			}

		}
		if (col1.getType() != null && col2.getType() == null) {
			if (dirA.getParent().contains("ExtractedEPNM0")) {
				s = "\ttype=" + col2.getType() + " \tadded";
				map.put(col1.getName(), s);
			} else if (dirA.getParent().contains("ExtractedEPNM1")) {
				s = "\ttype=" + col2.getType() + " \tremoved";
				map.put(col1.getName(), s);
			}
		} else if (col1.getType() != null && col2.getType() != null) {
			if (dirA.getParent().contains("ExtractedEPNM0")) {
				s = "\ttype modified: " + col1.getType() + "\tto\t" + col2.getType();
				map.put(col1.getName(), s);
			} else if (dirA.getParent().contains("ExtractedEPNM1")) {
				s = "\ttype modified: " + col2.getType() + "\tto\t" + col1.getType();
				map.put(col1.getName(), s);
			}
		}
		return map;
	}

	/*
	 * void CompareBK(BK_Class bk1[], int n, BK_Class bk2[], int m, File dirA,
	 * File dirB) {
	 * 
	 * for (int i = 0; i < n; i++) { for (int j = 0; j < m; j++) { if
	 * (bk1[i].getName().equals(bk2[j].getName())) { if
	 * (!bk1[i].getColumns().equals(bk2[j].getColumns()) &&
	 * dirA.getParent().contains("ExtractedEPNM0")) { //find added removed
	 * ArrayList<String> add,rem; add = new
	 * ArrayList<String>(bk2[j].getColumns()); //updated rem = new
	 * ArrayList<String>(bk1[i].getColumns()); //previous boolean added =
	 * add.removeAll(rem); //columns with added BK if (added) { for(int
	 * a=0;a<add.size();a++) //set BK is added addedBK.put(add.get(a),
	 * "added BK to:"+bk1[i].getName()); } add = new
	 * ArrayList<String>(bk2[j].getColumns()); //updated rem = new
	 * ArrayList<String>(bk1[i].getColumns()); //previous boolean removed =
	 * rem.removeAll(add); //columns with removed BK if (removed) { for(int
	 * a=0;a<rem.size();a++) //set BK is added removedBK.put(rem.get(a),
	 * "removed BK in:"+bk1[i].getName()); } } if
	 * (!bk1[i].getColumns().equals(bk2[j].getColumns()) &&
	 * dirA.getParent().contains("ExtractedEPNM1")) { //find added removed
	 * ArrayList<String> add,rem; rem = new
	 * ArrayList<String>(bk2[j].getColumns()); //previous add = new
	 * ArrayList<String>(bk1[i].getColumns()); //updated boolean added =
	 * add.removeAll(rem); if (added) { for(int a=0;a<add.size();a++) //set BK
	 * is added addedBK.put(add.get(a), " added BK:"+bk1[i].getName()); } rem =
	 * new ArrayList<String>(bk2[j].getColumns()); //previous add = new
	 * ArrayList<String>(bk1[i].getColumns()); //updated boolean removed =
	 * rem.removeAll(add); if (removed) { for(int a=0;a<rem.size();a++) //set BK
	 * is added removedBK.put(rem.get(a), "removed BK:"+bk1[i].getName()); } } }
	 * } } if(!addedBK.isEmpty()) System.out.println("added BK to COL:"
	 * +addedBK); if(!removedBK.isEmpty()) System.out.println(
	 * "removed BK in COL:"+removedBK); }
	 */

	void TestDDL(ArrayList<ArrayList<String>> ddl1, ArrayList<ArrayList<String>> ddl2, File dirA, File dirB)
			throws IOException {
		if (!ddl1.equals(ddl2)) {
			for (int i = 0; i < ddl1.size(); i++) {
				ArrayList<String> one;
				one = ddl1.get(i);
				for (int j = 0; j < ddl2.size(); j++) {
					ArrayList<String> two, two1, one1;
					two = ddl2.get(j);
					if (one.size() > 0 && two.size() > 0 && one.get(0).equals(two.get(0)) && !one.containsAll(two)) {

						one1 = new ArrayList<String>(one); // not working!
															// removed BKs
						two1 = new ArrayList<String>(two);
						boolean removed = one1.removeAll(two1);
						if (removed) {
							System.out.println(one.get(0) + "\tRemoved BKs from file:\t" + dirA.getParent() + one1);
							String html = one.get(0) + " Removed BKs from file:" + dirA.getParent() + one1;
							// bw.write("<body><p>"+html+"<p/></body>");
						}
						// added BKs
						one1 = new ArrayList<String>(one);
						two1 = new ArrayList<String>(two);
						boolean added = two1.removeAll(one1);
						if (added) {
							System.out.println(one.get(0) + "\tAdded BKs in file:\t" + dirB.getParent() + two1);
							String html = one.get(0) + " Added BKs in file:" + dirB.getParent() + two1;
							// bw.write("<body><p>"+html+"<p/></body>");
						}

					}
				}
			}
		}
	}

}
