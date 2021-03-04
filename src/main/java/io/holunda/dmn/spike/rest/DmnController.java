package io.holunda.dmn.spike.rest;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/dmn")
@RestController
@Transactional
public class DmnController {

  private final DmnEngine dmnEngine;

  public DmnController(DmnEngine dmnEngine) {
    this.dmnEngine = dmnEngine;
  }

  @GetMapping
  public ResponseEntity<String> hello() {
    return ResponseEntity.ok("hello world");
  }

}
