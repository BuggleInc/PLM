package plm.test.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import plm.test.unit.exercise.ExerciseFactoryTest;
import plm.test.unit.exercise.ExerciseRunnerTest;
import plm.test.unit.git.GitUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({ GitUtilsTest.class, ExerciseFactoryTest.class, ExerciseRunnerTest.class })
public class UnitTests {
}