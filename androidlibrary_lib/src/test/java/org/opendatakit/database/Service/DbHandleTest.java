package org.opendatakit.database.Service;

import android.os.Parcel;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.database.service.DbHandle;

import static org.junit.Assert.*;
public class DbHandleTest {
    private static final String TEST_DATABASE_HANDLE = "testDatabaseHandle";
    private DbHandle dbHandle;
    @Before
    public void setUp() {
        dbHandle = new DbHandle(TEST_DATABASE_HANDLE);
    }

    @Test
    public void constructor_ValidDatabaseHandle_CreatesInstance() {
        assertNotNull(dbHandle);
        assertEquals(TEST_DATABASE_HANDLE, dbHandle.getDatabaseHandle());
    }
    @Test
    public void writeToParcel_CreatesParcel() {
        Parcel parcel = Parcel.obtain();
        dbHandle.writeToParcel(parcel, 0);
        parcel.setDataPosition(0); // Reset the parcel for reading
        DbHandle createdFromParcel = DbHandle.CREATOR.createFromParcel(parcel);
        assertEquals(TEST_DATABASE_HANDLE, createdFromParcel.getDatabaseHandle());
        parcel.recycle(); // Clean up the parcel object
    }
    @Test
    public void readFromParcel_NullDatabaseHandle_ThrowsException() {
        Parcel parcel = Parcel.obtain();
        parcel.writeString(null); // Write null to the parcel
        parcel.setDataPosition(0); // Reset the parcel for reading
        try {
            DbHandle createdFromParcel = DbHandle.CREATOR.createFromParcel(parcel);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception was thrown as expected
        } finally {
            parcel.recycle(); // Clean up the parcel object
        }
    }
    @Test
    public void describeContents_ReturnsZero() {
        assertEquals(0, dbHandle.describeContents());
    }
}