package io.holunda.dmn.spike._test;

import io.holunda.dmn.spike.LiteralExpressionSpikeApplication;
import io.holunda.dmn.spike.LoggingDmnEvaluationListener;
import lombok.Getter;
import org.camunda.bpm.dmn.engine.DmnDecision;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationEvent;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.StandaloneInMemProcessEngineConfiguration;
import org.camunda.bpm.engine.impl.history.HistoryLevel;
import org.camunda.bpm.engine.test.mock.MockExpressionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static io.holunda.dmn.spike.LoggingDmnEvaluationListener.Type.POST;
import static io.holunda.dmn.spike.LoggingDmnEvaluationListener.Type.PRE;

public class TestHelper {

  private static final Logger LOG = LoggerFactory.getLogger(LiteralExpressionSpikeApplication.class);

  public static ProcessEngineConfigurationImpl createProcessEngineConfiguration() {
    var conf = new StandaloneInMemProcessEngineConfiguration();
    conf.setHistoryLevel(HistoryLevel.HISTORY_LEVEL_FULL);
    conf.setDatabaseSchemaUpdate(ProcessEngineConfigurationImpl.DB_SCHEMA_UPDATE_CREATE);
    conf.setJobExecutorActivate(false);
    conf.setExpressionManager(new MockExpressionManager());

    var plugins = Optional.ofNullable(conf.getProcessEnginePlugins()).orElseGet(ArrayList::new);

    plugins.add(new AbstractProcessEnginePlugin(){
      @Override
      public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        var dmnConf = processEngineConfiguration.getDmnEngineConfiguration();
        dmnConf.setDefaultInputEntryExpressionLanguage("groovy");
        dmnConf.setDefaultOutputEntryExpressionLanguage("groovy");
        dmnConf.getCustomPreDecisionEvaluationListeners().add(new LoggingDmnEvaluationListener(PRE));
        dmnConf.getCustomPostDecisionEvaluationListeners().add(new LoggingDmnEvaluationListener(POST));

      }
    });




    return conf;
  }


  public static DmnEngineConfiguration createEngineConfiguration() {
    var conf = (DefaultDmnEngineConfiguration) DefaultDmnEngineConfiguration.createDefaultDmnEngineConfiguration();
    conf.defaultInputEntryExpressionLanguage("groovy");
    conf.defaultOutputEntryExpressionLanguage("groovy");

    return conf;
  }

  private final DmnEngine dmnEngine;

  public TestHelper(DmnEngineRule dmnEngineRule) {
    this.dmnEngine = dmnEngineRule.getDmnEngine();
  }

  public DmnDecision parseDecision(String key, String resource) {
    return dmnEngine.parseDecision(key, ClassLoader.getSystemResourceAsStream(resource));
  }

  public DmnEngine getDmnEngine() {
    return dmnEngine;
  }
}
