package com.payment.server.craft.paymentservercraft.controller;

import com.payment.server.craft.paymentservercraft.config.Sender;
import com.payment.server.craft.paymentservercraft.utils.PaymentServiceConsts;
import com.payment.service.dto.beans.Payment;
import com.payment.service.dto.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(PaymentServiceConsts.PAYMENT_SERVICE_BASE_URI)
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Sender kafkaSender;


    @GetMapping("payment/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable("id") String id) {
        Payment payment = paymentService.getPaymentById(id);
        return new ResponseEntity<Payment>(payment, HttpStatus.OK);
    }

    @GetMapping("payment/all")
    public ResponseEntity<List<Payment>> getPayments() {
        List<Payment> list = paymentService.getAllPayments();
        return new ResponseEntity<List<Payment>>(list, HttpStatus.OK);
    }

    @PostMapping("payment/add")
    public ResponseEntity<Void> addPayment(@RequestBody Map requestBody, UriComponentsBuilder builder) {
        Payment payment = new Payment();
        payment.setPaymentid((String)requestBody.get("paymentid"));
        payment.setPayeeid((String) requestBody.get("payeeid"));
        payment.setPayerid((String) requestBody.get("payerid"));
        payment.setPaymentmethodid(((String) requestBody.get("paymentmethodid")));
        payment.setAmount(Float.valueOf(requestBody.get("amount").toString()));
        payment.setPaymentdescription((String) requestBody.get("paymentDescription"));
        payment.setCurrency((String) requestBody.get("currency"));

        boolean flag = paymentService.addPayment(payment);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/payment/{id}").buildAndExpand(payment.getPaymentid()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PostMapping("payment/add_queue")
    public void addPaymentToQueue(@RequestBody Map requestBody, UriComponentsBuilder builder) {
        Payment payment = new Payment();
        payment.setPaymentid((String)requestBody.get("paymentid"));
        payment.setPayeeid((String) requestBody.get("payeeid"));
        payment.setPayerid((String) requestBody.get("payerid"));
        payment.setPaymentmethodid(((String) requestBody.get("paymentmethodid")));
        payment.setAmount(Float.valueOf(requestBody.get("amount").toString()));
        payment.setPaymentdescription((String) requestBody.get("paymentdescription"));
        payment.setCurrency((String) requestBody.get("currency"));
        kafkaSender.send(payment);
    }

    @PutMapping("payment/update")
    public ResponseEntity<Payment> updatePayment(@RequestBody Payment payment) {
        paymentService.updatePayment(payment);
        return new ResponseEntity<Payment>(payment, HttpStatus.OK);
    }

    @DeleteMapping("payment/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("payment/payee/{id}")
    public ResponseEntity<List<Payment>> getPaymentsByPayeeId(@PathVariable("id") String id) {
        List<Payment> list = paymentService.getAllPaymentsByPayeeId(id);
        return new ResponseEntity<List<Payment>>(list, HttpStatus.OK);
    }

    @GetMapping("payment/payer/{id}")
    public ResponseEntity<List<Payment>> getPaymentsByPayerId(@PathVariable("id") String id) {
        List<Payment> list = paymentService.getAllPaymentsByPayerId(id);
        return new ResponseEntity<List<Payment>>(list, HttpStatus.OK);
    }
}