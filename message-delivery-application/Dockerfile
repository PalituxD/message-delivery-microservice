FROM openjdk:8-jdk-alpine
ADD ./target/message-delivery-application-1.0-SNAPSHOT.jar app.jar
ENV JAVA_OPTS=""
RUN apk --no-cache add curl
EXPOSE 9090
CMD ["/bin/sh"]
ENTRYPOINT ["java","-Duser.language=es-PE","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=container","-jar","/app.jar"]