package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.domain.News;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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

  @GetMapping("rs/list")
  public List<News> list(){
    return rsList;
  }
}
