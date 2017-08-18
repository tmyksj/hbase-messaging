#!/bin/bash

service ssh restart

if [ -f /entrypoints/first-run ]; then
  rm /entrypoints/first-run
  mkdir /var/lib/zookeeper
  echo ${1} > /var/lib/zookeeper/myid
fi

${ZOOKEEPER_HOME}/bin/zkServer.sh start

stop_docker() {
  ${ZOOKEEPER_HOME}/bin/zkServer.sh stop
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
