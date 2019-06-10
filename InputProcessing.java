import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Collection;
import java.util.Enumeration;

public class InputProcessing {

	public static void ExtractJAR(String destinationDir, String FolderPath, String folderName) throws IOException {
		File file = new File(FolderPath);
		JarFile jar = new JarFile(file);
		String fileName, fileName2 = " ";
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();
			fileName = destinationDir + File.separator + entry.getName();
			File f = new File(fileName);
			if (fileName.endsWith("/")) {
				f.mkdirs();
			}
		}
		// now create all files
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();
			fileName2 = " ";
			fileName2 = destinationDir + File.separator + entry.getName();
			File f = new File(fileName2);
			if (!fileName2.endsWith("/") && entry.getName().indexOf(folderName) == 0
					&& entry.getName().endsWith("hbm.xml")) {
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(f);
				while (is.available() > 0) {
					fos.write(is.read());
				}

				fos.close();
				is.close();
			} else if (!fileName2.endsWith("/") && entry.getName().indexOf(folderName) == 0
					&& entry.getName().endsWith("xml") && !entry.getName().contains("view")) {
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(f);
				while (is.available() > 0) {
					fos.write(is.read());
				}

				fos.close();
				is.close();
			}
		}

	}

	public static void listFilesForFolder(final File folder, String dest) throws IOException {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getAbsolutePath().endsWith("jar")) {
				ExtractJAR(dest, fileEntry.getAbsolutePath(), "hibernate");
				ExtractJAR(dest, fileEntry.getAbsolutePath(), "ddlschema");

			}
		}
	}

	public static void ExtractFolder(File FolderPath, String dest) throws IOException {

		listFilesForFolder(FolderPath, dest);

	}
}
