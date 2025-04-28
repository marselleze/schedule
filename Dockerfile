# Используем официальный образ OpenJDK
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar файл в контейнер
COPY target/schedule-1.1.6.jar schedule-1.1.6.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "schedule.jar"]
