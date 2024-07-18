package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ksu.schedule.domain.*;
import org.ksu.schedule.repository.*;
import org.ksu.schedule.service.ExcelImportService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для импорта данных из Excel файлов.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

    private final GroupRepository groupRepository;
    private final SubgroupRepository subgroupRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ScheduleRepository scheduleRepository;

    public static boolean fullCell(XSSFCell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK)
            return true;
        else if (cell == null || cell.getCellType() == CellType.BLANK)
            return false;
        return false;
    }

    // Метод, возвращающий номер строки, с которой начинается таблица
    public static int skipTopRows(XSSFSheet sheet) {
        int rows = sheet.getLastRowNum();
        for (int r = 0; r < rows; r++) {
            XSSFRow row = sheet.getRow(r);
            if (row == null)
                continue;
            XSSFCell cell = row.getCell(0);
            if (!fullCell(cell))
                continue;
            if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().contains("Д")) {
                return r;
            }
        }
        return 0;
    }

    // Метод возвращающий номер последней строки в расписании
    public static int skipLowRows(XSSFSheet sheet) {
        int rows = sheet.getLastRowNum();
        for (int r = rows; r >= 0; r--) {
            XSSFRow row = sheet.getRow(r);
            if (row == null)
                continue;
            XSSFCell cell = row.getCell(0);
            if (!fullCell(cell))
                continue;
            if (cell.getCellType() == CellType.STRING && (cell.getStringCellValue().contains("СОГЛ") || cell.getStringCellValue().contains("Согл"))) {
                return r;
            }
        }
        return 0;
    }

    // Метод возвращающий количество столбцов
    public static int numCols(XSSFSheet sheet) {
        int cols = sheet.getRow(skipTopRows(sheet) + 1).getLastCellNum();
        XSSFRow row = sheet.getRow(skipTopRows(sheet) + 1);
        while (true) {
            XSSFCell cell = row.getCell(cols);
            if (!fullCell(cell))
                cols -= 1;
            else if (cell.getStringCellValue().contains("А")) {
                return cols + 1;
            }
        }
    }

    // Метод считающий кол-во подгрупп
    public static int countSubGroups(XSSFSheet sheet) {
        int countSubGroups = 0;
        XSSFRow row = sheet.getRow(skipTopRows(sheet) + 1);
        for (int c = 2; c < numCols(sheet); c += 2) {
            XSSFCell cell = row.getCell(c);
            if (cell.getStringCellValue().contains("руп"))
                countSubGroups++;
        }
        return countSubGroups;
    }

    // Метод, который проверяет нужно ли выводить строку, проверя ячейки с дисциплинами на пустоту
    public static boolean skipExtraRow(XSSFRow row, int cols) {
        int countNONEmpty = 0;
        for (int c = 2; c < cols - 1; c++) {
            XSSFCell cell = row.getCell(c);
            if (fullCell(cell))
                countNONEmpty += 1;
        }
        if (countNONEmpty == 0)
            return true;
        else
            return false;
    }

    // Метод проверяющий пара делится на ЧИСЛИТЕЛЬ и ЗНАМЕНАТЕЛЬ или она ВСЕГДА
    // true - ЧИСЛИТЕЛЬ и ЗНАМЕНАТЕЛЬ
    // false - ВСЕГДА
    public static boolean numDenOrCom(XSSFSheet sheet, int r) {
        XSSFRow row2 = sheet.getRow(r + 1);
        XSSFCell cell = row2.getCell(2);
        if (fullCell(cell))
            return true;
        else
            return false;
    }

    // Метод из готового парсера, возвращаящий значение взависимости от полученного типа данных
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

    @Override
    @Transactional
    public void importExcel(List<MultipartFile> files) {
        if (!files.isEmpty()) {
            List<Group> groupTransactions = new ArrayList<>();
            List<Subgroup> subgroupTransactions = new ArrayList<>();
            List<Teacher> teacherTransactions = new ArrayList<>();
            List<Subject> subjectTransactions = new ArrayList<>();
            List<Schedule> scheduleTransactions = new ArrayList<>();
            files.forEach(multipartfile -> {
                try {
                    XSSFWorkbook workbook = new XSSFWorkbook(multipartfile.getInputStream());

                    /////// Блок переменных для БД //////
                    String day = "";
                    String time;
                    String[] fullTime;
                    String startTime = "";
                    String endTime = "";
                    String nameDirection = "";
                    String nameProfile = "";
                    String subGroup = "";
                    String numGroup = "";
                    String nameDiscipline = "";
                    String disciplineView = "";
                    String teacherFCs = "";
                    String teacherPost = "";
                    String weekView = "";
                    String numAuditorium = "";

                    // Счётчик листов
                    for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                        // Описываем лист по индексу
                        XSSFSheet sheet = workbook.getSheetAt(numSheet);

                        int rows = skipLowRows(sheet);
                        int cols = numCols(sheet);
                        int controlWeekView = skipTopRows(sheet) + 2;
                        int numSubGroup = 1;
                        int indexProf = 0;

                        List<String> fullName = new ArrayList<>(List.of(sheet.getRow(skipTopRows(sheet)).getCell(2).getStringCellValue().split(" ")));
                        fullName.remove("");
                        for (int i = 0; i < fullName.size(); i++) {
                            fullName.set(i, fullName.get(i).trim());
                        }
                        for (int i = 0; i < fullName.size(); i++) {
                            if (fullName.get(i).contains("(")) {
                                indexProf = i - 1;
                                break;
                            }
                        }
                        List<String> direction = new ArrayList<>();
                        List<String> profile = new ArrayList<>();
                        for (int listElms = 0; listElms < indexProf; listElms++) {
                            direction.add(fullName.get(listElms));
                        }
                        for (int listElms = indexProf + 2; listElms < fullName.size(); listElms++) {
                            profile.add(fullName.get(listElms));
                        }
                        nameDirection = String.join(" ", direction);
                        nameProfile = String.join(" ", profile);

                        for (int c = 2; c < cols - 1; c += 2) {
                            if (!fullCell(sheet.getRow(skipTopRows(sheet) + 1).getCell(c)))
                                continue;
                            String[] fullGroup = sheet.getRow(skipTopRows(sheet) + 1).getCell(c).getStringCellValue().split(" ");
                            String groupAndSub = fullGroup[1];
                            fullGroup = groupAndSub.split("\\.");
                            if (!(numGroup.equals(fullGroup[0]))) {
                                numGroup = fullGroup[0];
                            }
                        }

                        Group transaction =
                                Group.builder()
                                        .number(numGroup)
                                        .direction(nameDirection)
                                        .profile(nameProfile)
                                        .build();
                        groupTransactions.add(transaction);

                        for (int r = skipTopRows(sheet) + 1; r < rows + 1; r++) {
                            if (r == controlWeekView + 2)
                                controlWeekView = r;
                            if (numSubGroup > countSubGroups(sheet))
                                break;
                            if (r == rows) {
                                r = skipTopRows(sheet) + 1;
                                controlWeekView = skipTopRows(sheet) + 2;
                                numSubGroup++;
                            }
                            boolean flag = false;
                            boolean genPara = false;
                            XSSFRow row = sheet.getRow(r);
                            if (row == null)
                                continue;
                            if (skipExtraRow(row, cols)) {
                                if (fullCell(row.getCell(0))) {
                                    day = String.valueOf(row.getCell(0));
                                }
                                if (fullCell(row.getCell(1))) {
                                    switch (row.getCell(1).getStringCellValue()) {
                                        case "8.00 - 9.30":
                                            time = "08:00 09:30";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "9.40 - 11.10":
                                            time = "09:40 11:10";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "11.20 - 12.50":
                                            time = "11:20 12:50";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "13.10 - 14.40":
                                            time = "13:10 14:40";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "14.50 - 16.20":
                                            time = "14:50 16:20";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "16.30 – 18.00":
                                            time = "16:30 18:00";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "18.10 – 19.40":
                                            time = "18:10 19:40";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                    }
                                }
                                continue;
                            }

                            for (int c = 0; c < cols; c++) {
                                if (c == 2 && c != numSubGroup * 2)
                                    c = numSubGroup * 2;
                                if (c > numSubGroup * 2)
                                    break;

                                XSSFCell cell = row.getCell(c);

                                if (!fullCell(row.getCell(3)) && !fullCell(row.getCell(4)))
                                    genPara = true;
                                if (genPara && c == numSubGroup * 2)
                                    cell = row.getCell(2);

                                if (!fullCell(cell))
                                    continue;

                                if (r == skipTopRows(sheet) + 1 && cell.getStringCellValue().contains("А")) {
                                    if (!flag) {
                                        flag = true;
                                    }
                                    continue;
                                }

                                if (c == 1) {
                                    switch (cell.getStringCellValue()) {
                                        case "8.00 - 9.30":
                                            time = "08:00 09:30";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "9.40 - 11.10":
                                            time = "09:40 11:10";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "11.20 - 12.50":
                                            time = "11:20 12:50";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "13.10 - 14.40":
                                            time = "13:10 14:40";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "14.50 - 16.20":
                                            time = "14:50 16:20";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "16.30 – 18.00":
                                            time = "16:30 18:00";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                        case "18.10 – 19.40":
                                            time = "18:10 19:40";
                                            fullTime = time.split(" ");
                                            startTime = fullTime[0];
                                            endTime = fullTime[1];
                                            break;
                                    }
                                    continue;
                                }

                                if (r == skipTopRows(sheet) + 1) {
                                    String[] fullNumGroup = cell.getStringCellValue().split(" ");
                                    subGroup = fullNumGroup[1];

                                    if (!groupTransactions.isEmpty()) {
                                        groupRepository.saveAll(groupTransactions);
                                    }

                                    Subgroup subgroupTransaction = Subgroup.builder()
                                            .number(subGroup)
                                            .group(groupRepository.findByNumber(numGroup))
                                            .build();

                                    boolean SubgroupExists = false;
                                    for (Subgroup subgroup : subgroupTransactions) {
                                        if (subgroup.getNumber().equals(subgroupTransaction.getNumber()) && subgroup.getGroup().equals(subgroupTransaction.getGroup())) {
                                            SubgroupExists = true;
                                            break;
                                        }
                                    }

                                    if (!SubgroupExists) {
                                        subgroupTransactions.add(subgroupTransaction);
                                    } else {
                                        continue;
                                    }
                                    subgroupRepository.saveAllAndFlush(subgroupTransactions);

                                    continue;
                                }

                                if (r > skipTopRows(sheet) + 1 && (c > 1 && c % 2 == 0 && c != cols - 1) && cell.getCellType() == CellType.STRING) {
                                    int indexView = 0;
                                    int indexPost = 0;
                                    List<String> fullNameDiscip = new ArrayList<>(List.of(cell.getStringCellValue().split(" ")));
                                    fullNameDiscip.remove("");
                                    for (int i = 0; i < fullNameDiscip.size(); i++) {
                                        fullNameDiscip.set(i, fullNameDiscip.get(i).trim());
                                    }
                                    for (int i = 0; i < fullNameDiscip.size(); i++) {
                                        if (fullNameDiscip.get(i).contains("(")) {
                                            indexView = i;
                                            indexPost = i + 1;
                                            break;
                                        }
                                    }
                                    if (fullNameDiscip.get(indexPost + 1).contains(".")) {
                                        List<String> post = new ArrayList<>(List.of(fullNameDiscip.get(indexPost).split("\\.")));
                                        String fam = post.get(post.size() - 1);
                                        post.remove(post.size() - 1);
                                        fullNameDiscip.remove(indexPost);
                                        fullNameDiscip.add(indexPost, String.join(".", post));
                                        fullNameDiscip.add(indexPost + 1, fam);
                                    }

                                    List<String> discipline = new ArrayList<>();
                                    List<String> FCs = new ArrayList<>();
                                    for (int discElms = 0; discElms < indexView; discElms++) {
                                        discipline.add(fullNameDiscip.get(discElms));
                                    }
                                    for (int FCsElms = indexPost + 1; FCsElms < fullNameDiscip.size(); FCs.add(fullNameDiscip.get(FCsElms++))) {
                                    }

                                    nameDiscipline = String.join(" ", discipline);
                                    disciplineView = fullNameDiscip.get(indexView);
                                    teacherFCs = String.join(" ", FCs);
                                    teacherPost = fullNameDiscip.get(indexPost);
                                    if (numDenOrCom(sheet, controlWeekView)) {
                                        if (r == controlWeekView) {
                                            weekView = "ЧИСЛИТЕЛЬ";
                                        } else if (r == controlWeekView + 1) {
                                            weekView = "ЗНАМЕНАТЕЛЬ";
                                        }
                                    } else {
                                        weekView = "ВСЕГДА";
                                    }
                                }
                                if (c == 0) {
                                    day = String.valueOf(cell);
                                    continue;
                                }
                                if (genPara) {
                                    numAuditorium = String.valueOf(getValue(row.getCell(numCols(sheet) - 1)));
                                } else
                                    numAuditorium = String.valueOf(getValue(row.getCell(c + 1)));

                                Subject subjTransaction = Subject.builder()
                                        .name(nameDiscipline)
                                        .type(disciplineView)
                                        .build();
                                List<Subject> subjects = subjectRepository.findAll();
                                boolean SubjectExists = false;
                                for (Subject subject : subjects) {
                                    if (subject.getName().equals(subjTransaction.getName()) && subject.getType().equals(subjTransaction.getType())) {
                                        SubjectExists = true;
                                        break;
                                    }
                                }

                                if (!SubjectExists) {
                                    subjectTransactions.add(subjTransaction);
                                } else {
                                    continue;
                                }
                                subjectTransactions.add(subjTransaction);
                                subjectRepository.saveAll(subjectTransactions);

                                Teacher teachersTransaction = Teacher.builder()
                                        .name(teacherFCs)
                                        .post(teacherPost)
                                        .build();
                                boolean teacherExists = teacherRepository.existsByName(teachersTransaction.getName());

                                if (!teacherExists) {
                                    teacherTransactions.add(teachersTransaction);
                                } else {
                                    continue;
                                }
                                List<Teacher> uniqueTeachers = teacherTransactions.stream()
                                        .distinct()
                                        .collect(Collectors.toList());
                                teacherRepository.saveAll(uniqueTeachers);

                            }
                            System.out.println();
                        }
                    }
                } catch (IOException e) {
                    throw new RuntimeException("Ошибка при импорте файла Excel", e);
                }
            });
            if (!scheduleTransactions.isEmpty()) {
                scheduleRepository.saveAll(scheduleTransactions);
            }
        }
    }
}
