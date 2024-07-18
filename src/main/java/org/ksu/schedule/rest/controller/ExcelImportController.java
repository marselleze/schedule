package org.ksu.schedule.rest.controller;

import org.ksu.schedule.service.ExcelImportService;
import org.ksu.schedule.service.ScheduleImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Контроллер для импорта данных из Excel файлов.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@RestController
@RequestMapping(path = "/api/v1/import")
public class ExcelImportController {

    @Autowired
    private ExcelImportService excelImportService;

    @Autowired
    private ScheduleImportService scheduleImportService;

    /**
     * Обработчик POST-запроса для импорта данных расписания из Excel файлов.
     *
     * @param files список файлов Excel для импорта
     */
    @PostMapping(path = "/import-excel")
    public void importScheduleData(
            @RequestPart(required = true) List<MultipartFile> files) {

        excelImportService.importExcel(files);
        scheduleImportService.importExcel(files);
    }
}
