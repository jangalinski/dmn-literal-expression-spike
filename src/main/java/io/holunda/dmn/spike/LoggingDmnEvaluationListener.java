package io.holunda.dmn.spike;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingDmnEvaluationListener implements DmnDecisionEvaluationListener {
  private final Logger log = LoggerFactory.getLogger(LiteralExpressionSpikeApplication.class);

  private final Type type;

  public LoggingDmnEvaluationListener(Type type) {
    this.type = type;
  }

  @Override
  public void notify(DmnDecisionEvaluationEvent evt) {
    log.info("{}: {}", type, evt);
  }

  @Override
  public String toString() {
    return "LoggingDmnEvaluationListener{" +
      "type=" + type +
      '}';
  }

  public enum Type {
    PRE,POST;
  }
}
