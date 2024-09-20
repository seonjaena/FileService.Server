FROM gradle:8.6.0-jdk21-jammy AS builder

WORKDIR /usr/local/FileService.Server

COPY *.gradle ./
COPY src ./src

RUN gradle clean build
RUN mv build/libs/file-*.jar file.jar

FROM eclipse-temurin:21.0.2_13-jre-alpine AS production

ENV TZ=Asia/Seoul

WORKDIR /usr/local/FileService.Server

COPY --from=Builder /usr/local/FileService.Server/file.jar ./file.jar

RUN apk --no-cache add tzdata && \
    apk --no-cache add curl && \
    echo $TZ > /etc/timezone && \
    apk del tzdata

ENTRYPOINT ["java", "-jar", "file.jar"]
