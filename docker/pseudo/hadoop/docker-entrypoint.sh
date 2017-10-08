#!/bin/bash

service ssh restart
start-dfs.sh
start-yarn.sh

stop_docker() {
  stop-dfs.sh
  stop-yarn.sh
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
