package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.po.UserPO;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RsEventRepository rsEventRepository;


    @PostMapping("/user")
    public ResponseEntity register(@RequestBody @Valid User user){
        UserPO userPO=new UserPO();
        userPO.setName(user.getName());
        userPO.setGender(user.getGender());
        userPO.setAge(user.getAge());
        userPO.setEmail(user.getEmail());
        userPO.setPhone(user.getPhone());
        userPO.setVoteNum(user.getVoteNum());
        userRepository.save(userPO);
        return ResponseEntity.created(null).build();
    }

    @GetMapping("/user")
    public ResponseEntity getUserList(){
        List<UserPO> all = userRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/user/{id}")
    public ResponseEntity getUserById(@PathVariable int id){
        Optional<UserPO> user = userRepository.findById(id);
        return ResponseEntity.ok(user.get());
    }

    @DeleteMapping("/user/{id}")
    @Transactional
    public ResponseEntity deleteUserById(@PathVariable int id){
        userRepository.deleteById(id);
        return ResponseEntity.ok(null);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity userExceptionHandler(Exception e){
        Error error=new Error();
        error.setError("invalid user");
        return ResponseEntity.badRequest().body(error);
    }
}
