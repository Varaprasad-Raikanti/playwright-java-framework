package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtils {

	public static Map<String, Map<String, String>> readTestData(String brandName, String sheetName) {
		Map<String, Map<String, String>> sectionDataMap = new LinkedHashMap<>();
		String filePath = "src/test/resources/TestData/" + brandName + "/" + brandName + "TestData.xlsx";

		try (FileInputStream fis = new FileInputStream(filePath); Workbook workbook = WorkbookFactory.create(fis)) {

			Sheet sheet = workbook.getSheet(sheetName);
			if (sheet == null) {
				throw new RuntimeException("Sheet not found: " + sheetName);
			}

			String currentSection = null;
			List<String> headers = new ArrayList<>();
			Map<String, String> dataMap = null;

			for (Row row : sheet) {
				Cell firstCell = row.getCell(0);

				if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
					continue;
				}

				String cellValue = firstCell.getStringCellValue().trim();

				// Section Header starts with #
				if (cellValue.startsWith("#")) {
					currentSection = cellValue.substring(1).trim();
					headers.clear();
					dataMap = new LinkedHashMap<>();
				} else if (currentSection != null && headers.isEmpty()) {
					// Header Row
					int lastColumn = row.getLastCellNum();
					for (int i = 0; i < lastColumn; i++) {
						Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						cell.setCellType(CellType.STRING);
						headers.add(cell.getStringCellValue().trim());
					}
				} else if (currentSection != null && !headers.isEmpty() && dataMap != null) {
					// Data Row
					for (int i = 0; i < headers.size(); i++) {
						Cell cell = row.getCell(i, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
						cell.setCellType(CellType.STRING);
						dataMap.put(headers.get(i), cell.getStringCellValue().trim());
					}
					// Store only one data row per section
					sectionDataMap.put(currentSection, new LinkedHashMap<>(dataMap));
					dataMap.clear();
				}
			}

		} catch (IOException e) {
			throw new RuntimeException("Error reading Excel file: " + e.getMessage(), e);
		}

		return sectionDataMap;
	}

	// ✅ ExcelUtils.java (add this method)
	public static Iterator<Object[]> getExcelData(String brandName, String... sectionNames) {
		// Read all section data from Excel
		Map<String, Map<String, String>> allSections = readTestData(brandName, "Sheet1");

		// Prepare a single test row containing requested sections
		Map<String, Map<String, String>> filteredSections = new LinkedHashMap<>();

		for (String sectionName : sectionNames) {
			if (allSections.containsKey(sectionName)) {
				filteredSections.put(sectionName, allSections.get(sectionName));
			} else {
				throw new RuntimeException("Section not found in Excel: " + sectionName);
			}
		}

		// Wrap it in Object[][] structure for TestNG DataProvider
		List<Object[]> data = new ArrayList<>();
		data.add(new Object[] { filteredSections });
		return data.iterator();
	}

}
