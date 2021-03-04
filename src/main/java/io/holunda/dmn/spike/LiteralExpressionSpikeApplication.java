package io.holunda.dmn.spike;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.application.ProcessApplication;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.ClassPathResource;

@SpringBootApplication
public class LiteralExpressionSpikeApplication implements ApplicationRunner {

  private final Logger log = LoggerFactory.getLogger(LiteralExpressionSpikeApplication.class);

  public static void main(String[] args) {
    SpringApplication.run(LiteralExpressionSpikeApplication.class, args);
  }

  @Autowired
  private DmnEngine dmnEngine;

  @Override
  public void run(ApplicationArguments args) throws Exception {
    log.info("running application ...");
    log.info("engine: {}", dmnEngine);

    var res = new ClassPathResource("dmn/lit-spike.dmn");
    var dmn = Dmn.readModelFromStream(res.getInputStream());

    log.info("model: {}", Dmn.convertToString(dmn));

    log.info("eval: {}", dmnEngine.evaluateDecision("lit_2", dmn, Variables.createVariables()));
  }
}
