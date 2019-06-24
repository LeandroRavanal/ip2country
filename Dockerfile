FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} ip2country.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/ip2country.jar"]