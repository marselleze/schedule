package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ksu.schedule.domain.Group;
import org.ksu.schedule.repository.GroupRepository;
import org.ksu.schedule.service.GroupImportService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupImportServiceImpl implements GroupImportService {

    private final GroupRepository groupRepository;


    @Override
    public void importExcelToGroups(List<MultipartFile> files) {
        if (!files.isEmpty()) {
            List<Group> transactions = new ArrayList<>();
            files.forEach(multipartfile -> {
                try {
                    XSSFWorkbook workBook = new XSSFWorkbook(multipartfile.getInputStream());

                    XSSFSheet sheet = workBook.getSheetAt(0);
                    // looping through each row
                    for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                        // current row
                        XSSFRow row = sheet.getRow(rowIndex);
                        // skip the first row because it is a header row
                        if (rowIndex == 0) {
                            continue;
                        }
                        int number = Integer.parseInt(getValue(row.getCell(0)).toString());
                        String direction = String.valueOf(getValue(row.getCell(1)).toString());
                        String profile = String.valueOf(getValue(row.getCell(2)).toString());

                        Group transaction =
                                Group.builder().number(number).direction(direction)
                                        .profile(profile).build();
                        transactions.add(transaction);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            if (!transactions.isEmpty()) {
                // save to database
                groupRepository.saveAll(transactions);
            }
        }
    }
    private Object getValue(Cell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BLANK:
                return null;
            case _NONE:
                return null;
            default:
                break;
        }
        return null;
    }
    public static int getNumberOfNonEmptyCells(XSSFSheet sheet, int columnIndex) {
        int numOfNonEmptyCells = 0;
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            XSSFRow row = sheet.getRow(i);
            if (row != null) {
                XSSFCell cell = row.getCell(columnIndex);
                if (cell != null && cell.getCellType() != CellType.BLANK) {
                    numOfNonEmptyCells++;
                }
            }
        }
        return numOfNonEmptyCells;
    }
}
