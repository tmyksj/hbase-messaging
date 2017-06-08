#!/bin/bash

service ssh restart
start-hbase.sh

echo "[hit enter key to exit] or run 'docker stop <container>'"
read

stop-hbase.sh

