#!/bin/bash

service ssh restart
start-dfs.sh
start-yarn.sh

echo "[hit enter key to exit] or run 'docker stop <container>'"
read

stop-dfs.sh
stop-yarn.sh

