FROM maven:3.9.9-eclipse-temurin-21-alpine AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package

FROM eclipse-temurin:21-alpine
EXPOSE 8080
COPY --from=build /usr/src/app/target/MathMechBot-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/app/app.jar
ENTRYPOINT ["java", "-jar", "/usr/app/app.jar"]
