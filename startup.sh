#!/bin/bash

if [[ "$(docker images -q my-custom-vm:latest 2> /dev/null)" == "" ]]; then
  echo "building a custom image"
  docker build -t my-custom-vm ./vm/
fi

sh ./shutdown.sh
echo "====================================================="
echo "====================================================="
echo "====================================================="
echo "Starting... Build Application Jar"
mvn clean install -f $PWD/message-delivery-application/pom.xml
sleep 5
echo "End... Build Application Jar"
echo "====================================================="
echo "====================================================="
echo "====================================================="
echo "Starting... Build docker images"
docker-compose build
sleep 5
echo "End... Build docker images"
echo "====================================================="
echo "====================================================="
echo "====================================================="
echo "Starting... print docker-compose config"
docker-compose config
echo "End... print docker-compose config"
echo "====================================================="
echo "====================================================="
echo "====================================================="
echo "Starting... docker-compose database"
docker-compose up -d database
echo "End... docker-compose database"
sh ./scripts/wait-for-it.sh database
echo "====================================================="
echo "====================================================="
echo "====================================================="
echo "Starting... docker-compose"
docker-compose up -d
echo "End... docker-compose"
echo "====================================================="
echo "====================================================="
echo "====================================================="
docker-compose logs -f