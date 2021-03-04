package io.holunda.dmn.spike;

import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.DmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.camunda.bpm.engine.impl.dmn.configuration.DmnEngineConfigurationBuilder;
import org.camunda.bpm.engine.impl.dmn.el.ProcessEngineElProvider;
import org.camunda.bpm.engine.impl.dmn.transformer.DecisionDefinitionHandler;
import org.camunda.bpm.engine.impl.dmn.transformer.DecisionRequirementsDefinitionTransformHandler;
import org.camunda.bpm.engine.impl.history.parser.HistoryDecisionEvaluationListener;
import org.camunda.bpm.engine.impl.history.producer.DefaultDmnHistoryEventProducer;
import org.camunda.bpm.engine.impl.metrics.dmn.MetricsDecisionEvaluationListener;
import org.camunda.bpm.engine.impl.scripting.engine.BeansResolverFactory;
import org.camunda.bpm.engine.impl.scripting.engine.ScriptBindingsFactory;
import org.camunda.bpm.engine.impl.scripting.engine.ScriptingEngines;
import org.camunda.bpm.engine.impl.scripting.engine.VariableScopeResolverFactory;
import org.camunda.bpm.engine.spring.SpringExpressionManager;
import org.camunda.bpm.engine.test.mock.MocksResolverFactory;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.Definitions;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.camunda.bpm.engine.impl.util.EnsureUtil.ensureNotNull;

@Configuration
public class DmnEngineSpringConfiguration {

  @Bean
  public DmnEngine dmnEngine(DmnEngineConfiguration dmnEngineConfiguration) {
    return dmnEngineConfiguration.buildEngine();
  }

  @Bean
  public DefaultDmnEngineConfiguration dmnEngineConfiguration(
    DmnEngineConfigurationBuilder builder,
    SpringExpressionManager expressionManager,
    ScriptingEngines scriptingEngines
  ) {

    var conf = builder
      .dmnHistoryEventProducer(new DefaultDmnHistoryEventProducer())
      .expressionManager(expressionManager)
      .scriptEngineResolver(scriptingEngines)
      .enableFeelLegacyBehavior(true)
      .feelCustomFunctionProviders(new ArrayList<>())

      .build()
      ;
    conf.setDefaultOutputEntryExpressionLanguage("groovy");
    conf.setDefaultOutputEntryExpressionLanguage("groovy");
    return conf;
  }

  @Bean
  public SpringExpressionManager springExpressionManager(ApplicationContext applicationContext) {
    return new SpringExpressionManager(applicationContext, new HashMap<>());
  }

  @Bean
  public DmnEngineConfigurationBuilder dmnEngineConfigurationBuilder() {
    return new DmnEngineConfigurationBuilder(new DefaultDmnEngineConfiguration()) {
      @Override
      public DefaultDmnEngineConfiguration build() {


//        HistoryDecisionEvaluationListener historyDecisionEvaluationListener = new HistoryDecisionEvaluationListener(dmnHistoryEventProducer);
//
//        List<DmnDecisionEvaluationListener> customPostDecisionEvaluationListeners = dmnEngineConfiguration
//          .getCustomPostDecisionEvaluationListeners();
//        customPostDecisionEvaluationListeners.add(new MetricsDecisionEvaluationListener());
//        customPostDecisionEvaluationListeners.add(historyDecisionEvaluationListener);
//
//        List<DmnDecisionEvaluationListener> decisionEvaluationListeners = createCustomPostDecisionEvaluationListeners();
//        dmnEngineConfiguration.setCustomPostDecisionEvaluationListeners(decisionEvaluationListeners);

        // override the decision table handler
        DmnTransformer dmnTransformer = dmnEngineConfiguration.getTransformer();
        dmnTransformer.getElementTransformHandlerRegistry().addHandler(Definitions.class, new DecisionRequirementsDefinitionTransformHandler());
        dmnTransformer.getElementTransformHandlerRegistry().addHandler(Decision.class, new DecisionDefinitionHandler());

        // do not override the script engine resolver if set
        if (dmnEngineConfiguration.getScriptEngineResolver() == null) {
          ensureNotNull("scriptEngineResolver", scriptEngineResolver);

          dmnEngineConfiguration.setScriptEngineResolver(scriptEngineResolver);
        }

        // do not override the el provider if set
        if (dmnEngineConfiguration.getElProvider() == null) {
          ensureNotNull("expressionManager", expressionManager);

          ProcessEngineElProvider elProvider = new ProcessEngineElProvider(expressionManager);
          dmnEngineConfiguration.setElProvider(elProvider);
        }

        if (dmnEngineConfiguration.getFeelCustomFunctionProviders() == null) {
          dmnEngineConfiguration.setFeelCustomFunctionProviders(feelCustomFunctionProviders);
        }

        return dmnEngineConfiguration;
      }
    };
  }

  @Bean
  public ScriptingEngines scriptingEngines() {
    var resolverFactories = List.of(
      new MocksResolverFactory(),
      new VariableScopeResolverFactory(),
      new BeansResolverFactory()
    );

    var scriptingEngines = new ScriptingEngines(new ScriptBindingsFactory(resolverFactories)){
      @Override
      public ScriptEngine getScriptEngineForLanguage(String language) {
        return getGlobalScriptEngine(language);
      }
    };


    return scriptingEngines;
  }
}
