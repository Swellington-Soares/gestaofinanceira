FROM maven:3.9.9-eclipse-temurin-21 AS build

ARG MODULE

WORKDIR /build
COPY . .

RUN mvn clean package \
    -pl ${MODULE} \
    -am \
    -DskipTests

FROM eclipse-temurin:21-jre
WORKDIR /app

ARG MODULE

COPY --from=build /build/${MODULE}/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
