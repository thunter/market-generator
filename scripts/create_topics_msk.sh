#!/usr/bin/bash

BOOTSTRAP=$1
if [ -z "$1" ]; then
    echo "Missing BOOTSTRAP argument"
    exit
fi

# expecte kafka or confluent tooling to be in the PATH

PROPERTIES_FILE=kafka_tmp.properties

cat <<EOF > $PROPERTIES_FILE
security.protocol=SASL_SSL
sasl.mechanism=AWS_MSK_IAM
sasl.jaas.config=software.amazon.msk.auth.iam.IAMLoginModule required;
sasl.client.callback.handler.class=software.amazon.msk.auth.iam.IAMClientCallbackHandler
EOF

kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --create --topic trades --partitions 6 --replication-factor 3
kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --create --topic positions --partitions 6 --replication-factor 3
kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --create --topic trades.stream.buy  --partitions 6 --replication-factor 3
kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --create --topic trades.stream.sell  --partitions 6 --replication-factor 3

kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --create --topic trades.stream.opportunities --partitions 6 --replication-factor 3
kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --create --topic prices --partitions 6 --replication-factor 3


# kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --describe --topic trades
# kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --describe --topic positions
# kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --describe --topic trades.stream.buy
# kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --describe --topic trades.stream.sell

# kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --create --topic  streams-dsl-KSTREAM-TOTABLE-0000000008-repartition --partitions 6 --replication-factor 3


kafka-topics --bootstrap-server $BOOTSTRAP --command-config $PROPERTIES_FILE --describe --topic prices


rm $PROPERTIES_FILE
