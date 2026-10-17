package plm.test;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import plm.test.content.ExoTestCLang;
import plm.test.content.ExoTestJavaLang;
import plm.test.content.ExoTestPythonLang;
import plm.test.content.ExoTestScalaLang;
import plm.test.content.GatedExercisesTest;
import plm.test.content.LessonTest;
import plm.test.content.MissionMarkupTest;
import plm.test.content.TranslationsTest;

/** Tests the pedagogical material (lessons, exercises, mission texts and their translations) rather than the PLM code. */
@Suite
// Only the test methods annotated @Execution(CONCURRENT) run in parallel; the classes run one after the other
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.enabled", value = "true")
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.config.strategy", value = "fixed")
@ConfigurationParameter(key = "junit.jupiter.execution.parallel.config.fixed.parallelism", value = "4")
@SelectClasses({LessonTest.class, ExoTestJavaLang.class, ExoTestScalaLang.class, ExoTestPythonLang.class, ExoTestCLang.class, MissionMarkupTest.class,
                TranslationsTest.class, GatedExercisesTest.class})
public class ContentTests {}
