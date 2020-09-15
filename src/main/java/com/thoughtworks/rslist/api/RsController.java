package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<String> rsList = Arrays.asList("第一件事","第二件事","第三件事");


  @GetMapping("rs/list")
  public String list(){
    return rsList.toString();
  }
}
