FROM openjdk:17-jdk-slim

EXPOSE 5500

ADD target/BankCards-0.0.1-SNAPSHOT.jar backend.jar

CMD ["java", "-jar", "backend.jar"]