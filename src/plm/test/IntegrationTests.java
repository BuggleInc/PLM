package plm.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import plm.test.integration.ExoTestCLang;
import plm.test.integration.ExoTestJavaLang;
import plm.test.integration.ExoTestPythonLang;
import plm.test.integration.ExoTestScalaLang;
import plm.test.integration.LessonTest;

@Suite
@SelectClasses({LessonTest.class, ExoTestJavaLang.class, ExoTestScalaLang.class, ExoTestPythonLang.class, ExoTestCLang.class})
public class IntegrationTests {}
