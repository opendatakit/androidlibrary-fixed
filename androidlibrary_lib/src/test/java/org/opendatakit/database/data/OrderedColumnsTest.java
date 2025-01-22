package org.opendatakit.database.data;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.opendatakit.aggregate.odktables.rest.entity.Column;
import org.opendatakit.logging.WebLogger;
import org.opendatakit.logging.desktop.WebLoggerDesktopFactoryImpl;
import org.opendatakit.utilities.StaticStateManipulator;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import static org.junit.Assert.*;

public class OrderedColumnsTest {
    private static final String APP_NAME = "testApp";
    private static final String TABLE_ID = "testTable";
    private static final String ELEMENT_KEY_1 = "elementKey1";
    private static final String ELEMENT_NAME_1 = "elementName1";
    private static final String ELEMENT_KEY_2 = "elementKey2";
    private static final String ELEMENT_NAME_2 = "elementName2";
    private static final String ELEMENT_TYPE_NUMBER = "number";
    private static final String ELEMENT_TYPE_STRING = "string";
    private static final String ELEMENT_TYPE_GEO = "geopoint";
    private static final String LONGITUDE = "longitude";
    private static final String LATITUDE = "latitude";
    private List<Column> testColumns;
    private OrderedColumns orderedColumns;

    @BeforeClass
    public static void oneTimeSetUp() {
        StaticStateManipulator.get().reset();
        WebLogger.setFactory(new WebLoggerDesktopFactoryImpl());
    }

    @Before
    public void setUp() {
        testColumns = new ArrayList<>();
        testColumns.add(new Column(ELEMENT_KEY_1, ELEMENT_NAME_1, ELEMENT_TYPE_STRING, "[]")); // Use an empty JSON array for no children
        testColumns.add(new Column(ELEMENT_KEY_2, ELEMENT_NAME_2, ELEMENT_TYPE_STRING, "[]")); // Modify child keys as per actual validation rules
        orderedColumns = new OrderedColumns(APP_NAME, TABLE_ID, testColumns);
    }

    @After
    public void tearDown() {
        testColumns = null;
        orderedColumns = null;
    }

    @Test
    public void givenOrderedColumns_whenGetAppName_thenReturnCorrespondingAppName() {
        assertNotNull(orderedColumns);
        assertEquals(APP_NAME, orderedColumns.getAppName());
    }

    @Test
    public void givenOrderedColumns_whenGetTableId_thenReturnCorrespondingTableId() {
        assertNotNull(orderedColumns);
        assertEquals(TABLE_ID, orderedColumns.getTableId());
    }

    @Test
    public void givenOrderedColumns_whenGetColumnDefinitions_thenReturnCorrespondingDefinitions() {
        assertNotNull(orderedColumns);
        assertNotNull(orderedColumns.getColumnDefinitions());
    }

    @Test
    public void givenOrderedColumns_whenFindExistingElement_thenReturnColumnWithElementKey() {
        assertNotNull(orderedColumns);
        ColumnDefinition foundColumn = orderedColumns.find(ELEMENT_KEY_1);
        assertNotNull(foundColumn);
        assertEquals(ELEMENT_KEY_1, foundColumn.getElementKey());
    }

    @Test
    public void givenOrderedColumns_whenFindNonExistingElement_thenThrowIllegalArgumentException() {
        assertNotNull(orderedColumns);
        assertThrows(IllegalArgumentException.class, () -> orderedColumns.find("noExistenceKey"));
    }

    @Test
    public void givenOrderedColumns_whenGetRetentionColumnNames_thenReturnArrayOfElementKeys() {
        assertNotNull(orderedColumns);
        ArrayList<String> retentionColumns = orderedColumns.getRetentionColumnNames();
        assertNotNull(retentionColumns);
        ArrayList<String> expected = new ArrayList<>();
        expected.add(ELEMENT_KEY_1);
        expected.add(ELEMENT_KEY_2);
        assertEquals(expected, retentionColumns);
    }

