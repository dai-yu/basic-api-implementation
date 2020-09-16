package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.Rseven;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<Rseven> rsList = init();

  private List<Rseven> init() {
    List<Rseven> rsList=new ArrayList<>();
    rsList.add(new Rseven("第一件事","无标签"));
    rsList.add(new Rseven("第二件事","无标签"));
    rsList.add(new Rseven("第三件事","无标签"));
    return rsList;
  }

  @GetMapping("/rs/list")
  public List<Rseven> list(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start==null || end==null){
      return rsList;
    }
    return rsList.subList(start-1,end);
  }


  @GetMapping("/rs/{index}")
  public Rseven oneNews(@PathVariable int index){
    return rsList.get(index-1);
  }

  @PostMapping("/rs/add")
  public void add(@RequestBody Rseven rseven){
    rsList.add(rseven);
  }

  @PutMapping("/rs/modify")
  public void modify(@RequestParam Integer index,@RequestBody Rseven rseven){
    if(rseven.getName()==""){
      rseven.setName(rsList.get(index-1).getName());
    }
    if(rseven.getType()==""){
      rseven.setType(rsList.get(index-1).getType());
    }
    rsList.set(index-1, rseven);
  }

  @DeleteMapping("/rs/{index}")
  public void delete(@PathVariable Integer index){
    rsList.remove(index-1);
  }
}
