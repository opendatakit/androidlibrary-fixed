package org.opendatakit.logging;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.utilities.ODKFileUtils;
import java.io.File;

public class WebLoggerImplTest {

    private WebLoggerImpl logger;
    private final String TEST_APP_NAME = "testApp";

    @Before
    public void setUp() {
        logger = new WebLoggerImpl(TEST_APP_NAME);
        ODKFileUtils.assertDirectoryStructure(TEST_APP_NAME);
    }

    @After
    public void tearDown() {
        File loggingFolder = new File(ODKFileUtils.getLoggingFolder(TEST_APP_NAME));
        loggingFolder.delete();
    }

    @Test
    public void LogMethods_ShouldLogWithoutException() {
        // These methods should log messages without throwing exceptions
        String TEST_TAG = "TestTag";
        logger.a(TEST_TAG, "Assert log message");
        logger.t(TEST_TAG, "Tip log message");
        logger.v(TEST_TAG, "Verbose log message");
        logger.d(TEST_TAG, "Debug log message");
        logger.i(TEST_TAG, "Info log message");
        logger.w(TEST_TAG, "Warning log message");
        logger.e(TEST_TAG, "Error log message");
        logger.s(TEST_TAG, "Success log message");
        //TODO: Complete Test case and update function name
    }

    @Test
    public void Close_ShouldCloseLogFileWithoutException() {
        logger.close();
        //TODO: Complete Test case and update function name
    }

    @Test
    public void StaleFileScan_ShouldNotThrowException() {
        long now = System.currentTimeMillis();
        logger.staleFileScan(now);
        //TODO: Complete Test case and update function name
    }

    @Test
    public void givenWebLogger_whenGetMinimumLogLevel_thenReturnLoggerMinimumLogLevel() {
        assertEquals(WebLoggerIf.VERBOSE, logger.getMinimumSystemLogLevel());
    }

    @Test
    public void givenWebLogger_whenSetMinimumLogLevel_thenChangeLoggerMinimumLogLevel() {
        logger.setMinimumSystemLogLevel(WebLoggerIf.ERROR);
        assertEquals(WebLoggerIf.ERROR, logger.getMinimumSystemLogLevel());
    }

    @Test
    public void PrintStackTrace_ShouldLogStackTrace() {
        Exception testException = new Exception("Test exception");
        logger.printStackTrace(testException);
        //TODO: Complete Test case and update function name
    }
}