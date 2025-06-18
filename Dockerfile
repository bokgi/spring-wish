FROM openjdk:21-jdk
COPY target/wish-0.0.1.jar wish-app.jar
ENTRYPOINT ["java", "-jar", "wish-app.jar"]
