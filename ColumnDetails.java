import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

class ColumnDetails {

	String propertyName = " ";
	String componentName = " ";
	String columnName = " ";
	String type = " ";
	String unique = " ";
	String defaultValue = " ";
	String length = " ";
	String notNull = " ";

	void setPropertyName(String input) {
		this.propertyName = input;
	}

	String getPropertyName() {
		return this.propertyName;
	}

	void setColumnName(String input) {
		this.columnName = input;
	}

	String getColumnName() {
		return this.columnName;
	}

	void setComponentName(String input) {
		this.componentName = input;
	}

	String getComponentName() {
		return this.componentName;
	}

	void setType(String input) {
		this.type = input;
	}

	String getType() {
		return this.type;
	}

	void setUnique(String input) {
		this.unique = input;
	}

	String getUnique() {
		return this.unique;
	}

	void setDefaultValue(String input) {
		this.defaultValue = input;
	}

	String getDefaultValue() {
		return this.defaultValue;
	}

	void setLength(String input) {
		this.length = input;
	}

	String getLength() {
		return this.length;
	}

	void setNotNull(String input) {
		this.notNull = input;
	}

	String getNotNull() {
		return this.notNull;
	}

	void printColumn() {
		System.out.println("propertyName:" + this.propertyName + "\tcolumnName:\t" + this.columnName
				+ "\tcomponentName:\t" + this.componentName + "\ttype:\t" + this.type + "\tunique:\t" + this.unique
				+ "\tdefaultValue:\t" + this.defaultValue + "\tlength:\t" + this.length + "notNull:\t" + this.notNull);
	}

}
