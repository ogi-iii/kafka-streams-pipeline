#!/bin/bash
docker exec -it broker kafka-topics \
 --bootstrap-server broker:9092 \
 --topic pizzaOrders \
 --create \
 --replication-factor 1 \
 --partitions 5
