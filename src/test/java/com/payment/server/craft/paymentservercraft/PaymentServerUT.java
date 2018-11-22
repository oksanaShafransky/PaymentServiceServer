package com.payment.server.craft.paymentservercraft;

import java.util.Arrays;

import com.payment.service.dto.beans.Payment;
import com.payment.service.dto.service.PaymentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@WebMvcTest(value = Payment.class, secure = false)
public class PaymentServerUT {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    Payment mockPayment = new Payment("1u24a8t5-5555-4321-93b6-6efee67dk823","USD",(float)4.2, "1u24a8t5-5555-4321-93b6-6efee67dk823","1u24a8t5-5555-4321-93b6-6efee67dk823","aaa","12345");

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

    @Test
    public void getPayment() throws Exception {

        Mockito.when(
                paymentService.getPaymentById(Mockito.anyString())).thenReturn(mockPayment);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                "http://localhost/payment_service/payment/" + mockPayment.getPaymentid()).accept(
                MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        System.out.println(result.getResponse());

        JSONAssert.assertEquals(expected, result.getResponse()
                .getContentAsString(), false);
    }

    @Test
    public void createPayment() throws Exception {
        Mockito.when(paymentService.addPayment(Mockito.any(Payment.class))).thenReturn(true);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                "http://localhost/payment_service/payment/add")
                .accept(MediaType.APPLICATION_JSON).content(expected)
                .contentType(MediaType.APPLICATION_JSON);

        MvcResult result = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());

        assertEquals("http://localhost/payment_service/payment/add", response
                .getHeader(HttpHeaders.LOCATION));

    }

}

