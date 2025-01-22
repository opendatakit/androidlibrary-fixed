package org.opendatakit.logging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.utilities.ODKFileUtils;
import static org.junit.Assert.assertTrue;


import java.io.File;

public class WebLoggerFactoryImplTest {

    private WebLoggerFactoryImpl loggerFactory;
    private final String testAppName = "testApp";

    @Before
    public void setUp() {
        loggerFactory = new WebLoggerFactoryImpl();
    }

    @After
    public void tearDown() {
        File loggingFolder = new File(ODKFileUtils.getLoggingFolder(testAppName));
        loggingFolder.delete();
    }

    @Test
    public void WithValidAppName_ShouldReturnWebLoggerImpl() {
        WebLoggerIf logger = loggerFactory.createWebLogger(testAppName);

        assertTrue("Expected WebLoggerImpl instance", logger instanceof WebLoggerImpl);

        File loggingFolder = new File(ODKFileUtils.getLoggingFolder(testAppName));
        assertTrue("Expected logging folder to exist", loggingFolder.exists());
        assertTrue("Expected logging folder to be a directory", loggingFolder.isDirectory());
    }

    @Test
    public void WithNullAppName_ShouldReturnWebLoggerAppNameUnknownImpl() {
        WebLoggerIf logger = loggerFactory.createWebLogger(null);

        assertTrue("Expected WebLoggerAppNameUnknownImpl instance", logger instanceof WebLoggerAppNameUnknownImpl);
    }
}
