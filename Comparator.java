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
import java.util.Map.Entry;

import java.util.*;
import java.util.Map.Entry;

public class Comparator {
	public void CompareFileS(ArrayList<ColumnDetails> file1, ArrayList<ColumnDetails> file2, File dirA, File dirB)
			throws IOException {

		UpdatedTableDetails updatedColList = new UpdatedTableDetails();
		// for added deleted columns
		ArrayList<String> col1 = new ArrayList<String>();
		ArrayList<String> col2 = new ArrayList<String>();
		for (int i = 0; i < file1.size(); i++) {
			if (!file1.get(i).getColumnName().equals(" "))
				col1.add(file1.get(i).getColumnName());
		}
		for (int i = 0; i < file2.size(); i++) {
			if (!file2.get(i).getColumnName().equals(" "))
				col2.add(file2.get(i).getColumnName());
		}
		ArrayList<String> dummy1 = new ArrayList<String>();
		ArrayList<String> dummy2 = new ArrayList<String>();
		dummy1 = new ArrayList<>(col1);
		dummy2 = new ArrayList<>(col2);
		if (dirA.getParent().contains("ExtractedEPNM1")) {
			boolean add = col1.removeAll(col2);
			if (add && col1.size() > 0) {
				updatedColList.setAddedCol(col1);
			}
			boolean rem = dummy2.removeAll(dummy1);
			if (rem && dummy2.size() > 0)
				updatedColList.setRemovedCol(dummy2);
		} else if (dirA.getParent().contains("ExtractedEPNM0")) {
			boolean add = col2.removeAll(col1);
			if (add && col2.size() > 0) {
				updatedColList.setAddedCol(col2);
			}
			boolean rem = dummy1.removeAll(dummy2);
			if (rem && dummy1.size() > 0)
				updatedColList.setRemovedCol(dummy1);
		}

		// for updated columns
		ArrayList<HashMap<String, String>> updatedcol = new ArrayList<HashMap<String, String>>();
		for (int i = 0; i < file1.size(); i++) {
			HashMap<String, String> res = new HashMap<String, String>();
			for (int j = 0; j < file2.size(); j++) {
				String updates = " ";
				if (file1.get(i).getColumnName().equals(file2.get(j).getColumnName())
						&& !file1.get(i).equals(file2.get(j)) && !file1.get(i).getColumnName().equals(" ")) {
					if (dirA.getParent().contains("ExtractedEPNM0"))
						updates = findUpdates(file1.get(i), file2.get(j));
					else if (dirA.getParent().contains("ExtractedEPNM1"))
						updates = findUpdates(file2.get(j), file1.get(i));
				}
				if (!updates.equals(" "))
					res.put(file1.get(i).getColumnName(), updates);
			}
			if (!res.isEmpty())
				updatedcol.add(res);

		}
		if (!updatedcol.isEmpty())
			updatedColList.setUpdatedCol(updatedcol);
		TableComparison.allUpdatedTables.put(dirA.getName().substring(0, dirA.getName().indexOf(".")), updatedColList);

	}

	public String findUpdates(ColumnDetails col1, ColumnDetails col2) throws IOException {
		String attr = " ";
		if (!col1.getComponentName().equals(" ") && !col2.getComponentName().equals(" ")
				&& !col1.getType().equals(col1.getType())) {
			if (col1.getType().equals(" ") && !col2.getType().equals(" "))
				attr += "Complex type added:\t" + col2.getType() + "\n";
			else if (col2.getType().equals(" ") && !col1.getType().equals(" "))
				attr += "Complex type removed:\t" + col1.getType() + "\n";
			else if (!col2.getType().equals(" ") && !col1.getType().equals(" "))
				attr += "Complex type value changed:" + col1.getType() + "\t to \t" + col2.getType() + "\n";
		} else {
			if (!col2.getType().equals(col1.getType())) {
				if (col1.getType().equals(" ") && !col2.getType().equals(" "))
					attr += "type added:\t" + col2.getType() + "\n";
				else if (col2.getType().equals(" ") && !col1.getType().equals(" "))
					attr += "type removed:\t" + col1.getType() + "\n";
				else if (!col2.getType().equals(" ") && !col1.getType().equals(" "))
					attr += "type value changed:" + col1.getType() + "\t to \t" + col2.getType() + "\n";
			}
			if (!col2.getUnique().equals(col1.getUnique())) {
				if (col1.getUnique().equals(" ") && !col2.getUnique().equals(" "))
					attr += "Unique value added:\t" + col2.getUnique() + "\n";
				else if (col2.getUnique().equals(" ") && !col1.getUnique().equals(" "))
					attr += "Unique value removed:\t" + col1.getUnique() + "\n";
				else if (!col2.getUnique().equals(" ") && !col1.getUnique().equals(" "))
					attr += "Unique value changed:" + col1.getUnique() + "\t to \t" + col2.getUnique() + "\n";
			}
			if (!col2.getDefaultValue().equals(col1.getDefaultValue())) {
				if (col1.getDefaultValue().equals(" ") && !col2.getDefaultValue().equals(" "))
					attr += "DefaultValue added:\t" + col2.getDefaultValue() + "\n";
				else if (col2.getDefaultValue().equals(" ") && !col1.getDefaultValue().equals(" "))
					attr += "DefaultValue removed:\t" + col1.getDefaultValue() + "\n";
				else if (!col2.getDefaultValue().equals(" ") && !col1.getDefaultValue().equals(" ")
						&& Double.parseDouble(col1.getDefaultValue()) != Double.parseDouble(col2.getDefaultValue()))
					attr += "DefaultValue changed:" + col1.getDefaultValue() + "\t to \t" + col2.getDefaultValue()
							+ "\n";
			}
			if (!col2.getNotNull().equals(col1.getNotNull())) {
				if (col1.getNotNull().equals(" ") && !col2.getNotNull().equals(" "))
					attr += "NotNull Value added:\t" + col2.getNotNull() + "\n";
				else if (col2.getNotNull().equals(" ") && !col1.getNotNull().equals(" "))
					attr += "NotNull Value removed:\t" + col1.getNotNull() + "\n";
				else if (col2.getNotNull().equals(" ") && !col1.getNotNull().equals(" "))
					attr += "NotNull Value changed:" + col1.getNotNull() + "\t to \t" + col2.getNotNull() + "\n";
			}
			if (!col2.getLength().equals(col1.getLength())) {
				if (col1.getLength().equals(" ") && !col2.getLength().equals(" "))
					attr += "Length added:\t" + col2.getLength() + "\n";
				else if (col2.getLength().equals(" ") && !col1.getLength().equals(" "))
					attr = "Length removed:\t" + col1.getLength() + "\n";
				else if (!col2.getLength().equals(" ") && !col1.getLength().equals(" "))
					attr += "Length Value changed:" + col1.getLength() + "\t to \t" + col2.getLength() + "\n";
			}
		}
		return attr;
	}

}
