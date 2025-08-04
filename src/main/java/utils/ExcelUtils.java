package utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class ExcelUtils {

    // Existing method: Read one row of test data
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

                if (valueCell != null) {
                    if (valueCell.getCellType() == CellType.STRING) {
                        value = valueCell.getStringCellValue();
                    } else if (valueCell.getCellType() == CellType.NUMERIC) {
                        value = String.valueOf((long) valueCell.getNumericCellValue());
                    }
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

    // ✅ New method: Read all rows (excluding header) from the sheet
    public static List<Map<String, String>> readAllData(String excelPath, String sheetName) {
        List<Map<String, String>> dataList = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(excelPath);
            Workbook workbook = new XSSFWorkbook(fis);
            Sheet sheet = workbook.getSheet(sheetName);
            Row headerRow = sheet.getRow(0);

            int totalRows = sheet.getLastRowNum();
            int totalCols = headerRow.getLastCellNum();

            for (int i = 1; i <= totalRows; i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;

                Map<String, String> rowData = new HashMap<>();

                for (int j = 0; j < totalCols; j++) {
                    Cell headerCell = headerRow.getCell(j);
                    Cell valueCell = currentRow.getCell(j);

                    String key = headerCell.getStringCellValue();
                    String value = "";

                    if (valueCell != null) {
                        if (valueCell.getCellType() == CellType.STRING) {
                            value = valueCell.getStringCellValue();
                        } else if (valueCell.getCellType() == CellType.NUMERIC) {
                            value = String.valueOf((long) valueCell.getNumericCellValue());
                        }
                    }

                    rowData.put(key, value);
                }

                dataList.add(rowData);
            }

            workbook.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }
    
    public static Iterator<Object[]> getExcelData(String filePath, String sheetName) {
        List<Map<String, String>> allData = ExcelUtils.readAllData(filePath, sheetName);
        return allData.stream().map(data -> new Object[]{data}).iterator();
    }
}
