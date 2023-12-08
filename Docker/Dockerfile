FROM openjdk:17.0
LABEL org.opencontainers.image.source="https://github.com/maggiwuerze/xdccwebloader>"
WORKDIR /app
EXPOSE 8080
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app/app.jar"]