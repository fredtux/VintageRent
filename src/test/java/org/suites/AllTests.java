package org.suites;

import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages({"org.actions", "org.models", "org.gui.custom"})
public class AllTests {
}
