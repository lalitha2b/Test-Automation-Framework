package com.utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.ui.pojo.User;

public class ExcelReaderUtility {

    public static Iterator<User> readExcelFile(String fileName) throws InvalidFormatException {

        File xlsxFile = new File(System.getProperty("user.dir") + "//testData//" + fileName);
        XSSFWorkbook xssfWorkbook = null;
        List<User> userList = new ArrayList<>();

        try {
            xssfWorkbook = new XSSFWorkbook(xlsxFile);
            XSSFSheet sheet = xssfWorkbook.getSheet("LoginTestData");

            if (sheet == null) {
                System.err.println("Sheet 'LoginTestData' not found.");
                return userList.iterator();
            }

            Iterator<Row> rowIterator = sheet.iterator();

            // Skip header row if present
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }

            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();

                if (row == null) continue;

                Cell emailAddressCell = row.getCell(0);
                Cell passwordCell = row.getCell(1);

                if (emailAddressCell == null || passwordCell == null) {
                    System.out.println("Skipping row due to null cell(s)");
                    continue;
                }

                String email = emailAddressCell.toString().trim();
                String password = passwordCell.toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    System.out.println("Skipping row due to empty email or password");
                    continue;
                }

                User user = new User(email, password);
                userList.add(user);
            }

        } catch (IOException e) {
            System.err.println("Error reading Excel file: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (xssfWorkbook != null) {
                    xssfWorkbook.close();
                }
            } catch (IOException e) {
                System.err.println("Error closing workbook: " + e.getMessage());
            }
        }

        return userList.iterator();
    }
}

