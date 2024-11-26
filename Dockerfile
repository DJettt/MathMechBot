FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
WORKDIR /build
COPY ./src src/
COPY ./pom.xml .
RUN mvn -f ./pom.xml clean package

FROM eclipse-temurin:21-alpine
COPY --from=build /build/target/MathMechBot-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/app/app.jar
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]
