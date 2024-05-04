FROM eclipse-temurin:17-jre-alpine
LABEL maintainer="<PinkyZeus> <<marselleze.88@gmail.com>>"

RUN apk update \
    && export TZ='Europe/Moscow'\
    && echo 'Europe/Moscow' > /etc/timezone \
    && rm -rf /var/cache/

RUN apk --update add nodejs npm
RUN npm i -g redoc-cli

COPY docker/schedule-0.0.1-SNAPSHOT.jar /app.jar

CMD ["sh", "-c", "java ${JAVA_OPTS} -jar /app.jar ${@}"]

EXPOSE 8088