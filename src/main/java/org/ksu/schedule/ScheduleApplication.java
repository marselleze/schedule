package org.ksu.schedule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.h2.tools.Console;
import java.sql.SQLException;


@SpringBootApplication
public class ScheduleApplication {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = SpringApplication.run(ScheduleApplication.class, args);

//        try {
//            //Консоль для визуализации бд в браузере
//            Console.main(args);
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

    }

}
