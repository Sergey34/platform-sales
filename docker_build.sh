#!/bin/bash

gradle build

docker build -t seko/platform-sales-config ./infrastructures/configuration/.
docker build -t seko/platform-sales-registry ./infrastructures/registration/.
docker build -t seko/platform-sales-gateway ./infrastructures/gateway/.
docker build -t seko/platform-sales-auth-service ./infrastructures/auth-service/.
docker build -t seko/platform-sales-mongodb ./infrastructures/mongodb/.
docker build -t seko/platform-sales-monitoring ./infrastructures/monitoring/.
docker build -t seko/platform-elk-app-statistic ./infrastructures/elk-app-statistic/.

docker build -t seko/platform-elkx-sales-statistic ./buisness/elk-sales-statistic/.
docker build -t seko/platform-sales-communication ./buisness/communication/.
docker build -t seko/platform-sales-market ./buisness/market/.
docker build -t seko/platform-sales-statistic ./buisness/statistic-service/.
docker build -t seko/platform-sales-telegram-sender ./buisness/telegram-sender/.
