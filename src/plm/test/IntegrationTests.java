package plm.test;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import plm.test.integration.ExoTestCLang;
import plm.test.integration.ExoTestJavaLang;
import plm.test.integration.ExoTestPythonLang;
import plm.test.integration.ExoTestScalaLang;
import plm.test.integration.LessonTest;
import plm.test.integration.MethodsDogHouseSourceCheckTest;

@Suite
// Only the test methods annotated @Execution(CONCURRENT) run in parallel; the classes run one after the other
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.enabled", value = "true")
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.config.strategy", value = "fixed")
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.config.fixed.parallelism", value = "4")
@SelectClasses(
    {LessonTest.class, ExoTestJavaLang.class, ExoTestScalaLang.class, ExoTestPythonLang.class, ExoTestCLang.class, MethodsDogHouseSourceCheckTest.class})
public class IntegrationTests {}
