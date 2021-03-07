package io.holunda.dmn.spike;

import io.holunda.dmn.spike.dmn.HolundaDmnEngineConfigurationBuilder;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.engine.spring.SpringExpressionManager;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DmnEngineSpringConfiguration {

  @Bean
  public DmnEngine dmnEngine(
    SpringExpressionManager expressionManager
  ) {
    return new HolundaDmnEngineConfigurationBuilder()
      .expressionManager(expressionManager)
      .defaultExpressionLanguage("groovy")
      .buildEngine();
  }

  @Bean
  public SpringExpressionManager springExpressionManager(ApplicationContext applicationContext) {
    return new SpringExpressionManager(applicationContext, null);
  }

}
