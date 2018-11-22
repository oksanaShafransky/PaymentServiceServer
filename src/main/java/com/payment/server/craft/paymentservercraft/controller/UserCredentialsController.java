package com.payment.server.craft.paymentservercraft.controller;

import com.payment.service.dto.beans.UserCredentials;
import com.payment.service.dto.service.UserCredentialsService;
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
@RequestMapping("/credentials")
public class UserCredentialsController {
    private static final Logger logger = LoggerFactory.getLogger(UserCredentialsController.class);

    @Autowired
    UserCredentialsService userCredentialsService;

    /**
     * getUserCredentialsByUserId - get user credentials by specific user id
     * @param id
     * @return the user credentials or NOT_FOUND status
     */
    @GetMapping("userid/{id}")
    public ResponseEntity<?> getUserCredentialsByUserId(@PathVariable("id") String id) {
        List<UserCredentials> userCredentials = userCredentialsService.getByUserId(id);
        if(userCredentials == null) {
            logger.warn("No user credentials with id {} was found", id);
            return new ResponseEntity<>("no user credentials found", HttpStatus.NOT_FOUND);
        }
        logger.info("The user credentials with id {} was found successfully", id);
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }

    /**
     * getAllUserCredentials - get all user credentials
     * @return list of user credentials
     */
    @GetMapping("all")
    public ResponseEntity<?> getAllUserCredentials() {
        List<UserCredentials> list = userCredentialsService.getAllUserCredentials();
        if(list == null) {
            logger.warn("No user credentials were found");
            return new ResponseEntity<>("no user credentials found", HttpStatus.NOT_FOUND);
        }
        logger.warn("It was found {} user credentials", list.size());
        return new ResponseEntity<List<UserCredentials>>(list, HttpStatus.OK);
    }

    /**
     * getUserCredentialsByPaymentMethodId - find user credentials for specific payment method id
     * @param id
     * @return list of user credentials for specific payment method or NOT_FOUND status
     */
    @GetMapping("paymentmethod/{name}")
    public ResponseEntity<?> getUserCredentialsByPaymentMethodId(@PathVariable("id") String id) {
        List<UserCredentials> userCredentials = userCredentialsService.getByPaymentMethodId(id);
        if(userCredentials == null) {
            logger.warn("No user credentials with payment method id {} was found", id);
            return new ResponseEntity<>("no user credentials for payment method found", HttpStatus.NOT_FOUND);
        }
        logger.info("It was found {} user credentials with payment method id {1}", userCredentials.size(), id);
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }

    /**
     * getUserCredentialsByUserIdAndPaymentMethodId - find user credentials by both userid and paymentmethodid
     * @param userid
     * @param methodid
     * @return list of user credentials or NOT_FOUND STATUS
     */
    @GetMapping("both/{userid}/{methodid}")
    public ResponseEntity<?> getUserCredentialsByUserIdAndPaymentMethodId(@PathVariable("userid") String userid, @PathVariable("methodid") String methodid) {
        List<UserCredentials> userCredentials = userCredentialsService.getByUserNameAndByPaymentMethodId(userid, methodid);
        if(userCredentials == null) {
            logger.warn("No user credentials with userid {} and paymentmethodid {1} was found", userid, methodid);
            return new ResponseEntity<>("no user credentials for user id and payment method found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<UserCredentials>>(userCredentials, HttpStatus.OK);
    }
}
