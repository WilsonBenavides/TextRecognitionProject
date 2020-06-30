package application;

import javafx.beans.property.SimpleStringProperty;

public class TableContent {
	
	private final SimpleStringProperty col1;
	private final SimpleStringProperty col2;
	private final SimpleStringProperty col3;
	
	
	public TableContent(String col1, String col2, String col3) {
		this.col1 = new SimpleStringProperty(col1);
		this.col2 = new SimpleStringProperty(col2);
		this.col3 = new SimpleStringProperty(col3);
	}

	public String getCol1() {
		return col1.get();
	}

	public String getCol2() {
		return col2.get();
	}

	public String getCol3() {
		return col3.get();
	}

	public void setCol1(String col1) {
		this.col1.set(col1);
	}

	public void setCol2(String col2) {
		this.col2.set(col2);
	}

	public void setCol3(String col3) {
		this.col3.set(col3);
	}
}
