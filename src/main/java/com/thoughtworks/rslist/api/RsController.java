package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import com.thoughtworks.rslist.domain.User;
import org.springframework.web.bind.annotation.*;

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
  public List<RsEvent> list(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start==null || end==null){
      return rsList;
    }
    return rsList.subList(start-1,end);
  }


  @GetMapping("/rs/{index}")
  public RsEvent oneRsEvent(@PathVariable int index){
    return rsList.get(index-1);
  }

  @PostMapping("/rs")
  public void add(@RequestBody RsEvent rsEvent){
    if(!userList.contains(rsEvent)){
      userList.add(rsEvent.getUser());
    }
    rsList.add(rsEvent);
  }

  @PutMapping("/rs/modify")
  public void modify(@RequestParam Integer index,@RequestBody RsEvent rsEvent){
    if(rsEvent.getName()==""){
      rsEvent.setName(rsList.get(index-1).getName());
    }
    if(rsEvent.getKeyword()==""){
      rsEvent.setKeyword(rsList.get(index-1).getKeyword());
    }
    rsList.set(index-1, rsEvent);
  }

  @DeleteMapping("/rs/{index}")
  public void delete(@PathVariable Integer index){
    rsList.remove(index-1);
  }

  @GetMapping("/rs/user")
  public List<User> getUserList(){
    return userList;
  }
}
