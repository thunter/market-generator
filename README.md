# Market Generator

Simulator of a stock market with Brownian motion of stocks which move in a random method.

Goal is to generate 'interesting' messages for pushing to a kafka system for further processing.

## TODO

* :white_check_mark: kafka support.  
* :white_check_mark: Avro messages.
* fix the properties loading.
* paramertise it a bit better.
* enable a while(1) loop
* rate limit message production
 

# Kafka

# Notes

## Generating Avro files

Avro schema are in src/main/resource/schema. To generate .java files a maven plugin is enabled:

```
mvn generate-sources
```

## Viewing Avro messages

There is a docker-compose.yml under confluent which boots up a minimal confluent environment to get this working. In order to read messages, jump into a bash shell on the schma registry with

```
docker exec -it <container id> /bin/bash
```

and run:

```sh
kafka-avro-console-consumer --topic trades --property schema.registry.url=http://localhost:8081   --bootstrap-server localhost:9092   --property key.deserializer=org.apache.kafka.common.serialization.StringDeserializer   --property print.key=true   --property key.separator="-" --from-beginning
```
