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
@RequestMapping("/credentials")
public class UserCredentialsController {
    @Autowired
    UserCredentialsService userCredentialsService;

    @GetMapping("userid/{id}")
    public ResponseEntity<?> getUserCredentialsByUserId(@PathVariable("id") String id) {
        List<UserCredentials> userCredentials = userCredentialsService.getByUserId(id);
        if(userCredentials == null) {
            return new ResponseEntity<>("no user credentials found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<?> getAllPaymentMethod() {
        List<UserCredentials> list = userCredentialsService.getAllUserCredentials();
        if(list == null) {
            return new ResponseEntity<>("no user credentials found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<UserCredentials>>(list, HttpStatus.OK);
    }

    @GetMapping("paymentmethod/{name}")
    public ResponseEntity<?> getUserCredentialsByPaymentMethodId(@PathVariable("id") String id) {
        List<UserCredentials> userCredentials = userCredentialsService.getByPaymentMethodId(id);
        if(userCredentials == null) {
            return new ResponseEntity<>("no user credentials for payment method found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }

    @GetMapping("both/{userid}/{methodid}")
    public ResponseEntity<?> getUserCredentialsByUserIdAndPaymentMethodId(@PathVariable("userid") String userid, @PathVariable("methodid") String methodid) {
        List<UserCredentials> userCredentials = userCredentialsService.getByUserNameAndByPaymentMethodId(userid, methodid);
        if(userCredentials == null) {
            return new ResponseEntity<>("no user credentials for user id and payment method found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }
}
