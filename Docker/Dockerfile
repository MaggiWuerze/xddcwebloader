FROM amazoncorretto:21-alpine-jdk
LABEL org.opencontainers.image.source="https://github.com/maggiwuerze/xdccwebloader>"
WORKDIR /xdcc_webloader
EXPOSE 8080
COPY Docker/*.jar /xdcc_webloader/app.jar
RUN ls /xdcc_webloader/
ENTRYPOINT ["java","-jar","/xdcc_webloader/app.jar"]