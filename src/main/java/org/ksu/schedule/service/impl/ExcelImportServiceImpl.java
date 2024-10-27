package org.ksu.schedule.service.impl;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ksu.schedule.config.AuthenticationService;
import org.ksu.schedule.domain.*;
import org.ksu.schedule.repository.*;
import org.ksu.schedule.service.ExcelImportService;
import org.ksu.schedule.service.ScheduleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для импорта данных из Excel файлов.
 *
 * @version 1.0
 * @author Александр Миронов
 */
@Service
@RequiredArgsConstructor
public class ExcelImportServiceImpl implements ExcelImportService {

    private final GroupRepository groupRepository;
    private final SubgroupRepository subgroupRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final ScheduleRepository scheduleRepository;
    private final FacultyRepository facultyRepository;

    private final ScheduleServiceImpl scheduleServiceImpl;

    private static final Logger logger = LoggerFactory.getLogger(ExcelImportServiceImpl.class);

    //Переменная количества строк добавленных к skipTopRows для коректной работы программы
    //Значение задаётся в numCols
    static int numRowsAdd = 0;

    //Метод проверяющий пустая ячейка или нет
    //true - не пустая
    //false - пустая
    public static boolean fullCell(XSSFCell cell) {
        if (cell != null && cell.getCellType() != CellType.BLANK)
            return true;
        else if (cell == null || cell.getCellType() == CellType.BLANK)
            return false;
        return false;
    }

