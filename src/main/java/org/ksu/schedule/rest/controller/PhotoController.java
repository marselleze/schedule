package org.ksu.schedule.rest.controller;

import org.ksu.schedule.domain.Photo;
import org.ksu.schedule.domain.Teacher;
import org.ksu.schedule.repository.TeacherRepository;
import org.ksu.schedule.service.impl.PhotoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Контроллер для управления фотографиями.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@RestController
@RequestMapping("/api/photos")
public class PhotoController {

    @Autowired
    private PhotoServiceImpl photoService;

    private final TeacherRepository teacherRepository;

    @Autowired
    public PhotoController(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    /**
     * Загрузка фотографии по электронной почте пользователя.
     *
     * @param email адрес электронной почты пользователя
     * @param file  загружаемый файл
     * @return строка с результатом загрузки
     */
    @PostMapping("/upload/{email}")
    public String uploadPhoto(@PathVariable String email, @RequestPart(required = true) MultipartFile file) {
        try {
            photoService.uploadPhoto(email, file);
            return "File uploaded successfully";
        } catch (Exception e) {
            return "Failed to upload file: " + e.getMessage();
        }
    }

    /**
     * Скачивание фотографии по идентификатору.
     *
     * @param id идентификатор фотографии
     * @return ResponseEntity с ресурсом фотографии
     */
    @GetMapping("/download/id")
    public ResponseEntity<Resource> getPhotoById(@RequestParam Long id) {
        Photo photo = photoService.getById(id);
        if (photo != null) {
            try {
                Path filePath = Paths.get(photo.getUrl()).toAbsolutePath().normalize();
                System.out.println("File path: " + filePath.toString());
                Resource resource = new UrlResource(filePath.toUri());
                System.out.println("Resource URI: " + filePath.toUri());

                if (resource.exists()) {
                    String contentType = Files.probeContentType(filePath);
                    if (contentType == null) {
                        contentType = "application/octet-stream"; // В случае если тип не распознается
                    }

                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                            .body(resource);

                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().build();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
