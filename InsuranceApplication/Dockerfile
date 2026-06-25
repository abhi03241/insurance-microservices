FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim

COPY --from=build /app/target/InsuranceApp-0.0.1-SNAPSHOT.jar .

EXPOSE 8000

ENTRYPOINT ["java", "-jar", "./InsuranceApp-0.0.1-SNAPSHOT.jar"]