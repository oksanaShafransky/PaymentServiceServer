package com.payment.server.craft.paymentservercraft.controller;

import com.payment.server.craft.paymentservercraft.utils.PaymentServiceConsts;
import com.payment.service.dto.beans.PaymentMethod;
import com.payment.service.dto.service.PaymentMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(PaymentServiceConsts.PAYMENT_SERVICE_BASE_URI)
public class PaymentMethodController {
    @Autowired
    PaymentMethodService paymentMethodService;

    @GetMapping("paymentmethod/{id}")
    public ResponseEntity<PaymentMethod> getPaymentMethodById(@PathVariable("id") String id) {
        PaymentMethod method = paymentMethodService.getPaymentMethodById(id);
        return new ResponseEntity<PaymentMethod>(method, HttpStatus.OK);
    }

    @GetMapping("paymentmethod/all")
    public ResponseEntity<List<PaymentMethod>> getAllPaymentMethod() {
        List<PaymentMethod> list = paymentMethodService.getAllPaymentMethods();
        return new ResponseEntity<List<PaymentMethod>>(list, HttpStatus.OK);
    }

    @GetMapping("paymentmethod/name/{name}")
    public ResponseEntity<PaymentMethod> getPaymentMethodByName(@PathVariable("name") String name) {
        PaymentMethod method = paymentMethodService.getPaymentMethodByName(name);
        return new ResponseEntity<PaymentMethod>(method, HttpStatus.OK);
    }
}
