package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelUtils {

	public static Map<String, String> getTestData(String excelPath, String sheetName, int rowNumber) {
	    Map<String, String> data = new HashMap<>();
	    try {
	        FileInputStream fis = new FileInputStream(excelPath);
	        Workbook workbook = new XSSFWorkbook(fis);
	        Sheet sheet = workbook.getSheet(sheetName);
	        Row headerRow = sheet.getRow(0);
	        Row dataRow = sheet.getRow(rowNumber);

	        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
	            Cell headerCell = headerRow.getCell(i);
	            Cell valueCell = dataRow.getCell(i);

	            String key = headerCell.getStringCellValue();
	            String value = "";

	            if (valueCell.getCellType() == CellType.STRING) {
	                value = valueCell.getStringCellValue();
	            } else if (valueCell.getCellType() == CellType.NUMERIC) {
	                value = String.valueOf((long) valueCell.getNumericCellValue());
	            }

	            data.put(key, value);
	        }

	        workbook.close();
	        fis.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    return data;
	}

}
