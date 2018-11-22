package com.payment.server.craft.paymentservercraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
@EnableAutoConfiguration(exclude={KafkaAutoConfiguration.class})
public class PaymentServerCraftApplication {

	public static void main(String[] args) {

		SpringApplication.run(PaymentServerCraftApplication.class, args);
	}
}
