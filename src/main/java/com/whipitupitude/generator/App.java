package com.whipitupitude.generator;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.confluent.kafka.serializers.KafkaAvroSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import com.whipitupitude.market.PositionAvro;
import com.whipitupitude.market.TradeAvro;

import picocli.CommandLine;
import picocli.CommandLine.Option;

public class App implements Callable<Integer> {

    @Option(names = { "-s", "--size" }, description = "Size of market (no of distinct stocks)", defaultValue = "5")
    private int size;

    @Option(names = { "-r", "--rate" }, description = "Rate of events generated per second", defaultValue = "10")
    private int rate;

    @Option(names = "-D", mapFallbackValue = "") // allow -Dkey
    void setProperty(Map<String, String> props) {
        props.forEach((k, v) -> System.setProperty(k, v));
    }

    @Option(names = "--kafka.properties", description = "Path to kafka.properties files", defaultValue = "kafka.properties")
    private String kafkaConfig = "kafka.properties";

    @Option(names = { "-T", "--trade-topic" }, description = "Topic to write trade data to", defaultValue = "trades")
    private String tradeTopicName;

    @Option(names = { "-p" }, description = "Disable position generation", defaultValue = "true")
    private boolean generatePositions;

    @Option(names = { "-P",
            "--position-topic" }, description = "Topic to write market positions to", defaultValue = "positions")
    private String positionTopicName;

    // public static void main(String[] args) {
    public Integer call() throws Exception {
        Logger logger = LoggerFactory.getLogger(App.class);

        Market m = new Market(size, rate);

        logger.info("Size cli: ", size);

        logger.warn("Hello Students of Kafka");

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

        if (generatePositions) {
            generateInitialMarketPositions(producer, m);
        }

        // for (int i = 0; i <= 10; i++) {
        while (true) {

            Trade t = m.getEvent();

            TradeAvro td = new TradeAvro(t.symbol(), t.price(), t.buySell(), t.quantity());
            logger.info("Avro Record");
            System.out.println(td);
            ProducerRecord<String, Object> record = new ProducerRecord<>(tradeTopicName, t.symbol(), td);
            producer.send(record);
        }
        // producer.flush();
        // producer.close();

        // System.out.println("Hello!");

    }

    private void generateInitialMarketPositions(KafkaProducer<String, Object> producer, Market m) {
        for (Stock s : m.stocks) {
            Position p = s.getInitialPosition();
            PositionAvro pa = new PositionAvro(p.symbol(), p.lastTradePrice(), p.position(), p.lastTradeTime());
            ProducerRecord<String, Object> record = new ProducerRecord<String, Object>(positionTopicName, p.symbol(),
                    pa);
            producer.send(record);
        }
    }

    // simple test method
    public static void mainTest(String[] args) {
        Market m = new Market(5, 1);

        System.out.println(m);
        System.out.println(m.marketStocksNames());
        for (int i = 0; i <= 10; i++) {
            m.getEvent();
            System.out.println(m);
        }

    }

    public static void main(String... args) {
        int exitCode = new CommandLine(new App()).execute(args);
        System.exit(exitCode);
    }

}
