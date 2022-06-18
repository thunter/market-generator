# Market Generator

Simulator of a stock market with Brownian motion of stocks which move in a random method.

Goal is to generate 'interesting' messages for pushing to a kafka system for further processing.

## TODO

* How do you do Avro between different packages via schema registry?
* More filtering and build out the streams

* :white_check_mark: Add MSK IAM Support
* :white_check_mark: kafka support.  
* :white_check_mark: Avro messages.
* :white_check_mark:fix the properties loading.
* :white_check_mark:paramertise it a bit better.
* :white_check_mark:enable a while(1) loop
* :white_check_mark:rate limit message production
 

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

## Schema registy

Integration with 

To start: ` schema-registry-start ~/git/market-generator/schema-registry.properties`

Properties file

```ini
kafkastore.bootstrap.servers=SASL_SSL://boot-xxxxxxxx.c1.kafka-serverless.ap-southeast-2.amazonaws.com:9098
host.name=localhost

kafkastore.security.protocol=SASL_SSL
kafkastore.sasl.mechanism=AWS_MSK_IAM
kafkastore.sasl.jaas.config=software.amazon.msk.auth.iam.IAMLoginModule required;
kafkastore.sasl.client.callback.handler.class=software.amazon.msk.auth.iam.IAMClientCallbackHandler
```