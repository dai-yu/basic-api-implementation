package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.News;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
  private List<News> rsList = init();

  private List<News> init() {
    List<News> rsList=new ArrayList<>();
    rsList.add(new News("第一件事","无标签"));
    rsList.add(new News("第二件事","无标签"));
    rsList.add(new News("第三件事","无标签"));
    return rsList;
  }

  @GetMapping("/rs/list")
  public List<News> list(@RequestParam(required = false)Integer start, @RequestParam(required = false)Integer end){
    if(start==null || end==null){
      return rsList;
    }
    return rsList.subList(start-1,end);
  }


  @GetMapping("/rs/{index}")
  public News oneNews(@PathVariable int index){
    return rsList.get(index-1);
  }

  @PostMapping("/rs/add")
  public void add(@RequestBody News news){
    rsList.add(news);
  }

  @PutMapping("/rs/modify")
  public void modify(@RequestParam Integer index,@RequestBody News news){
    if(news.getName()==""){
      news.setName(rsList.get(index-1).getName());
    }
    if(news.getType()==""){
      news.setType(rsList.get(index-1).getType());
    }
    rsList.set(index-1,news);
  }
}
