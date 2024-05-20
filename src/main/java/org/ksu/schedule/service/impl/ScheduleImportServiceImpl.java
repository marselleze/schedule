package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ksu.schedule.domain.*;
import org.ksu.schedule.repository.*;
import org.ksu.schedule.service.ScheduleImportService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleImportServiceImpl implements ScheduleImportService {

    private final GroupRepository groupRepository;
    private final SubgroupRepository subgroupRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ScheduleRepository scheduleRepository;


//    @Override
//    public void importExcelToGroups(List<MultipartFile> files) {
//        if (!files.isEmpty()) {
//            List<Group> transactions = new ArrayList<>();
//            files.forEach(multipartfile -> {
//                try {
//                    XSSFWorkbook workBook = new XSSFWorkbook(multipartfile.getInputStream());
//
//                    XSSFSheet sheet = workBook.getSheetAt(0);
//                    // looping through each row
//                    for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
//                        // current row
//                        XSSFRow row = sheet.getRow(rowIndex);
//                        // skip the first row because it is a header row
//                        if (rowIndex == 0) {
//                            continue;
//                        }
//                        int number = Integer.parseInt(getValue(row.getCell(0)).toString());
//                        String direction = String.valueOf(getValue(row.getCell(1)).toString());
//                        String profile = String.valueOf(getValue(row.getCell(2)).toString());
//
//                        Group transaction =
//                                Group.builder().number(number).direction(direction)
//                                        .profile(profile).build();
//                        transactions.add(transaction);
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//
//            if (!transactions.isEmpty()) {
//                // save to database
//                groupRepository.saveAll(transactions);
//            }
//        }
//    }


    private static Object getValue(XSSFCell cell) {
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case ERROR:
                return cell.getErrorCellValue();
            case FORMULA:
                return cell.getCellFormula();
            case BLANK, _NONE:
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

    public static boolean fullCell(XSSFCell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK)
            return true;
        else if (cell == null || cell.getCellType() == CellType.BLANK)
            return false;
        return false;
    }

    public static int skipTopRows(XSSFSheet sheet) {
        // Создаём переменную хронящую номер последний строки(кол-во строк)
        int rows = sheet.getLastRowNum();

        //r-строки, с-столбцы
        for (int r = 0; r < rows; r++) {
            //Получаем строку номера r
            XSSFRow row = sheet.getRow(r);
            //Если строка пустая, пропускаем
            if (row == null)
                continue;
            //Получаем ячейку номера c
            XSSFCell cell = row.getCell(0);
            //Пропускаем если ячейка пустая или внутри неё пробел
            if (!fullCell(cell))
                continue;
            //С помощью флага пропускаем все строки до строки с "День недели"
            if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().contains("Д")) {
                return r;
            }
        }
        return 0;
    }

    public static int skipLowRows(XSSFSheet sheet) {
        // Создаём переменную хронящую номер последний строки(кол-во строк)
        int rows = sheet.getLastRowNum();

        //r-строки
        for (int r = rows; r >= 0; r--) {
            //Получаем строку номера r
            XSSFRow row = sheet.getRow(r);
            //Если строка пустая, пропускаем
            if (row == null)
                continue;
            //Получаем ячейку номера 1
            XSSFCell cell = row.getCell(0);
            //Пропускаем если ячейка пустая или внутри неё пробел
            if (!fullCell(cell))
                continue;
            //Если доходим до строки с "СОГЛАСОВАНО" возвращаем номер этой строки
            if (cell.getCellType() == CellType.STRING && (cell.getStringCellValue().contains("СОГЛ") || cell.getStringCellValue().contains("Согл"))) {
                return r;
            }
        }
        return 0;
    }

    //Метод возвращающий количество столбцов
    public static int numCols(XSSFSheet sheet) {
        // Cоздаём переменную хронящую кол-во ячеек в заданной строке - кол-во столбцов
        int cols = sheet.getRow(skipTopRows(sheet) + 1).getLastCellNum();
        //Получаем строку
        XSSFRow row = sheet.getRow(skipTopRows(sheet) + 1);

        while (true) {
            //Получаем ячейку номера cols
            XSSFCell cell = row.getCell(cols);
            //Пропускаем если ячейка пустая или внутри неё пробел
            if (!fullCell(cell))
                cols -= 1;
            else if (cell.getStringCellValue().contains("А")) {
                return cols + 1;
            }
        }
    }

    //Метод, который проверяет нужно ли выводить строку, проверя ячейки с дисциплинами на пустоту
    public static boolean skipExtraRow(XSSFRow row, int cols) {
        //Счётчик не пустых ячеек
        int countNONEmpty = 0;

        //c - индекс ячейки
        for (int c = 2; c < cols - 1; c++) {
            //Получаем ячейку индекса с
            XSSFCell cell = row.getCell(c);
            //Пропускаем если ячейка пустая или внутри неё пробел
            if (fullCell(cell))
                countNONEmpty += 1;
        }

        //Если все ячейки пустые пропускаем, иначе - нет
        if (countNONEmpty == 0)
            return true;
        else
            return false;
    }

    //Метод проверяющий пара делится на ЧИСЛИТЕЛЬ и ЗНАМЕНАТЕЛЬ или она ВСЕГДА
    //true - ЧИСЛИТЕЛЬ и ЗНАМЕНАТЕЛЬ
    //false - ВСЕГДА
    public static boolean numDenOrCom(XSSFSheet sheet, int r) {
        XSSFRow row2 = sheet.getRow(r + 1);
        XSSFCell cell = row2.getCell(2);
        if (fullCell(cell))
            return true;
        else
            return false;
    }


    @Override
    public void importExcel(List<MultipartFile> files) {
        if (!files.isEmpty()) {
            List<Group> groupTransactions = new ArrayList<>();
            List<Subgroup> subgroupTransactions = new ArrayList<>();
            List<Teacher> teacherTransactions = new ArrayList<>();
            List<Subject> subjectTransactions = new ArrayList<>();
            List<Schedule> scheduleTransactions = new ArrayList<>();
            List<Long> subgrIds = new ArrayList<>();
            files.forEach(multipartfile -> {
                try {
                    // Описываем рабочую книгу
                    XSSFWorkbook workbook = new XSSFWorkbook(multipartfile.getInputStream());

                    /////// Блок переменных для БД //////
                    String day = "";
                    String time;
                    String[] fullTime;
                    String startTime = "";
                    String endTime = "";
                    String nameDirection = "";
                    String nameProfile = "";
                    String numGroup = "";
                    String subGroup = "";
                    String nameDiscipline = "";
                    String disciplineView = "";
                    String teacherFCs = "";
                    String teacherPost = "";
                    String weekView = "";
                    String numAuditorium = "";

                    //Счётчик листов
                    for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
//                        //Выводим номер листа для тестов программы
//                        System.out.println(numSheet + 1);
                        // Описываем лист по индексу
                        XSSFSheet sheet = workbook.getSheetAt(numSheet);

                        ////// Чтение используя for //////

                        // Создаём переменную хронящую номер последний строки(кол-во строк)
                        int rows = skipLowRows(sheet);
                        // Cоздаём переменную хронящую кол-во ячеек в заданной строке - кол-во столбцов
                        int cols = numCols(sheet);
                        //Переменная для контроля при делении пар на числитель и знаменатель
                        int controlWeekView = skipTopRows(sheet) + 2;
                        //Создаём переменную, которая будет хранить индекс первой ячейки с аудиториией
                        int indexFirstAuditCell = 0;
                        //Разбиваем направление
                        int indexProf = 0;
                        //Разбиваем строку на список
                        List<String> fullName = new ArrayList<>(List.of(sheet.getRow(skipTopRows(sheet)).getCell(2).getStringCellValue().split(" ")));
                        //Удаляем лишние пробелы
                        fullName.remove("");
                        //Удаляем лишние переносы строк
                        for (int i = 0; i < fullName.size(); i++) {
                            fullName.set(i, fullName.get(i).trim());
                        }

                        //i - индекс подстроки в списке
                        for (int i = 0; i < fullName.size(); i++) {
                            //Запоминаем индекс вида пары и должности препода
                            if (fullName.get(i).contains("(")) {
                                indexProf = i - 1;
                                break;
                            }
                        }

                        //Создаём 2 списка куда будем помещать направление и профиль
                        List<String> direction = new ArrayList<>();
                        List<String> profile = new ArrayList<>();

                        //Заполняем списки нужными данными
                        for (int listElms = 0; listElms < indexProf; listElms++) {
                            direction.add(fullName.get(listElms));
                        }
                        for (int listElms = indexProf + 2; listElms < fullName.size(); listElms++) {
                            profile.add(fullName.get(listElms));
                        }

                        //Создаём переменные для удобства переноса данных в БД

                        //Вывод направления
                        nameDirection = String.join(" ", direction);
                        //Следующую строку меняешь на добавление в БД
                        //System.out.println(nameDirection);
                        //Вывод профиля
                        nameProfile = String.join(" ", profile);
                        //Следующую строку меняешь на добавление в БД
                        //System.out.println(nameProfile);
                        //Направление
                        //System.out.println(nameDirection);
                        //Профиль
                        //System.out.println(nameProfile);

                        /////Получаем номер группы////
                        for (int c = 2; c < cols - 1; c += 2) {

                            if (!fullCell(sheet.getRow(skipTopRows(sheet) + 1).getCell(c)))
                                continue;

                            String[] fullGroup = sheet.getRow(skipTopRows(sheet) + 1).getCell(c).getStringCellValue().split(" ");
                            String groupAndSub = fullGroup[1];
                            fullGroup = groupAndSub.split("\\.");
                            if (!(numGroup.equals(fullGroup[0]))) {
                                numGroup = fullGroup[0];
                                //Номер группы
                                //System.out.println(numGroup);
                            }
                        }

                        //Создаем транзакции групп
                        Group transaction =
                                Group.builder()
                                        .number(numGroup)
                                        .direction(nameDirection)
                                        .profile(nameProfile)
                                        .build();
                        groupTransactions.add(transaction);


                        //r-строки, с-столбцы
                        for (int r = skipTopRows(sheet) + 1; r < rows; r++) {


                            //Создаём флаг, который помогает запомнить индекс ячейки для определения аудитории
                            boolean flag = false;
                            //Создаём вспомогательную переменную для удобного вывода номера аудиторий
                            int auditCell = indexFirstAuditCell;
                            //Получаем строку номера r
                            XSSFRow row = sheet.getRow(r);
                            //Если строка пустая, пропускаем
                            if (row == null)
                                continue;

                            //Пропускаяем пустые строки, выводим только день недели и время
                            if (skipExtraRow(row, cols)) {
                                if (fullCell(row.getCell(0))) {
                                    //Переменная хранящая день недели
                                    day = String.valueOf(row.getCell(0));
                                    //Следующую строку меняешь на добавление в БД
                                    //scheduleTransaction.setDay(day);
                                    //System.out.println(day);
                                }
                                if (fullCell(row.getCell(1))) {

                                    switch (row.getCell(1).getStringCellValue()) {
                                        case "8.00 - 9.30":
                                            time = "8:00:00-9:30:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "9.40 - 11.10":
                                            time = "9:40:00-11:10:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "11.20 - 12.50":
                                            time = "11:20:00-12:50:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "13.10 - 14.40":
                                            time = "13:10:00-14:40:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "14.50 - 16.20":
                                            time = "14:50:00-16:20:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "16.30 – 18.00":
                                            time = "16:30:00-18:00:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "18.10 – 19.40":
                                            time = "18:10:00-19:40:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                    }
                                }
                                continue;
                            }

                            Subject subjectTransaction = null;
                            Teacher teacherTransaction;
                            Subject subjTransaction = null;
                            Teacher teachersTransaction = null;
                            for (int c = 0; c < cols; c++) {

                                //Если первая или вторая строка таблицы, не выводим первые 2 ячейки
                                if (r == skipTopRows(sheet)) {
                                    c += 2;
                                }

                                //Получаем ячейку номера c
                                XSSFCell cell = row.getCell(c);

                                //Пропускаем если ячейка пустая или внутри неё пробел
                                if (!fullCell(cell))
                                    continue;

                                //Не выводим "Ауд."
                                if (r == skipTopRows(sheet) + 1 && cell.getStringCellValue().contains("А")) {
                                    if (!flag) {
                                        indexFirstAuditCell = c;
                                        flag = true;
                                    }
                                    continue;
                                }

                                //Разбиваем время
                                if (c == 1) {
                                    switch (cell.getStringCellValue()) {
                                        case "8.00 - 9.30":
                                            time = "8:00:00-9:30:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "9.40 - 11.10":
                                            time = "9:40:00-11:10:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "11.20 - 12.50":
                                            time = "11:20:00-12:50:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "13.10 - 14.40":
                                            time = "13:10:00-14:40:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "14.50 - 16.20":
                                            time = "14:50:00-16:20:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "16.30 – 18.00":
                                            time = "16:30:00-18:00:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                        case "18.10 – 19.40":
                                            time = "18:10:00-19:40:00";
                                            fullTime = time.split("-");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setStartTime(startTime);
                                            //System.out.println(startTime);
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setEndTime(endTime);
                                            //System.out.println(endTime);
                                            break;
                                    }
                                    continue;
                                }

                    /*//Разбиваем направление
                    if (r == skipTopRows(sheet)) {

                        int indexProf = 0;

                        //Разбиваем строку на список
                        List<String> fullName = new ArrayList<>(List.of(cell.getStringCellValue().split(" ")));
                        //Удаляем лишние пробелы
                        fullName.remove("");
                        //Удаляем лишние переносы строк
                        for (int i = 0; i < fullName.size(); i++) {
                            fullName.set(i, fullName.get(i).trim());
                        }

                        //i - индекс подстроки в списке
                        for (int i = 0; i < fullName.size(); i++) {
                            //Запоминаем индекс вида пары и должности препода
                            if (fullName.get(i).contains("(")) {
                                indexProf = i - 1;
                                break;
                            }
                        }

                        //Создаём 2 списка куда будем помещать направление и профиль
                        List<String> direction = new ArrayList<>();
                        List<String> profile = new ArrayList<>();

                        //Заполняем списки нужными данными
                        for (int listElms = 0; listElms < indexProf; listElms++) {
                            direction.add(fullName.get(listElms));
                        }
                        for (int listElms = indexProf + 2; listElms < fullName.size(); listElms++) {
                            profile.add(fullName.get(listElms));
                        }

                        //Создаём переменные для удобства переноса данных в БД

                        //Вывод направления
                        nameDirection = String.join(" ", direction);
                        //Следующую строку меняешь на добавление в БД
                        //System.out.println(nameDirection);
                        //Вывод профиля
                        nameProfile = String.join(" ", profile);
                        //Следующую строку меняешь на добавление в БД
                        //System.out.println(nameProfile);
                        continue;
                    }*/

                                //Разбиваем группы, получаем номер группы и подгрупп

                                if (r == skipTopRows(sheet) + 1) {
                                    //Создаём массив из слов
                                    String[] fullNumGroup = cell.getStringCellValue().split(" ");
                                    //Получем номер подгруппы
                                    subGroup = fullNumGroup[1];
                        /*//Разбиваем номер подгруппы
                        fullNumGroup = subGroup.split("\\.");
                        //Получаем номер группы
                        numGroup = fullNumGroup[0];
                        if(!blockerGroup) {
                            //Следующую строку меняешь на добавление в БД
                            System.out.println(numGroup);
                            blockerGroup = true;
                        }*/
                                    //Создаем тразакцию для подгрупп
                                    //Subgroup subgroupTransaction = Subgroup.builder()
                                    //.number(subGroup)
                                    //.group(groupRepository.findByNumber(numGroup))
                                    //.build();
                                    //subgroupTransactions.add(subgroupTransaction);
                                    //System.out.println(subgroupRepository.findByNumber(subGroup));

                                    //scheduleTransaction.setSubgroup(subgroupRepository.findByNumber(subGroup));

                                    System.out.println(subGroup);

                                    continue;
                                }

                                System.out.println(subGroup);





                                //Разбиваем дисциплину
                                if (r > skipTopRows(sheet) + 1 && (c > 1 && c % 2 == 0 && c != cols - 1) && cell.getCellType() == CellType.STRING) {
                                    int indexView = 0;
                                    int indexPost = 0;
                                    //Разбиваем строку на список
                                    List<String> fullNameDiscip = new ArrayList<>(List.of(cell.getStringCellValue().split(" ")));
                                    //Удаляем лишние пробелы
                                    fullNameDiscip.remove("");
                                    //Удаляем лишние переносы строк
                                    for (int i = 0; i < fullNameDiscip.size(); i++) {
                                        fullNameDiscip.set(i, fullNameDiscip.get(i).trim());
                                    }
                                    //i - индекс подстроки в списке
                                    for (int i = 0; i < fullNameDiscip.size(); i++) {
                                        //Запоминаем индекс вида пары и должности препода
                                        if (fullNameDiscip.get(i).contains("(")) {
                                            indexView = i;
                                            indexPost = i + 1;
                                            break;
                                        }
                                    }
                                    //Создаём 2 списка куда будем помещать название и ФИО
                                    List<String> discipline = new ArrayList<>();
                                    List<String> FCs = new ArrayList<>();
                                    //Заполняем списки нужными данными
                                    for (int discElms = 0; discElms < indexView; discElms++) {
                                        discipline.add(fullNameDiscip.get(discElms));
                                    }
                                    for (int FCsElms = indexPost + 1; FCsElms < fullNameDiscip.size(); FCsElms++) {
                                        FCs.add(fullNameDiscip.get(FCsElms));
                                    }

                                    //Создаём переменные для удобства переноса данных в БД

                                    //Вывод дисциплины
                                    nameDiscipline = String.join(" ", discipline);
                                    //Следующую строку меняешь на добавление в БД
                                    //System.out.println(nameDiscipline);
                                    //Вывод вида пары
                                    disciplineView = fullNameDiscip.get(indexView);
                                    //Следующую строку меняешь на добавление в БД
                                    //System.out.println(disciplineView);
                                    //scheduleTransaction.setSubject(subjectRepository.findByNameAndType(nameDiscipline, disciplineView));
                                    //Вывод ФИО преподователя
                                    teacherFCs = String.join(" ", FCs);
                                    //Следующую строку меняешь на добавление в БД
                                    //System.out.println(teacherFCs);
                                    //Вывод должности преподователя
                                    teacherPost = fullNameDiscip.get(indexPost);
                                    //Следующую строку меняешь на добавление в БД
                                    //System.out.println(teacherPost);
                                    //scheduleTransaction.setTeacher(teacherRepository.findByNameAndPost(teacherFCs, teacherPost));


                                    //Вывод разделения по виду недели
                                    if (r == controlWeekView + 2)
                                        controlWeekView = r;
                                    if (numDenOrCom(sheet, controlWeekView)) {
                                        if (r == controlWeekView) {
                                            weekView = "ЧИСЛИТЕЛЬ";
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setWeekView(weekView);
                                            //System.out.println(weekView);
                                        } else if (r == controlWeekView + 1) {
                                            weekView = "ЗНАМЕНАТЕЛЬ";
                                            //Следующую строку меняешь на добавление в БД
                                            //scheduleTransaction.setWeekView(weekView);
                                            //System.out.println(weekView);
                                        }
                                    } else {
                                        weekView = "ВСЕГДА";
                                        //Следующую строку меняешь на добавление в БД

                                        //System.out.println(weekView);
                                    }
                                    //scheduleTransaction.setWeekView(weekView);
                                    continue;
                                }
                                //Вывод дня недели
                                if (c == 0) {
                                    day = String.valueOf(cell);
                                    //Следующую строку меняешь на добавление в БД

                                    //System.out.println(day);
                                    continue;
                                }
                                //scheduleTransaction.setDay(day);
                                //Вывод аудитории
                                if (c == auditCell) {
                                    numAuditorium = String.valueOf(getValue(cell));
                                    //Следующую строку меняешь на добавление в БД
                                    //scheduleTransaction.setClassroom(numAuditorium);
                                    //System.out.println(numAuditorium);
                                    //scheduleTransaction.setClassroom(numAuditorium);
                                    auditCell += 2;
                                }


                                //Schedule schedule = scheduleTransaction
                                //.build();
                                //scheduleTransactions.add(schedule);




                    /*//Направление
                    System.out.println(nameDirection);
                    //Профиль
                    System.out.println(nameProfile);
                    //Номер группы
                    if(!blockerGroup) {
                        //Следующую строку меняешь на добавление в БД
                        System.out.println(numGroup);
                        blockerGroup = true;
                    }*/

                                //Дисциплина
                                //System.out.println(nameDiscipline);
                                //Вид пары
                                //System.out.println(disciplineView);
                                //Создаем тразакцию для дисциплин
                                //subjTransaction = Subject.builder()
                                //.name(nameDiscipline)
                                //.type(disciplineView)
                                //.build();

                                //subjectTransactions.add(subjTransaction);


                                //ФИО препода
                                //System.out.println(teacherFCs);
                                //Должность
                                //System.out.println(teacherPost);

                                //Создаём транзакцию для преподавтеля
                                //teachersTransaction =
                                //Teacher.builder()
                                //.name(teacherFCs)
                                //.post(teacherPost)
                                //.build();
                                //teacherTransactions.add(teachersTransaction);

                                //Вид недели
                                //System.out.println(weekView);
                                //День недели
                                //System.out.println(day);
                                //Время начала
                                //System.out.println(startTime);
                                //Время конца
                                //System.out.println(endTime);
                                //Аудитория
                                //System.out.println(numAuditorium);
                                //Создаем тразакцию для расписания


                                //System.out.println(groupRepository.findByNumber(numGroup));

                                //System.out.println(subjectTransactions);
                                //System.out.println(teacherRepository.findByNameAndPost(teacherFCs, teacherPost));
                                System.out.println(subGroup);

                                Schedule scheduleTransaction=
                                        Schedule.builder()
                                                .parity(weekView)
                                                .subgroup(subgroupRepository.findByNumber(subGroup))
                                                .subject(subjectRepository.findByNameAndType(nameDiscipline, disciplineView))
                                                .teacher(teacherRepository.findByNameAndPost(teacherFCs, teacherPost))
                                                .dayWeek(day)
                                                .timeStart(startTime)
                                                .timeEnd(endTime)
                                                .classroom(numAuditorium)
                                                .build();

                                scheduleTransactions.add(scheduleTransaction);


                                //Выводим информацию из ячейки, в зависимости от типа
                                //System.out.println(getValue(cell));
                            }

                            //Пропуск строки для удобства чтения
                            //System.out.println();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });


            if (!scheduleTransactions.isEmpty()) {

                // save to database
                scheduleRepository.saveAll(scheduleTransactions);
            }




        }
    }
}
