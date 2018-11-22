package com.payment.server.craft.paymentservercraft.controller;

import com.payment.service.dto.beans.PaymentMethod;
import com.payment.service.dto.service.PaymentMethodService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/paymentmethod")
public class PaymentMethodController {
    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodController.class);

    @Autowired
    PaymentMethodService paymentMethodService;

    /**
     * getPaymentMethodById - get the paymentmethod for specific id
     * @param id
     * @return the payment method
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getPaymentMethodById(@PathVariable("id") String id) {
        PaymentMethod method = paymentMethodService.getPaymentMethodById(id);
        if(method == null) {
            logger.warn("No paymentmethod with {} was found", id);
            return new ResponseEntity<>("no payment method found", HttpStatus.NOT_FOUND);
        }
        logger.info("Payment method with id {} was found successfully", id);
        return new ResponseEntity<PaymentMethod>(method, HttpStatus.OK);
    }

    /**
     * getAllPaymentMethods
     * @return list of all payment methods
     */
    @GetMapping("all")
    public ResponseEntity<?> getAllPaymentMethods() {
        List<PaymentMethod> list = paymentMethodService.getAllPaymentMethods();
        if(list == null) {
            logger.warn("No payment method was found");
            return new ResponseEntity<>("no payment method found", HttpStatus.NOT_FOUND);
        }
        logger.info("It was found {} paymentmethods", list.size());
        return new ResponseEntity<List<PaymentMethod>>(list, HttpStatus.OK);
    }

    /**
     * getPaymentPaymentMethodByName - find payment method with specific name
     * @param name of payment method
     * @return paymentmethod with the specific name or NOT_FOUND status
     */
    @GetMapping("name/{name}")
    public ResponseEntity<?> getPaymentMethodByName(@PathVariable("name") String name) {
        PaymentMethod method = paymentMethodService.getPaymentMethodByName(name);
        if(method == null) {
            return new ResponseEntity<>("no payment method found", HttpStatus.NOT_FOUND);
        }
        logger.info("Payment method with name {} was found successfully", method, name);
        return new ResponseEntity<PaymentMethod>(method, HttpStatus.OK);
    }
}
