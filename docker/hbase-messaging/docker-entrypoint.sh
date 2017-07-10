#!/bin/sh

sleep 30
java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar
while true; do sleep 1; done
