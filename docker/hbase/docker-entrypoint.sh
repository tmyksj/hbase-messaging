#!/bin/bash

service ssh restart
start-hbase.sh

stop_docker() {
  stop-hbase.sh
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done