    @Test
    public void givenOrderedColumnsWithNumberType_whenCheckGraphViewIsPossible_thenReturnTrue() {
        tearDown();
        ArrayList<Column> numberColumns = new ArrayList<>();
        numberColumns.add(new Column(ELEMENT_KEY_1, ELEMENT_NAME_1, ELEMENT_TYPE_NUMBER, "[]")); // Use an empty JSON array for no children
        numberColumns.add(new Column(ELEMENT_KEY_2, ELEMENT_NAME_2, ELEMENT_TYPE_NUMBER, "[]")); // Modify child keys as per actual validation rules
        orderedColumns = new OrderedColumns(APP_NAME, TABLE_ID, numberColumns);
        assertNotNull(orderedColumns);
        assertTrue(orderedColumns.graphViewIsPossible());
    }

    @Test
    public void givenOrderedColumnsWithNonNumberType_whenCheckGraphViewIsPossible_thenReturnFalse() {
        assertNotNull(orderedColumns);
        assertFalse(orderedColumns.graphViewIsPossible());
    }

    @Test
    public void givenOrderedColumnsWithGeopointsType_whenCheckGraphViewIsPossible_thenReturnTrue() {
        tearDown();
        ArrayList<Column> geoColumns = new ArrayList<>();
        geoColumns.add(new Column(ELEMENT_KEY_1, ELEMENT_NAME_1, ELEMENT_TYPE_GEO, "[]")); // Use an empty JSON array for no children
        geoColumns.add(new Column(ELEMENT_KEY_2, ELEMENT_NAME_2, ELEMENT_TYPE_GEO, "[]")); // Modify child keys as per actual validation rules
        orderedColumns = new OrderedColumns(APP_NAME, TABLE_ID, geoColumns);
        assertNotNull(orderedColumns);
        assertTrue(orderedColumns.mapViewIsPossible());
    }

    @Test
    public void givenOrderedColumnsWithLongitudeAndLatitude_whenCheckGraphViewIsPossible_thenReturnTrue() {
        assertNotNull(orderedColumns);
        //TODO: cater for Longitude and latitude clause
//        assertTrue(orderedColumns.mapViewIsPossible());
    }

    @Test
    public void givenOrderedColumnsWithNoGeopoints_whenCheckMapViewIsPossible_thenReturnFalse() {
        assertNotNull(orderedColumns);
        assertFalse(orderedColumns.mapViewIsPossible());
    }

    @Test
    public void givenOrderedColumnsWithNoGeopoints_whenGetGeoColumnDefinitions_thenReturnEmptyArray() {
        ArrayList<ColumnDefinition> geoPointColumns = orderedColumns.getGeopointColumnDefinitions();
        assertNotNull(geoPointColumns);
        assertTrue(geoPointColumns.isEmpty());
    }

    @Test
    public void givenOrderedColumnsWithGeopoints_whenGetGeoColumnDefinitions_thenReturnArrayOfColDefinition() {
        ArrayList<Column> geoColumns = new ArrayList<>();
        geoColumns.add(new Column(LATITUDE, ELEMENT_NAME_1, ELEMENT_TYPE_GEO, "[]"));
        geoColumns.add(new Column(LONGITUDE, ELEMENT_NAME_2, ELEMENT_TYPE_GEO, "[]"));
        orderedColumns = new OrderedColumns(APP_NAME, TABLE_ID, geoColumns);
        ArrayList<ColumnDefinition> geoPointColumns = orderedColumns.getGeopointColumnDefinitions();
        assertNotNull(geoPointColumns);
        assertFalse(geoPointColumns.isEmpty());
    }

    @Test
    public void givenGetDataModel() {
        TreeMap<String, Object> dataModel = orderedColumns.getDataModel();
        assertNotNull(dataModel);
        assertFalse(dataModel.isEmpty());
    }

    @Test
    public void GetExtendedDataModel() {
        OrderedColumns orderedColumns = new OrderedColumns(APP_NAME, TABLE_ID, testColumns);
        TreeMap<String, Object> extendedDataModel = orderedColumns.getExtendedDataModel();
        assertNotNull(extendedDataModel);
        assertFalse(extendedDataModel.isEmpty());
    }

    @AfterClass
    public static void oneTimeTearDown() {
        StaticStateManipulator.get().reset();
        WebLogger.closeAll();
    }
}
