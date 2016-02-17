package suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import unit.git.GitUtilsTest;
import unit.exercise.ExerciseFactoryTest;
import unit.exercise.ExerciseRunnerTest;

@RunWith(Suite.class)
@SuiteClasses({ GitUtilsTest.class, ExerciseFactoryTest.class, ExerciseRunnerTest.class })
public class UnitTests {
}