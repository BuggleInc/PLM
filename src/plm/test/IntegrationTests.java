package plm.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import plm.test.integration.ExoTest;
import plm.test.integration.LessonTest;

@RunWith(Suite.class)
@SuiteClasses({ ExoTest.class, LessonTest.class})
public class IntegrationTests {
}
