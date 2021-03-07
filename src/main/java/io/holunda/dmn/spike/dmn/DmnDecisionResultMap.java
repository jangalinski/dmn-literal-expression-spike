package io.holunda.dmn.spike.dmn;

import org.camunda.bpm.dmn.engine.DmnDecisionResult;
import org.camunda.bpm.dmn.engine.impl.DmnDecisionResultImpl;
import org.camunda.bpm.dmn.engine.impl.DmnEngineLogger;
import org.camunda.bpm.dmn.engine.impl.DmnLogger;

import java.util.Map;

public class DmnDecisionResultMap extends DmnDecisionResultImpl {
  private static final long serialVersionUID = 1L;
  public static final DmnEngineLogger LOG = DmnLogger.ENGINE_LOGGER;

  private final String key;
  private final Map<String, DmnDecisionResult> allResults;


  public DmnDecisionResultMap(String key, Map<String, DmnDecisionResult> allResults) {
    super(allResults.get(key));
    this.key = key;
    this.allResults = allResults;
  }

  public String getKey() {
    return key;
  }

  public Map<String, DmnDecisionResult> getAllResults() {
    return allResults;
  }

  @Override
  public String toString() {
    return "DmnDecisionResultMap{" +
      "key='" + key + '\'' +
      ", allResults=" + allResults +
      '}';
  }
}
