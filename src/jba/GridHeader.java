package jba;

//Class for Gird header data
public class GridHeader {
	private String xRef;
	private String yRef;
	private int startYear;
	private int endYear;

	public String getXref() {
		return this.xRef;
	}

	public void setXref(String ref) {
		this.xRef = ref;
	}

	public String getYref() {
		return this.yRef;
	}

	public void setYref(String ref) {
		this.yRef = ref;
	}

	public int getStartYear() {
		return this.startYear;
	}

	public void setStartYear(int start) {
		this.startYear = start;
	}

	public int getEndtYear() {
		return this.endYear;
	}

	public void setEndYear(int end) {
		this.endYear = end;
	}
}
