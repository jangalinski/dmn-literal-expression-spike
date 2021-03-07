package io.holunda.dmn.spike;

import io.holunda.dmn.spike._test.EvaluationListeners;
import io.holunda.dmn.spike.dmn.DmnDecisionResultMap;
import io.holunda.dmn.spike.dmn.HolundaDmnEngineConfigurationBuilder;
import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.impl.DefaultDmnEngineConfiguration;
import org.camunda.bpm.dmn.engine.test.DmnEngineRule;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.model.dmn.Dmn;
import org.camunda.bpm.model.dmn.DmnModelInstance;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import static org.assertj.core.api.Assertions.assertThat;

public class DmnOnlyTest {

  private final Logger log = LoggerFactory.getLogger(DmnOnlyTest.class);


  @Rule
  public final DmnEngineRule dmn = new DmnEngineRule(new HolundaDmnEngineConfigurationBuilder()
    .addCustomDecisionEvaluationListener(EvaluationListeners.EVALUATION)
    .addCustomDecisionTableEvaluationListener(EvaluationListeners.TABLE_EVALUATION)
    .build());

  @Test
  public void eval_literalexpression_and_store_intermediate_results() {
    var model = load("dmn/lit-spike.dmn");

    var result = dmn.getDmnEngine().evaluateDecision("expr_e", model, Variables.createVariables());

    log.info("eval: {}", result);

    ((DmnDecisionResultMap)result).getAllResults().entrySet()
      .forEach(e -> log.info("expression:{}, result:{}", e.getKey(), e.getValue().getSingleResult().getEntryMap()));
  }

  @Test
  public void eval_decisonTable_and_store_intermediate_results() {
    var conf = (DefaultDmnEngineConfiguration) dmn.getDmnEngine().getConfiguration();
    assertThat(conf.getCustomPostDecisionEvaluationListeners()).isNotEmpty();
    assertThat(conf.getCustomPostDecisionTableEvaluationListeners()).isNotEmpty();

    var model = load("dmn/dmn-spike.dmn");


    var result = dmn.getDmnEngine().evaluateDecision("dec_2", model, Variables.createVariables());

    log.info("eval: {}", result);
  }

  private DmnModelInstance load(String path) {
    try {

      var res = new ClassPathResource(path);
      return Dmn.readModelFromStream(res.getInputStream());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
  public void diff() {
    assertThat("DmnDecisionTableEvaluationEventImpl{ key=dec_1, name=Decision 1, decisionLogic=DmnDecisionTableImpl{ hitPolicyHandler=UniqueHitPolicyHandler{}, inputs=[DmnDecisionTableInputImpl{id='Input_1', name='null', expression=DmnExpressionImpl{id='InputExpression_1', name='null', typeDefinition=DmnTypeDefinitionImpl{typeName='string'}, expressionLanguage='null', expression='null'}, inputVariable='null'}], outputs=[DmnDecisionTableOutputImpl{id='Output_1', name='null', outputName='Foo', typeDefinition=DmnTypeDefinitionImpl{typeName='integer'}}], rules=[DmnDecisionTableRuleImpl{id='DecisionRule_19b2165', name='null', conditions=[DmnExpressionImpl{id='UnaryTests_13neu1g', name='null', typeDefinition=null, expressionLanguage='null', expression='null'}], conclusions=[DmnExpressionImpl{id='LiteralExpression_030puvn', name='null', typeDefinition=null, expressionLanguage='null', expression='5'}]}]}, inputs=[DmnEvaluatedInputImpl{id='Input_1', name='null', inputVariable='cellInput', value=Untyped 'null' value}], matchingRules=[DmnEvaluatedDecisionRuleImpl{id='DecisionRule_19b2165', outputEntries={Foo=DmnEvaluatedOutputImpl{id='Output_1', name='null', outputName='Foo', value=Value '5' of type 'PrimitiveValueType[integer]', isTransient=false}}}], collectResultName='null', collectResultValue=null, executedDecisionElements=2}")
      .isEqualTo("DmnDecisionTableEvaluationEventImpl{ key=dec_1, name=Decision 1, decisionLogic=DmnDecisionTableImpl{ hitPolicyHandler=UniqueHitPolicyHandler{}, inputs=[DmnDecisionTableInputImpl{id='Input_1', name='null', expression=DmnExpressionImpl{id='InputExpression_1', name='null', typeDefinition=DmnTypeDefinitionImpl{typeName='string'}, expressionLanguage='null', expression='null'}, inputVariable='null'}], outputs=[DmnDecisionTableOutputImpl{id='Output_1', name='null', outputName='Foo', typeDefinition=DmnTypeDefinitionImpl{typeName='integer'}}], rules=[DmnDecisionTableRuleImpl{id='DecisionRule_19b2165', name='null', conditions=[DmnExpressionImpl{id='UnaryTests_13neu1g', name='null', typeDefinition=null, expressionLanguage='null', expression='null'}], conclusions=[DmnExpressionImpl{id='LiteralExpression_030puvn', name='null', typeDefinition=null, expressionLanguage='null', expression='5'}]}]}, inputs=[DmnEvaluatedInputImpl{id='Input_1', name='null', inputVariable='cellInput', value=Untyped 'null' value}], matchingRules=[DmnEvaluatedDecisionRuleImpl{id='DecisionRule_19b2165', outputEntries={Foo=DmnEvaluatedOutputImpl{id='Output_1', name='null', outputName='Foo', value=Value '5' of type 'PrimitiveValueType[integer]', isTransient=false}}}], collectResultName='null', collectResultValue=null, executedDecisionElements=2}");
  }
}
