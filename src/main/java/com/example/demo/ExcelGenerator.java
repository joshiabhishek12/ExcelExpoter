package com.example.demo;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ExcelGenerator {


    public static void generateExcel(List<Map<String, Object>> data, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Data");

        int rowNum = 0;
        int colNum = 0;

        // Create header row
        Row headerRow = sheet.createRow(rowNum++);
        for (String columnName : data.get(0).keySet()) {
            Cell cell = headerRow.createCell(colNum++);
            cell.setCellValue(columnName);
        }

        // Populate data rows
        for (Map<String, Object> rowMap : data) {
            Row row = sheet.createRow(rowNum++);
            colNum = 0;
            for (Object value : rowMap.values()) {
                Cell cell = row.createCell(colNum++);
                if (value instanceof String) {
                    cell.setCellValue((String) value);
                } else if (value instanceof Integer) {
                    cell.setCellValue((Integer) value);
                } else if (value instanceof Boolean) {
                    cell.setCellValue((Boolean) value);
                }
            }
        }

        FileOutputStream outputStream = new FileOutputStream(filePath);
        workbook.write(outputStream);
        workbook.close();
    }
}
	
	
