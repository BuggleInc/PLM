package plm.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

import plm.test.integration.ExoTest;
import plm.test.integration.LessonTest;

@Suite
@SelectClasses({ LessonTest.class, ExoTest.class })
public class IntegrationTests {
}
