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

		//TODO: define payment
		Payment payment = new Payment();

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/payment/" + payment.getPaymentid()),
				HttpMethod.GET, entity, String.class);

		String expected = "{id:Course1,name:Spring,description:10Steps}";

		JSONAssert.assertEquals(expected, response.getBody(), false);
	}

	@Test
	public void addPayment() {

	    //TODO: define the payment
		Payment payment = new Payment();
		HttpEntity<Payment> entity = new HttpEntity<Payment>(payment, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				createURLWithPort("/payment/add"),
				HttpMethod.POST, entity, String.class);

		String actual = response.getHeaders().get(HttpHeaders.LOCATION).get(0);

		assertTrue(actual.contains("/payment/add"));

	}

	private String createURLWithPort(String uri) {
		return "http://localhost:" + port + uri;
	}
}
