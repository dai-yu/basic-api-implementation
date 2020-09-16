package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import com.thoughtworks.rslist.exception.Error;
import com.thoughtworks.rslist.exception.RsEventNotVaildException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = init();
  private List<User> userList;

  private List<RsEvent> init() {
    List<RsEvent> rsList=new ArrayList<>();
    User user=new User("xiaowang","female",19,"a@thoughtworks.com","18888888888");
    userList =new ArrayList<>();
    userList.add(user);
    rsList.add(new RsEvent("第一件事","无标签",user));
    rsList.add(new RsEvent("第二件事","无标签",user));
    rsList.add(new RsEvent("第三件事","无标签",user));
    return rsList;
  }

  @GetMapping("/rs/list")
  public ResponseEntity list(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start==null || end==null){
      return ResponseEntity.ok(rsList);
    }
    return ResponseEntity.ok(rsList.subList(start-1,end));
  }


  @GetMapping("/rs/{index}")
  public ResponseEntity oneRsEvent(@PathVariable int index){
    if (index<=0 || index>rsList.size()){
      throw new RsEventNotVaildException("invalid index");
    }
    return ResponseEntity.ok(rsList.get(index-1));
  }

  @PostMapping("/rs/event")
  public ResponseEntity add(@RequestBody @Valid RsEvent rsEvent){
    if(!userList.contains(rsEvent.getUser())){
      userList.add(rsEvent.getUser());
    }
    rsList.add(rsEvent);
    return ResponseEntity.created(null).body(rsList.indexOf(rsEvent));
  }

  @PutMapping("/rs/modify")
  public ResponseEntity modify(@RequestParam Integer index,@RequestBody RsEvent rsEvent){
    if(rsEvent.getName()==""){
      rsEvent.setName(rsList.get(index-1).getName());
    }
    if(rsEvent.getKeyword()==""){
      rsEvent.setKeyword(rsList.get(index-1).getKeyword());
    }
    rsList.set(index-1, rsEvent);
    return ResponseEntity.ok(null);
  }

  @DeleteMapping("/rs/{index}")
  public ResponseEntity delete(@PathVariable Integer index){
    rsList.remove(index-1);
    return ResponseEntity.ok(null);
  }

  @GetMapping("/rs/user")
  public ResponseEntity getUserList(){
    return ResponseEntity.ok(userList);
  }

  @ExceptionHandler({RsEventNotVaildException.class, MethodArgumentNotValidException.class})
  public ResponseEntity rsExceptionHandler(Exception e){
    String errorMessage;
    if (e instanceof MethodArgumentNotValidException){
      errorMessage = "invaild param";
    }else {
      errorMessage=e.getMessage();
    }
    Error error=new Error();
    error.setError(errorMessage);
    return ResponseEntity.badRequest().body(error);
  }
}
