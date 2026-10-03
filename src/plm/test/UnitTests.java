package plm.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import plm.test.git.GitSpyTest;
import plm.test.git.GitUtilsTest;
import plm.test.lang.EntityTemplateParserTest;
import plm.test.lang.IndentationTest;
import plm.test.simple.test.AllSimpleExerciseTests;

@Suite
@SelectClasses({AllSimpleExerciseTests.class, GitSpyTest.class, GitUtilsTest.class, EntityTemplateParserTest.class, IndentationTest.class})
public class UnitTests {}