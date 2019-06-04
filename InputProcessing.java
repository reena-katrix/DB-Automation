    import java.io.File;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.InputStream;
	import java.util.zip.ZipFile;
	import java.util.jar.JarEntry;
	import java.util.jar.JarFile;
	import java.util.Enumeration;


	import java.util.ArrayList;
	import java.util.HashMap;
	import java.util.HashSet;
	import java.util.Map;
	import java.util.Map.Entry;
	import java.util.List;
	import javax.lang.model.element.Element;
	import javax.xml.parsers.DocumentBuilder;
	import javax.xml.parsers.DocumentBuilderFactory;
	import javax.xml.parsers.ParserConfigurationException;
	import javax.xml.xpath.XPath;
	import javax.xml.xpath.XPathConstants;
	import javax.xml.xpath.XPathFactory;

	import org.w3c.dom.Document;
	import org.w3c.dom.NodeList;
	import org.w3c.dom.xpath.XPathExpression;
	import org.w3c.dom.NamedNodeMap;
	import org.w3c.dom.Node;
	import org.xml.sax.InputSource;
	import org.xml.sax.SAXException;

	import java.io.BufferedInputStream;
	import java.io.BufferedReader;
	import java.io.ByteArrayInputStream;
	import java.io.FileNotFoundException;
	import java.io.FileReader;
	import java.io.InputStreamReader;
	import java.io.OutputStream;
	public class InputProcessing {
		//to extract folder contents
		public static String unZip(String destinationDir, String FolderPath) throws IOException {
			File file = new File(FolderPath);
			JarFile jar = new JarFile(file);
			String fileName=" ";
			// fist get all directories,
			// then make those directory on the destination Path
			for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
				JarEntry entry = (JarEntry) enums.nextElement();
	 
			    fileName = destinationDir + File.separator + entry.getName();
				File f = new File(fileName);
	 
				if (fileName.endsWith("/")) {
					f.mkdirs();
				}
	 
			}
			//now create all files
			for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
				JarEntry entry = (JarEntry) enums.nextElement();
	 
				String fileName2 = destinationDir + File.separator + entry.getName();
				File f = new File(fileName2);
	 
				if (!fileName2.endsWith("/")) {
					InputStream is = jar.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(f);
	 
					// write contents of 'is' to 'fos'
					while (is.available() > 0) {
						fos.write(is.read());
					}
	 
					fos.close();
					is.close();
				}
			}
			return fileName;
		}
		
		public static void ExtractJAR(String destinationDir, String FolderPath, String folderName) throws IOException {
			File file = new File(FolderPath);
			JarFile jar = new JarFile(file);
			String fileName=" ";
			// fist get all directories,
			// then make those directory on the destination Path
			for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
				JarEntry entry = (JarEntry) enums.nextElement();
	 
			    fileName = destinationDir + File.separator + entry.getName();
				File f = new File(fileName);
	 
				if (fileName.endsWith("/")) {
					f.mkdirs();
				}
			}
			//now create all files
			for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
				JarEntry entry = (JarEntry) enums.nextElement();
	 
				String fileName2 = destinationDir + File.separator + entry.getName();
				File f = new File(fileName2);
	 
				if (!fileName2.endsWith("/") && entry.getName().indexOf(folderName)==0 &&
						entry.getName().endsWith("xml")) {
					InputStream is = jar.getInputStream(entry);
					FileOutputStream fos = new FileOutputStream(f);
	 
					// write contents of 'is' to 'fos'
					while (is.available() > 0) {
						fos.write(is.read());
					}
	 
					fos.close();
					is.close();
				}
			}
		}
	}
