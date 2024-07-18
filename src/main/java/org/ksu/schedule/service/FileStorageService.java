package org.ksu.schedule.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

/**
 * Сервис для хранения файлов на сервере.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Service
public class FileStorageService {

    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось создать директорию для хранения загруженных файлов.", ex);
        }
    }

    /**
     * Сохраняет загруженный файл на сервере.
     *
     * @param file загружаемый файл
     * @return путь к сохраненному файлу
     */
    public String storeFile(MultipartFile file) {
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = getFileExtension(originalFilename);
        String newFileName = UUID.randomUUID().toString() + "." + fileExtension;

        try {
            if (originalFilename.contains("..")) {
                throw new RuntimeException("Некорректная последовательность пути файла " + originalFilename);
            }

            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось сохранить файл " + originalFilename + ". Попробуйте снова!", ex);
        }
    }

    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? "" : filename.substring(dotIndex + 1);
    }

    /**
     * Удаляет файл с сервера.
     *
     * @param oldPhotoUrl путь к удаляемому файлу
     */
    public void deleteFile(String oldPhotoUrl) {
        try {
            Path filePath = Paths.get(oldPhotoUrl).toAbsolutePath().normalize();
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            } else {
                throw new RuntimeException("Файл не найден: " + oldPhotoUrl);
            }
        } catch (IOException ex) {
            throw new RuntimeException("Не удалось удалить файл " + oldPhotoUrl + ". Попробуйте снова!", ex);
        }

    }
}
