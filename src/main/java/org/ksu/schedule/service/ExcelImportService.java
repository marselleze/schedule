package org.ksu.schedule.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelImportService{

    void importExcel(List<MultipartFile> files);
}
