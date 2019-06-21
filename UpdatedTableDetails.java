import java.util.ArrayList;
import java.util.HashMap;

public class UpdatedTableDetails {

	ArrayList<HashMap<String, String>> updated = new ArrayList<HashMap<String, String>>();
	ArrayList<String> removedCol = new ArrayList<String>();
	ArrayList<String> addedCol = new ArrayList<String>();

	UpdatedTableDetails() {
	}

	UpdatedTableDetails(ArrayList<String> addedCol, ArrayList<String> removedCol,
			ArrayList<HashMap<String, String>> updated) {
		this.addedCol = addedCol;
		this.removedCol = removedCol;
		this.updated = updated;
	}

	void setRemovedCol(ArrayList<String> rem) {
		this.removedCol = rem;
	}

	void setAddedCol(ArrayList<String> add) {
		this.addedCol = add;
	}

	ArrayList<String> getRemovedColumns() {
		return this.removedCol;
	}

	ArrayList<String> getAddedColumns() {
		return this.addedCol;
	}

	void setUpdatedCol(ArrayList<HashMap<String, String>> up) {
		this.updated = up;
	}

	ArrayList<HashMap<String, String>> getUpdatedCol() {
		return this.updated;
	}

	void printTableUpdates() {
		if (!this.addedCol.isEmpty())
			System.out.println("Added col:\t" + this.addedCol);
		if (!this.removedCol.isEmpty())
			System.out.println("Removed col:\t" + this.removedCol);
		if (!this.updated.isEmpty())
			System.out.println("updated col\t:" + this.updated);
		else
			return;
	}
}
