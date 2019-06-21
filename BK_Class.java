import java.io.File;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class BK_Class {
	String name = " ";
	Set<String> previousColumns = new TreeSet<>();
	Set<String> updatedColumns = new TreeSet<>();

	BK_Class(ArrayList<String> list, File dirParent) {
		if (dirParent.getParent().contains("ExtractedEPNM1")) {
			this.name = list.get(0);
			for (int i = 1; i < list.size(); i++)
				updatedColumns.add(list.get(i));
		} else if (dirParent.getParent().contains("ExtractedEPNM0")) {
			this.name = list.get(0);
			for (int i = 1; i < list.size(); i++)
				previousColumns.add(list.get(i));
		}
	}

	String getName() {
		return this.name;
	}

	Set<String> getUpdatedColumns() {
		return this.updatedColumns;
	}

	Set<String> getpreviousColumns() {
		return this.previousColumns;
	}

	Set<String> getAddedBKColumns() {
		boolean added = this.updatedColumns.removeAll(this.previousColumns);
		if (added)
			return this.updatedColumns;
		else
			return null;
	}

	Set<String> getRemovedBKColumns() {
		boolean rem = this.previousColumns.removeAll(this.updatedColumns);
		if (rem)
			return this.previousColumns;
		else
			return null;
	}
}
