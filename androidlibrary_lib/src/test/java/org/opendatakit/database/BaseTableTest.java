package org.opendatakit.database;

import android.os.Parcel;
import org.junit.Before;
import org.junit.Test;
import org.opendatakit.database.data.BaseTable;
import org.opendatakit.database.queries.ResumableQuery;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BaseTableTest {

    private BaseTable baseTable;

    @Before
    public void setUp() {
        String[] primaryKey = {"id"};
        String[] elementKeyForIndex = {"column1", "column2"};
        Map<String, Integer> elementKeyToIndex = new HashMap<>();
        elementKeyToIndex.put("column1", 0);
        elementKeyToIndex.put("column2", 1);

        ResumableQuery query = null;

        baseTable = new BaseTable(query, elementKeyForIndex, elementKeyToIndex, primaryKey, 0);
    }

    @Test
    public void testGetWidth() {

        int width = baseTable.getWidth();

        assertEquals(2, width);
    }

    @Test
    public void testGetPrimaryKey() {
        String[] primaryKey = baseTable.getPrimaryKey();

        assertArrayEquals(new String[]{"id"}, primaryKey);
    }

    @Test
    public void testParcelable() {
        Parcel parcel = Parcel.obtain();
        baseTable.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        BaseTable createdFromParcel = BaseTable.CREATOR.createFromParcel(parcel);

        assertNotNull(createdFromParcel);
        assertArrayEquals(baseTable.getPrimaryKey(), createdFromParcel.getPrimaryKey());
        assertEquals(baseTable.getWidth(), createdFromParcel.getWidth());

        parcel.recycle();
    }
}
