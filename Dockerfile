FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build
COPY pom.xml .
COPY common-lib/pom.xml common-lib/pom.xml
COPY ms-user/pom.xml ms-user/pom.xml
COPY ms-transaction-api/pom.xml ms-transaction-api/pom.xml
COPY ms-transaction-processor/pom.xml ms-transaction-processor/pom.xml
RUN mvn dependency:go-offline -B
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine as ms-user
WORKDIR /app
COPY --from=build /build/ms-user/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-alpine as ms-transaction-api
WORKDIR /app
COPY --from=build /build/ms-transaction-api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

FROM eclipse-temurin:21-jre-alpine as ms-transaction-processor
WORKDIR /app
COPY --from=build /build/ms-transaction-processor/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]