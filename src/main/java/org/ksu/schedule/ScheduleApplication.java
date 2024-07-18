package org.ksu.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.h2.tools.Console;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.sql.SQLException;

/**
 * Основной класс приложения расписания.
 *
 * @version 1.0
 * @автор Егор Гришанов
 */
@SpringBootApplication
public class ScheduleApplication {

    /**
     * Главный метод приложения.
     *
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ScheduleApplication.class, args);

        // Закомментированный код для запуска консоли H2
//        try {
//            // Консоль для визуализации БД в браузере
//            Console.main(args);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

}
