package org.ksu.schedule.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Конфигурация MVC.
 *
 * @version 1.0
 * @author Егор Гришанов
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    /**
     * Добавляет контроллеры представлений.
     *
     * @param registry Реестр контроллеров представлений.
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/password-reset/confirm").setViewName("forward:/resetPassword.html");
    }

    /**
     * Настраивает обработку статических ресурсов.
     *
     * @param registry Реестр обработчиков ресурсов.
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }
}
