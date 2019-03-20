FROM openjdk:8-jdk-alpine
ADD target/gateway-0.0.1-SNAPSHOT.jar zone-gateway.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/zone-gateway.jar"]
EXPOSE 8765