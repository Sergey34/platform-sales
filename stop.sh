#!/usr/bin/env bash

docker-compose stop
docker stop $(docker ps -q)