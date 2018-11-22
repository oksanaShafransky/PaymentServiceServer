package com.payment.server.craft.paymentservercraft.controller;

import com.payment.server.craft.paymentservercraft.exceptions.UserException;
import com.payment.service.dto.beans.User;
import com.payment.service.dto.service.UserService;
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
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    /**
     * getUserById - find user with specific id
     * @param id of user
     * @return user or NOT_FOUND
     */
    @GetMapping("{id}")
    public ResponseEntity<?> getUserById(@PathVariable("id") String id) {
        User user = userService.getUserById(id);
        if(user == null) {
            logger.warn("No user with id {} was found", id);
            return new ResponseEntity<>("no user found", HttpStatus.NOT_FOUND);
        }
        logger.info("User with  id {} was found successfully", id);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    /**
     * getUserByMail - find user with the specific mail
     * @param mail
     * @return user or NOT_FOUND status
     */
    @GetMapping("mail/{mail}")
    public ResponseEntity<?> getUserByMail(@PathVariable("mail") String mail) {
        User user = userService.getUserByMail(mail);
        if(user == null) {
            logger.warn("No user with mail {} was found", mail);
            return new ResponseEntity<>("no usermail found", HttpStatus.NOT_FOUND);
        }
        logger.info("User with mail {} was found successfully", mail);
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    /**
     * getAllUsers - find all registered users
     * @return list of users or NOT_FOUND status
     */
    @GetMapping("all")
    public ResponseEntity<?> getAllUsers() {
        List<User> list = userService.getAllUsers();
        if(list == null) {
            logger.warn("No user were found");
            return new ResponseEntity<>("no user found", HttpStatus.NOT_FOUND);
        }
        logger.info("It was found {} users", list.size());
        return new ResponseEntity<List<User>>(list, HttpStatus.OK);
    }

    /**
     * addUser - add new user to the database
     * @param requestBody
     * @param builder
     * @return the new added user
     */
    @PostMapping("add")
    public ResponseEntity<?> addUser(@RequestBody Map requestBody, UriComponentsBuilder builder) {
        User user = new User();
        try {
            user.setUserid(UUID.randomUUID().toString());
            user.setUsermail(requestBody.get("usermail").toString());
            user.setUsername((String) requestBody.get("username"));

            boolean flag = userService.addUser(user);
            if (flag == false) {
                throw new UserException("failed to add user " + user.getUsername());
            }
            logger.info("New user with id {} was added to the database", user.getUserid());
            HttpHeaders headers = new HttpHeaders();
            headers.setLocation(builder.path("/user/{id}").buildAndExpand(user.getUserid()).toUri());
            return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
        } catch (UserException e){
            String errorMessage;
            errorMessage = e + " <== error";
            logger.error(errorMessage);
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * updareUser - update user in database with new properties
     * @param user
     * @return the updated user
     */
    @PutMapping("update")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        logger.info("The user {} was updated successfully", user.getUserid());
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }

    /**
     * deleteUser - delete the user from the database
     * @param id
     * @return
     */
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") String id) {
        userService.deleteUser(id);
        logger.info("The user {} was deleted successfully", id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
