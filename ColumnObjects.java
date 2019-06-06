import java.util.HashMap;
import java.util.Map.Entry;

public class ColumnObjects {
	String name;
	String type;
	String unique;
	String defaultValue;
	String length;

	ColumnObjects() // default constructor
	{
		this.name = " ";
		this.type = " ";
		this.unique = " ";
		this.defaultValue = " ";
		this.length = " ";

	}

	// to add column attributes
	public ColumnObjects(HashMap<String, String> temp) {
		for (Entry<String, String> m : temp.entrySet()) {
			String key = m.getKey();
			switch (key) {
			case "name":
				this.name = m.getValue();
				break;
			case "type":
				this.type = m.getValue();
				break;
			case "unique":
				this.unique = m.getValue();
				break;
			case "defaultValue":
				this.defaultValue = m.getValue();
				break;
			case "length":
				this.length = m.getValue();
				break;
			default:
				break;
			}
		}
	}

	String getObjName() {
		return this.name;
	}

	void printColumnObjectAttriInfo() {
		System.out.println("Object name" + " " + "=" + " " + this.name + " " + "type" + " " + "=" + " " + this.type
				+ " " + "unique" + " " + "=" + " " + this.unique + " " + "default" + " " + "=" + " " + this.defaultValue
				+ " " + "length" + " " + "=" + " " + this.length);
	}

}
