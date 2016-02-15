package plm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import plm.test.git.GitUtilsTest;
import plm.test.simple.test.AllSimpleExerciseTests;

@RunWith(Suite.class)
@SuiteClasses({ AllSimpleExerciseTests.class, GitUtilsTest.class })
public class UnitTests {
}