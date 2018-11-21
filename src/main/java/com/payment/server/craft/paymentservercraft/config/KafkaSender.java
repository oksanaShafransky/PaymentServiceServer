package com.payment.server.craft.paymentservercraft.config;

import com.payment.service.dto.beans.Payment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaSender {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaSender.class);

    @Autowired
    private KafkaTemplate<String, Payment> kafkaTemplate;

    @Value("${spring.kafka.topic}")
    private String topic;

    public void send(Payment payment){
        LOG.info("sending payment='{}' to topic='{}'", payment, topic);
        kafkaTemplate.send(topic, payment);
    }
}