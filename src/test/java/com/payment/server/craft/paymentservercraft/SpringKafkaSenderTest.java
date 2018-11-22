package com.payment.server.craft.paymentservercraft;

import com.payment.server.craft.paymentservercraft.config.KafkaSender;
import com.payment.server.craft.paymentservercraft.exceptions.PaymentException;
import com.payment.service.dto.beans.Payment;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertThat;
import static org.springframework.kafka.test.assertj.KafkaConditions.key;
import static org.springframework.kafka.test.hamcrest.KafkaMatchers.hasValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.test.rule.KafkaEmbedded;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class SpringKafkaSenderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpringKafkaSenderTest.class);

    private static String SENDER_TOPIC = "payment_service_craft";

    @Autowired
    private KafkaSender sender;

    private KafkaMessageListenerContainer<String, Payment> container;

    private BlockingQueue<ConsumerRecord<String, Payment>> records;

    @ClassRule
    public static KafkaEmbedded embeddedKafka = new KafkaEmbedded(1, true, SENDER_TOPIC);

    @Before
    public void setUp() throws Exception {
        // set up the Kafka consumer properties
        Map<String, Object> consumerProperties =
                KafkaTestUtils.consumerProps("payment_service_craft", "false", embeddedKafka);

        // create a Kafka consumer factory
        DefaultKafkaConsumerFactory<String, Payment> consumerFactory =
                new DefaultKafkaConsumerFactory<String, Payment>(consumerProperties);

        // set the topic that needs to be consumed
        ContainerProperties containerProperties = new ContainerProperties(SENDER_TOPIC);

        // create a Kafka MessageListenerContainer
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);

        // create a thread safe queue to store the received message
        records = new LinkedBlockingQueue<>();

        // setup a Kafka message listener
        container.setupMessageListener(new MessageListener<String, Payment>() {
            @Override
            public void onMessage(ConsumerRecord<String, Payment> record) {
                LOGGER.debug("test-listener received message='{}'", record.toString());
                records.add(record);
            }
        });

        // start the container and underlying message listener
        container.start();

        // wait until the container has the required number of assigned partitions
        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @After
    public void tearDown() {
        // stop the container
        container.stop();
    }

    @Test
    public void testSend() throws InterruptedException, PaymentException {
        Payment payment = new Payment("1u24a8t5-5555-4321-93b6-6efee67dk823","USD",(float)4.2, "1u24a8t5-5555-4321-93b6-6efee67dk823","1u24a8t5-5555-4321-93b6-6efee67dk823","aaa","12345");

        sender.send(payment);

        // check that the message was received
        ConsumerRecord<String, Payment> received = records.poll(10, TimeUnit.SECONDS);
        // Hamcrest Matchers to check the value
        assertThat(received, hasValue(payment));
        // AssertJ Condition to check the key
        assertThat(received).has(key(null));
    }
}