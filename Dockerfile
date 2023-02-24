FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} target/app.jar
#ENTRYPOINT ["java","-jar","target/app.jar"]
VOLUME /tmp
ADD target/core-0.1.0-RELEASE.jar target/app.jar
RUN bash -c 'touch target/app.jar'
ENTRYPOINT ["java","-jar","-Dspring.profiles.active=local","target/app.jar"]
