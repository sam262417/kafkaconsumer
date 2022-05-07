package com.example.kafkaconsumer.controller;

import com.example.kafkaconsumer.service.KafkaConsumerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
public class KafkaConsumerController {

    @Autowired
    public KafkaConsumerService kafkaConsumerService;

    @RequestMapping(method = RequestMethod.GET, value = "/consumer")
    public ResponseEntity<String> getKafkaEvents()  throws Exception {
        List<String> kafkaMessage = kafkaConsumerService.getKafkaEvents();
        return new ResponseEntity<>(kafkaMessage.toString(), HttpStatus.OK);
    }

}
