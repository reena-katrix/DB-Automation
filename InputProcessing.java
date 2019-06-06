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
					&& entry.getName().endsWith("xml")) {
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

	public static void listFilesForFolder(final File folder) throws IOException {
		System.out.println(folder.length());
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.getAbsolutePath().endsWith("jar")) {
				System.out.println(fileEntry.getAbsolutePath());
				ExtractJAR("C:\\Users\\reenaya\\Music", fileEntry.getAbsolutePath(), "hibernate");

			}
		}
	}

	public static void ExtractFolder(File FolderPath) throws IOException {

		listFilesForFolder(FolderPath);

	}
}
