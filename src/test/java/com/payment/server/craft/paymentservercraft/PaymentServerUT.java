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


public class PaymentServerUT {

    @RunWith(SpringRunner.class)
    @WebMvcTest(value = Payment.class, secure = false)
    public class PaymentControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private PaymentService paymentService;

        //TODO:
        Payment mockPayment = new Payment();

        String exampleCourseJson = "{\"name\":\"Spring\",\"description\":\"10 Steps\",\"steps\":[\"Learn Maven\",\"Import Project\",\"First Example\",\"Second Example\"]}";

        @Test
        public void getPayment() throws Exception {

            //Mockito.when(
            //        paymentService.getPaymentById(Mockito.any(), Mockito
             //               .anyString())).thenReturn(mockPayment);

            RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
                    "http://localhost/payment_service/payment/" + mockPayment.getPaymentid()).accept(
                    MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            System.out.println(result.getResponse());
            String expected = "{id:Course1,name:Spring,description:10 Steps}";

            //{"id":"Course1","name":"Spring","description":"10 Steps, 25 Examples and 10K Students","steps":["Learn Maven","Import Project","First Example","Second Example"]}

            JSONAssert.assertEquals(expected, result.getResponse()
                    .getContentAsString(), false);
        }

        @Test
        public void createPayment() throws Exception {
            //TODO: define
            Payment mockPayment = new Payment();

            //paymentService.addCourse to respond back with mockCourse
            //Mockito.when(
            //        paymentService.addPayment(Mockito.anyString(), Mockito
            //                .any(Payment.class))).thenReturn(mockPayment);

            //Send course as body to /students/Student1/courses
            RequestBuilder requestBuilder = MockMvcRequestBuilders.post(
                    "http://localhost/payment_service/payment/add")
                    .accept(MediaType.APPLICATION_JSON).content(exampleCourseJson)
                    .contentType(MediaType.APPLICATION_JSON);

            MvcResult result = mockMvc.perform(requestBuilder).andReturn();

            MockHttpServletResponse response = result.getResponse();

            assertEquals(HttpStatus.CREATED.value(), response.getStatus());

            assertEquals("http://localhost/payment_service/payment/" + mockPayment.getPaymentid(), response
                    .getHeader(HttpHeaders.LOCATION));

        }

    }
}
