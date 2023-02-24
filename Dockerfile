FROM eclipse-temurin:17-jdk-alpine
#VOLUME /tmp
#ARG JAR_FILE
#COPY ${JAR_FILE} target/app.jar
#ENTRYPOINT ["java","-jar","target/app.jar"]
COPY target/ShoppingCart-0.0.1-SNAPSHOT.jar demo.jar
# ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","demo.jar"]
