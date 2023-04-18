package jba;

import java.io.BufferedReader;
import java.io.FileReader;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class Parser {

	String thisLine = "";
	// Holder for Headers
	HashMap<String, String> headers = new HashMap<String, String>();
	// Holder for Grid section
	GridHeader gridHeader = null;
	// Holder for Grid data
	ArrayList<DataRow> dRow = new ArrayList<DataRow>();
	// Year incrementer
	int yearCount = 0;
	// Header flag
	boolean headersFound = false;
	// Grid Section flag
	boolean gridHeadersFound = false;
	int skippedLineCount = 0;

	public void parse(Properties props) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(props.getProperty("app.file")));
			while ((thisLine = br.readLine()) != null) {
				// Lines starting with '[' are treated as header fields
				if (thisLine.startsWith("[")) {
					headers.putAll(processHeaders(thisLine));
					headersFound = true;
				} else
				// Line starting with 'Grid' are treated as Grid Headers
				if ((thisLine.startsWith("Grid")) && (headersFound)) {
					// Every Grid section , year count is set to 0 , to start with value set in
					// [Years=1991-2000]
					yearCount = 0;
					String[] grid = processGridHeaders(thisLine);
					gridHeader = new GridHeader();

					gridHeader.setXref(grid[0]);
					gridHeader.setYref(grid[1]);
					// Extract Start and End Years
					gridHeader.setStartYear(Integer.parseInt((String) headers.get("Years").split("-")[0]));
					gridHeader.setEndYear(Integer.parseInt((String) headers.get("Years").split("-")[1]));
					// Set flag to track grid headers processed
					gridHeadersFound = true;
				} else
				// Grid data processing
				if ((thisLine.startsWith(" ")) && (gridHeadersFound)) {
					dRow.addAll(processDataRow(thisLine, gridHeader, yearCount));
					yearCount++;
				} else {
					skippedLineCount++;
				}

			}
			br.close();
			System.out.println("File read complete !");
			System.out.println("Rows Generated \t:\t " + dRow.size());
			System.out.println("Total Rows Skipped \t:\t " + skippedLineCount);

			SqliteDB db = new SqliteDB(props.getProperty("app.db"));
			db.addData(dRow);
			if (Boolean.parseBoolean(props.getProperty("app.displayTableData")))
				db.getData();
			else
				System.out.println("Data loaded.");

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Extract precipitation Data
	private static ArrayList<DataRow> processDataRow(String txt, GridHeader ghdr, int yearInc) {
		List<String> dataList = Arrays.asList(txt.trim().split(" "));
		// remove empty items
		dataList = dataList.stream().filter(empty -> (!empty.equals(""))).collect(Collectors.toList());
		ArrayList<DataRow> rowList = new ArrayList<DataRow>();
		// Used for date creation 1=jan , 2= feb
		int startMonthIdx = 1;

		for (String dl : dataList) {
			// Set date , startMonthIdx indicates the month for the date
			LocalDate date = LocalDate.of(ghdr.getStartYear() + yearInc, startMonthIdx, 1);

			DataRow dr = new DataRow();
			dr.setDate(date);
			dr.setxRef(ghdr.getXref());
			dr.setyRef(ghdr.getYref());
			dr.setValue(Integer.parseInt(dl));
			startMonthIdx++;

			rowList.add(dr);
		}

		return rowList;

	}

	// Extract headers from lines starting with '['
	private static Map<String, String> processHeaders(String txt) {
		Map<String, String> _headers = new HashMap<String, String>();
		txt = txt.replace("[", "");
		Arrays.asList(txt.split("]")).forEach(e -> {
			String[] temp = e.split("=");
			_headers.put(temp[0].trim(), temp[1].trim());
		});
		return _headers;
	}

	// Extracts Grid-ref information
	private static String[] processGridHeaders(String txt) {
		return txt.split("=")[1].split(",");
	}

}
