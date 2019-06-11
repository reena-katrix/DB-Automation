
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.*;
import java.util.List;

public class Comparator {

	void AddedDeleted(HashMap<String, String> h1, HashMap<String, String> h2, String col) {
		Iterator iter = h1.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			if (h2.get(entry.getKey()) == null)
				System.out.println(col + ": " + "removed attribues: " + entry.getKey() + "=" + entry.getValue());

		}

		Iterator iter2 = h2.entrySet().iterator();

		while (iter2.hasNext()) {
			Map.Entry entry = (Map.Entry) iter2.next();
			if (h1.get(entry.getKey()) == null)
				System.out.println(col + ": " + "added attributes :" + entry.getKey() + " =" + entry.getValue());

		}
	}

	void updatedPoints(HashMap<String, String> h1, HashMap<String, String> h2, String col) {
		Iterator iter = h1.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			if (h2.get(entry.getKey()) != null) {
				if (!entry.getValue().equals(h2.get(entry.getKey())))
					System.out.println(col + " :" + entry.getKey() + "=" + entry.getValue() + " is changed to "
							+ entry.getKey() + "=" + h2.get(entry.getKey()));
			}
		}
	}

	void Test(ArrayList<ArrayList<HashMap<String, String>>> list1, ArrayList<ArrayList<HashMap<String, String>>> list2,
			ArrayList<String> c1, ArrayList<String> c2) {

		// System.out.println(Col2);
		// System.out.println("--------------Removed Columns-------------");
		for (int i = 0; i < c1.size(); i++) {
			if (c2.contains(c1.get(i)) == false) {
				System.out.println(c1.get(i) + " " + " column is Removed");
			}
		}
		// System.out.println("--------------Added Columns-------------");
		for (int i = 0; i < c2.size(); i++) {
			if (c1.contains(c2.get(i)) == false) {
				System.out.println(c2.get(i) + " " + " column is Added");
			}
		}

		// System.out.println("--------------Updated Columns-------------");
		for (int i = 0; i < c1.size(); i++) {
			if (c2.contains(c1.get(i))) // if column is in list2
			{
				// System.out.println(c1.get(i)); //common column
				int index1 = -1, index2 = -1;
				for (int j = 0; j < list1.size(); j++) // find properties of
														// both the lists for
														// that colm
				{
					ArrayList<HashMap<String, String>> print = list1.get(j);

					HashMap<String, String> map = print.get(0); // each list
																// details
					if (map.containsValue(c1.get(i))) {
						index1 = j;
						break;
					}
				}

				for (int j = 0; j < list2.size(); j++) // find properties of
														// both the lists for
														// that colm
				{
					ArrayList<HashMap<String, String>> print = list2.get(j);

					HashMap<String, String> map = print.get(0); // each list
																// details
					if (map.containsValue(c1.get(i))) // CORRECTION
					{
						index2 = j;
						break;
					}
				}
				// System.out.println(index1+" "+index2);
				// now we have indexes to compare details at them.
				ArrayList<HashMap<String, String>> comp1 = list1.get(index1);
				ArrayList<HashMap<String, String>> comp2 = list2.get(index2);
				// System.out.println(c1.get(i) + " is updated with following
				// changes:");
				if(!comp1.equals(comp2)){
				String col = c1.get(i);
				HashMap<String, String> colm1 = comp1.get(0);
				HashMap<String, String> colm2 = comp2.get(0);
				// System.out.println("changes in column :");
				AddedDeleted(colm1, colm2, col);
				updatedPoints(colm1, colm2, col);
				}

				// System.out.println("changes in column objetcs:");
				for (int p = 1; p < comp1.size(); p++) {
					for (int s = 1; s < comp2.size(); s++) {
						HashMap<String, String> change1 = comp1.get(p);
						HashMap<String, String> change2 = comp2.get(s);
						if(!change1.equals(change2)){
						String column = c1.get(i) + "\tReferences";
						AddedDeleted(change1, change2, column);
						updatedPoints(change1, change2, column);
						}
					}
				}
			}
		}
		return;
	}

	void TestDDL(ArrayList<ArrayList<String>> ddl1, ArrayList<ArrayList<String>> ddl2) {
		if(!ddl1.equals(ddl2)){
		for (int i = 0; i < ddl1.size(); i++) {
			ArrayList<String>one,one1;
			one = ddl1.get(i);one1 = ddl1.get(i);
			for(int j=0;j<ddl2.size();j++){
				ArrayList<String>two,two1 ;
				two = ddl2.get(j);two1 = ddl2.get(j);
				if(one.size()>0 && two.size()>0 && one.get(0).equals(two.get(0)) && !one.containsAll(two)){            //same BK
					
					//added BKs
					two1.removeAll(one1);
					if(two1.size()>0)
						System.out.println(two.get(0)+"Added BKs:\t"+two1);
					//removed BKs
					one1.removeAll(two);
					if(one1.size()>0)
						System.out.println(one.get(0)+"Removed BKs:\t"+one1);
					
				}
			}
		}
		}
	}
}
