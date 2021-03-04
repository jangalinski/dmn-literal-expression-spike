package io.holunda.dmn.spike;

import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.engine.impl.dmn.configuration.DmnEngineConfigurationBuilder;
import org.camunda.bpm.engine.impl.history.parser.HistoryDecisionEvaluationListener;
import org.camunda.bpm.engine.impl.metrics.dmn.MetricsDecisionEvaluationListener;

import java.util.List;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;

public class SpringBootDmnConfigurationBuilder {

//  createCustomPostDecisionEvaluationListeners() {
//    ensureNotNull("dmnHistoryEventProducer", dmnHistoryEventProducer);
//    // note that the history level may be null - see CAM-5165
//
//    HistoryDecisionEvaluationListener historyDecisionEvaluationListener = new HistoryDecisionEvaluationListener(dmnHistoryEventProducer);
//
//    List<DmnDecisionEvaluationListener> customPostDecisionEvaluationListeners = dmnEngineConfiguration
//      .getCustomPostDecisionEvaluationListeners();
//    customPostDecisionEvaluationListeners.add(new MetricsDecisionEvaluationListener());
//    customPostDecisionEvaluationListeners.add(historyDecisionEvaluationListener);
//
//    return customPostDecisionEvaluationListeners;
//  }
//
//  public DmnEngineConfigurationBuilder enableFeelLegacyBehavior(boolean dmnFeelEnableLegacyBehavior) {
//    dmnEngineConfiguration
//      .enableFeelLegacyBehavior(dmnFeelEnableLegacyBehavior);
//    return this;
//  }

  public DefaultDmnEngineConfiguration build() {

    return null;
  }
}
