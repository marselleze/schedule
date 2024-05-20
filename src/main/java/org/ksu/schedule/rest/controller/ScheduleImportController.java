package org.ksu.schedule.rest.controller;

import org.ksu.schedule.service.ScheduleImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/schedule-import")
public class ScheduleImportController {

    @Autowired
    private ScheduleImportService scheduleImportService;

    @RequestMapping(path = "/import")
    public void importSchedule(@RequestPart(required = true) List<MultipartFile> files) {
        scheduleImportService.importExcel(files);
    }
}
