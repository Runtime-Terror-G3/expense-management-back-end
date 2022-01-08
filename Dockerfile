FROM alpine:3.15

ARG JAR_FILE=Rest/build/libs/*.jar
COPY ${JAR_FILE} application.jar

COPY init_script.sh .
RUN apk add --update openrc postgresql openjdk11-jre
CMD ./init_script.sh
