FROM maven:3.9.6-eclipse-temurin-21 AS maven-build
COPY pom.xml /app/
COPY core/pom.xml /app/core/
COPY rest/pom.xml /app/rest/
COPY core/src/ /app/core/src/
COPY rest/src/ /app/rest/src/
RUN mvn -f /app/pom.xml -D skipTests clean package

FROM eclipse-temurin:21-jdk AS jdk-build
RUN mkdir /app
COPY --from=maven-build /app/rest/target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
