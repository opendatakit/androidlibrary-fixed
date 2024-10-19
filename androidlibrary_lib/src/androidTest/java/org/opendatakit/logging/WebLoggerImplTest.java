package org.opendatakit.logging;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.utilities.ODKFileUtils;

import java.io.File;
import java.io.IOException;

public class WebLoggerImplTest {

    private WebLoggerImpl logger;
    private String testAppName = "testApp";

    @Before
    public void setUp() throws IOException {
        logger = new WebLoggerImpl(testAppName);

        ODKFileUtils.assertDirectoryStructure(testAppName);
    }

    @After
    public void tearDown() {
        File loggingFolder = new File(ODKFileUtils.getLoggingFolder(testAppName));
        deleteDirectory(loggingFolder);
    }

    @Test
    public void LogMethods_ShouldLogWithoutException() {
        // These methods should log messages without throwing exceptions
        logger.a("TestTag", "Assert log message");
        logger.t("TestTag", "Tip log message");
        logger.v("TestTag", "Verbose log message");
        logger.d("TestTag", "Debug log message");
        logger.i("TestTag", "Info log message");
        logger.w("TestTag", "Warning log message");
        logger.e("TestTag", "Error log message");
        logger.s("TestTag", "Success log message");
    }

    @Test
    public void Close_ShouldCloseLogFileWithoutException() {
        logger.close();
    }

    @Test
    public void StaleFileScan_ShouldNotThrowException() {
        long now = System.currentTimeMillis();
        logger.staleFileScan(now);
    }

    @Test
    public void SetAndGetMinimumLogLevel() {
        logger.setMinimumSystemLogLevel(WebLoggerIf.DEBUG);
        assertEquals(WebLoggerIf.DEBUG, logger.getMinimumSystemLogLevel());

        logger.setMinimumSystemLogLevel(WebLoggerIf.ERROR);
        assertEquals(WebLoggerIf.ERROR, logger.getMinimumSystemLogLevel());
    }

    @Test
    public void PrintStackTrace_ShouldLogStackTrace() {
        Exception testException = new Exception("Test exception");
        logger.printStackTrace(testException);
    }

    @Test
    public void LogFileFlush_ShouldNotThrowException() throws IOException {
        logger.d("TestTag", "Debug log message");
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