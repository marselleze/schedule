package org.ksu.schedule.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Сервис для импорта расписания из Excel файлов.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface ScheduleImportService {

    /**
     * Импортирует расписание из списка Excel файлов.
     *
     * @param files список файлов для импорта
     */
    void importExcel(List<MultipartFile> files);
}
