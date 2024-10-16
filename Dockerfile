# Dockerfile

# Используем официальный образ OpenJDK 17
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию для приложения
WORKDIR /app

# Копируем jar-файл из сборки Spring Boot приложения
COPY target/schedule-1.0.2.jar /app/schedule-1.0.2.jar

# Открываем порт, на котором будет запущено приложение (8081 согласно конфигу)
EXPOSE 8081

# Переменные окружения для конфигурации базы данных и почты
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres-db:5432/scheduleproject
ENV SPRING_DATASOURCE_USERNAME=pinkyzeus
ENV SPRING_DATASOURCE_PASSWORD=bdd185-kv114
ENV SPRING_MAIL_HOST=smtp.timeweb.ru
ENV SPRING_MAIL_PORT=587
ENV SPRING_MAIL_USERNAME=ksu@24schedule.ru
ENV SPRING_MAIL_PASSWORD=95wnqo37s0

# Команда для запуска Spring Boot приложения с указанным профилем
ENTRYPOINT ["java", "-jar", "/app/schedule-1.0.2.jar"]
