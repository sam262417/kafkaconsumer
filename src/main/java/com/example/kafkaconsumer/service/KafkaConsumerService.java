package com.example.kafkaconsumer.service;


import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

@Service
public class KafkaConsumerService {

    @Value("${spring.kafka.topic}")
    private String topic;

    @Value("${spring.kafka.consumer.group-id}")
    private String grp_id;

    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String bootstrapServers;

    public List<String> getKafkaEvents() {
        Logger log = LoggerFactory.getLogger(KafkaConsumerService.class.getName());
        List<String> kafkaEventList = new ArrayList<>();

        //Creating consumer properties
        Properties properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, grp_id);
        properties.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");


        // creating consumer
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(properties);

        //get a reference to current thread
        final Thread mainThread = Thread.currentThread();

        //adding the shutdown hook
        Runtime.getRuntime().addShutdownHook(
                new Thread() {
                    public void run() {
                        log.info("Detected a shutdown, let's exit by calling consumer.wakeup()..");
                        consumer.wakeup();

                        //join the main thread to allow the execution of the code kin main thread
                        try {
                            mainThread.join();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
        try {
            //subscribe consumer to our topic(s)
            consumer.subscribe(Arrays.asList(topic));

            //polling
            while (true) {
                //log.info("Polling");
                ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(10000));
                for (ConsumerRecord<String, String> record : records) {

                    log.info("Key:" + record.key() + ",value:" + record.value());
                    kafkaEventList.add("Key:" + record.key() + ",Value:" + record.value());
                    log.info("Partition:" + record.partition() + ", Offset:" + record.offset());
                    kafkaEventList.add("Partition:" + record.partition() + ",Offset:" + record.offset());
                }
                break;
            }
        } catch (WakeupException e) {
            log.info("Shutting Down...");
            // we ignore this as this is an expected exception when closing a consumer
        } catch (Exception e) {
            log.error("Unexpected Exception");
        } finally {
            consumer.close();
            log.info("The consumer is now gracefully closed");
        }
        return kafkaEventList;
    }
}
