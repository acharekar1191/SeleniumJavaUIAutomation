package com.qa.opencart.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelUtil {
	
	private static String filePath = "./src/test/resources/testdata/TestData_QKart.xlsx";
	private static FileInputStream ip;
	private static Workbook wb;
	private static Sheet sheet;
	private static Object data[][];
	
	public static Object[][] getData(String sheetName) {
		try {
			ip = new FileInputStream(filePath);
			wb = WorkbookFactory.create(ip);
			sheet = wb.getSheet(sheetName);
			int totalRows = sheet.getLastRowNum();
			int totalColumns = sheet.getRow(0).getLastCellNum();
			data = new Object[totalRows][totalColumns];
			
			for(int row=0;row<totalRows;row++) {
				for(int column=0;column<totalColumns;column++) {
					data[row][column] = sheet.getRow(row+1).getCell(column).toString();
				}
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (EncryptedDocumentException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return data;
	}
	
	private static String getTimeStamp() {
        return new SimpleDateFormat("yy_MM_dd_HH_mm_ss")
                .format(new Date());
    }
	
	public static void main(String[] args) {
		Object data[][] = getData("register");
		for (Object[] row : data) {
		    for (Object cell : row) {
		        System.out.print(cell + " ");
		    }
		    System.out.println();
		}
		System.out.println(getTimeStamp());
		
	}

}
