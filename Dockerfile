# Используем образ OpenJDK
FROM openjdk:17-jdk-slim

# Указываем рабочую директорию
WORKDIR /app

# Копируем файл pom.xml и загружаем зависимости
COPY pom.xml .
RUN mvn dependency:go-offline

# Копируем код приложения
COPY src ./src

# Собираем приложение
RUN mvn clean package -DskipTests

# Указываем команду запуска приложения
CMD ["java", "-jar", "target/schedule-1.0.2.jar"]
