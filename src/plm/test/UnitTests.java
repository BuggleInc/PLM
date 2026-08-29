package plm.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import plm.test.git.GitSpyTest;
import plm.test.git.GitUtilsTest;
import plm.test.lang.LangScalaTemplateTest;
import plm.test.simple.test.AllSimpleExerciseTests;

@Suite
@SelectClasses({AllSimpleExerciseTests.class, GitSpyTest.class, GitUtilsTest.class, LangScalaTemplateTest.class})
public class UnitTests {}