#!/bin/bash

SERVICE=$1
status=$(docker ps --filter "name=$SERVICE" --filter "health=healthy" -q)
echo "$status"

while [ "$status" = "" ]
do
    echo "$SERVICE is not healthy yet - sleeping"
    sleep 5
    status=$(docker ps --filter "name=$SERVICE" --filter "health=healthy" -q)
    echo "$status"
done

echo "$SERVICE is healthy"