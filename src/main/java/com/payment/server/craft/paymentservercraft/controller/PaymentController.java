package com.payment.server.craft.paymentservercraft.controller;

import com.payment.server.craft.paymentservercraft.config.KafkaSender;
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

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @Autowired
    private KafkaSender kafkaSender;


    @GetMapping("{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable("id") String id) {
        Payment payment = paymentService.getPaymentById(id);
        if(payment == null){
            return new ResponseEntity<>("no payment found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Payment>(payment, HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<?> getPayments() {
        List<Payment> list = paymentService.getAllPayments();
        if(list == null || list.isEmpty()){
            return new ResponseEntity<>("no payments found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Payment>>(list, HttpStatus.OK);
    }

    @PostMapping("add")
    public ResponseEntity<?> addPayment(@RequestBody Map requestBody, UriComponentsBuilder builder) {
        Payment payment = new Payment();
        try {
            payment.setPaymentid((String) requestBody.get("paymentid"));
            payment.setPayeeid((String) requestBody.get("payeeid"));
            payment.setPayerid((String) requestBody.get("payerid"));
            payment.setPaymentmethodid(((String) requestBody.get("paymentmethodid")));
            payment.setAmount(Float.valueOf(requestBody.get("amount").toString()));
            payment.setPaymentdescription((String) requestBody.get("paymentDescription"));
            payment.setCurrency((String) requestBody.get("currency"));

            boolean flag = paymentService.addPayment(payment);
            if (flag == false) {
                return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(builder.path("/payment/{id}").buildAndExpand(payment.getPaymentid()).toUri());
            return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        } catch (Exception e){
            String errorMessage;
            errorMessage = e + " <== error";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("add_queue")
    public ResponseEntity<?> addPaymentToQueue(@RequestBody Map requestBody, UriComponentsBuilder builder) {
        Payment payment = new Payment();
        try {
            payment.setPaymentid((String) requestBody.get("paymentid"));
            payment.setPayeeid((String) requestBody.get("payeeid"));
            payment.setPayerid((String) requestBody.get("payerid"));
            payment.setPaymentmethodid(((String) requestBody.get("paymentmethodid")));
            payment.setAmount(Float.valueOf(requestBody.get("amount").toString()));
            payment.setPaymentdescription((String) requestBody.get("paymentdescription"));
            payment.setCurrency((String) requestBody.get("currency"));
            payment.setPaymentnumber((String) requestBody.get("paymentnumber"));
            kafkaSender.send(payment);
            return new ResponseEntity<Payment>(payment, HttpStatus.CREATED);
        } catch (Exception e) {
            String errorMessage;
            errorMessage = e + " <== error";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("update")
    public ResponseEntity<?> updatePayment(@RequestBody Payment payment) {
        try {
            paymentService.updatePayment(payment);
            return new ResponseEntity<Payment>(payment, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage;
            errorMessage = e + " <== error";
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        paymentService.deletePayment(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("payment/payee/{id}")
    public ResponseEntity<?> getPaymentsByPayeeId(@PathVariable("id") String id) {
        List<Payment> list = paymentService.getAllPaymentsByPayeeId(id);
        if(list == null) {
            return new ResponseEntity<>("no payment for payee found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Payment>>(list, HttpStatus.OK);
    }

    @GetMapping("payment/payer/{id}")
    public ResponseEntity<?> getPaymentsByPayerId(@PathVariable("id") String id) {
        List<Payment> list = paymentService.getAllPaymentsByPayerId(id);
        if(list == null) {
            return new ResponseEntity<>("no payment for payer found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Payment>>(list, HttpStatus.OK);
    }
}