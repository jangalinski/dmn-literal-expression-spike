package io.holunda.dmn.spike.dmn;


import org.apache.commons.lang3.builder.Builder;
import org.camunda.bpm.dmn.engine.DmnEngine;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionEvaluationListener;
import org.camunda.bpm.dmn.engine.delegate.DmnDecisionTableEvaluationListener;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.impl.spi.el.DmnScriptEngineResolver;
import org.camunda.bpm.dmn.engine.impl.spi.transform.DmnTransformer;
import org.camunda.bpm.dmn.feel.impl.scala.function.FeelCustomFunctionProvider;
import org.camunda.bpm.engine.impl.dmn.el.ProcessEngineElProvider;
import org.camunda.bpm.engine.impl.dmn.transformer.DecisionDefinitionHandler;
import org.camunda.bpm.engine.impl.dmn.transformer.DecisionRequirementsDefinitionTransformHandler;
import org.camunda.bpm.engine.impl.el.ExpressionManager;
import org.camunda.bpm.engine.impl.history.producer.DefaultDmnHistoryEventProducer;
import org.camunda.bpm.engine.impl.history.producer.DmnHistoryEventProducer;
import org.camunda.bpm.engine.impl.scripting.engine.*;
import org.camunda.bpm.engine.test.mock.MocksResolverFactory;
import org.camunda.bpm.model.dmn.instance.Decision;
import org.camunda.bpm.model.dmn.instance.Definitions;

import javax.script.ScriptEngine;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HolundaDmnEngineConfigurationBuilder implements Builder<HolundaDmnEngineConfiguration> {

  private List<ResolverFactory> resolverFactories = new ArrayList<>();

  protected ExpressionManager expressionManager;
  protected DmnHistoryEventProducer dmnHistoryEventProducer = new DefaultDmnHistoryEventProducer();
  protected boolean enableFeelLegacyBehavior = false;
  protected String defaultInputEntryExpressionLanguage;
  protected String defaultOutputEntryExpressionLanguage;
  protected String defaultLiteralExpressionLanguage;

  protected List<FeelCustomFunctionProvider> feelCustomFunctionProviders = new ArrayList<>();
  protected DmnScriptEngineResolver scriptEngineResolver;
  protected List<DmnDecisionTableEvaluationListener> customPostDecisionTableEvaluationListeners = new ArrayList<>();
  protected List<DmnDecisionEvaluationListener> customPostDecisionEvaluationListeners = new ArrayList<>();


  public HolundaDmnEngineConfigurationBuilder() {
    resolverFactories.addAll(List.of(
      new MocksResolverFactory(),
      new VariableScopeResolverFactory(),
      new BeansResolverFactory()
    ));
  }

  public HolundaDmnEngineConfigurationBuilder expressionManager(ExpressionManager expressionManager) {
    this.expressionManager = expressionManager;

    return this;
  }

  public HolundaDmnEngineConfigurationBuilder historyEventProducer(DmnHistoryEventProducer historyEventProducer) {
    this.dmnHistoryEventProducer = historyEventProducer;
    return this;
  }

  public HolundaDmnEngineConfigurationBuilder defaultExpressionLanguage(String language) {
    return defaultLiteralExpressionLanguage(language)
      .defaultInputEntryExpressionLanguage(language)
      .defaultOutputEntryExpressionLanguage(language);
  }

  public HolundaDmnEngineConfigurationBuilder defaultLiteralExpressionLanguage(String language) {
    this.defaultLiteralExpressionLanguage = language;
    return this;
  }

  public HolundaDmnEngineConfigurationBuilder defaultInputEntryExpressionLanguage(String language) {
    this.defaultInputEntryExpressionLanguage = language;
    return this;
  }

  public HolundaDmnEngineConfigurationBuilder defaultOutputEntryExpressionLanguage(String language) {
    this.defaultOutputEntryExpressionLanguage = language;
    return this;
  }

  public HolundaDmnEngineConfigurationBuilder enableFeelLegacyBehavior(boolean dmnFeelEnableLegacyBehavior) {
    this.enableFeelLegacyBehavior = dmnFeelEnableLegacyBehavior;
    return this;
  }

//  protected List<DmnDecisionTableEvaluationListener> customPreDecisionTableEvaluationListeners = new ArrayList<>();
//  protected List<DmnDecisionTableEvaluationListener> customPostDecisionTableEvaluationListeners = new ArrayList<>();
//  protected List<DmnDecisionEvaluationListener> customPreDecisionEvaluationListeners = new ArrayList<>();
//  protected List<DmnDecisionEvaluationListener> customPostDecisionEvaluationListeners = new ArrayList<>();

  public HolundaDmnEngineConfigurationBuilder addCustomDecisionTableEvaluationListener(DmnDecisionTableEvaluationListener listener) {
    this.customPostDecisionTableEvaluationListeners.add(listener);
    return this;
  }

  public HolundaDmnEngineConfigurationBuilder addCustomDecisionEvaluationListener(DmnDecisionEvaluationListener listener) {
    this.customPostDecisionEvaluationListeners.add(listener);
    return this;
  }

  @Override
  public HolundaDmnEngineConfiguration build() {
    var dmnEngineConfiguration = new HolundaDmnEngineConfiguration();
    addElementTransformHandlers(dmnEngineConfiguration);
    dmnEngineConfiguration.setScriptEngineResolver(scriptingEngines());
    dmnEngineConfiguration.setElProvider(new ProcessEngineElProvider(expressionManager));
    dmnEngineConfiguration.setFeelCustomFunctionProviders(feelCustomFunctionProviders);

    Optional.ofNullable(defaultLiteralExpressionLanguage).ifPresent(dmnEngineConfiguration::setDefaultLiteralExpressionLanguage);
    Optional.ofNullable(defaultInputEntryExpressionLanguage).ifPresent(dmnEngineConfiguration::setDefaultInputEntryExpressionLanguage);
    Optional.ofNullable(defaultOutputEntryExpressionLanguage).ifPresent(dmnEngineConfiguration::setDefaultOutputEntryExpressionLanguage);

    dmnEngineConfiguration.getCustomPostDecisionEvaluationListeners()
      .addAll(customPostDecisionEvaluationListeners);

    dmnEngineConfiguration.getCustomPostDecisionTableEvaluationListeners()
      .addAll(customPostDecisionTableEvaluationListeners);

    return dmnEngineConfiguration;
  }

  public DmnEngine buildEngine() {
    return build().buildEngine();
  }

  private ScriptingEngines scriptingEngines() {
    return new ScriptingEngines(new ScriptBindingsFactory(resolverFactories)) {
      @Override
      public ScriptEngine getScriptEngineForLanguage(String language) {
        return getGlobalScriptEngine(language);
      }
    };
  }

  private static void addElementTransformHandlers(DefaultDmnEngineConfiguration dmnEngineConfiguration) {
    DmnTransformer dmnTransformer = dmnEngineConfiguration.getTransformer();
    dmnTransformer.getElementTransformHandlerRegistry().addHandler(Definitions.class, new DecisionRequirementsDefinitionTransformHandler());
    dmnTransformer.getElementTransformHandlerRegistry().addHandler(Decision.class, new DecisionDefinitionHandler());
  }
}
