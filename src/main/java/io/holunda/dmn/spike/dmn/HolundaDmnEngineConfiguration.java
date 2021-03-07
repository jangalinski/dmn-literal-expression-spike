package io.holunda.dmn.spike.dmn;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;

public class HolundaDmnEngineConfiguration extends DefaultDmnEngineConfiguration {

  @Override
  public DmnEngine buildEngine() {
    init();
    return new HolundaDmnEngine(this);
  }
}
