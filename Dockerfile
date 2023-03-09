#
# Build stage
#
FROM maven:3.8.1-openjdk-17-slim AS build
COPY . .
RUN mvn clean package

#
# Package stage
#
FROM openjdk:17-slim
COPY --from=build /target/ShoppingCart-0.0.1-SNAPSHOT.jar ShoppingCart.jar
ENV PORT=8080
EXPOSE 8080
ENTRYPOINT ["java","-jar","ShoppingCart.jar"]