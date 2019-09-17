#!/usr/bin/env bash

docker pull sonarqube:6.7.7-community

docker stop sonarqube

docker rm sonarqube

docker run -d --name sonarqube -p 9000:9000 -p 9092:9092 sonarqube:6.7.7-community

docker logs sonarqube -f