package com.payment.server.craft.paymentservercraft.controller;

import com.payment.server.craft.paymentservercraft.utils.PaymentServiceConsts;
import com.payment.service.dto.beans.User;
import com.payment.service.dto.service.UserService;
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
public class UserController {
    @Autowired
    UserService userService;

    @GetMapping("user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") String id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    @GetMapping("user/mail/{mail}")
    public ResponseEntity<User> getUserByMail(@PathVariable("mail") String mail) {
        User user = userService.getUserByMail(mail);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @GetMapping("user/all")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> list = userService.getAllUsers();
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

    @PostMapping("user/add")
    public ResponseEntity<Void> addUser(@RequestBody Map requestBody, UriComponentsBuilder builder) {
        User user = new User();
        user.setUserid(UUID.randomUUID().toString());
        user.setUsermail(requestBody.get("usermail").toString());
        user.setUsername((String) requestBody.get("username"));

        boolean flag = userService.addUser(user);
        if (flag == false) {
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/user/{id}").buildAndExpand(user.getUserid()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
    @PutMapping("user/update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }
    @DeleteMapping("user/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id) {
        userService.deleteUser(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}