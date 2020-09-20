package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.UserIdIncorrectException;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/user")
    public ResponseEntity register(@RequestBody @Valid User user){
        UserPO userPO = UserPO.builder()
                .name(user.getName())
                .gender(user.getGender())
                .age(user.getAge())
                .email(user.getEmail())
                .phone(user.getPhone())
                .voteNum(user.getVoteNum()).build();
        userService.save(userPO);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user")
    public ResponseEntity getUserList(){
        List<UserPO> all = userService.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable int id){
        if (id<=0){
            throw new UserIdIncorrectException("UserId is incorrect");
        }
        UserPO user = userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity deleteUserById(@PathVariable int id){
        if (id<=0){
            throw new UserIdIncorrectException("UserId is incorrect");
        }
        userService.deleteById(id);
        return ResponseEntity.ok(null);
    }

}
