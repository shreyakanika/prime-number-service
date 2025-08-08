FROM openjdk:17-slim

VOLUME /tmp
COPY ./prime-number-service-core/target/*.jar /tmp/app.jar
ENV JAVA_OPTS="-XX:+UseContainerSupport"
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /tmp/app.jar" ]
