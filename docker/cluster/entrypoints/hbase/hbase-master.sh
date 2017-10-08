#!/bin/bash

service ssh restart

${HBASE_HOME}/bin/start-hbase.sh

stop_docker() {
  ${HBASE_HOME}/bin/stop-hbase.sh
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
