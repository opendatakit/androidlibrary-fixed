package org.opendatakit.database.Service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.opendatakit.database.service.DbHandle;

public class DbHandleTest {
    private static final String TEST_DATABASE_HANDLE = "testDatabaseHandle";
    private DbHandle dbHandle;

    @Before
    public void setUp() {
        dbHandle = new DbHandle(TEST_DATABASE_HANDLE);
    }

    @Test
    public void constructor_withValidDatabaseHandle_createsInstance() {
        assertNotNull(dbHandle);
        assertEquals(TEST_DATABASE_HANDLE, dbHandle.getDatabaseHandle());
    }

    @Test
    public void describeContents_ReturnsZero() {
        assertEquals(0, dbHandle.describeContents());
    }
}
