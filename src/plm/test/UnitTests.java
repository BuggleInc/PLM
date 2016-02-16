package plm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import plm.test.git.GitUtilsTest;

@RunWith(Suite.class)
@SuiteClasses({ GitUtilsTest.class })
public class UnitTests {
}