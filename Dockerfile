FROM openjdk:11
WORKDIR /app
ENTRYPOINT ["java","-Dloader.path=/lib","-jar","websocket-java-0.0.1-SNAPSHOT.jar"]