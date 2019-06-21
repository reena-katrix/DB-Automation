import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class CompareVersion {

	public void getDiff(File dirA, File dirB) throws ParserConfigurationException, SAXException, IOException {
		File[] fileList1 = dirA.listFiles();
		File[] fileList2 = dirB.listFiles();
		Arrays.sort(fileList1);
		Arrays.sort(fileList2);
		HashMap<String, File> map1; // name
		if (fileList1.length < fileList2.length) {
			map1 = new HashMap<String, File>();
			for (int i = 0; i < fileList1.length; i++) {
				map1.put(fileList1[i].getName(), fileList1[i]);
			}

			compareNow(fileList2, map1);
		} else {
			map1 = new HashMap<String, File>();
			for (int i = 0; i < fileList2.length; i++) {
				map1.put(fileList2[i].getName(), fileList2[i]); // name,location
																// of file
			}

			compareNow(fileList1, map1);
		}

	}

	public void compareNow(File[] fileArr, HashMap<String, File> map)
			throws ParserConfigurationException, SAXException, IOException {

		for (int i = 0; i < fileArr.length; i++) {
			String fName = fileArr[i].getName();
			File fComp = map.get(fName);
			map.remove(fName);
			if (fComp != null) // shorter file is found in bigger one
			{
				if (fComp.isDirectory()) // check if directory
				{
					getDiff(fileArr[i], fComp);
				} else // else check if same or diff contents
				{
					String cSum1 = checksum(fileArr[i]);
					String cSum2 = checksum(fComp);
					if (!cSum1.equals(cSum2)) {
						CompareFiles compare = new CompareFiles();
						compare.CompareFileContent(fileArr[i], fComp);
					} else {
						// TablesIdentical.add(fileArr[i].getName());
					}
				}
			} else // if shorter file not found in bigger one
			{
				if (fileArr[i].isDirectory()) // check if old is directory
				{
					traverseDirectory(fileArr[i]); // it
				} else // if not then old is not in newer
				{
					if (fileArr[i].getParent().contains("ExtractedEPNM0")
							&& !fileArr[i].getParent().contains("ExtractedEPNM1")
							&& fileArr[i].getName().contains("hbm.xml"))
						TableComparison.TablesDeleted
								.add(fileArr[i].getName().substring(0, fileArr[i].getName().indexOf(".")));
					else if (fileArr[i].getParent().contains("ExtractedEPNM1")
							&& fileArr[i].getName().contains("hbm.xml"))
						TableComparison.TablesAdded
								.add(fileArr[i].getName().substring(0, fileArr[i].getName().indexOf(".")));
				}
			}
		}
		Set<String> set = map.keySet(); // set of all keys
		Iterator<String> it = set.iterator();
		while (it.hasNext()) {
			String n = it.next();
			File fileFrmMap = map.get(n);
			map.remove(n);
			if (fileFrmMap.isDirectory()) {
				traverseDirectory(fileFrmMap);
			} else // only in newer
			{
				if (fileFrmMap.getParent().contains("ExtractedEPNM0")
						&& !fileFrmMap.getParent().contains("ExtractedEPNM1")
						&& fileFrmMap.getName().contains("hbm.xml"))
					TableComparison.TablesDeleted
							.add(fileFrmMap.getName().substring(0, fileFrmMap.getName().indexOf(".")));
				else if (fileFrmMap.getParent().contains("ExtractedEPNM1") && fileFrmMap.getName().contains("hbm.xml"))
					TableComparison.TablesAdded
							.add(fileFrmMap.getName().substring(0, fileFrmMap.getName().indexOf(".")));
			}
		}
	}

	public void traverseDirectory(File dir) throws IOException {
		File[] list = dir.listFiles();
		for (int k = 0; k < list.length; k++) {
			if (list[k].isDirectory()) {
				traverseDirectory(list[k]);
			} else {
				if (list[k].getParent().contains("ExtractedEPNM0") && !list[k].getParent().contains("ExtractedEPNM1")
						&& list[k].getName().contains("hbm.xml"))
					TableComparison.TablesDeleted.add(list[k].getName().substring(0, list[k].getName().indexOf(".")));
				else if (list[k].getParent().contains("ExtractedEPNM1") && list[k].getName().contains("hbm.xml"))
					TableComparison.TablesAdded.add(list[k].getName().substring(0, list[k].getName().indexOf(".")));
			}
		}
	}

	public String checksum(File file) {
		try {
			InputStream fin = new FileInputStream(file);
			java.security.MessageDigest md5er = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[1024];
			int read;
			do {
				read = fin.read(buffer);
				if (read > 0)
					md5er.update(buffer, 0, read);
			} while (read != -1);
			fin.close();
			byte[] digest = md5er.digest();
			if (digest == null)
				return null;
			String strDigest = "0x";
			for (int i = 0; i < digest.length; i++) {
				strDigest += Integer.toString((digest[i] & 0xff) + 0x100, 16).substring(1).toUpperCase();
			}
			return strDigest;
		} catch (Exception e) {
			return null;
		}
	}

}
