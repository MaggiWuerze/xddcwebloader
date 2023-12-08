FROM openjdk:17.0
WORKDIR /
ADD xdccloader.jar xdccloader.jar
EXPOSE 8080
CMD java -jar xdccloader.jar