FROM gradle:8.6.0-jdk21-jammy AS builder

ARG version

WORKDIR /usr/local/FileService.Server

COPY *.gradle gradle.* gradlew ./
COPY gradle ./gradle
COPY src/main ./src/main

RUN ./gradlew clean build -x test
RUN mv build/libs/file-$version.jar file.jar

FROM eclipse-temurin:21.0.2_13-jre-alpine AS production

ENV TZ=Asia/Seoul

WORKDIR /usr/local/FileService.Server

COPY --from=Builder /usr/local/FileService.Server/file.jar ./file.jar

RUN apk --no-cache add tzdata && \
    apk --no-cache add curl && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

ENTRYPOINT ["java", "-jar", "file.jar"]
