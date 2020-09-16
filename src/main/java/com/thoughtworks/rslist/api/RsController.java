package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.RsEvent;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<RsEvent> rsList = init();

  private List<RsEvent> init() {
    List<RsEvent> rsList=new ArrayList<>();
    rsList.add(new RsEvent("第一件事","无标签"));
    rsList.add(new RsEvent("第二件事","无标签"));
    rsList.add(new RsEvent("第三件事","无标签"));
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

  @PostMapping("/rs/add")
  public void add(@RequestBody RsEvent rsEvent){
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
}
