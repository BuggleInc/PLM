package plm.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import plm.core.ValueSerializerTest;
import plm.test.git.GitSpyTest;
import plm.test.git.GitUtilsTest;
import plm.test.lang.EntityTemplateParserTest;
import plm.test.lang.IndentationTest;
import plm.test.session.SessionDBTest;
import plm.test.simple.test.AllSimpleExerciseTests;
import plm.test.universe.ApiGatingTest;
import plm.test.universe.LightBotProgramTest;
import plm.universe.bat.ValueFormatterTest;

@Suite
@SelectClasses({AllSimpleExerciseTests.class, GitSpyTest.class, GitUtilsTest.class, EntityTemplateParserTest.class, IndentationTest.class,
                ValueFormatterTest.class, ValueSerializerTest.class, ApiGatingTest.class, SessionDBTest.class, LightBotProgramTest.class})
public class UnitTests {}