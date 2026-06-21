package plm.test.simple.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ JavaSimpleExerciseTest.class, /*ScalaSimpleExerciseTest.class,*/ PythonSimpleExerciseTest.class })
public class AllSimpleExerciseTests {
}
