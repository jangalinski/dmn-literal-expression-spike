package io.holunda.dmn.spike;

import io.holunda.dmn.spike._test.TestHelper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.test.assertions.ProcessEngineTests;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;

@Deployment(resources = "dmn/lit-spike.dmn")
public class DmnLitSpikeTest {

  private final Logger log = LoggerFactory.getLogger(DmnLitSpikeTest.class);


  @Rule
  public final ProcessEngineRule camunda = new ProcessEngineRule(TestHelper.createProcessEngineConfiguration().buildProcessEngine());

  @Before
  public void setUp() {
    ProcessEngineTests.init(camunda.getProcessEngine());

    Assertions.assertThat( BpmnAwareTests.historyService().createHistoricDecisionInstanceQuery().list()).isEmpty();
  }

  @Test
  public void name() {
    var conf = (ProcessEngineConfigurationImpl)camunda.getProcessEngine().getProcessEngineConfiguration();
    var dmnConf = conf.getDmnEngineConfiguration();
    var dmnEngine = conf.getDmnEngine();

    log.info(dmnConf.toString());
  }

  @Test
  public void test() {
    var decisionService = BpmnAwareTests.decisionService();
    var dmnConf = camunda.getProcessEngineConfiguration().getDmnEngineConfiguration();


    var result = BpmnAwareTests.decisionService().evaluateDecisionByKey("lit_2")
      .evaluate();

    log.info("result: {}", result);

    var dec = BpmnAwareTests.historyService().createHistoricDecisionInstanceQuery().list();
    var decIn = BpmnAwareTests.historyService().createHistoricVariableInstanceQuery().list();
    log.info(dec.toString());
    log.info(decIn.toString());


  }
}
