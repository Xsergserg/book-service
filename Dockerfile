FROM gradle:7.2.0-jdk11 AS build
WORKDIR /app
COPY build.gradle settings.gradle ./
COPY src ./src
RUN gradle clean bootJar -x test

FROM eclipse-temurin:11-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
#ENV SERVER_PORT=8090
#ENV CONTEXT_PATH=/dot-interview
#ENV DB_URL=jdbc:h2:mem:innodb
#ENV DB_USERNAME=inno
#ENV DB_PASSWORD=innopass
#ENV LOGGING_LEVEL = DEBUG
CMD ["java", "-jar", "app.jar"]
