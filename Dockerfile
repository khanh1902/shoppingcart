FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} /app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]
MAINTAINER baeldung.com
#COPY target/ShoppingCart-0.0.1-SNAPSHOT.jar shopping0-1.0.0.jar
#ENTRYPOINT ["java","-jar","/shopping0-1.0.0.jar"]

COPY /target/ShoppingCart-0.0.1-SNAPSHOT.jar ShoppingCart.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","ShoppingCart.jar"]