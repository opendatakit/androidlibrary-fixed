package org.opendatakit.database.data;

import org.junit.Before;
import org.junit.Test;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ColumnListTest {

    private static final String ELEMENT_KEY_1 = "elementKey1";
    private static final String ELEMENT_NAME_1 = "elementName1";
    private static final String ELEMENT_KEY_2 = "elementKey2";
    private static final String ELEMENT_NAME_2 = "elementName2";
    private static final String ELEMENT_TYPE_STRING = "string";
    private static final String CHILD_KEY_1 = "childKeys1";
    private static final String CHILD_KEY_2 = "childKeys2";
    private List<Column> testColumns;

    @Before
    public void setUp() {
        // Initialize a list of columns
        testColumns = new ArrayList<>();
        testColumns.add(new Column(ELEMENT_KEY_1, ELEMENT_NAME_1, ELEMENT_TYPE_STRING, CHILD_KEY_1));
        testColumns.add(new Column(ELEMENT_KEY_2, ELEMENT_NAME_2, ELEMENT_TYPE_STRING, CHILD_KEY_2));
    }

    @Test
    public void ConstructorWithValidList() {
        ColumnList columnList = new ColumnList(testColumns);
        assertNotNull(columnList);
        assertEquals(testColumns, columnList.getColumns());
    }
}