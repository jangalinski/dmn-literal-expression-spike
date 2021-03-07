package io.holunda.dmn.spike.dmn;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngine;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionImpl;
import org.camunda.bpm.engine.variable.context.VariableContext;

import static org.camunda.commons.utils.EnsureUtil.ensureNotNull;

public class HolundaDmnEngine extends DefaultDmnEngine {

  public HolundaDmnEngine(DefaultDmnEngineConfiguration dmnEngineConfiguration) {
    super(dmnEngineConfiguration);
  }

  @Override
  public DmnDecisionResult evaluateDecision(DmnDecision decision, VariableContext variableContext) {
    ensureNotNull("decision", decision);
    ensureNotNull("variableContext", variableContext);

    if (decision instanceof DmnDecisionImpl) {
      return new HolundaDmnDecisionContext(dmnEngineConfiguration).evaluateDecision(decision, variableContext);
    } else {
      throw LOG.decisionTypeNotSupported(decision);
    }
  }

}
