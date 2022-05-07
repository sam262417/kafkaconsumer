package com.example.kafkaconsumer.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

@SpringBootTest()
@EmbeddedKafka(topics = {"topic_madhi"},partitions=2)
@TestPropertySource(properties = {"spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"
                ,"spring.kafka.consumer.bootstrap-servers=${spring.embedded.kafka.brokers}"
                ,"spring.kafka.producer.bootstrap-servers=${spring.embedded.kafka.brokers}"
                ,"spring.kafka.admin.properties.bootstrap.servers=${spring.embedded.kafka.brokers}"})
public class KafkaConsumerServiceTest {

    @InjectMocks
    KafkaConsumerService kafkaConsumerServiceSpy;

    @Test
    public void testMe() {

        List<String> message = new ArrayList<>();
        Mockito.when(kafkaConsumerServiceSpy.getKafkaEvents()).thenReturn(message);
        verify(kafkaConsumerServiceSpy).getKafkaEvents();

    }



}
