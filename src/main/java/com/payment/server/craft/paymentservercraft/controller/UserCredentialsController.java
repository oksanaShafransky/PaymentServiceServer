package com.payment.server.craft.paymentservercraft.controller;

import com.payment.server.craft.paymentservercraft.utils.PaymentServiceConsts;
import com.payment.service.dto.beans.PaymentMethod;
import com.payment.service.dto.beans.UserCredentials;
import com.payment.service.dto.service.UserCredentialsService;
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
public class UserCredentialsController {
    @Autowired
    UserCredentialsService userCredentialsService;

    @GetMapping("credentials/user/{id}")
    public ResponseEntity<List<UserCredentials>> getUserCredentialsByUserId(@PathVariable("id") String id) {
        List<UserCredentials> userCredentials = userCredentialsService.getByUserId(id);
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }

    @GetMapping("credentials/all")
    public ResponseEntity<List<UserCredentials>> getAllPaymentMethod() {
        List<UserCredentials> list = userCredentialsService.getAllUserCredentials();
        return new ResponseEntity<List<UserCredentials>>(list, HttpStatus.OK);
    }

    @GetMapping("credentials/name/{name}")
    public ResponseEntity<List<UserCredentials>> getUserCredentialsByPaymentMethodId(@PathVariable("id") String id) {
        List<UserCredentials> userCredentials = userCredentialsService.getByPaymentMethodId(id);
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }
}
