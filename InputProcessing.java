import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.Enumeration;

public class InputProcessing {
	// to extract folder contents
	public static String unZip(String destinationDir, String FolderPath) throws IOException {
		File file = new File(FolderPath);
		JarFile jar = new JarFile(file);

		// fist get all directories,
		// then make those directory on the destination Path
		String fileName = " ";
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

			String fileName2 = destinationDir + File.separator + entry.getName();
			File f = new File(fileName2);

			if (!fileName2.endsWith("/")) {
				InputStream is = jar.getInputStream(entry);
				FileOutputStream fos = new FileOutputStream(f);

				// write contents of 'is' to 'fos'
				while (is.available() > 0) {
					// System.out.println(is.toString());
					fos.write(is.read());
				}

				fos.close();
				is.close();
			}
		}
		return fileName; // returns extracted folder
	}

	public static void ExtractJAR(String destinationDir, String FolderPath, String folderName) throws IOException {
		File file = new File(FolderPath);
		JarFile jar = new JarFile(file);
		String fileName, fileName2 = " ";
		// fist get all directories,
		// then make those directory on the destination Path
		for (Enumeration<JarEntry> enums = jar.entries(); enums.hasMoreElements();) {
			JarEntry entry = (JarEntry) enums.nextElement();
			// fileName = " ";
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

				// write contents of 'is' to 'fos'
				while (is.available() > 0) {
					fos.write(is.read());
				}

				fos.close();
				is.close();
			}
		}
		// return fileName2;
	}
}
