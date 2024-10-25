package org.opendatakit.logging;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.utilities.ODKFileUtils;

import java.io.File;

public class WebLoggerFactoryImplTest {

    private WebLoggerFactoryImpl loggerFactory;
    private String testAppName = "testApp";

    @Before
    public void setUp() {
        loggerFactory = new WebLoggerFactoryImpl();
    }

    @After
    public void tearDown() {
        File loggingFolder = new File(ODKFileUtils.getLoggingFolder(testAppName));
        deleteDirectory(loggingFolder);
    }

    @Test
    public void WithValidAppName_ShouldReturnWebLoggerImpl() {
        WebLoggerIf logger = loggerFactory.createWebLogger(testAppName);

        org.junit.Assert.assertTrue("Expected WebLoggerImpl instance", logger instanceof WebLoggerImpl);

        File loggingFolder = new File(ODKFileUtils.getLoggingFolder(testAppName));
        org.junit.Assert.assertTrue("Expected logging folder to exist", loggingFolder.exists());
        org.junit.Assert.assertTrue("Expected logging folder to be a directory", loggingFolder.isDirectory());
    }

    @Test
    public void WithNullAppName_ShouldReturnWebLoggerAppNameUnknownImpl() {
        WebLoggerIf logger = loggerFactory.createWebLogger(null);

        org.junit.Assert.assertTrue("Expected WebLoggerAppNameUnknownImpl instance", logger instanceof WebLoggerAppNameUnknownImpl);
    }

    private void deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        directoryToBeDeleted.delete();
    }
}
