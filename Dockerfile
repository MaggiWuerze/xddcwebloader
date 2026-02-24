# 1) Build the Spring Boot jar
FROM gradle:8.7-jdk21-alpine AS build
WORKDIR /workspace

COPY gradlew build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src

RUN ./gradlew --no-daemon bootJar

# 2) Runtime image
FROM amazoncorretto:21-alpine-jdk
LABEL org.opencontainers.image.source="https://git.maggiwuerze.de/maggiwuerze/xdcwebloader"
WORKDIR /xdcc_webloader
EXPOSE 8080

COPY --from=build /workspace/build/libs/*.jar /xdcc_webloader/app.jar
ENTRYPOINT ["java","-jar","/xdcc_webloader/app.jar"]