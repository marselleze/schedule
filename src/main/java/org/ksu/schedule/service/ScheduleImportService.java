package org.ksu.schedule.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ScheduleImportService {

    void importExcel(List<MultipartFile> files);
}
