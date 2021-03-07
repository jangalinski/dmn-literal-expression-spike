package io.holunda.dmn.spike.dmn;

import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionLogicEvaluationEvent;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnDecisionContext;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.evaluation.DmnDecisionLogicEvaluationHandler;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.context.VariableContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HolundaDmnDecisionContext extends DefaultDmnDecisionContext {

  public HolundaDmnDecisionContext(DefaultDmnEngineConfiguration configuration) {
    super(configuration);
  }

  @Override
  public DmnDecisionResult evaluateDecision(DmnDecision decision, VariableContext variableContext) {
    var allResults = new HashMap<String, DmnDecisionResult>();

    if (decision.getKey() == null) {
      throw LOG.unableToFindAnyDecisionTable();
    }
    VariableMap variableMap = buildVariableMapFromVariableContext(variableContext);

    List<DmnDecision> requiredDecisions = new ArrayList<DmnDecision>();
    buildDecisionTree(decision, requiredDecisions);

    List<DmnDecisionLogicEvaluationEvent> evaluatedEvents = new ArrayList<DmnDecisionLogicEvaluationEvent>();

    for (DmnDecision evaluateDecision : requiredDecisions) {
      DmnDecisionLogicEvaluationHandler handler = getDecisionEvaluationHandler(evaluateDecision);
      DmnDecisionLogicEvaluationEvent evaluatedEvent = handler.evaluate(evaluateDecision, variableMap.asVariableContext());
      evaluatedEvents.add(evaluatedEvent);

      final var evaluatedResult = handler.generateDecisionResult(evaluatedEvent);
      allResults.put(evaluateDecision.getKey(), evaluatedResult);

      if (decision != evaluateDecision) {
        addResultToVariableContext(evaluatedResult, variableMap, evaluateDecision);
      }
    }

    generateDecisionEvaluationEvent(evaluatedEvents);
    return new DmnDecisionResultMap(decision.getKey(), allResults);
  }

}
