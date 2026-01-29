# syntax=docker/dockerfile:1.6

FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

ARG MODULE

COPY pom.xml .
COPY common-lib/pom.xml common-lib/pom.xml
COPY ms-user/pom.xml ms-user/pom.xml
COPY ms-transaction-api/pom.xml ms-transaction-api/pom.xml
COPY ms-transaction-processor/pom.xml ms-transaction-processor/pom.xml

RUN --mount=type=cache,target=/root/.m2 mvn -B -q dependency:go-offline

COPY . .

RUN --mount=type=cache,target=/root/.m2 mvn -B -pl ${MODULE} -am clean package -DskipTests


FROM eclipse-temurin:21-jre-alpine AS ms-user
WORKDIR /app
COPY --from=build /build/ms-user/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]


FROM eclipse-temurin:21-jre-alpine AS ms-transaction-api
WORKDIR /app
COPY --from=build /build/ms-transaction-api/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]


FROM eclipse-temurin:21-jre-alpine AS ms-transaction-processor
WORKDIR /app
COPY --from=build /build/ms-transaction-processor/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
