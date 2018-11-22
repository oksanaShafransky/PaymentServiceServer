package com.payment.server.craft.paymentservercraft;

import com.payment.service.dto.beans.Payment;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentServerIT {
	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();

	HttpHeaders headers = new HttpHeaders();

	@Before
	public void before() {
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	}

	@Test
	public void testGetPayment() throws JSONException {

		HttpEntity<String> entity = new HttpEntity<String>(null, headers);

		Payment payment = new Payment("1u24a8t5-5555-4321-93b6-6efee67dk823","USD",(float)4.2, "1u24a8t5-5555-4321-93b6-6efee67dk823","1u24a8t5-5555-4321-93b6-6efee67dk823","aaa","12345");

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/payment/" + payment.getPaymentid()),
				HttpMethod.GET, entity, String.class);

		String expected = "{\n" +
				"        \"paymentid\": \"1u24a8t5-5555-4321-93b6-6efee67dk823\",\n" +
				"        \"payerid\": \"1u24a8t5-5555-4321-93b6-6efee67dk823\",\n" +
				"        \"payeeid\": \"1u24a8t5-5555-4321-93b6-6efee67dk823\",\n" +
				"        \"paymentmethodid\": \"1u24a8t5-5555-4321-93b6-6efee67dk823\",\n" +
				"        \"amount\": 4.2,\n" +
				"        \"currency\": \"USD\",\n" +
				"        \"paymentdescription\": \"aaa\",\n" +
				"        \"paymentnumber\": \"12345\"\n" +
				"    }";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void addPayment() {

		Payment payment = new Payment("1u24a8t5-5555-4321-93b6-6efee67dk823","USD",(float)4.2, "1u24a8t5-5555-4321-93b6-6efee67dk823","1u24a8t5-5555-4321-93b6-6efee67dk823","aaa","12345");

		HttpEntity<Payment> entity = new HttpEntity<Payment>(payment, headers);

		ResponseEntity<Payment> response = restTemplate.exchange(
				createURLWithPort("/payment/add"),
				HttpMethod.POST, entity, Payment.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

		assertTrue(actual.contains("/payment/add"));

	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
