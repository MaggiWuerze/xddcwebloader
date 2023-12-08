FROM openjdk:17.0
LABEL org.opencontainers.image.source="https://github.com/maggiwuerze/xdccwebloader>"
WORKDIR /xdcc_webloader
EXPOSE 8080
COPY *.jar /xdcc_webloader/app.jar
ENTRYPOINT ["java","-jar","/xdcc_webloader/app.jar"]