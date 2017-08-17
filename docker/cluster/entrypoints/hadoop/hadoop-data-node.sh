#!/bin/bash

service ssh restart

${HADOOP_HOME}/bin/hdfs --daemon start datanode
${HADOOP_HOME}/bin/yarn --daemon start nodemanager

stop_docker() {
  ${HADOOP_HOME}/bin/hdfs --daemon stop datanode
  ${HADOOP_HOME}/bin/yarn --daemon stop nodemanager
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
