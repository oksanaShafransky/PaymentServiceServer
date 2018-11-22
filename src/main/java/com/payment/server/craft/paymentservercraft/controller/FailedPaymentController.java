package com.payment.server.craft.paymentservercraft.controller;

import com.payment.server.craft.paymentservercraft.config.KafkaSender;
import com.payment.server.craft.paymentservercraft.exceptions.PaymentException;
import com.payment.service.dto.beans.FailedPayment;
import com.payment.service.dto.beans.Payment;
import com.payment.service.dto.service.FailedPaymentService;
import com.payment.service.dto.service.PaymentService;
import com.payment.service.dto.utils.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/failed_payment")
public class FailedPaymentController {
    private static final Logger logger = LoggerFactory.getLogger(FailedPaymentController.class);

    @Autowired
    private FailedPaymentService paymentService;

    /**
     * getPaymentById - find the failed payment with specific id
     * @param id
     * @return ResponseEntity with failed Payment found on db or NOT_FOUND status
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getFailedPaymentById(@PathVariable("id") String id) {
        FailedPayment payment = paymentService.getPaymentById(id);
        logger.info("getPaymentById found payment for payment id %d", id);
        if(payment == null){
            logger.warn("getPaymentById did not found payment for payment id {}", id);
            return new ResponseEntity<>("no payment found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<FailedPayment>(payment, HttpStatus.OK);
    }

    /**
     * getAllPayments - find all failed payments
     * @param
     * @return ResponseEntity with list of failed Payments found on db or NOT_FOUND status
     */
    @GetMapping("all")
    public ResponseEntity<?> getAllFailedPayments() {
        List<FailedPayment> list = paymentService.getAllPayments();
        logger.info("getPayments found {} payments", list.size());
        if(list == null || list.isEmpty()){
            logger.warn("getPayments did not found any payment");
            return new ResponseEntity<>("no payments found", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<FailedPayment>>(list, HttpStatus.OK);
    }

    /**
     * addPayment - add a failed payment to the database
     * @param requestBody
     * @param builder
     * @return new added failed payment or error
     */
    @PostMapping("add")
    public ResponseEntity<?> addFailedPayment(@RequestBody Map requestBody, UriComponentsBuilder builder) {
        FailedPayment payment = new FailedPayment();
        try {
            payment.setPaymentid(UUID.randomUUID().toString());
            payment.setPayeeid((String) requestBody.get("payeeid"));
            payment.setPayerid((String) requestBody.get("payerid"));
            payment.setPaymentmethodid(((String) requestBody.get("paymentmethodid")));
            payment.setAmount(Float.valueOf(requestBody.get("amount").toString()));
            payment.setPaymentdescription((String) requestBody.get("paymentDescription"));
            payment.setCurrency((String) requestBody.get("currency"));
            payment.setPaymentstatus(PaymentStatus.INPROCCESS.name());

            boolean flag = paymentService.addPayment(payment);
            if (flag == false) {
                payment.setPaymentstatus(PaymentStatus.FAILED.name());
                throw new PaymentException("failed to add the payment to the database");
            }
            logger.info("New payment was added to the database with id {}", payment.getPaymentid());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(builder.path("/add").buildAndExpand().toUri());
            return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        } catch (PaymentException e){
            String errorMessage;
            errorMessage = e + " <== error";
            logger.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * updatePayment - update failed payment with new properties
     * @param payment
     * @return the updated payment or BAD_REQUEST status
     */
    @PutMapping("update")
    public ResponseEntity<?> updatePayment(@RequestBody FailedPayment payment) {
        try {
            paymentService.updatePayment(payment);
            logger.info("the payment {} was updated successfully");
            return new ResponseEntity<FailedPayment>(payment, HttpStatus.OK);
        } catch (Exception e) {
            String errorMessage;
            errorMessage = e + " <== error";
            logger.info(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

    }

    /**
     * deletePayment with specific id
     * @param id
     * @return NO_CONTENT
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable("id") String id) {
        paymentService.deletePayment(id);
        logger.info("the payment {} was deleted from the database successfully",id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    /**
     * getPaymentsByPayeeID - find all failed payments received by specific payee
     * @param id of payee
     * @return list of payments received by the payee
     */
    @GetMapping("payment/payee/{id}")
    public ResponseEntity<?> getPaymentsByPayeeId(@PathVariable("id") String id) {
        List<FailedPayment> list = paymentService.getAllPaymentsByPayeeId(id);
        if(list == null) {
            logger.warn("No payments for payerid {} was found", id);
            return new ResponseEntity<>("no payment for payee found", HttpStatus.NOT_FOUND);
        }
        logger.info("It was found {} payments for payeeid {1}", list.size(), id);
        return new ResponseEntity<List<FailedPayment>>(list, HttpStatus.OK);
    }

    /**
     * getPaymentsByPayerID - find all failed payments performed by specific payer
     * @param id of payer
     * @return list of payments performed by the payer
     */
    @GetMapping("payment/payer/{id}")
    public ResponseEntity<?> getPaymentsByPayerId(@PathVariable("id") String id) {
        List<FailedPayment> list = paymentService.getAllPaymentsByPayerId(id);
        if(list == null) {
            logger.warn("No payments for payerid {} was found", id);
            return new ResponseEntity<>("no payment for payer found", HttpStatus.NOT_FOUND);
        }
        logger.info("It was found {} payments for payerid {}", list.size(), id);
        return new ResponseEntity<List<FailedPayment>>(list, HttpStatus.OK);
    }
}