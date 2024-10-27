# Используем официальный образ OpenJDK
FROM openjdk:17-jre-slim

# Устанавливаем рабочую директорию
WORKDIR /app

# Копируем jar файл в контейнер
COPY target/schedule.jar schedule.jar

# Запускаем приложение
ENTRYPOINT ["java", "-jar", "schedule.jar"]
