FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} target/app.jar
#ENTRYPOINT ["java","-jar","target/app.jar"]

VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
