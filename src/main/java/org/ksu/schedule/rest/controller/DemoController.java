package org.ksu.schedule.rest.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Демонстрационный контроллер для проверки работы API.
 *
 * @version 1.0
 * @autor Егор Гришанов
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/demo-controller")
public class DemoController {

    /**
     * Обработчик GET-запроса для демонстрации работы API.
     *
     * @return строка "Hello World"
     */
    @GetMapping
    public ResponseEntity<String> demo(){
        return ResponseEntity.ok("Hello World");
    }
}
