package org.ksu.schedule.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * Интерфейс для сервиса импорта данных из Excel.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
public interface ExcelImportService {

    /**
     * Импортирует данные из списка Excel-файлов.
     *
     * @param files список файлов для импорта
     */
    void importExcel(List<MultipartFile> files);
}
