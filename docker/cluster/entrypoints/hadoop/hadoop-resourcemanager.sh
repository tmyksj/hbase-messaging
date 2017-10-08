#!/bin/bash

service ssh restart

${HADOOP_HOME}/bin/yarn --daemon start resourcemanager
${HADOOP_HOME}/bin/yarn --daemon start proxyserver

stop_docker() {
  ${HADOOP_HOME}/bin/yarn --daemon stop resourcemanager
  ${HADOOP_HOME}/bin/yarn --daemon stop proxyserver
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
