#!/bin/bash
curl -i -X PUT http://localhost:8083/connectors/datagen/config \
     -H "Content-Type: application/json" \
     -d '{
            "connector.class": "io.confluent.kafka.connect.datagen.DatagenConnector",
            "key.converter": "org.apache.kafka.connect.storage.StringConverter",
            "kafka.topic": "pizzaOrders",
            "quickstart": "pizza_orders",
            "max.interval": 10,
            "iterations": 1000,
            "tasks.max": "1"
        }'
