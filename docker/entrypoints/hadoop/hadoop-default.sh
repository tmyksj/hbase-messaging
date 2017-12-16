#!/bin/bash

service ssh restart

if [ -f /entrypoints/first-run ]; then
  rm /entrypoints/first-run
  ${HADOOP_HOME}/bin/hdfs namenode -format
fi

${HADOOP_HOME}/bin/hdfs --daemon start namenode
${HADOOP_HOME}/bin/hdfs --daemon start datanode
${HADOOP_HOME}/bin/yarn --daemon start resourcemanager
${HADOOP_HOME}/bin/yarn --daemon start nodemanager
${HADOOP_HOME}/bin/yarn --daemon start proxyserver

stop_docker() {
  ${HADOOP_HOME}/bin/hdfs --daemon stop namenode
  ${HADOOP_HOME}/bin/hdfs --daemon stop datanode
  ${HADOOP_HOME}/bin/yarn --daemon stop resourcemanager
  ${HADOOP_HOME}/bin/yarn --daemon stop nodemanager
  ${HADOOP_HOME}/bin/yarn --daemon stop proxyserver
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
