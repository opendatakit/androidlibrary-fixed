package org.opendatakit.database;

import org.junit.Before;
import org.junit.Test;
import org.opendatakit.database.data.BaseTable;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class BaseTableTest {
    private static final String ID = "id";
    private static final String COLUMN_1 = "column1";
    private static final String COLUMN_2 = "column2";
    private BaseTable baseTable;

    @Before
    public void setUp() {
        String[] primaryKey = {ID};
        String[] elementKeyForIndex = {COLUMN_1, COLUMN_2};
        Map<String, Integer> elementKeyToIndex = new HashMap<>();
        elementKeyToIndex.put(COLUMN_1, 0);
        elementKeyToIndex.put(COLUMN_2, 1);
        baseTable = new BaseTable(null, elementKeyForIndex, elementKeyToIndex, primaryKey, 0);
    }

    @Test
    public void givenBaseTable_whenGetWidth_thenReturnTheCountOfElementForKeyIndex() {
        int width = baseTable.getWidth();
        assertEquals(2, width);
    }

    @Test
    public void givenBaseTable_whenGetPrimaryKey_thenReturnStringArrayOfPrimaryKeys() {
        String[] primaryKey = baseTable.getPrimaryKey();
        assertArrayEquals(new String[]{"id"}, primaryKey);
    }

}
