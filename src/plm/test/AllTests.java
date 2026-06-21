package plm.test;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ UnitTests.class, IntegrationTests.class })
public class AllTests {
}