    //Метод, возвращающий номер строки, с которой начинается таблица
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
            if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().contains("недел")) {
                return r;
            }
        }
        return 0;
    }

    //Метод возвращающий номер последней строки в расписании
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
            if (cell.getCellType() == CellType.STRING && (cell.getStringCellValue().contains("Дек") || cell.getStringCellValue().contains("огл") || cell.getStringCellValue().contains("ОГЛ"))) {
                return r;
            }
        }
        return 0;
    }

    //Метод возвращающий количество столбцов
    //В нём считается корректное значение numRowsAdd
    public static int numCols(XSSFSheet sheet) {
        // Cоздаём переменную хронящую кол-во ячеек в заданной строке - кол-во столбцов
        int cols = sheet.getRow(skipTopRows(sheet)).getLastCellNum();
        // Переменная для цикла правильного определения столбцов
        int extraCols;
        ///////////////   Цикл   //////////////////
        for (int i = 0; ; i++) {
            //Получаем строку
            XSSFRow row = sheet.getRow(skipTopRows(sheet) + i);

            extraCols = cols;

            while (true) {
                //Получаем ячейку номера cols
                XSSFCell cell = row.getCell(extraCols);

                if (extraCols == 0) {
                    break;
                }
                //Пропускаем если ячейка пустая или внутри неё пробел
                if (!fullCell(cell) || !(cell.getStringCellValue().contains("Ауд") || (cell.getStringCellValue().contains("АУД"))))
                    extraCols -= 1;
                else if (cell.getStringCellValue().contains("Ауд") || cell.getStringCellValue().contains("АУД")) {
                    numRowsAdd = i;
                    return extraCols + 1;
                }

            }

        }
    }

    //Метод считающий кол-во подгрупп
    public static int countSubGroups(XSSFSheet sheet) {
        int countSubGroups = 0;
        XSSFRow row = sheet.getRow(skipTopRows(sheet) + numRowsAdd);
        for (int c = 2; c < numCols(sheet); c += 2) {
            //Получаем ячейку номера c
            XSSFCell cell = row.getCell(c);
            if (cell.getStringCellValue().contains("руп"))
                countSubGroups++;
        }
        return countSubGroups;
    }

    //Метод проверяющий пара делится на ЧИСЛИТЕЛЬ и ЗНАМЕНАТЕЛЬ или она ВСЕГДА
    //true - ЧИСЛИТЕЛЬ и ЗНАМЕНАТЕЛЬ
    //false - ВСЕГДА
    public static boolean numDenOrCom(XSSFSheet sheet, int controlWeekView, int countSubGroups) {
        XSSFRow row2 = sheet.getRow(controlWeekView + 1);
        int count = 0;
        for (int i = 1; i <= countSubGroups; i++) {
            XSSFCell cell = row2.getCell(i * 2);
            if (fullCell(cell))
                count++;
        }
        if (count > 0)
            return true;
        else
            return false;
    }

    //Метод из готового парсера, возвращаящий значение взависимости от полученного типа данных
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
            List<Faculty> facultyTransactions = new ArrayList<>();

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
                    boolean GroupExists = false;
                    String facultyFull = "";
                    String facultyReduc = "";

                    //Счётчик листов
                    for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
                        //Выводим номер листа для тестов программы
                        System.out.println(numSheet + 1);
                        // Описываем лист по индексу
                        XSSFSheet sheet = workbook.getSheetAt(numSheet);
                        //вывод кол-во подгрупп
                        //System.out.println(countSubGroups(sheet));
                        ////// Чтение используя for //////\

                        // Создаём переменную хронящую номер последний строки(кол-во строк)
                        int rows = skipLowRows(sheet);
                        // Cоздаём переменную хронящую кол-во ячеек в заданной строке - кол-во столбцов
                        int cols = numCols(sheet);
                        //Переменная для контроля при делении пар на числитель и знаменатель
                        int controlWeekView = skipTopRows(sheet) + numRowsAdd + 1;
                        //Создаём переменную, которая будет хранить индекс первой ячейки с аудиториией
                        //int indexFirstAuditCell = 0;
                        //Создаём переменную, номера подгруппы, для проходки по столбцам
                        int numSubGroup = 1;
                        //переменная numSubGroup для определения номера группы
                        int numSubGroupForNumGroup = 2;

                        //Получаем факультет
                        for (int r = 0; r < rows; r++) {
                            if (!(facultyReduc.isEmpty() && facultyFull.isEmpty())){
                                break;
                            }
                            //Получаем строку номера r
                            XSSFRow row = sheet.getRow(r);
                            for (int c = 0; c < cols; c++) {
                                //Получаем ячейку номер с
                                XSSFCell cell = row.getCell(c);

                                if (!fullCell(cell)){
                                    continue;
                                }

                                if (cell.getStringCellValue().contains("акультет") || cell.getStringCellValue().contains("олледж") || cell.getStringCellValue().contains("нститут")) {
                                    //Присвоение полного названия факультета
                                    facultyFull = cell.getStringCellValue();
                                    //Перевод в сокращение
                                    if (facultyFull.contains("ефектологический")) {
                                        facultyReduc = "ДЕФ";
                                        break;
                                    } else if (facultyFull.contains("стественно-географич")) {
                                        facultyReduc = "ЕГФ";
                                        break;
                                    } else if (facultyFull.contains("ндустриально-педагогич")) {
                                        facultyReduc = "ИПФ";
                                        break;
                                    } else if (facultyFull.contains("сторич")) {
                                        facultyReduc = "ИСТ";
                                        break;
                                    } else if (facultyFull.contains("коммерции, технологий и сервиса")) {
                                        facultyReduc = "ККТС";
                                        break;
                                    } else if (facultyFull.contains("иностранных язык")) {
                                        facultyReduc = "ФИЯ";
                                        break;
                                    } else if (facultyFull.contains("искусств и арт-педагогики")) {
                                        facultyReduc = "ФИАП";
                                        break;
                                    } else if (facultyFull.contains("педагогики и психологии")) {
                                        facultyReduc = "ПИП";
                                        break;
                                    } else if (facultyFull.contains("теологии и религиоведения")) {
                                        facultyReduc = "ФТиР";
                                        break;
                                    } else if (facultyFull.contains("физики, математики, информатики")) {
                                        facultyReduc = "ФМИ";
                                        break;
                                    } else if (facultyFull.contains("ФМИ")) {
                                        facultyReduc = "ФМИ";
                                        facultyFull = "Факультет физики, математики, информатики";
                                        break;
                                    } else if (facultyFull.contains("физической культуры и спорта")) {
                                        facultyReduc = "ФФСК";
                                        break;
                                    } else if (facultyFull.contains("философии и социологии")) {
                                        facultyReduc = "ФФС";
                                        break;
                                    } else if (facultyFull.contains("экономики и управления")) {
                                        facultyReduc = "ИЭУ";
                                        break;
                                    } else if (facultyFull.contains("илологичес")) {
                                        facultyReduc = "ФИЛ";
                                        break;
                                    } else if (facultyFull.contains("удожественно-графичес")) {
                                        facultyReduc = "ХГФ";
                                        break;
                                    } else if (facultyFull.contains("ридичес")) {
                                        facultyReduc = "ЮРФ";
                                        break;
                                    }
                                }
                            }
                        }

                        //Выводим название факультета и сокращение
                        System.out.println(facultyFull);
                        System.out.println(facultyReduc);

                        Faculty faculty =
                                Faculty.builder()
                                        .facultyName(facultyFull)
                                        .abbreviation(facultyReduc)
                                        .build();

                        boolean facultyExist = facultyRepository.existsByFacultyName(facultyFull);


                        if (!facultyExist) {
                            facultyTransactions.add(faculty);
                        }

                        facultyRepository.saveAll(facultyTransactions);

                        //Разбиваем направление
                        int indexProf = 0;
                        //Разбиваем строку на список
                        List<String> fullName = new ArrayList<>(List.of(sheet.getRow(skipTopRows(sheet))
                                .getCell(2).getStringCellValue().replace("\n", " ")
                                .split(" ")));
                        //Удаляем лишние пробелы
                        while(fullName.contains(""))
                            fullName.remove("");

                        //i - индекс подстроки в списке
                        for (int i = 0; i < fullName.size(); i++) {
                            //Запоминаем индекс вида пары и должности препода
                            if (fullName.get(i).contains("(профиль")) {
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
                        //Вывод профиля
                        nameProfile = String.join(" ", profile);
                        //Направление
                        System.out.println(nameDirection);
                        //Профиль
                        System.out.println(nameProfile);





                        //r-строки, с-столбцы
                        for (int r = skipTopRows(sheet) + numRowsAdd; r < rows + 1; r++) {
                            //Вывод разделения по виду недели
                            if (r == controlWeekView + 2)
                                controlWeekView = r;

                            //выходим из цикла если прошлись по всем подгруппам
                            if (numSubGroup > countSubGroups(sheet))
                                break;
                            //Если прошлись по всем строкам возвращаемся в первую строку и меняем номер подгруппы
                            if (r == rows) {
                                r = skipTopRows(sheet) + numRowsAdd;
                                controlWeekView = skipTopRows(sheet) + numRowsAdd + 1;
                                numSubGroup++;
                            }

                            //Создаём флаг, который помогает запомнить индекс ячейки для определения аудитории
                            boolean flag = false;
                            //Создаём флаг, для проверки пары на общую
                            boolean genPara = false;
                            //Получаем строку номера r
                            XSSFRow row = sheet.getRow(r);
                            //Если строка пустая, пропускаем
                            if (row == null)
                                continue;

                            /////Получаем номер группы////
                            if (r == skipTopRows(sheet) + numRowsAdd && numSubGroupForNumGroup <= countSubGroups(sheet) * 2) {
                                String[] fullGroup = sheet.getRow(skipTopRows(sheet) + numRowsAdd)
                                        .getCell(numSubGroupForNumGroup)
                                        .getStringCellValue()
                                        .split(" ");
                                String groupAndSub = fullGroup[1];
                                fullGroup = groupAndSub.split("\\.");
                                numSubGroupForNumGroup += 2;
                                if (!(numGroup.equals(fullGroup[0]))) {
                                    numGroup = fullGroup[0];
                                    //Номер группы
                                    System.out.println(numGroup);
                                    logger.info(facultyRepository.findAll().toString());
                                    logger.info("Ищем факультет: " + facultyFull);

                                    Group transaction =
                                            Group.builder()
                                                    .number(numGroup)
                                                    .direction(nameDirection)
                                                    .profile(nameProfile)
                                                    .faculty(facultyRepository.findByFacultyName(facultyFull))
                                                    .build();
                                    groupTransactions.add(transaction);



                                    for (Group group : groupTransactions) {
                                        if (group.getNumber().equals(transaction.getNumber())) {
                                            GroupExists = true;
                                            break;
                                        }
                                    }
                                }

                            }



                            for (int c = 0; c < cols; c++) {
                                //Меняем номер ячейки в зависимости от номера подгруппы
                                if (c == 2 && c != numSubGroup * 2)
                                    c = numSubGroup * 2;
                                //Если вышли за пределы подгруппы, выходим из цикла
                                if (c > numSubGroup * 2)
                                    break;

                                //Получаем ячейку номера c
                                XSSFCell cell = row.getCell(c);

                                //Проверяем есть ли что-то в ячейке с аудиторией для первой подгруппы, чтобы определить общая пара или нет
                                if (fullCell(row.getCell(2)) && fullCell(row.getCell(numCols(sheet) - 1))) {
                                    int countI = 0;
                                    for (int i = 4; i < cols - 1; i += 2) {
                                        if (fullCell(row.getCell(i))) {
                                            countI++;
                                        }
                                    }
                                    if (countI == 0)
                                        genPara = true;
                                }
                                //Меняем значение ячеек с дисциплиной и аудиторией если пара общая
                                if (genPara && c == numSubGroup * 2)
                                    cell = row.getCell(2);

                                //Пропускаем если ячейка пустая или внутри неё пробел
                                if (!fullCell(cell))
                                    continue;

                                //Не выводим "Ауд."
                                if (r == skipTopRows(sheet) + numRowsAdd && cell.getStringCellValue().contains("А")) {
                                    if (!flag) {
                                        flag = true;
                                    }
                                    continue;
                                }

                                //Разбиваем время
                                if (c == 1) {
                                    switch (cell.getStringCellValue()) {
                                        case "08.00 - 09.30":
                                            time = "08:00 09:30";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "8.00 - 9.30":
                                            time = "08:00 09:30";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "09.40 - 11.10":
                                            time = "09:40 11:10";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "9.40 - 11.10":
                                            time = "09:40 11:10";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "11.20 - 12.50":
                                            time = "11:20 12:50";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "13.10 - 14.40":
                                            time = "13:10 14:40";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "14.50 - 16.20":
                                            time = "14:50 16:20";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "16.30 - 18.00":
                                            time = "16:30 18:00";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "18.10 - 19.40":
                                            time = "18:10 19:40";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                        case "19.50 - 21.20":
                                            time = "19:50 21:20";
                                            fullTime = time.split(" ");
                                            //Время начала
                                            startTime = fullTime[0];
                                            //Время конца
                                            endTime = fullTime[1];
                                            break;
                                    }
                                    continue;
                                }

                                //Разбиваем группы, получаем номер группы и подгрупп
                                if (r == skipTopRows(sheet) + numRowsAdd) {
                                    //Создаём массив из слов
                                    String[] fullNumGroup = cell.getStringCellValue().split(" ");
                                    //Получем номер подгруппы
                                    subGroup = fullNumGroup[1];

                                    Optional<Group> groupInDb = Optional.ofNullable(groupRepository.findByNumber(numGroup));

                                    if (!groupTransactions.isEmpty() && !groupInDb.isPresent()) {
                                        groupRepository.saveAll(groupTransactions);
                                    }

                                    //Здесь было прошлое удаление
                                    logger.info("Удаление расписания по номеру подгруппы: " + subGroup);
                                    Subgroup subgroupForDelete = subgroupRepository.findFirstByNumber(subGroup);

                                    if (subgroupForDelete != null) {
                                        Group group = subgroupForDelete.getGroup(); // Получаем группу, к которой относится подгруппа

                                        // Проверяем, совпадает ли номер подгруппы с номером группы
                                        if (!subGroup.equals(group.getNumber())) {
                                            // Удаляем все записи расписания, связанные с подгруппой
                                            List<Schedule> schedules = scheduleRepository.findBySubgroupId(subgroupForDelete.getId());
                                            logger.info("Найдено расписаний для удаления: " + schedules.size());

                                            for (Schedule schedule : schedules) {
                                                scheduleRepository.delete(schedule);
                                            }

                                            logger.info("Расписания удалены");

                                            // Удаляем подгруппу после удаления всех связанных расписаний
                                            logger.info("Удаление подгруппы с номером: " + subGroup);
                                            subgroupRepository.delete(subgroupForDelete);
                                            logger.info("Подгруппа удалена");
                                        } else {
                                            List<Schedule> schedules = scheduleRepository.findBySubgroupId(subgroupForDelete.getId());
                                            logger.info("Найдено расписаний для удаления: " + schedules.size());

                                            for (Schedule schedule : schedules) {
                                                scheduleRepository.delete(schedule);
                                            }
                                            logger.info("Номер подгруппы совпадает с номером группы. Удаление отменено.");
                                        }
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
                                        subgroupRepository.saveAll(subgroupTransactions);
                                    } else {
                                        continue;
                                    }


                                    //groupRepository.deleteByNumber(numGroup);

                                    continue;
                                }

                                //Разбиваем дисциплину
                                if (r > skipTopRows(sheet) + numRowsAdd && (c > 1 && c % 2 == 0 && c != cols - 1) && cell.getCellType() == CellType.STRING) {
                                    int indexView = 0;
                                    int indexPost = 0;
                                    //Заменяем переносы строк
                                    String fullNameDiscipStr = cell.getStringCellValue().replace("\n", " ");
                                    //Замена вида пары при физ-ре
                                    if (fullNameDiscipStr.contains("(общая физическая подготовка)"))
                                        fullNameDiscipStr = fullNameDiscipStr.replace("(общая физическая подготовка)", "(ПР)");
                                    //Разбиваем строку на список
                                    List<String> fullNameDiscip = new ArrayList<>(List.of(fullNameDiscipStr.split(" ")));
                                    //Удаляем лишние пробелы
                                    while(fullNameDiscip.contains(""))
                                        fullNameDiscip.remove("");
                                    //i - индекс подстроки в списке
                                    for (int i = 0; i < fullNameDiscip.size(); i++) {
                                        //Запоминаем индекс вида пары и должности препода
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


                                    //Создаём 2 списка куда будем помещать название и ФИО
                                    List<String> discipline = new ArrayList<>();
                                    List<String> FCs = new ArrayList<>();
                                    //Заполняем списки нужными данными
                                    for (int discElms = 0; discElms < indexView; discElms++) {
                                        discipline.add(fullNameDiscip.get(discElms));
                                    }
                                    for (int FCsElms = indexPost + 1; FCsElms <= indexPost + 2; FCsElms++) {
                                        FCs.add(fullNameDiscip.get(FCsElms));
                                    }

                                    //Создаём переменные для удобства переноса данных в БД

                                    //Вывод дисциплины
                                    nameDiscipline = String.join(" ", discipline);
                                    //Вывод вида пары
                                    disciplineView = fullNameDiscip.get(indexView);
                                    //Вывод ФИО преподователя
                                    teacherFCs = String.join(" ", FCs);
                                    if (teacherFCs.contains(","))
                                        teacherFCs = teacherFCs.substring(0, teacherFCs.length() - 1);
                                    //Вывод должности преподователя
                                    teacherPost = fullNameDiscip.get(indexPost);
                                    if (teacherPost.contains("ст.пр"))
                                        teacherPost = "ст.пр";
                                    String teacherPostSubStr = teacherPost.substring(teacherPost.length() - 1);
                                    if (teacherPostSubStr.contains("."))
                                        teacherPost = teacherPost.substring(0, teacherPost.length() - 1);
                                    //Вывод разделения по виду недели
                                    if (numDenOrCom(sheet, controlWeekView, countSubGroups(sheet))) {
                                        if (r == controlWeekView) {
                                            weekView = "ЧИСЛИТЕЛЬ";
                                            //Следующую строку меняешь на добавление в БД
                                            //System.out.println(weekView);
                                        } else if (r == controlWeekView + 1) {
                                            weekView = "ЗНАМЕНАТЕЛЬ";
                                            //Следующую строку меняешь на добавление в БД
                                            //System.out.println(weekView);
                                        }
                                    } else {
                                        weekView = "ВСЕГДА";
                                        //Следующую строку меняешь на добавление в БД
                                        //System.out.println(weekView);
                                    }
                                }
                                //Вывод дня недели
                                if (c == 0) {
                                    day = String.valueOf(cell);
                                    //Следующую строку меняешь на добавление в БД
                                    //System.out.println(day);
                                    continue;
                                }
                                //Вывод аудитории
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
                                }

                                subjectRepository.saveAll(subjectTransactions);

                                // Создаем нового преподавателя
                                Teacher teachersTransaction = Teacher.builder()
                                        .name(teacherFCs)
                                        .post(teacherPost)
                                        .build();

                                System.out.println(teachersTransaction);

                                // Проверяем, существует ли преподаватель с таким именем
                                Optional<Teacher> existingTeacherOpt = Optional.ofNullable(teacherRepository.findByName(teachersTransaction.getName()));
                                boolean teacherExists = existingTeacherOpt.isPresent();
                                System.out.println(teacherExists);

                                if (teacherExists) {
                                    Teacher existingTeacher = existingTeacherOpt.get();

                                    // Проверяем, отличается ли роль
                                    if (!existingTeacher.getPost().equals(teachersTransaction.getPost())) {
                                        // Если роль отличается, обновляем существующую запись
                                        existingTeacher.setPost(teachersTransaction.getPost());
                                        teacherRepository.save(existingTeacher); // Сохраняем изменения
                                        System.out.println("Обновлена роль преподавателя: " + existingTeacher);
                                    } else {
                                        System.out.println("Преподаватель с такой ролью уже существует.");
                                    }
                                } else {
                                    // Если преподаватель не существует, добавляем его в список транзакций
                                    teacherTransactions.add(teachersTransaction);
                                }

                                // Сохраняем уникальных преподавателей
                                List<Teacher> uniqueTeachers = teacherTransactions.stream()
                                        .distinct()
                                        .collect(Collectors.toList());
                                System.out.println(uniqueTeachers);
                                teacherRepository.saveAll(uniqueTeachers);

                                /*
                                //Следующую строку меняешь на добавление в БД
                                System.out.println(subGroup);
                                //Дисциплина
                                System.out.println(nameDiscipline);
                                //Вид пары
                                System.out.println(disciplineView);
                                //ФИО препода
                                System.out.println(teacherFCs);
                                //Должность
                                System.out.println(teacherPost);
                                //Вид недели
                                System.out.println(weekView);
                                //День недели
                                System.out.println(day);
                                //Время начала
                                System.out.println(startTime);
                                //Время конца
                                System.out.println(endTime);
                                //Аудитория
                                System.out.println(numAuditorium);
                                 */

                                //Выводим информацию из ячейки, в зависимости от типа
                                //System.out.println(getValue(cell));
                            }
                            //Пропуск строки для удобства чтения
                            System.out.println();

                        }
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    // Метод для удаления расписаний
    private void deleteSchedules(Subgroup subgroup) {
        List<Schedule> schedules = scheduleRepository.findBySubgroupId(subgroup.getId());
        logger.info("Найдено расписаний для удаления: " + schedules.size());
        for (Schedule schedule : schedules) {
            scheduleRepository.delete(schedule);
        }
        logger.info("Расписания удалены");
    }
}
