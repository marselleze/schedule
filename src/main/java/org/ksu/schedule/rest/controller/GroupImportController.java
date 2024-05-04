package org.ksu.schedule.rest.controller;

import lombok.AllArgsConstructor;
import org.ksu.schedule.service.GroupImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping(path = "/import")
public class GroupImportController {

    @Autowired
    private GroupImportService groupImportService;

    @PostMapping(path = "/import-excel")
    public void importExcelToDatabase(
            @RequestPart(required = true) List<MultipartFile> files) {

        groupImportService.importExcelToGroups(files);

    }

}
