package io.holunda.dmn.spike._test;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.delegate.DmnDecisionEvaluationEventImpl;
import org.camunda.bpm.dmn.engine.impl.delegate.DmnDecisionTableEvaluationEventImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public enum EvaluationListeners {
  ;

  private static final Logger log = LoggerFactory.getLogger(EvaluationListeners.class);


  public static final DmnDecisionTableEvaluationListener TABLE_EVALUATION = evt -> {
    var e = (DmnDecisionTableEvaluationEventImpl) evt;

    log.info("evaluateTable: {}", asString(e));
  };

  public static final DmnDecisionEvaluationListener EVALUATION = evt -> {
    var e = (DmnDecisionEvaluationEventImpl) evt;
    log.info("evaluateDecision: {}", asString(e));
  };

  private static String asString(DmnDecisionEvaluationEvent evt) {
    return evt.toString();
  }

  private static String asString(DmnDecisionTableEvaluationEvent evt) {
    return evt.toString();
  }
}
