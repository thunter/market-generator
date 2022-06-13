#!/usr/bin/bash

# expecte kafka or confluent tooling to be in the PATH

kafka-topics --bootstrap-server localhost:9092 --create --topic trades --partitions 3 --replication-factor 1
kafka-topics --bootstrap-server localhost:9092 --create --topic positions --partitions 3 --replication-factor 1


kafka-topics --describe --bootstrap-server localhost:9092  --topic trades
kafka-topics --describe --bootstrap-server localhost:9092  --topic positions