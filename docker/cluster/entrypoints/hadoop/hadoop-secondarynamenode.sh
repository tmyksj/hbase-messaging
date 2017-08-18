#!/bin/bash

service ssh restart

if [ -f /entrypoints/first-run ]; then
  rm /entrypoints/first-run
  ${HADOOP_HOME}/bin/hdfs secondarynamenode -format
fi

${HADOOP_HOME}/bin/hdfs --daemon start secondarynamenode

stop_docker() {
  ${HADOOP_HOME}/bin/hdfs --daemon stop secondarynamenode
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
