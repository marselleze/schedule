# Используем официальный образ OpenJDK 17
FROM openjdk:17-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем JAR-файл в контейнер
COPY target/schedule-0.3.8.jar /app/my-app.jar

# Указываем команду для запуска приложения
ENTRYPOINT ["java", "-jar", "/app/my-app.jar"]
