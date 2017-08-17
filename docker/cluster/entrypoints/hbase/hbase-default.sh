#!/bin/bash

service ssh restart

stop_docker() {
  exit 0
}

trap "stop_docker" HUP INT QUIT TERM
while true; do sleep 1; done
