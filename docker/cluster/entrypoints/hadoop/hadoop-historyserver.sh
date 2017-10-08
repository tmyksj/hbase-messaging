#!/bin/bash

service ssh restart

${HADOOP_HOME}/bin/mapred --daemon start historyserver

stop_docker() {
  ${HADOOP_HOME}/bin/mapred --daemon stop historyserver
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
