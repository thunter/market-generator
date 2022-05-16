package com.whipitupitude.generator;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.whipitupitude.market.TradeData;

public class App {
    private static final Logger logger = LogManager.getLogger();
    private static final String kafkaConfig = "kafka.properties";
    private static final String topicName = "trades";

    public static void main(String[] args) {
        Market m = new Market(5);

        logger.info("Hello Students of Kafka");

        logger.trace("Creating kafka config");
        Properties properties = new Properties();
        try {
            // InputStream kafkaConfigStream =
            // ClassLoader.class.getResourceAsStream(kafkaConfig);
            // properties.load(kafkaConfigStream);
            properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
            properties.put("schema.registry.url", "http://localhost:8081");
            properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
            properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class.getName());

        } catch (Exception e) {
            logger.error("Cannot open Kafka config " + kafkaConfig);
            throw new RuntimeException(e);
        }

        KafkaProducer<String, Object> producer = new KafkaProducer<>(properties);

        for (int i = 0; i <= 10; i++) {
            Trade t = m.getEvent();

            TradeData td = new TradeData(t.symbol(), t.price(), t.buySell(), t.quantity());
            logger.info("Avro Record");
            logger.info(td);
            ProducerRecord<String, Object> record = new ProducerRecord<>(topicName, t.symbol(), td);
            producer.send(record);
        }
        producer.flush();
        producer.close();

        System.out.println("Hello!");
    }

    // simple test method
    public static void mainTest(String[] args) {
        Market m = new Market(5);

        System.out.println(m);
        System.out.println(m.marketStocksNames());
        for (int i = 0; i <= 10; i++) {
            m.getEvent();
            System.out.println(m);
        }

    }

}
