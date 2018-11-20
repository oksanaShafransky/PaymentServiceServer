package com.payment.server.craft.paymentservercraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.transaction.KafkaTransactionManager;

import javax.servlet.annotation.MultipartConfig;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableAutoConfiguration(exclude={KafkaAutoConfiguration.class})
public class PaymentServerCraftApplication {

	public static void main(String[] args) {

		SpringApplication.run(PaymentServerCraftApplication.class, args);
	}
}
