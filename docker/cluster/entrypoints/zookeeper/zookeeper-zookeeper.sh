#!/bin/bash

service ssh restart

if [ -f /entrypoints/first-run ]; then
  rm /entrypoints/first-run
  echo ${1} > ${ZOOKEEPER_HOME}/myid
fi

${ZOOKEEPER_HOME}/bin/zkServer.sh start

stop_docker() {
  ${ZOOKEEPER_HOME}/bin/zkServer.sh stop
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
